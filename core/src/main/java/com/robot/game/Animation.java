package com.robot.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Animation {
    int frameWidth;
    int frameHeight;
    public int rowsWidth;
    Sprite sprite;
    private int fps;


    private int defaultY = 0;

    public boolean isCycleEnded() {
        return currentFrame >= frames - 1;
    }


    public void setX(int x) {
        this.x = x;
    }

    public int x = 0;
    public int y;


    public void setTextureRegion(TextureRegion textureRegion) {
        this.textureRegion = textureRegion;
    }

    TextureRegion textureRegion;
    public int currentFrame = 0;
    float counter;


    public void setFrames(int frames) {
        this.frames = frames;
    }

    public int frames;

    public Animation(TextureRegion textureRegion, Sprite sprite, int frames, int frameWidth, int frameHeight) {
        this.frameWidth =  frameWidth;
        this.frameHeight = frameHeight;
        this.frameHeight *= 25;
        this.frameWidth *= 25;
        this.textureRegion = textureRegion;
        this.sprite = sprite;
        this.frames = frames;
        rowsWidth = frameWidth * (textureRegion.getRegionWidth()/frameWidth);
        fps = 24;

        y = 0;

    }

    public void play(){
        if(stop) return;

        counter += Gdx.graphics.getDeltaTime();

        if(counter >= (float) 1/fps){

            currentFrame +=1;
            if(x < rowsWidth - frameWidth) {
                x += frameWidth;
            }
            else {

                if(currentFrame < frames){
                    y += frameHeight;
                    x = 0;
                }
            }
            counter = 0;
        }

        if(currentFrame >= frames){
            currentFrame = 0;
            x = 0;
            y = defaultY;

        }

        sprite.setRegion(x, y, frameWidth, frameHeight);
        fps = 24;

    }
    public void changeRow(int newRow){

        y = newRow * frameHeight;
        defaultY = y;

    }

    public void setCurrentFrame(int i) {
        currentFrame = i;
    }

    public void speedUp(float value){
        fps = Math.round(fps*value);
    }
    boolean stop = false;
    public void setStop(boolean stop) {
        this.stop = stop;
    }
    public void setFrameSize(int frameWidth, int frameHeight){
        this.frameWidth = frameWidth*25;
        frameWidth *= 25;
        rowsWidth = frameWidth * (textureRegion.getRegionWidth()/frameWidth);
        this.frameHeight = frameHeight*25;
    }
}
