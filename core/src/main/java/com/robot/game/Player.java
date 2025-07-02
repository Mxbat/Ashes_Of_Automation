package com.robot.game;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.robot.game.attacks.EnemyAttack;
import com.robot.game.attacks.PlayerAttack;
import com.robot.game.constants.FilterBits;
import com.robot.game.constants.GameSettings;
import com.robot.game.constants.PlayerStat;
import com.robot.game.constants.Scores;
import com.robot.game.enemies.Enemy;
import com.robot.game.enums.PlayerBodyState;
import com.robot.game.enums.PlayerLegsState;
import com.robot.game.screens.GameScreen;

public class Player extends GameObject {

    PlayerAttack attack;
    PlayerBodyState bodyState = PlayerBodyState.IDLE;
    PlayerLegsState legsState;
    Array<EnemyAttack> enemyAttacks = new Array<>();
    private boolean runningBlocked;

    boolean canEnterIdle;





    private float stamina = PlayerStat.MAX_STAMINA;

    public void setRunning(boolean running) {
        this.running = running;
    }

    private boolean running = false;







    public void setGettingHit(boolean gettingHit) {
        isGettingHit = gettingHit;
    }

    public boolean isGettingHit() {
        return isGettingHit;
    }

    private boolean isGettingHit = false;
    public void reduceHp(int value){
        hp-=value;
        AudioManager.playHit();
    }
    public int getHp() {
        return hp;
    }

    private int hp = PlayerStat.PLAYER_DEFAULT_HP;



    public boolean isTouchingEntrance() {
        return touchingEntrance;
    }

    public void setTouchingEntrance(boolean touchingEntrance) {
        this.touchingEntrance = touchingEntrance;
    }

    private boolean touchingEntrance;


    public double attackCounter;
    World world;
    float joystickAngle = 0;

    public boolean isAttacking;
    boolean canSeeEnemy;
    public void setCanSeeEnemy(boolean canSeeEnemy) {
        this.canSeeEnemy = canSeeEnemy;
    }

    boolean isSetStart;
    public float drawAngle;

    public float dx;
    public float dy;
    Joystick joystick;
    Sprite legsSprite;

    public Animation getBodyAnim() {
        return bodyAnim;
    }

    Animation bodyAnim;




    Animation legsAnim;
    TextureRegion bodyRegion;
    TextureRegion legsRegion;

    int roundedStamina = (int) Math.floor(stamina);
    public boolean enteredIdle = false;


    public Player(Joystick joystick, int x, int y, World world) {
        super(x, y, 200, 200, (TextureRegion) null, BodyDef.BodyType.DynamicBody);
        bodyRegion = new TextureRegion(new Texture(Resources.PLAYER_BODY_REGION));
        legsRegion = new TextureRegion(new Texture(Resources.PLAYER_LEGS_REGION));
        sprite = new Sprite(bodyRegion);
        sprite.setSize(getWidth(), getHeight());
        sprite.setOriginCenter();
        bodyAnim = new Animation(bodyRegion, sprite, 24, 3, 3);

        legsSprite = new Sprite(legsRegion);
        legsSprite.setSize(getWidth(), getHeight());
        legsSprite.setOriginCenter();
        legsAnim = new Animation(legsRegion, legsSprite, 24, 4, 4);

        attackCounter = PlayerStat.PLAYER_ATTACK_COOLDOWN;
        isAttacking = false;
        this.world = world;

        this.setX(x);
        this.setY(y);

        this.body = createBody(BodyDef.BodyType.DynamicBody, x, y, getWidth(), true, true);
        //getFixture().setSensor(true);
        initCategoryBits(FilterBits.PLAYER_BITS, (short) 0, (short) (FilterBits.OBSTACLE_BITS |  FilterBits.ENEMY_ATTACK_BITS));
        setUserData(this);
        attack = new PlayerAttack(drawAngle, this);



        this.joystick = joystick;


        drawAngle = 0.1f;
        isSetStart = false;
        GameScreen.attackArray.add(attack);
        dx = (float) Math.round(sin(Math.toRadians(-1)) * PlayerStat.PLAYER_SPEED_RATIO);
        dy = (float) Math.round(cos(Math.toRadians(-1)) * PlayerStat.PLAYER_SPEED_RATIO);

    }

    public void setAttackCounter(double attackCounter) {
        this.attackCounter = attackCounter;
    }

    public void move(){

        joystickAngle = joystick.getAngle();
        if(!joystick.isSendingMove){
            dx = 0; dy = 0;
            legsState = PlayerLegsState.IDLE;
        }
        if (joystick.isSendingMove && bodyState == PlayerBodyState.ATTACKING){
            enteredIdle = false;
            legsState = PlayerLegsState.MOVING;
            dx = (float) Math.round(sin(Math.toRadians(-joystickAngle)) * PlayerStat.PLAYER_SPEED_RATIO)/PlayerStat.PLAYER_ATTACK_SLOWING;
            dy = (float) Math.round(cos(Math.toRadians(-joystickAngle)) * PlayerStat.PLAYER_SPEED_RATIO)/PlayerStat.PLAYER_ATTACK_SLOWING;
            legsAnim.speedUp(1/PlayerStat.PLAYER_ATTACK_SLOWING);
        }

        else if(joystick.isSendingMove && running){
            enteredIdle = false;
            legsState = PlayerLegsState.MOVING;
            bodyState = PlayerBodyState.MOVING;
            dx = (float) Math.round(sin(Math.toRadians(-joystickAngle)) * PlayerStat.PLAYER_SPEED_RATIO) * PlayerStat.PLAYER_RUN_MULTIPLIER;
            dy = (float) Math.round(cos(Math.toRadians(-joystickAngle)) * PlayerStat.PLAYER_SPEED_RATIO) * PlayerStat.PLAYER_RUN_MULTIPLIER;
        }
        else if(joystick.isSendingMove){
            enteredIdle = false;
            legsState = PlayerLegsState.MOVING;
            bodyState = PlayerBodyState.MOVING;
            dx = (float) Math.round(sin(Math.toRadians(-joystickAngle)) * PlayerStat.PLAYER_SPEED_RATIO);
            dy = (float) Math.round(cos(Math.toRadians(-joystickAngle)) * PlayerStat.PLAYER_SPEED_RATIO);

        }
        else if(bodyState != PlayerBodyState.ATTACKING){
            if(!enteredIdle) {
                bodyState = PlayerBodyState.IDLE;
                enteredIdle = true;
            }
            else {
                bodyAnim.play();
            }
            dx = 0; dy = 0;

        }
        body.setLinearVelocity(dx, dy );

    }
    public void update(Enemy enemy){
        System.out.println(enemyAttacks);

        if(isAttacking && attackCounter < 0){
            setAttackCounter(PlayerStat.PLAYER_ATTACK_COOLDOWN);
        }
        body.setAwake(true);


        setX(body.getPosition().x * GameSettings.PPM - getWidth()/2);
        setY(body.getPosition().y * GameSettings.PPM - getHeight()/2);
        setAngle(enemy);
        sprite.setRotation(drawAngle);
        sprite.setX(getX());
        sprite.setY(getY());


        legsSprite.setRotation(drawAngle);
        legsSprite.setX(getX());
        legsSprite.setY(getY());

        roundedStamina = (int) Math.ceil(stamina);
        if(running && roundedStamina > 0 && joystick.isSendingMove){
            runningBlocked = false;
            stamina -= Gdx.graphics.getDeltaTime()*4;
        }
        else if ((!running || roundedStamina <= 1) && !joystick.isSendingMove) {
            stamina += Gdx.graphics.getDeltaTime()*8;
        }else if (!running || roundedStamina <= 1) {
            stamina += Gdx.graphics.getDeltaTime()*8;
            runningBlocked = true;
        }
        if(stamina >= PlayerStat.MAX_STAMINA){
            stamina = PlayerStat.MAX_STAMINA;
        }
        if(runningBlocked){
            running = false;
        }


        attackCounter -=Gdx.graphics.getDeltaTime();

        switch (bodyState){
            case MOVING:
                if(running){
                    bodyAnim.speedUp(PlayerStat.PLAYER_RUN_MULTIPLIER);
                    legsAnim.speedUp(PlayerStat.PLAYER_RUN_MULTIPLIER);
                }
                if(attack.canSetMoving) {
                    bodyAnim.play();
                }
                break;
            case IDLE:
                if(attack.playerRowChanged){
                    sprite.setRegion(750, 150, 75, 75);
                }
                break;
            case ATTACKING:
                bodyAnim.changeRow(2);
                bodyAnim.play();
                break;
        }
        switch (legsState){
            case IDLE:
                legsSprite.setRegion(0, 200, 100, 100);
                break;
            case MOVING:
                legsAnim.play();
                break;

        }




    }
    public void setAngle(Enemy enemy){


        if(canSeeEnemy)
        {
            drawAngle = AngleLerp.returnAngle( drawAngle,
                ( float) Math.toDegrees((Math.atan2((getX() + getWidth()/2) - (enemy.getX() + enemy.getWidth()/2),
                (enemy.getY() + enemy.getHeight()/2) - (getY() + getWidth()/2)))),
                4);

        }
        else if(joystick.isSendingMove){
            drawAngle = AngleLerp.returnAngle(drawAngle, joystick.getAngle(), 4);

        }
    }
    public void draw(SpriteBatch batch){

        legsSprite.draw(batch);
        canEnterIdle = bodyState == PlayerBodyState.ATTACKING;

        sprite.draw(batch);



    }

    public void setPosition(Vector2 vector2) {
        body.setTransform((vector2.x) / GameSettings.PPM, (vector2.y) / GameSettings.PPM, body.getAngle());
        this.setX(vector2.x);
        this.setY(vector2.y);
    }

    public void setBodyState(PlayerBodyState playerBodyState) {
        this.bodyState = playerBodyState;
    }



    public int getRoundedStamina() {
        return roundedStamina;
    }

    public Joystick getJoystick() {
        return joystick;
    }

    public void setRunningBlocked(boolean runningBlocked) {
        this.runningBlocked = runningBlocked;
    }



    public Array<EnemyAttack> getEnemiesAttacks() {
        return enemyAttacks;
    }

    public void plusHp(int value) {
        GameScreen.gameController.plusScore(Scores.SCORE_PER_HEAL);
        GameScreen.gameController.minusScrap(GameSettings.HEAL_COST);
        hp += value;
    }
}
