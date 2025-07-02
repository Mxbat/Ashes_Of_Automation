package com.robot.game.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.robot.game.Animation;
import com.robot.game.AudioManager;
import com.robot.game.Resources;
import com.robot.game.attacks.Attack;
import com.robot.game.GameObject;
import com.robot.game.Player;
import com.robot.game.Utils;
import com.robot.game.constants.EnemiesStat;
import com.robot.game.constants.GameSettings;
import com.robot.game.enums.EnemyState;

public abstract class Enemy extends GameObject {
    public boolean isDoingHit() {
        return isDoingHit;
    }

    public void setDoingHit(boolean doingHit) {
        isDoingHit = doingHit;
    }

    private boolean isDoingHit;
    boolean shouldIncreaseDamage = true;
    int baseHp;
    int baseDamage = 1;
    boolean spawning;
    Animation animation;
    TextureRegion spawningRegion;
    public Animation getAnimation() {
        return animation;
    }
    float spawnCounter = EnemiesStat.ENEMY_SPAWN_TIMER;

    public EnemyState getEnemyState() {
        return enemyState;
    }

    private EnemyState enemyState = EnemyState.ACTION;
    int hp;
    boolean hpReduced = false;
    Vector2 noPos = new Vector2(Integer.MAX_VALUE, Integer.MAX_VALUE);

    public Attack attack;

    public void setHit(boolean hit) {
        this.hit = hit;
    }
    boolean hit = false;
    TextureRegion textureRegion;
    float speed;
    public boolean hasToBeDestroyed;
    public float angle;
    public Player player;
    float playerAngle;


    abstract public void reUse(Vector2 position);
    abstract public void destroy();
    Array<Sprite> sprites = new Array<>();


    public void reduceHp(int value){
        hp -= (byte) value;
        AudioManager.playHit();
    }

    public void setPosition(Vector2 vector2){
        for (Sprite s:
             sprites) {
            setX(vector2.x - getWidth() / 2);
            setY(vector2.y - getHeight() / 2);
            if(this instanceof BaseEnemy) s.setPosition(getX() + getWidth()/4, getY() + getWidth() /4);
            else s.setPosition(getX(), getY());
        }

        spawning = true;
        body.setTransform(vector2.x/GameSettings.PPM, vector2.y/GameSettings.PPM, 0);


    }

    public Enemy(float x, float y, float width, float height, Player player, Texture texture, World world, BodyDef.BodyType bodyType) {

        super(x, y, width, height, texture, bodyType);


        spawningRegion = new TextureRegion(new Texture(Resources.SPAWNING));
        spawning = true;
        this.texture = texture;
        this.player = player;
        this.world = world;
        angle = 359f;
        playerAngle = 359.9f;
        body = createBody(bodyType, x, y, width, true, true);
        setUserData(this);
        body.setActive(false);


    }
    public abstract void draw(Batch batch);
    public abstract void update();
    public double getDistanceToPlayer(){
        return  Utils.doPythagoras(new Vector2(getX() + getWidth() /2, getY() + getWidth() /2),
            new Vector2(player.getX() + player.getWidth()/2, player.getY() + player.getWidth()/2));
    }
    public float getPlayerAngle(){

        return (float) Math.toDegrees((Math.atan2((player.getX() + player.getWidth() /2) - (getX() + getWidth() /2), (getY() + getWidth() /2) - (player.getY() + player.getWidth() /2)))) + 180;
    }


    public void setSensorIfNeed() {
        getFixture().setSensor(enemyState != EnemyState.ACTION);
    }
    void countSpawn(){
        if(!spawning){
            return;
        }
        animation.play();
        body.setLinearVelocity(0 ,0);
        body.setActive(false);
        spawnCounter -= Gdx.graphics.getDeltaTime();

        if(spawnCounter < 0){
            angle = getPlayerAngle();
            animation.setTextureRegion(textureRegion);
            animation.setFrames(24);
            animation.setX(0);
            animation.setCurrentFrame(0);
            animation.changeRow(0);

            if (this instanceof BaseEnemy)
                animation = new Animation(textureRegion, sprite, 24, 3, 3);
            sprite.setOrigin(getWidth() /2, getHeight() /2);
            sprite.setSize(getWidth(), getHeight());

            if(this instanceof BaseEnemy) sprite.setRegion(textureRegion, 0, 0, 3, 3);
            else sprite.setRegion(textureRegion);
            body.setActive(true);
            spawning = false;
            attack.reSetAll();

        }
    }
    public void setPosNone(){
        setPosition(noPos);
    }



    public boolean isSpawning() {
        return spawning;
    }

    public Player getPlayer() {
        return player;
    }


    String type;
    public  void plusDifficulty(){
        baseHp += 1;
        if(shouldIncreaseDamage) {
            baseDamage += 1;
        }
        shouldIncreaseDamage = !shouldIncreaseDamage;
    }

    public String getType() {
        return type;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void setEnemyState(EnemyState enemyState) {
        this.enemyState= enemyState;
    }
}
