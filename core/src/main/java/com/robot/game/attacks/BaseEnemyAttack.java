package com.robot.game.attacks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.robot.game.AudioManager;
import com.robot.game.Utils;
import com.robot.game.constants.EnemiesStat;
import com.robot.game.constants.GameSettings;
import com.robot.game.enemies.Enemy;
import com.robot.game.enums.EnemyState;

public class BaseEnemyAttack extends EnemyAttack {
    float cooldownTimer = 0;
    float cooldown = EnemiesStat.ENEMY_ATTACK_COOLDOWN;
    boolean rowChanged = true;
    boolean hasHit = false;


    public BaseEnemyAttack(float x, float y, float width, float height, Texture texture, float range, int damage, float duration, float angle, Enemy enemy) {
        super(x, y, width, height, texture, range, damage, duration, angle);
        this.enemy = enemy;
        sprite = new Sprite(texture);
        sprite.setOrigin(0, 0);
        sprite.setSize(width, height);
        Utils.updateChild(sprite, enemy.sprite, -width/2, range, MathUtils.randomSign());
        getFixture().setSensor(true);
        setUserData(this);
    }

    @Override
    public void update() {

        body.setAwake(true);

        if(enemy.isSpawning()) return;
        System.out.println(this);
        System.out.println(enemy.getPlayer().getEnemiesAttacks().contains(this, false));
        if(enemy.getEnemyState() == EnemyState.ACTION){
            Utils.updateChild(sprite, enemy.getSprite(), -getWidth()/2, -getHeight()/2, 0);
        }
        else Utils.updateChild(sprite, enemy.sprite, -getWidth()/2, range, 0);
        body.setTransform(sprite.getX()/GameSettings.PPM, sprite.getY()/GameSettings.PPM,
            (float) Math.toRadians(enemy.angle));
       switch (enemy.getEnemyState()){
           case ATTACKING:

                if(enemy.getPlayer().isGettingHit() && !hasHit && enemy.getPlayer().getEnemiesAttacks().contains(this, false)){
                    enemy.getPlayer().reduceHp();
                    hasHit = true;
                }

                if(!rowChanged){
                    enemy.getAnimation().changeRow(4);
                    enemy.getAnimation().setFrames(4);
                    enemy.getAnimation().setCurrentFrame(0);
                    enemy.getAnimation().setX(0);
                    rowChanged = true;
                }
               done = false;

               currentDuration += Gdx.graphics.getDeltaTime();
               if(enemy.getAnimation().isCycleEnded()){
                   enemy.getAnimation().setStop(true);
               }
               if(currentDuration > duration){
                   currentDuration = 0;
                   cooldownTimer = 0;
                   reload();
                   check();
               }
               break;
           case RELOADING:
               hasHit = false;
               enemy.getAnimation().setStop(false);

               if(rowChanged){
                   enemy.getAnimation().changeRow(2);
                   enemy.getAnimation().setFrames(24);
                   enemy.getAnimation().setCurrentFrame(0);
                   enemy.getAnimation().setX(0);


                   rowChanged = false;
               }


               done = true;

               cooldownTimer += Gdx.graphics.getDeltaTime();
               if(cooldownTimer > cooldown){
                   AudioManager.playEnemyAttack();
                   cooldownTimer = 0;
                   currentDuration = 0;
                  enemy.setEnemyState(EnemyState.ATTACKING);

               }
               break;

       }
    }

    @Override
    public void draw(Batch batch) {
        if(!done){
            sprite.draw(batch);
        }
    }
    void check(){

        if(!enemy.isDoingHit()){
            rowChanged = false;
            reSetAll();
            enemy.getAnimation().setStop(false);
            hasHit = false;


            enemy.getAnimation().changeRow(0);
            enemy.getAnimation().setFrames(24);
            enemy.getAnimation().setCurrentFrame(0);
            enemy.getAnimation().setX(0);


            enemy.setEnemyState(EnemyState.ACTION);
            done = true;
            body.setAwake(false);
        }

    }
    @Override
    public void reload(){
        rowChanged = true;
        enemy.setEnemyState(EnemyState.RELOADING);
    }



    @Override
    public void reUse(Vector2 position, float playerAngle) {

    }
    @Override
    public void reSetAll(){
        super.reSetAll();
        rowChanged = true;
    }


    @Override
    public void destroy() {
        active = false;
        destroyBits();
    }

    @Override
    public void disable(){
        initCategoryBits((short) 0, (short) 0, (short) 0);
    }



}
