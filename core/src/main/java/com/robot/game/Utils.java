package com.robot.game;


import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

public class Utils {
    public static double doPythagoras(Vector2 start, Vector2 end){
        return Math.sqrt((start.x - end.x) * (start.x - end.x) + (start.y - end.y) * (start.y - end.y));
    }
    public static double doPythagoras(float x1, float y1, float x2, float y2){
        return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }
    public static void updateChild(Sprite child, Sprite parent, float localX, float localY, float localRotation) {

        float parentRotation = parent.getRotation();
        float radians = MathUtils.degreesToRadians * parentRotation;

        float rotatedX = localX * MathUtils.cos(radians) - localY * MathUtils.sin(radians);
        float rotatedY = localX * MathUtils.sin(radians) + localY * MathUtils.cos(radians);

        child.setPosition(
            parent.getX() + parent.getOriginX() + rotatedX,
            parent.getY() + parent.getOriginY() + rotatedY
        );

        child.setRotation(parentRotation + localRotation);
    }
    public static int getScrapPerEnemy(){
        Random random = new Random();
        return random.nextInt(3) + 3;
    }

}
