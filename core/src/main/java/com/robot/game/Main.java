package com.robot.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.World;
import com.robot.game.constants.GameSettings;
import com.robot.game.screens.GameOverScreen;
import com.robot.game.screens.StartScreen;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {

    public static SpriteBatch batch;
    public static AudioManager audioManager;
    public static GameOverScreen gameOverScreen;
    public static OrthographicCamera camera;
    public static World world;


    public static StartScreen startScreen;

    public void setNewScreen(ScreenAdapter screen) {
        setScreen(screen);
    }


    @Override
    public void create() {
        audioManager = new AudioManager();
        gameOverScreen = new GameOverScreen(this);



        Box2D.init();
        world = new World(new Vector2(0, 0), true);



        camera = new OrthographicCamera();

        camera.setToOrtho(false, GameSettings.SCREEN_WIDTH, GameSettings.SCREEN_HEIGHT);
        batch = new SpriteBatch();


        startScreen = new StartScreen(this);
        setNewScreen(startScreen);
    }



    @Override
    public void dispose() {
        batch.dispose();
    }

}
