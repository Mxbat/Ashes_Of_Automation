package com.robot.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class Save {
    static Preferences prefs = Gdx.app.getPreferences("My Preferences");


    public static long getMaxScore(){
        System.out.println(prefs.getLong("maxScore") + "new score");
        return prefs.getLong("maxScore");
    }
    public static boolean getSoundDisabled(){
        return prefs.getBoolean("sound");
    }
    public static boolean getMusicDisabled(){
        return prefs.getBoolean("music");
    }
    public static void enableSound(){
        prefs.putBoolean("sound", false);
    }
    public static void disableSound(){
        prefs.putBoolean("sound", true);
    }
    public static void enableMusic(){
        AudioManager.startMusic();
        prefs.putBoolean("music", false);
    }
    public static void disableMusic(){
        AudioManager.stopMusic();
        prefs.putBoolean("music", true);
    }

    public static void rewriteIfNeed(long score){
        if(score > getMaxScore()){
            prefs.putLong("maxScore", score);
            prefs.flush();
        }
    }
    public static void flush(){
        prefs.flush();
    }
}
