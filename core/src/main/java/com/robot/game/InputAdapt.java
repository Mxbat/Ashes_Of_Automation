package com.robot.game;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.robot.game.constants.GameSettings;
import com.robot.game.constants.UI;
import com.robot.game.enums.GameStage;
import com.robot.game.enums.PlayerBodyState;
import com.robot.game.screens.GameScreen;


public class InputAdapt extends com.badlogic.gdx.InputAdapter {
     World world;
    Vector3 touchPos;
    Main main;
    Vector3 touchJ;
    Vector3 touchB;
    Joystick joystick;

    Player pla;
    Camera camera;
    Array<Button> buttons;

    public InputAdapt(Main main, OrthographicCamera camera, Joystick joystick, Array<Button> buttons, Player pla,  World world) {
        this.main = main;
        this.buttons = buttons;
        this.camera = camera;
        this.joystick = joystick;
        touchJ = new Vector3();
        touchB = new Vector3();
        this.world = world;
        this.pla = pla;
    }


    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer){
        touchPos = new Vector3(screenX, screenY, 0);
        camera.unproject(touchPos);
        if(GameScreen.gameStage == GameStage.PLAYING){
            if( touchPos.x < UI.JOYSTICK_SAFE_ZONE){
                joystick.setJoystick(true);
                touchJ.x = touchPos.x;
                touchJ.y = touchPos.y;
                joystick.move(touchJ.x, touchJ.y);

            }
            else if( touchPos.x < (float) GameSettings.SCREEN_WIDTH /2){
                joystick.disable();
            }
        }
        return true;
    }
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        touchPos = new Vector3(screenX,screenY, 0);
        camera.unproject(touchPos);
        if(GameScreen.gameStage == GameStage.PLAYING){
            if(touchPos. x < UI.JOYSTICK_SAFE_ZONE && joystick.g > 100){
                joystick.setJoystick(true);
                touchJ.x = touchPos.x;
                touchJ.y = touchPos.y;
                joystick.move(touchJ.x, touchJ.y);

            }

            if((buttons.get(0).getTouch(touchPos.x, touchPos.y) && pla.attack.attackCounter < 0 )){
                pla.attack.resetAttack();
                AudioManager.playPlayerAttack();
                pla.isAttacking = true;
            }

            if(buttons.get(1).getTouch(touchPos.x, touchPos.y)){
                joystick.setJoystick(false);
                GameScreen.gameStage = GameStage.PAUSED;
                buttons.get(1).changeState(true);
            }
            if(touchPos.x > UI.JOYSTICK_SAFE_ZONE
                && !(buttons.get(0).getTouch(touchPos.x, touchPos.y))){
                pla.setRunning(true);
            }
        }
        else {
            if(buttons.get(1).getTouch(touchPos.x, touchPos.y)){
                GameScreen.gameStage = GameStage.PLAYING;
                buttons.get(1).changeState(false);
            }
        }
        return true;
    }
    public boolean touchUp(int screenX, int screenY, int pointer, int button){
        touchPos = Main.camera.unproject(new Vector3(screenX, screenY, 0));
        if(GameScreen.gameStage == GameStage.PLAYING) {
            if (touchPos.x < (float) GameSettings.SCREEN_WIDTH / 2) {
                joystick.disable();
            }
            if (touchPos.x > (float) GameSettings.SCREEN_WIDTH / 2) {
                pla.isAttacking = false;
                pla.setRunningBlocked(false);
                pla.setRunning(false);
            }
        }
        return true;
    }

}

