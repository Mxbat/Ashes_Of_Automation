package com.robot.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.robot.game.constants.GameSettings;
import com.robot.game.screens.GameOverScreen;
import com.robot.game.screens.MenuScreen;
import com.robot.game.screens.SettingsScreen;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {

    public static SpriteBatch batch;
    public static AudioManager audioManager;

    public static OrthographicCamera camera;



    public static MenuScreen menuScreen;
    public static SettingsScreen settingsScreen;




    @Override
    public void create() {
        settingsScreen = new SettingsScreen( this);
        audioManager = new AudioManager();




        Box2D.init();




        camera = new OrthographicCamera();

        camera.setToOrtho(false, GameSettings.SCREEN_WIDTH, GameSettings.SCREEN_HEIGHT);
        batch = new SpriteBatch();


        menuScreen = new MenuScreen(this);
        setScreen(menuScreen);
    }



    @Override
    public void dispose() {
        batch.dispose();
    }

}
