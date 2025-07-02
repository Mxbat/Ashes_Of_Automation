package com.robot.game.screens;


import static com.robot.game.Main.batch;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.ScreenUtils;
import com.robot.game.AudioManager;
import com.robot.game.Button;
import com.robot.game.FontManager;
import com.robot.game.GameOverInputAdapt;
import com.robot.game.Main;
import com.robot.game.Resources;
import com.robot.game.Save;
import com.robot.game.constants.GameSettings;
import com.robot.game.constants.UI;

public class GameOverScreen extends ScreenAdapter {
    float startCounter;
    Main main;
    private float accessCounter;
    OrthographicCamera camera = new OrthographicCamera();
    BitmapFont font;
    FontManager fontManager = new FontManager();
    Button menuButton;
    private final long finalScore;
    private float newHighScoreTextCounter = 0f;
    Texture uiButtonTexture = new Texture(Resources.UI_BUTTON_TEXTURE);

    public GameOverScreen(Main main, long finalScore) {

        font = fontManager.getFont();
        this.main = main;
        this.finalScore = finalScore;
    }

    GameScreen gameScreen;



    public void show() {

        AudioManager.playGameOver();
        menuButton = new Button(UI.START_BUTTON_POS_X, UI.MENU_BUTTON_POS_Y, UI.MENU_BUTTON_WIDTH,
            UI.MENU_BUTTON_HEIGHT, uiButtonTexture, true);

        startCounter = 2;

        camera.setToOrtho(false, GameSettings.SCREEN_WIDTH, GameSettings.SCREEN_HEIGHT);

        gameScreen = new GameScreen(main);

    }

    @Override
    public void render(float delta) {



        newHighScoreTextCounter += Gdx.graphics.getDeltaTime();
        batch.begin();
        ScreenUtils.clear(new Color(0.1f, 0.1f, 0.1f, 1));
        batch.setProjectionMatrix(camera.combined);
        if(finalScore > Save.getMaxScore()){
            if(newHighScoreTextCounter > 1){
                font.getData().setScale(0.8f);
                font.setColor(Color.SKY);
                font.draw(batch, "Новый Рекорд!", (float) GameSettings.SCREEN_WIDTH /2 - GameSettings.SCREEN_WIDTH /7.4f - 20, UI.NAME_POS_Y - 300);
            }
            if(newHighScoreTextCounter > 2){
                newHighScoreTextCounter = 0;
            }
        }




        if(accessCounter > 2){
            menuButton.draw(batch);
            font.draw(batch, "МЕНЮ", UI.START_BUTTON_POS_X + UI.MENU_BUTTON_WIDTH/4f, UI.MENU_BUTTON_POS_Y + UI.MENU_BUTTON_HEIGHT/2f + 30);
            Gdx.input.setInputProcessor(new GameOverInputAdapt(menuButton, main, finalScore));
        }
        else {
            accessCounter += Gdx.graphics.getDeltaTime();
        }

        font.setColor(Color.WHITE);
        font.getData().setScale(1);


        font.setColor(Color.WHITE);
        font.getData().setScale(0.8f);




        font.setColor(Color.RED);
        font.getData().setScale(1.5f);
        font.draw(batch, "ИГРА ОКОНЧЕНА", (float) GameSettings.SCREEN_WIDTH /2 - GameSettings.SCREEN_WIDTH /3.35f, UI.NAME_POS_Y);
        font.setColor(Color.WHITE);
        font.getData().setScale(0.8f);
        font.draw(batch, "Ваш счёт:" + finalScore, (float) GameSettings.SCREEN_WIDTH /2 - GameSettings.SCREEN_WIDTH /7.4f , UI.NAME_POS_Y - 200);

        batch.end();

    }


}
