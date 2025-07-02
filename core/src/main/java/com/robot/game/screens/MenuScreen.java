package com.robot.game.screens;


import static com.robot.game.Main.batch;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.robot.game.Button;
import com.robot.game.FontManager;
import com.robot.game.Main;
import com.robot.game.MenuInputAdapt;
import com.robot.game.Resources;
import com.robot.game.Save;
import com.robot.game.constants.GameSettings;
import com.robot.game.constants.UI;

public class MenuScreen extends ScreenAdapter {
    Main main;
    OrthographicCamera camera = new OrthographicCamera();
    Button startButton;
    Button settingsButton;
    Button exitButton;
    Texture uiButtonTexture = new Texture(Resources.UI_BUTTON_TEXTURE);
    BitmapFont font;
    FontManager fontManager = new FontManager();

    public MenuScreen(Main main) {
        font = fontManager.getFont();
        this.main = main;
    }

    GameScreen gameScreen;
    Texture bgTexture;
    Sprite bg;
    Array<Button> buttons = new Array<>();


    public void show() {
        camera.setToOrtho(false, GameSettings.SCREEN_WIDTH, GameSettings.SCREEN_HEIGHT);
        startButton = new Button(UI.START_BUTTON_POS_X, UI.MENU_BUTTON_POS_Y, UI.MENU_BUTTON_WIDTH,
            UI.MENU_BUTTON_HEIGHT, uiButtonTexture, true);
        settingsButton = new Button(UI.SETTINGS_BUTTON_POS_X, UI.MENU_BUTTON_POS_Y, UI.MENU_BUTTON_WIDTH,
            UI.MENU_BUTTON_HEIGHT, uiButtonTexture, true);
        exitButton = new Button(UI.EXIT_BUTTON_POS_X, UI.MENU_BUTTON_POS_Y, UI.MENU_BUTTON_WIDTH,
            UI.MENU_BUTTON_HEIGHT, uiButtonTexture, true);

        buttons.add(startButton);
        buttons.add(settingsButton);
        buttons.add(exitButton);
        Gdx.input.setInputProcessor(new MenuInputAdapt(startButton, settingsButton, exitButton, main));

        bgTexture = new Texture(Resources.FLOOR_TEXTURE_PATH);

        bg = new Sprite(bgTexture);
        bg.setSize(3000, 3000);
        gameScreen = new GameScreen(main);

    }

    @Override
    public void render(float delta) {
        Vector3 touchPos = new Vector3(Gdx.input.getX(),Gdx.input.getY(), 0);
        camera.unproject(touchPos);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        bg.draw(batch);
        for (Button b :
            buttons) {
            b.draw(batch);
        }
        font.getData().setScale(1);
        font.setColor(Color.WHITE);
        font.draw(batch, "ИГРАТЬ", UI.START_BUTTON_POS_X + UI.MENU_BUTTON_WIDTH/5, UI.MENU_BUTTON_POS_Y + UI.MENU_BUTTON_HEIGHT/2 + 30);

        font.draw(batch, "ВЫХОД", UI.EXIT_BUTTON_POS_X + UI.MENU_BUTTON_WIDTH/5, UI.MENU_BUTTON_POS_Y + UI.MENU_BUTTON_HEIGHT/2 + 30);
        font.getData().setScale(0.8f);
        font.draw(batch, "НАСТРОЙКИ", UI.SETTINGS_BUTTON_POS_X + UI.MENU_BUTTON_WIDTH/9, UI.MENU_BUTTON_POS_Y + UI.MENU_BUTTON_HEIGHT/2 + 25);

        font.draw(batch, "Рекорд:" + Save.getMaxScore(), (float) GameSettings.SCREEN_WIDTH /2 - GameSettings.SCREEN_WIDTH /7.4f , UI.NAME_POS_Y - 200);
        font.getData().setScale(1.5f);
        font.setColor(Color.RED);
        font.draw(batch, "ASHES OF AUTOMATION", UI.NAME_POS_X, UI.NAME_POS_Y);
        batch.end();

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
        bgTexture.dispose();
    }


}
