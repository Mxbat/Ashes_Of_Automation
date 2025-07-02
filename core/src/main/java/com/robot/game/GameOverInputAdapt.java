package com.robot.game;

import static com.robot.game.Main.camera;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.robot.game.constants.GameSettings;
import com.robot.game.constants.UI;
import com.robot.game.enums.GameStage;
import com.robot.game.screens.GameScreen;


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

