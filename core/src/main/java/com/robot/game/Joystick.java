package com.robot.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.robot.game.constants.UI;

public class Joystick {
    Texture body;
    Texture control;

    public void setJoystick(boolean joystick) {
        isJoystick = joystick;
    }

    private boolean isJoystick;
    float startPosX, startPosY;
    public boolean isSendingMove;
    public boolean isStarted;
    Main main;
    double dx;
    double dy;
    double g;
    float x;
    float y;
    float cx;
    float cy;
    int width;

    int cw;
    float prPosX;
    float prPosY;
    float angle;


    public float getY() {
        return y;
    }


    public float getX() {
        return x;
    }

    public Joystick(int x, int y,
                    int width,
                    Main main) {

        control = new Texture(Resources.JOYSTICK_CONTROL);
        body = new Texture(Resources.JOYSTICK_BODY);

        this.main = main;
        this.x = x;
        this.y = y;
        this.cx = x + (float) width /4;
        this.cy = y + (float) width /4;
        this.width = width;
        this.cw = width/4;
        isStarted = false;
        isJoystick = false;
        isSendingMove = false;

    }

    public void normalize(){
        prPosX = getX();
        prPosY = getY();
        g = Utils.doPythagoras(x + (float) width /2, y + (float) width /2,cx + (float) cw /2, cy + (float) cw /2);
        if(g > UI.JOYSTICK_MAX_OFFSET){
            dx = Math.sin(Math.toRadians(-getAngle())) * (g - UI.JOYSTICK_MAX_OFFSET);
            dy = Math.cos(Math.toRadians(-getAngle())) * (g - UI.JOYSTICK_MAX_OFFSET);
            x += (float) dx;
            y += (float) dy;

        }

    }
    public void move(float touchX, float touchY){

        if (isJoystick) {
            normalize();
            if(!isStarted){
                startPosX = touchX - width/2f;
                startPosY = touchY - width/2f;
                x = startPosX;
                y = startPosY;
                isSendingMove = true;
                isStarted = true;

            }
            cx = (int) touchX - cw/2f;
            cy = (int) touchY - cw/2f;

        }
    }
    public float getAngle(){
            angle = (float) Math.toDegrees(CalculateAngle()) + 180;

        return angle;
    }
    public float CalculateAngle(){
        angle = (float) Math.atan2((cx + cw/2f) - (x + width/2f), (y + width/2f) - (cy + cw/2f));
        return angle;
    }
    public void draw(Batch batch){
        if(!isJoystick){
            isSendingMove = false;
            isStarted = false;
           setDefaultPositions();
        }
        batch.draw(body, x, y, width, width);
        batch.draw(control, cx, cy, cw, cw);

    }
    public void disable(){
        isJoystick = false;
        isSendingMove = false;
    }
    public void setDefaultPositions(){
        x = UI.JOYSTICK_POS_X;
        y = UI.JOYSTICK_POS_Y;
        cx = x + (float) width /4;
        cy = y + ((float) width / 4);
        cw = width/2;
        angle = 0;
    }


}
