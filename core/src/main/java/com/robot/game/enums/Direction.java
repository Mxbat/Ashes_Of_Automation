package com.robot.game.enums;

public enum Direction {
    LEFT, RIGHT, UP, DOWN;

    public Direction invert() {
        switch (this){
            case RIGHT: return LEFT;
            case LEFT: return RIGHT;
            case DOWN: return UP;
            default: return DOWN;
        }
    }
}
