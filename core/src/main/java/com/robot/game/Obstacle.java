package com.robot.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.robot.game.constants.FilterBits;
import com.robot.game.constants.GameSettings;

public class Obstacle extends GameObject{
    Animation animation;
    Sprite glowSprite;
    Animation glowAnimation;


    public Obstacle(float x, float y, int width, int height, Texture texture) {
        super(x, y, width, height, texture, BodyDef.BodyType.StaticBody);

        sprite = new Sprite(texture);
        sprite.setSize(width, height);
        sprite.setPosition(x, y);

    }

    public Obstacle(float x, float y, float width, float height, TextureRegion textureRegion, TextureRegion glowRegion, int frames, boolean rotated, int frameWidth, int frameHeight) {
        super(x, y, width, height, textureRegion, BodyDef.BodyType.StaticBody);


        if (rotated) {
            sprite = new Sprite(textureRegion, 0, 0, frameHeight, frameWidth);

            sprite.setOrigin(0, 0);
            sprite.setRotation(90);
            glowSprite = new Sprite(glowRegion, 0, 0, frameHeight, frameWidth);
            glowSprite.setOrigin(0, 0);
            sprite.setRotation(90);
            glowSprite.setRotation(90);
            animation = new Animation(textureRegion, sprite, frames, frameHeight, frameWidth);
            glowAnimation = new Animation(glowRegion, glowSprite, frames, frameHeight, frameWidth);
            glowSprite.setSize(height, width);
            glowSprite.setPosition(x + width, y);
            sprite.setSize(height, width);
            sprite.setPosition(x + width, y);

        } else {
            sprite = new Sprite(textureRegion, 0, 0, frameWidth, frameHeight);
            animation = new Animation(textureRegion, sprite, frames, frameWidth, frameHeight);
            sprite.setOrigin(0, 0);

            glowSprite = new Sprite(glowRegion, 0, 0, frameWidth, frameHeight);
            glowSprite.setOrigin(0, 0);
            glowAnimation = new Animation(glowRegion, glowSprite, frames, frameWidth, frameHeight);
            glowSprite.setSize(width, height);
            glowSprite.setPosition(x, y);
            sprite.setSize(width, height);
            sprite.setPosition(x, y);
        }

    }
    public Obstacle(float x, float y, float width, float height, Texture texture) {
        super(x, y, width, height, texture, BodyDef.BodyType.StaticBody);

        sprite = new Sprite(texture);
        sprite.setSize(width, height);
        sprite.setPosition(x, y);

    }

    public void draw(SpriteBatch batch){
        if(animation == null) {
            batch.draw(texture, sprite.getX(), sprite.getY(), getWidth(), getHeight());
            return;
        }

        sprite.draw(batch);
    }
    public void update(){
        if(animation == null){
            return;
        }
        glowAnimation.play();
        animation.play();
    }
    public void drawGlow(SpriteBatch batch){
        if(glowAnimation == null) {
            return;
        }

        glowSprite.draw(batch);
    }

    public void setBody(Body body) {
        this.body = body;
    }
}
