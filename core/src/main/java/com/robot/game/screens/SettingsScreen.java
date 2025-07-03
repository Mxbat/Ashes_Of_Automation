package com.robot.game.screens;

import static com.robot.game.Main.batch;
import static com.robot.game.Main.camera;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.robot.game.Button;
import com.robot.game.FontManager;
import com.robot.game.Main;
import com.robot.game.Resources;
import com.robot.game.Save;
import com.robot.game.constants.GameSettings;
import com.robot.game.constants.UI;

public class SettingsScreen extends ScreenAdapter {
    BitmapFont font;
    FontManager fontManager = new FontManager();
    Texture bgTexture;
    Sprite bg;

    Texture menuTexture;
    Sprite menu;
    public SettingsScreen(Main main) {
        font = fontManager.getFont();

        this.main = main;
    }

    Button backButton;
    Button musicCheckBox;
    Button soundCheckBox;

    Main main;
    Texture backTexture;
    TextureRegion checkBoxTexture;

    @Override
    public void show () {
        Gdx.input.setInputProcessor(null);
        menuTexture = new Texture(Resources.MENU_TEXTURE);
        menu = new Sprite(menuTexture);
        checkBoxTexture = new TextureRegion(new Texture(Resources.CHECK_BOX_TEXTURE));
        menu.setSize(UI.MENU_SIZE, UI.MENU_SIZE);
        menu.setPosition(GameSettings.SCREEN_WIDTH/2f - UI.MENU_SIZE/2f, GameSettings.SCREEN_HEIGHT/2f - UI.MENU_SIZE/2f);

        musicCheckBox = new Button(menu.getX() + UI.MENU_SIZE/6f, menu.getY() + UI.MENU_SIZE/2f, 100, 100, 100, 100, checkBoxTexture, true);
        soundCheckBox = new Button(menu.getX() + UI.MENU_SIZE/6f, menu.getY() + UI.MENU_SIZE/4f, 100, 100,100, 100, checkBoxTexture, true);

        backTexture = new Texture(Resources.BACK_BUTTON_TEXTURE);
        backButton = new Button(10, GameSettings.SCREEN_HEIGHT - 160, 150, 150, backTexture, true);
        bgTexture = new Texture(Resources.FLOOR_TEXTURE_PATH);
        bg = new Sprite(bgTexture);
        bg.setSize(3000, 3000);

        musicCheckBox.changeState(!Save.getMusicDisabled());
        soundCheckBox.changeState(!Save.getSoundDisabled());

    }

    @Override
    public void render (float delta) {

        Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(touchPos);
        if(Gdx.input.justTouched()){

            if(backButton.getTouch(touchPos.x,touchPos.y)){
                main.setScreen(Main.menuScreen);
            }

            if(musicCheckBox.getTouch(touchPos.x,touchPos.y)){
                if(!Save.getMusicDisabled())Save.disableMusic();
                else Save.enableMusic();
                Save.flush();
                musicCheckBox.changeState(!Save.getMusicDisabled());


            }

            if(soundCheckBox.getTouch(touchPos.x,touchPos.y)){
                if(!Save.getSoundDisabled())Save.disableSound();
                else Save.enableSound();
                Save.flush();
                soundCheckBox.changeState(!Save.getSoundDisabled());

            }

        }


        ScreenUtils.clear(new Color(0.1f, 0.1f, 0.1f, 1));
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        menu.draw(batch);
        backButton.draw(batch);
        musicCheckBox.draw(batch);
        soundCheckBox.draw(batch);
        font.draw(batch, "Музыка",menu.getX() + UI.MENU_SIZE/3f, musicCheckBox.getY() + 85);
        font.draw(batch, "Звуки",menu.getX() + UI.MENU_SIZE/3f, soundCheckBox.getY() + 85);
        font.draw(batch, "НАСТРОЙКИ",GameSettings.SCREEN_WIDTH/2f - GameSettings.SCREEN_WIDTH/6.7f, GameSettings.SCREEN_HEIGHT - UI.MENU_SIZE/4f);
        batch.end();
    }


}
