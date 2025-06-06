package com.robot.game.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.ScreenUtils;
import com.robot.game.FontManager;
import com.robot.game.Main;
import com.robot.game.Resources;
import com.robot.game.constants.GameSettings;

import java.awt.ScrollPane;

public class StartScreen extends ScreenAdapter {
    Main main;
    OrthographicCamera camera = new OrthographicCamera();
    private float textCounter;
    BitmapFont font;
    FontManager fontManager = new FontManager();

    public StartScreen(Main main) {
        font = fontManager.getFont();
        this.main = main;
    }

    GameScreen gameScreen;
    Texture texture;
    Sprite sprite;


    public void show() {
        texture = new Texture(Resources.FLOOR_TEXTURE_PATH);
        camera.setToOrtho(false, GameSettings.SCREEN_WIDTH, GameSettings.SCREEN_HEIGHT);
        sprite = new Sprite(texture);
        sprite.setSize(3000, 3000);
        gameScreen = new GameScreen(main);

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);
        textCounter += Gdx.graphics.getDeltaTime();
        Main.batch.begin();
        Main.batch.setProjectionMatrix(camera.combined);
        //sprite.draw(Main.batch);
        Main.batch.draw(texture, 0, 0, 3000, 3000);
        font.setColor(Color.WHITE);
        font.getData().setScale(0.8f,0.8f);
        if(textCounter > 1){
            font.draw(Main.batch, "Нажмите чтобы начать", (float) GameSettings.SCREEN_WIDTH /2 - GameSettings.SCREEN_WIDTH /5f - font.getData().scaleX*96, (float) (GameSettings.SCREEN_HEIGHT/7));
        }
        font.getData().setScale(0.8f);
        font.setColor(Color.RED);
        font.getData().setScale(1.5f,1.5f);
        font.draw(Main.batch, "ASHES OF AUTOMATION", (float) GameSettings.SCREEN_WIDTH /2 - GameSettings.SCREEN_WIDTH /1.9f + font.getData().scaleX*96, (float) (GameSettings.SCREEN_HEIGHT/1.5));

        Main.batch.end();
        if(textCounter > 2){
            textCounter = 0;
        }
        if(Gdx.input.justTouched()){
            main.setNewScreen(new GameScreen(main));
        }
    }
    @Override
    public void hide(){
    }
    @Override
    public void pause(){

    }
    @Override
    public void resume(){

    }
    @Override
    public void dispose(){
        texture.dispose();
    }


}
