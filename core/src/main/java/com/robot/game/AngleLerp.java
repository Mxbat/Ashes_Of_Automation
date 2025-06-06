package com.robot.game;

public class AngleLerp {

    float currentAngle;
    float targetAngle;
    static float delta = 7;
    private static final float EPSILON = 0.5f;
    public AngleLerp(float currentAngle, float targetAngle) {

        delta = 7;
        this.currentAngle = getPathToAngle(0.0f, currentAngle);
        this.targetAngle = getPathToAngle(0.0f, targetAngle);
    }

    public static float returnAngle(float currentAngle, float targetAngle, int delta){
        currentAngle = getPathToAngle(0.0f, currentAngle);
        targetAngle = getPathToAngle(0.0f, targetAngle);


        if(Math.abs(targetAngle - currentAngle) > EPSILON){
                currentAngle += getPathToAngle(currentAngle, targetAngle)/delta;
        }
        else {
            return targetAngle;
        }
        return currentAngle;



    }
    public static float getPathToAngle(float source, float target) {
        float diff = target - source;
        diff %= 360.0f;
        if (diff > 180.0f) {
            diff -= 360.0f;
        } else if (diff <= -180.0f) {
            diff += 360.0f;
        }
        return diff;
    }
    public static float roundToNearestAngle(float angle) {

        float normalized = getPathToAngle(0.0f, angle);


        float[] targets = {-180.0f,  -90.0f,
            0.0f,  90.0f,  180.0f};

        float minDiff = Float.MAX_VALUE;
        float closestAngle = 0.0f;


        for (float target : targets) {

            float diff = Math.abs(getPathToAngle(normalized, target));


            if (diff < minDiff) {
                minDiff = diff;
                closestAngle = target;
            }
        }

        return closestAngle;
    }

}

