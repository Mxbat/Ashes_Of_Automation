package com.robot.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class AudioManager {
    static private Sound doorSound;
    static private Sound gameOver;
    static private Sound enemyAttack;
    static private Sound click;
    static private Sound score;
    static private Sound playerAttack;
    private static final float playerAttackVolume = 2f;
    static float doorSoundVolume = 0.15f;
    static Music music;
    static private Sound hit;
    private static final float gameOverSoundVolume = 2f;
    private static final float enemyAttackSoundVolume = 0.15f;
    private static final float hitSoundVolume = 0.4f;
    public static final float scoreSoundVolume = 0.5f;
    public static final float clickSoundVolume = 0.5f;

    public static void init() {
        click = Gdx.audio.newSound(Gdx.files.internal(Resources.SCORE_SOUND));
        score = Gdx.audio.newSound(Gdx.files.internal(Resources.SCORE_SOUND));
        enemyAttack = Gdx.audio.newSound(Gdx.files.internal(Resources.ENEMY_SOUND));
        hit = Gdx.audio.newSound(Gdx.files.internal(Resources.HIT_SOUND));
        playerAttack = Gdx.audio.newSound(Gdx.files.internal(Resources.PLAYER_ATTACK_SOUND));
        doorSound = Gdx.audio.newSound(Gdx.files.internal(Resources.DOOR_SOUND));
        gameOver = Gdx.audio.newSound(Gdx.files.internal(Resources.GAME_OVER));
        music = Gdx.audio.newMusic(Gdx.files.internal(Resources.MUSIC));
        music.setLooping(true);
        music.setVolume(0.7f);
        if(!Save.getMusicDisabled()){
            music.play();
        }

    }
    public static void stopMusic(){
        music.stop();
    }
    public static void startMusic(){
        music.play();
    }
    public static void pauseMusic(){
        music.pause();
    }

    public static void playDoor(){
        if(Save.getSoundDisabled()){
            return;
        }
        doorSound.play(doorSoundVolume);
    }
    public static void playGameOver(){
        if(Save.getSoundDisabled()){
            return;
        }
        gameOver.play(gameOverSoundVolume);
    }
    public static void playPlayerAttack(){
        if(Save.getSoundDisabled()){
            return;
        }
        playerAttack.play(playerAttackVolume);
    }
    public static void playScore(){
        if(Save.getSoundDisabled()){
            return;
        }
        score.play(scoreSoundVolume);
    }
    public static void playClick(){
        if(Save.getSoundDisabled()){
            return;
        }
        click.play(clickSoundVolume);
    }
    public static void playEnemyAttack(){
        if(Save.getSoundDisabled()){
            return;
        }
        enemyAttack.play(enemyAttackSoundVolume);
    }

    public static void playHit() {
        if(Save.getSoundDisabled()){
            return;
        }
        hit.play(hitSoundVolume);
    }
}
