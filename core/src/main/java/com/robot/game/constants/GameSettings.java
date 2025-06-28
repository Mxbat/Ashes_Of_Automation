package com.robot.game.constants;

import com.badlogic.gdx.Gdx;

public class GameSettings {
    public static final int TARGET_SCREEN_WIDTH = 1600;
    public static double multiplyCoefficient = (double) TARGET_SCREEN_WIDTH / Gdx.graphics.getWidth();
    public static float PPM  = 32f;
    public final static short SCREEN_WIDTH = (short) (Gdx.graphics.getWidth() * multiplyCoefficient);
    public final static short SCREEN_HEIGHT = (short) (Gdx.graphics.getHeight() * multiplyCoefficient);

    public static final float STAMINA_FONT_SCALE = 0.8F;
    public static final float HP_FONT_SCALE = 1.2F;
    public static final int ICONS_SIZE = 75;
    public static final int ICONS_X = 25;
    public static final int ICONS_Y_OFFSET = 125;
    public static final int WALLS_WIDTH = 50;
    public static final float SCORE_FONT_SCALE = 0.9f;
}
