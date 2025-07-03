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
import com.robot.game.AngleLerp;
import com.robot.game.Animation;
import com.robot.game.Raycast;
import com.robot.game.screens.GameScreen;
import com.robot.game.Player;
import com.robot.game.Resources;
import com.robot.game.Room;
import com.robot.game.Utils;
import com.robot.game.astar.AStar;
import com.robot.game.astar.Node;
import com.robot.game.attacks.BaseEnemyAttack;
import com.robot.game.constants.EnemiesStat;
import com.robot.game.constants.FilterBits;
import com.robot.game.constants.GameSettings;
import com.robot.game.enums.EnemyState;

import java.util.Arrays;

public class BaseEnemy extends Enemy{


    AStar aStar;



    boolean pathToPlayerBlocked = true;
    Vector2 position = new Vector2();


    public void setPathToPlayerBlocked(boolean pathToPlayerBlocked) {
        this.pathToPlayerBlocked = pathToPlayerBlocked;
    }


    public boolean shouldGoByPath;



    Room room;
    boolean isAvoiding;

    Vector2 currentPlayerPosition;
    Vector2 previousPlayerPosition;
    Array<Vector2> path;
    int currentWaypoint = 0;
    Node start;
    float baseSpeed;
    Node end;
    float changeCounter;
    boolean needToUpdatePath = true;
    Vector2 direction = new Vector2();


    public BaseEnemy(float x, float y, Player player, World world, Room room, String type) {
        super(x, y, 200, 200, player, null, world, BodyDef.BodyType.DynamicBody);
        sprite = new Sprite();
        switch (type){
            case "Hammer":
                baseHp = EnemiesStat.HAMMER_BOT_HP;
                break;
            case "Turret":
                baseHp = EnemiesStat.TURRET_HP;
                break;
            case "Cart":
                baseHp = EnemiesStat.CART_HP;
                break;
        }
        if(type.equals("Hammer")){
            hp = EnemiesStat.HAMMER_BOT_HP;

            textureRegion = new TextureRegion(new Texture(Resources.HAMMER_REGION));
            speed = EnemiesStat.HAMMER_SPEED;

            attack = new BaseEnemyAttack(x, y, EnemiesStat.HAMMER_ATTACK_WIDTH, EnemiesStat.HAMMER_ATTACK_HEIGHT, new Texture(Resources.HAMMER_ATTACK_TEXTURE), EnemiesStat.NORMAL_ATTACK_RANGE, 1, EnemiesStat.NORMAL_ATTACK_DURATION, angle, this);
        }
        if(type.equals("Cart")){
            hp = EnemiesStat.CART_HP;

            textureRegion = new TextureRegion(new Texture(Resources.CART_REGION));
            speed = EnemiesStat.CART_SPEED;
            attack = new BaseEnemyAttack(x, y, EnemiesStat.CART_ATTACK_WIDTH, EnemiesStat.CART_ATTACK_HEIGHT, new Texture(Resources.CART_ATTACK_TEXTURE), EnemiesStat.NORMAL_ATTACK_RANGE, 1, EnemiesStat.NORMAL_ATTACK_DURATION, angle, this);
        }
        switch (type){
            case "Hammer":
                baseSpeed = EnemiesStat.HAMMER_SPEED;
                break;
            case "Cart":
                baseSpeed = EnemiesStat.CART_SPEED;
                break;
        }
        sprite.setOriginCenter();
        sprite = new Sprite(spawningRegion, 0, 0, 50 ,50);
        sprite.setSize(getWidth() /2, getHeight() /2);

        animation = new Animation(spawningRegion, sprite, 24, 2, 2);
        sprite.setPosition(x + getWidth(), y + getWidth());
        sprites.add(sprite);


        this.room = room;
        aStar = new AStar(room);

        isAvoiding = false;
        this.type = type;



        currentPlayerPosition = new Vector2(player.getX() + player.getWidth(), player.getY() + player.getHeight()/2);
        currentPlayerPosition.set(player.getX() + player.getWidth(), player.getY() + player.getHeight()/2);
        previousPlayerPosition = new Vector2();


        shouldGoByPath = true;
        initCategoryBits(FilterBits.ENEMY_BITS, (short) 0, (short) ((short) ~FilterBits.ENEMY_BITS | FilterBits.PLAYER_ATTACK_BITS));
        start = aStar.getPointByCords(new Vector2(x + getWidth() /2, y + getHeight() /2));
        end = aStar.getPointByCords(new Vector2(player.getX() + player.getWidth()/2, player.getY() + player.getHeight()/2));
        path = aStar.findPath(start, end);

        changeCounter = 0.3f;

        GameScreen.attackArray.add(attack);
    }
    public void move(){
        System.out.println(animation.rowsWidth);
        if(isDoingHit() || getEnemyState() != EnemyState.ACTION){
            body.setAwake(true);
            changeCounter = 0;
            body.setLinearVelocity(0, 0);
            return;
        }

        changeCounter -= Gdx.graphics.getDeltaTime();
        position.set(getX() + getWidth() /4, getY() + getHeight() /4);

        currentPlayerPosition.set(player.getX() + player.getWidth()/2, player.getY() + player.getHeight()/2);
        if(changeCounter < 0){
            shouldGoByPath = Raycast.isBehindWall(body, player.body, world);
            changeCounter = 0.3f;
        }

        if (!shouldGoByPath) {
            direction.set(player.getX() + player.getWidth()/4 - position.x, player.getY() + player.getWidth()/4 - position.y).nor();
            body.setLinearVelocity(direction.cpy().nor().scl(speed));
            return;
        }

        if (!isPlayerPositionEqual()) {
            needToUpdatePath = true;
            previousPlayerPosition.set(currentPlayerPosition);
        }


        if (needToUpdatePath) {
            start = aStar.getPointByCords(new Vector2(getX() + getWidth() /2, getY() + getHeight() /2));
            end = aStar.getPointByCords(new Vector2(player.getX() + player.getWidth()/2, player.getY() + player.getHeight()/2));
            path = aStar.findPath(start, end);
            currentWaypoint = 0;


            needToUpdatePath = false;
        }


        if (!path.isEmpty() && currentWaypoint < path.size && getEnemyState() == EnemyState.ACTION) {

            Vector2 target = path.get(currentWaypoint);

            float targetX = target.x + (float) Room.multiplier / 2;
            float targetY = target.y + ((float) Room.multiplier / 2);
            float enemyCenterX = position.x + getWidth() /4;
            float enemyCenterY = position.y + getHeight() /4;

            double dx = targetX - enemyCenterX;
            double dy = targetY - enemyCenterY;

            float desiredAngle = AngleLerp.roundToNearestAngle((float) (Math.toDegrees(Math.atan2(dy, dx)) - 90));
            if(shouldGoByPath) angle = AngleLerp.returnAngle(angle, desiredAngle, EnemiesStat.ROTATION_SPEED);

            float distanceToTarget = (float) Utils.doPythagoras(enemyCenterX, enemyCenterY, targetX, targetY);

            if (distanceToTarget <= speed * 2) {
                currentWaypoint++;

            }
            else {
                direction = direction.lerp((target).cpy().sub(position), 0.5f).nor();
            }
        }

        body.setLinearVelocity(direction.cpy().scl(speed));
    }

    public void update(){


        countSpawn();

        if(spawning){
            body.setActive(false);
            return;
        }
        body.setActive(true);
        animation.play();


        if (Raycast.lightBehindWall(body, player.getBody(), world)){
            attack.disable();
        }
        else {
            attack.reSetBits();
        }

        if(hp <= 0){
            hasToBeDestroyed = true;
        }
        setX(body.getPosition().x * GameSettings.PPM - getWidth() / 2);
        setY(body.getPosition().y * GameSettings.PPM - getHeight() / 2);

        move();
        if(getEnemyState() == EnemyState.ACTION && !Raycast.lightBehindWall(body, player.getBody(), world)) playerAngle = getPlayerAngle();
        if(!shouldGoByPath) angle = AngleLerp.returnAngle(angle, playerAngle, EnemiesStat.ROTATION_SPEED);



        sprite.setRotation(angle);
        sprite.setX(getX());
        sprite.setY(getY());
    }

    @Override
    public void plusDifficulty() {
        super.plusDifficulty();
        baseSpeed += 0.1f;
    }


    @Override
    public void reUse(Vector2 position) {
        spawning = true;
        setEnemyState(EnemyState.ACTION);

        animation = new Animation(spawningRegion, sprite, 24, 2, 2);

        spawnCounter = EnemiesStat.ENEMY_SPAWN_TIMER;


        sprite.setRegion(spawningRegion);
        sprite.setSize(getWidth() /2, getHeight() /2);
        sprite.setOriginCenter();


        hpReduced = false;
        speed = baseSpeed;
        hp = baseHp;
        setPosition(position);
        hasToBeDestroyed = false;
        attack.setDamage(baseDamage);

    }

    @Override
    public void destroy() {
        System.out.println("reuse");
        setEnemyState(EnemyState.ACTION);
        //attack.disable();
        attack.destroy();
        hpReduced = false;

        setPosNone();
    }


    public void draw(Batch batch){

        sprite.draw(batch);
    }
    private boolean isPlayerPositionEqual() {
        return aStar.getPointByCords(currentPlayerPosition).equals(aStar.getPointByCords(previousPlayerPosition));
    }
    public void setRoom(Room room) {
        aStar = new AStar(room);
        Gdx.app.log("real map2", Arrays.deepToString(aStar.room.getMap()));
        start = aStar.getPointByCords(new Vector2(getX() + getWidth() /2, getY() + getHeight() /2));
        end = aStar.getPointByCords(new Vector2(player.getX() + player.getWidth()/2, player.getY() + player.getHeight()/2));
        path = aStar.findPath(start, end);
    }



}
