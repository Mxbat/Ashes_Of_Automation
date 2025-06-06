package com.robot.game.attacks;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.robot.game.Resources;
import com.robot.game.constants.EnemiesStat;
import com.robot.game.constants.GameSettings;
import com.robot.game.enemies.TurretEnemy;


public class TurretBullet extends EnemyAttack {
    TurretEnemy enemy;
    Vector2 direction = new Vector2();
    float dx, dy;
    boolean drawing;

    public TurretBullet(float x, float y, float angle, TurretEnemy enemy) {
        super(x, y, EnemiesStat.TURRET_BULLET_DIAMETER, new Texture(Resources.TURRET_BULLET_TEXTURE), (float) 0, (byte) 0, EnemiesStat.TURRET_BULLET_FLIGHT_DURATION, angle);
        this.enemy = enemy;
        sprite= new Sprite(texture);
        sprite.setSize(getWidth(), getHeight());
        sprite.setTexture(texture);
        sprite.setOriginCenter();
        sprite.setRotation(angle);
        drawing = false;
        hasToBeDestroyed = true;
        dx = (float) Math.round(sin(Math.toRadians(-angle)) * EnemiesStat.TURRET_PROJECTILE_SPEED);
        dy = (float) Math.round(cos(Math.toRadians(-angle)) * EnemiesStat.TURRET_PROJECTILE_SPEED);
        direction.set(dx, dy);
        getFixture().setSensor(true);
        body.setLinearVelocity(direction);
        body.setActive(true);
    }

    @Override
    public void update() {

        if(enemy.isSpawning()) {
            drawing = false;
            return;
        }
        currentDuration += Gdx.graphics.getDeltaTime();
        if(currentDuration > duration || hasToBeDestroyed){
            drawing = false;
            hasToBeDestroyed = true;
            disable();
            body.setAwake(false);
            body.setLinearVelocity(0 ,0);
        }
        setX(body.getPosition().x * GameSettings.PPM - getWidth()/2);
        setY(body.getPosition().y * GameSettings.PPM - getWidth()/2);
        sprite.setX(getX());
        sprite.setY(getY());
    }

    @Override
    public void draw(Batch batch) {
        done = drawing;
        if (done) sprite.draw(batch);
    }
    @Override
    public void reUse(Vector2 position, float angle){
        body.setActive(true);
        reSetBits();
        sprite.setRotation(angle);
        body.setTransform(position, 0);
        hasToBeDestroyed = false;
        currentDuration = 0;
        dx = (float) Math.round(sin(Math.toRadians(-angle)) * EnemiesStat.TURRET_PROJECTILE_SPEED);
        dy = (float) Math.round(cos(Math.toRadians(-angle)) * EnemiesStat.TURRET_PROJECTILE_SPEED);
        direction.set(dx, dy);
        body.setLinearVelocity(direction);
        update();
        drawing = true;

    }
    public void destroy(){
        active = false;
        drawing = false;
        destroyBits();
    }
}
