package com.robot.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Button {
    float x;
    float y;
    float a;
    Sprite sprite;
    Texture texture;
    TextureRegion textureRegion;

    public Button(float x, float y, float a, Texture texture) {
        this.x = x;
        this.y = y;
        this.a = a;

        sprite = new Sprite(texture);
        sprite.setSize(a, a);
        sprite.setX(x);
        sprite.setY(y);
    }
    public Button(float x, float y, float a, int scrWidth, int scrHeight, TextureRegion textureRegion) {
        this.x = x;
        this.y = y;
        this.a = a;
        this.textureRegion = textureRegion;
        sprite = new Sprite(textureRegion, 0, 0,scrWidth, scrHeight);
        sprite.setSize(a, a);
        sprite.setX(x);
        sprite.setY(y);
    }
    public void changeState(boolean b){
        if(b){
            sprite.setRegion(textureRegion, 100, 0 , 100 , 100);
        }
        else {
            sprite.setRegion(textureRegion, 0, 0 , 100 , 100);
        }
    }

    public void draw(Batch batch){
        sprite.draw(batch);
    }
    public boolean getTouch(float tx, float ty){
        return tx >x && ty > y && tx<x+a && ty < y + a;
    }
}
