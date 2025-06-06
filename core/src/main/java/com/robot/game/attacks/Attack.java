package com.robot.game.attacks;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.robot.game.GameObject;



public abstract class Attack extends GameObject {

    public int damage;

    public boolean isActive() {
        return active;
    }

    public boolean active = false;
    public float duration;
    public float currentDuration = 0;
    public float angle;
    public float range;
    public boolean done = true;

    public void setHasToBeDestroyed(boolean hasToBeDestroyed) {
        this.hasToBeDestroyed = hasToBeDestroyed;
    }

    public boolean hasToBeDestroyed = false;
    float cooldownTimer;

    public Attack(float x, float y, float width, float height, Texture texture, float range, int damage, float duration, float angle) {
        super(x, y, width, height, texture, BodyDef.BodyType.DynamicBody);
        this.range = range;
        this.damage = damage;
        this.duration = duration;
        this.angle = angle;
        body = createBody(BodyDef.BodyType.DynamicBody, x, y, width, height);
        getFixture().setSensor(true);
    }
    public Attack(float x, float y, float diameter, Texture texture, float range, int damage, float duration, float angle) {
        super(x, y, diameter, diameter, texture, BodyDef.BodyType.DynamicBody);
        this.range = range;
        this.damage = damage;
        this.texture = texture;
        this.duration = duration;
        this.angle = angle;
        body = createBody(BodyDef.BodyType.DynamicBody, x, y, diameter, false, false);

        getFixture().setSensor(true);
    }



    abstract public void update();
    abstract public void draw(Batch batch);
    public void disable(){
        body.setActive(false);
        done = true;
    }
    public void reSetBits(){
        body.setActive(true);
    }
    public void reSetAll(){
        done = true;
        cooldownTimer = 0;
        currentDuration = 0;
    }
    public abstract void reUse(Vector2 position, float playerAngle);
    public abstract void destroy();
    public void destroyBits(){
        initCategoryBits((short) 0, (short) 0, (short) 0);
    }

}
