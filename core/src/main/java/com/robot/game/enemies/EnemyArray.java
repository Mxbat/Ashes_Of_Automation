package com.robot.game.enemies;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.robot.game.Raycast;
import com.robot.game.Main;
import com.robot.game.Player;
import com.robot.game.Room;

import java.util.HashMap;
import java.util.Iterator;

public class EnemyArray implements Iterable<Enemy>{
    Enemy result;
    Main main;
    int startPos = -10000;
    Player player;
    private final Array<Enemy> hammerPool = new Array<>();
    private final Array<Enemy> turretPool = new Array<>();
    private final Array<Enemy> cartPool = new Array<>();
    public Array<Enemy> list = new Array<>();

    public HashMap<String, Array<Enemy>> getPoolSet() {
        return poolSet;
    }

    private final HashMap<String, Array<Enemy>> poolSet = new HashMap<>();

    public EnemyArray(Main main, Player player, Room room) {
        this.main = main;
        this.player = player;

        for (int i = 0; i < 6; i++) {
            hammerPool.add(new BaseEnemy(startPos, startPos, player, Main.world, room, "Hammer"));
        }
        for (int i = 0; i < 6; i++) {
            turretPool.add(new TurretEnemy(startPos, startPos, player, Main.world, room));
        }
        for (int i = 0; i < 3; i++) {
            cartPool.add(new BaseEnemy(startPos, startPos, player, Main.world, room, "Cart"));
        }
        poolSet.put("Hammer", hammerPool);
        poolSet.put("Turret", turretPool);
        poolSet.put("Cart", cartPool);


    }





    public void add(Enemy enemy){
        list.add(enemy);
    }
    public Enemy getClosestEnemy(){
        float minDistance = (float) list.get(0).getDistanceToPlayer();
        result = list.get(0);
        for (Enemy e : this) {
            if(e.getDistanceToPlayer() < minDistance){
                minDistance = (float) e.getDistanceToPlayer();
                result = e;
            }
        }
        return result;
    }
    public void update(){
        setPathBlocked(player, Main.world);
        for (Enemy e:
             this) {
            e.update();

            e.attack.active = true;
        }
    }

    public void draw(SpriteBatch batch){
        for (Enemy e : this) {
            e.draw(batch);
        }
    }
    public void setPathBlocked(Player player, World world){
        for (Enemy e : this) {
            if(e instanceof BaseEnemy)
                ((BaseEnemy) e).setPathToPlayerBlocked(Raycast.isBehindWall(e.body, player.body, world));
        }
    }


    @Override
    public Iterator<Enemy> iterator() {
        return list.iterator();
    }
}
