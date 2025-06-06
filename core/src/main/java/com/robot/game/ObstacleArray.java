package com.robot.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import java.util.Iterator;

public class ObstacleArray implements Iterable<Obstacle>{
    public Array<Obstacle> list = new Array<>();

    public void add(Obstacle obstacle){
        list.add(obstacle);
    }

    public void draw(SpriteBatch batch){
        for (Obstacle o : list) {
            o.draw(batch);
        }
    }


    public Obstacle createObstacle(Vector2 bottomLeft, Vector2 topRight, Room room, Texture texture){
        float x = room.x;
        float y = room.y;
        return new Obstacle((bottomLeft.x ) * Room.multiplier + x, (bottomLeft.y ) * Room.multiplier + y,
            Math.abs((topRight.x) * Room.multiplier - (bottomLeft.x ) * Room.multiplier + Room.multiplier), Math.abs(topRight.y ) * Room.multiplier - (bottomLeft.y ) * Room.multiplier + Room.multiplier,
            texture);
    }
    public Obstacle createObstacle(Vector2 bottomLeft, Vector2 topRight, Room room, TextureRegion textureRegion, TextureRegion glowRegion, int frames, boolean rotated, int frameWidth, int frameHeight){
        float x = room.x;
        float y = room.y;
        return new Obstacle((bottomLeft.x ) * Room.multiplier + x, (bottomLeft.y ) * Room.multiplier + y,
             Math.abs((topRight.x) * Room.multiplier - (bottomLeft.x ) * Room.multiplier + Room.multiplier),  (Math.abs(topRight.y ) * Room.multiplier - (bottomLeft.y ) * Room.multiplier + Room.multiplier),
            textureRegion, glowRegion, frames, rotated, frameWidth, frameHeight);
    }

    @Override
    public Iterator<Obstacle> iterator() {
        return list.iterator();
    }
}
