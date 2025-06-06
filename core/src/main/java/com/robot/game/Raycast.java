package com.robot.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.robot.game.enemies.BaseEnemy;

public  class Raycast {
    public static boolean isBehindWall(Body sender, Body target, World world) {
        Vector2 start = sender.getPosition();
        Vector2 end = target.getPosition();
        float thickness = 1F;
        Vector2 direction = new Vector2(end).sub(start).nor();
        Vector2 perpendicular = new Vector2(-direction.y, direction.x).scl(thickness);


        boolean hitCenter = raycast(world, start, end);
        boolean hitLeft = raycast(world, start.cpy().add(perpendicular), end.cpy().add(perpendicular));
        boolean hitRight = raycast(world, start.cpy().sub(perpendicular), end.cpy().sub(perpendicular));
        return hitCenter || hitLeft || hitRight;
    }

    private static boolean raycast(World world, Vector2 start, Vector2 end) {
        final boolean[] isHit = {false};

        world.rayCast((fixture, point, normal, fraction) -> {
            if (fixture.isSensor()) return -1;
            if(fixture.getUserData() instanceof BaseEnemy) return -1;
            if(fixture.getUserData() instanceof Obstacle) {
                isHit[0] = true;
                return 0;
            }

            return -1;
        }, start, end);

        return isHit[0];
    }

    public static boolean lightRaycast(World world, Vector2 start, Vector2 end) {
        final boolean[] isHit = {false};

        world.rayCast((fixture, point, normal, fraction) -> {
            if (fixture.isSensor()) return -1;
            if(fixture.getUserData() instanceof BaseEnemy) return -1;
            if(fixture.getUserData() instanceof Obstacle) {
                isHit[0] = true;
                return 0;
            }
            return -1;
        }, start, end);

        return isHit[0];
    }
    public static boolean lightBehindWall(Body sender, Body target, World world) {
        Vector2 start = sender.getPosition();
        Vector2 end = target.getPosition();
        return lightRaycast(world, start, end);
    }
}






