package com.robot.game;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector3;


public class GameOverInputAdapt extends InputAdapter {
    Button menuButton;
    Main main;
    long finalScore;

    public GameOverInputAdapt(Button menuButton, Main main, long finalScore) {
        this.menuButton = menuButton;
        this.main = main;
        this.finalScore = finalScore;
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
        if(menuButton.getTouch(touchPos.x, touchPos.y)){
            Save.rewriteIfNeed(finalScore);
            main.setScreen(Main.menuScreen);
        }
        return true;
    }


}

