package com.robot.game.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.robot.game.AngleLerp;
import com.robot.game.Animation;
import com.robot.game.AudioManager;
import com.robot.game.Raycast;
import com.robot.game.Resources;
import com.robot.game.constants.GameSettings;
import com.robot.game.screens.GameScreen;
import com.robot.game.Player;
import com.robot.game.Room;
import com.robot.game.attacks.TurretBullet;
import com.robot.game.constants.EnemiesStat;
import com.robot.game.constants.FilterBits;
import com.robot.game.enums.EnemyState;

public class TurretEnemy extends Enemy{
    boolean seePlayer;
    float shootCounter = EnemiesStat.TURRET_SHOOT_COUNTER;
    float seeCounter = 0.1f;
    Sprite base;
    public Room room;
    Texture headTexture;
    Texture baseTexture;



    public TurretEnemy(float x, float y, Player player, World world, Room room) {
        super(x, y, 100, 100, player, null, world,
            BodyDef.BodyType.StaticBody);

        headTexture = new Texture(Resources.TURRET_HEAD);
        baseTexture = new Texture(Resources.TURRET_BASE_TEXTURE);
        this.room = room;

        hp = EnemiesStat.TURRET_HP;
        type = "Turret";
        textureRegion = new TextureRegion(headTexture);

        attack = new TurretBullet(x, y, angle, this);
        body = createBody(BodyDef.BodyType.StaticBody, x, y, getWidth(), false, false);
        sprite = new Sprite(spawningRegion);
        animation = new Animation(spawningRegion, sprite, 24, 2, 2);
        sprites.add(sprite);
        sprite.setOrigin(getWidth() /2, getHeight() /2);
        sprite.setPosition(x, y);
        sprite.setSize(getWidth(), getHeight());

        base = new Sprite(baseTexture);
        sprites.add(base);
        base.setOrigin(getWidth() /2, getHeight() /2);
        base.setPosition(x, y);
        base.setSize(getWidth(), getHeight());

        getFixture().setSensor(true);
        setUserData(this);
        initCategoryBits(FilterBits.STATIC_ENEMY_BITS, (short) 0, (short) (FilterBits.PLAYER_BITS | FilterBits.PLAYER_ATTACK_BITS));
        setPosition(new Vector2(x, y));

        GameScreen.attackArray.add(attack);
    }

    @Override
    public void reUse(Vector2 position) {
        animation = new Animation(spawningRegion, sprite, 24, 2, 2);
        sprite.setRegion(spawningRegion);
        spawnCounter = EnemiesStat.ENEMY_SPAWN_TIMER;
        System.out.println("reuse");
        setEnemyState(EnemyState.ACTION);
        attack.reSetAll();
        hpReduced = false;
        hp = EnemiesStat.TURRET_HP;
        setPosition(position);
        hasToBeDestroyed = false;
        spawning = true;
    }

    @Override
    public void destroy() {
        System.out.println("reuse");
        setEnemyState(EnemyState.ACTION);
        attack.disable();
        setPosNone();
        hpReduced = false;
        hp = EnemiesStat.HAMMER_BOT_HP;
        hasToBeDestroyed = false;
    }

    @Override
    public void draw(Batch batch) {
        if(!spawning) {
            base.draw(batch);
        }
        sprite.draw(batch);

    }

    @Override
    public void update() {
        body.setAwake(true);
        countSpawn();

        if(spawning){
            return;
        }

        if(hp <= 0){
            hasToBeDestroyed = true;
        }
        if(seeCounter > 0){
            seeCounter -=Gdx.graphics.getDeltaTime();
        }else {
            seePlayer = !Raycast.lightBehindWall(body, player.body, world);
            seeCounter = 0.1f;
        }
        if(attack.hasToBeDestroyed && getEnemyState() != EnemyState.RELOADING){
            attack.destroy();
            shootCounter = EnemiesStat.TURRET_SHOOT_COUNTER;
            setEnemyState(EnemyState.RELOADING);
        }

        if(seePlayer && GameScreen.camera.frustum.pointInFrustum(new Vector3(new Vector2(getX(), getY()), 0))){
            angle = AngleLerp.returnAngle(angle, getPlayerAngle(), 6);

            if(attack.hasToBeDestroyed){
                shootCounter -= Gdx.graphics.getDeltaTime();
                if(shootCounter < 0){
                    attack.reUse(body.getPosition(), getPlayerAngle());
                    shootCounter = EnemiesStat.TURRET_SHOOT_COUNTER;
                    setEnemyState(EnemyState.ATTACKING);
                    AudioManager.playEnemyAttack();
                }
            }
        }
        else {
            angle -=1;
            if(angle >= 360){
                angle =0;
            }
            setEnemyState(EnemyState.ACTION);
        }

        sprite.setRotation(angle);
        sprite.setPosition(getX(), getY());

    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

}
