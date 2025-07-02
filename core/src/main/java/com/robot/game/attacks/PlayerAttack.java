package com.robot.game.attacks;



import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.robot.game.Player;
import com.robot.game.Resources;
import com.robot.game.Utils;
import com.robot.game.constants.FilterBits;
import com.robot.game.constants.GameSettings;
import com.robot.game.constants.PlayerStat;
import com.robot.game.enums.PlayerBodyState;

import java.util.Random;

public class PlayerAttack extends Attack {
    public double attackCounter;
    boolean critical;
    public Player player;
    public boolean playerRowChanged = true;
    boolean enteredAttack = false;
    Texture criticalTexture;
    public boolean canSetMoving = false;



    public PlayerAttack(float angle, Player player) {
        super(player.getX(), player.getY(), PlayerStat.PLAYER_ATTACK_WIDTH, PlayerStat.PLAYER_ATTACK_HEIGHT, new Texture(Resources.PLAYER_ATTACK_TEXTURE), PlayerStat.PLAYER_ATTACK_RANGE, (byte) 1, PlayerStat.PLAYER_ATTACK_DURATION, angle);
        criticalTexture = new Texture(Resources.PLAYER_CRIT_TEXTURE);
        sprite = new Sprite(texture);
        this.player = player;
        sprite.setSize(getWidth(), getHeight());
        getFixture().setSensor(true);
        sprite.setOrigin(0, 0);
        sprite.setRotation(player.drawAngle);
        initCategoryBits(FilterBits.PLAYER_ATTACK_BITS, (short) 0, (short) (FilterBits.ENEMY_BITS | FilterBits.STATIC_ENEMY_BITS));
        Utils.updateChild(sprite, player.sprite, -getWidth()/2, range, MathUtils.randomSign());
        body.setTransform(
            sprite.getX()/GameSettings.PPM,
            sprite.getY()/GameSettings.PPM,
            (float) Math.toRadians(player.drawAngle)
        );
        body.setAngularVelocity(0);
        active = true;
        setUserData(this);
    }


    @Override
    public void update() {
        System.out.println(getDamage());
        canSetMoving = done;

        body.setAwake(true);
        if(!done) {
            playerRowChanged = false;
            if(!enteredAttack){



                player.getBodyAnim().setCurrentFrame(0);
                player.getBodyAnim().setFrames(10);
                player.getBodyAnim().setX(0);

                player.setBodyState(PlayerBodyState.ATTACKING);


                enteredAttack = true;
            }



            Utils.updateChild(sprite, player.sprite, -getWidth()/2, range, 0);
            body.setTransform(sprite.getX()/GameSettings.PPM, sprite.getY()/GameSettings.PPM,
                (float) Math.toRadians(player.drawAngle));
        }


        if(done){
            critical = false;
            enteredAttack = false;
            if(!playerRowChanged){
                player.setBodyState(player.getJoystick().isSendingMove ? PlayerBodyState.MOVING : PlayerBodyState.IDLE);
                if(player.getBodyAnim().isCycleEnded()){
                        playerRowChanged = true;

                        player.getBodyAnim().setFrames(24);
                        player.getBodyAnim().setCurrentFrame(0);
                        player.getBodyAnim().setX(0);
                        player.getBodyAnim().changeRow(0);


                    }

            }
           disable();
        }


        currentDuration += Gdx.graphics.getDeltaTime();
        if(currentDuration > duration){
            attackCounter -= Gdx.graphics.getDeltaTime();
        }
        done = currentDuration > duration;
    }

    @Override
    public void draw(Batch batch) {
        if(!done){
            sprite.draw(batch);
        }
    }

    @Override
    public void reUse(Vector2 position, float playerAngle) {

    }

    @Override
    public void destroy() {

    }

    public void resetAttack(){
        Random random = new Random();
        critical = random.nextInt(3) == 0;
        if(critical) sprite.setTexture(criticalTexture);
        else sprite.setTexture(texture);
        reSetBits();
        attackCounter = PlayerStat.PLAYER_ATTACK_COOLDOWN;

        currentDuration = 0;
        done = false;
    }
    public int getDamage(){
        return critical ? 2 : 1;
    }

}
