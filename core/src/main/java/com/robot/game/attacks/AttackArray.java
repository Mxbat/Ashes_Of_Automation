package com.robot.game.attacks;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Array;

public class AttackArray {
    Array<Attack> list = new Array<>();

    public void add(Attack attack){
        list.add(attack);
    }
    public void update(){
        for (Attack att:
             list) {
            if(att.isActive()) att.update();
        }
    }

    public void draw(Batch batch){
        for (Attack att:
            list) {
            att.draw(batch);
        }
    }

}
