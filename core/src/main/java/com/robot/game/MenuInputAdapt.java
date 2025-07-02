package com.robot.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector3;
import com.robot.game.screens.GameScreen;


public class MenuInputAdapt extends InputAdapter {
    Button startButton;
    Button settingButton;
    Button exitButton;
    Main main;

    public MenuInputAdapt(Button startButton, Button settingButton, Button exitBUtton, Main main) {
        this.startButton = startButton;
        this.settingButton = settingButton;
        this.exitButton = exitBUtton;
        this.main = main;
    }

    Vector3 touchPos;
    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer){
        touchPos = Main.camera.unproject(new Vector3(screenX, screenY, 0));


        return true;
    }
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        touchPos = Main.camera.unproject(new Vector3(screenX, screenY, 0));

        return true;
    }
    public boolean touchUp(int screenX, int screenY, int pointer, int button){
        touchPos = Main.camera.unproject(new Vector3(screenX, screenY, 0));
        if(settingButton.getTouch(touchPos.x, touchPos.y)){
            main.setScreen(Main.settingsScreen);
        }
        else if(startButton.getTouch(touchPos.x, touchPos.y)){
            main.setScreen(new GameScreen(main));
        }
        else if(exitButton.getTouch(touchPos.x, touchPos.y)){
            Gdx.app.exit();
        }
        return true;
    }


}

