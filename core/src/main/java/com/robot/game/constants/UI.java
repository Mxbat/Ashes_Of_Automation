package com.robot.game.constants;

public class UI {

    public static final int JOYSTICK_MAX_OFFSET = GameSettings.SCREEN_HEIGHT/6;
    public static final float ATTACK_BUTTON_POS_X = (float) GameSettings.SCREEN_WIDTH /2 + (float) GameSettings.SCREEN_WIDTH/4;
    public static final float ATTACK_BUTTON_POS_Y = (float) ((float) GameSettings.SCREEN_HEIGHT/2 - (float) GameSettings.SCREEN_HEIGHT/3 * 1.2);
    public static final float ATTACK_BUTTON_DIAMETER = (float) GameSettings.SCREEN_HEIGHT /3;

    public static final float HEAL_BUTTON_POS_X = (float) GameSettings.SCREEN_WIDTH /2 + (float) GameSettings.SCREEN_WIDTH/4.5f;
    public static final float HEAL_BUTTON_POS_Y = (float) GameSettings.SCREEN_HEIGHT/2 - (float) GameSettings.SCREEN_HEIGHT/2.5f;
    public static final float HEAL_BUTTON_DIAMETER = (float) GameSettings.SCREEN_HEIGHT /5.2f;

    public static final float JOYSTICK_POS_X = (float) GameSettings.SCREEN_WIDTH /2 - (float) GameSettings.SCREEN_WIDTH/3;
    public static final float JOYSTICK_POS_Y = (float) GameSettings.SCREEN_HEIGHT/2 - (float) GameSettings.SCREEN_HEIGHT/3;
    public static final float JOYSTICK_DIAMETER = (float) GameSettings.SCREEN_HEIGHT /3;
    public final static short JOYSTICK_SAFE_ZONE = 700;

    public static final int PAUSE_BUTTON_DIAMETER = 100;

    public static final int MENU_BUTTON_WIDTH = GameSettings.SCREEN_WIDTH/3 - 20;
    public static final float START_BUTTON_POS_X = GameSettings.SCREEN_WIDTH - MENU_BUTTON_WIDTH;
    public static final float MENU_BUTTON_POS_Y = 0;
    public static final int MENU_BUTTON_HEIGHT = (int) ((double) GameSettings.SCREEN_WIDTH/3/2.5);

    public static final float SETTINGS_BUTTON_POS_X = GameSettings.SCREEN_WIDTH - MENU_BUTTON_WIDTH *2 - 30;
    public static final float EXIT_BUTTON_POS_X = 0;
    public static final int NAME_POS_X = GameSettings.SCREEN_WIDTH / 18;
    public static final int NAME_POS_Y = GameSettings.SCREEN_HEIGHT - 50;
    public static final int MENU_SIZE = (int) (GameSettings.SCREEN_HEIGHT / 1.1f);





}
