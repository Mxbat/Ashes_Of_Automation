package com.robot.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Button {
    float x;
    float y;
    float w, h;
    Sprite sprite;
    float srcWidth, srcHeight;
    Texture texture;
    TextureRegion textureRegion;
    private boolean ui;

    public Button(float x, float y, float w, float h, Texture texture, boolean ui) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.ui = ui;
        sprite = new Sprite(texture);
        sprite.setSize(w, h);
        sprite.setX(x);
        sprite.setY(y);
    }

    public Button(float x, float y, float w, float h, float srcWidth, float srcHeight, TextureRegion textureRegion, boolean ui) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.srcWidth = srcWidth;
        this.srcHeight = srcHeight;
        this.textureRegion = textureRegion;
        this.ui = ui;
        sprite = new Sprite(textureRegion, 0, 0, (int) srcWidth, (int) srcHeight);
        sprite.setSize(w, h);
        sprite.setX(x);
        sprite.setY(y);
    }
    public void changeState(boolean b){
        if(b){
            sprite.setRegion(textureRegion, (int) srcWidth, 0 , (int) srcWidth, (int) srcHeight);
        }
        else {
            sprite.setRegion(textureRegion, 0, 0 , (int) srcWidth, (int) srcHeight);
        }
    }

    public void draw(Batch batch){
        sprite.draw(batch);
    }
    public boolean getTouch(float tx, float ty){
        if(tx >x && ty > y && tx<x+w && ty < y + h && ui){
            AudioManager.playClick();
        }
        return tx >x && ty > y && tx<x+w && ty < y + h;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}
