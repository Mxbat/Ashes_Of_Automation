package com.robot.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.robot.game.constants.EnemiesStat;
import com.robot.game.constants.Scores;
import com.robot.game.enemies.Enemy;
import com.robot.game.enemies.BaseEnemy;
import com.robot.game.enemies.EnemyArray;
import com.robot.game.enemies.TurretEnemy;
import com.robot.game.screens.GameScreen;

import java.util.Arrays;
import java.util.Random;

public class EnemySpawnController {
    EnemyArray enemyArray;
    GameController gameController;


    public void setRoom(Room room) {
        this.room = room;
        currentRoom = room;
        for (Enemy e:
            enemyArray.getPoolSet().get("Hammer") ) {

            ((BaseEnemy) e).setRoom(room);

        }
        for (Enemy e:
            enemyArray.getPoolSet().get("Turret") ) {

            ((TurretEnemy) e).setRoom(room);
        }



    }

    Room room;
    Array<Vector2> freePositions = new Array<>();
    World world;
    Random rand = new Random();
    Room currentRoom;
    int times;

    public EnemySpawnController(EnemyArray enemyArray, Room room, World world, GameController gameController) {
        this.enemyArray = enemyArray;
        this.room = room;
        this.gameController = gameController;
        this.world = world;
        currentRoom = room;
        this.freePositions = getFreePositions(room);
    }

    public void generateEnemies(int times){
        this.times = times;
        freePositions = getFreePositions(room);
        for (int i = 0; i < times * EnemiesStat.BASE_ENEMIES_COUNT; i++) {
            addEnemy(getRandomFreePosition(), "Turret");
        }
        for (int i = 0; i < times * EnemiesStat.BASE_ENEMIES_COUNT; i++) {
            addEnemy(getRandomFreePosition(), "Hammer");

        }
        for (int i = 0; i < times * EnemiesStat.TURRET_ENEMIES_COUNT; i++) {
            addEnemy(getRandomFreePosition(), "Cart");
        }

        for (Enemy e:
             enemyArray) {
            if(e instanceof BaseEnemy){
                ((BaseEnemy) e).setRoom(currentRoom);
            }
        }


    }
    public void addEnemy(Vector2 position, String type){

        switch (type){
            case "Hammer":
                times = (int) Math.ceil(times * EnemiesStat.BASE_ENEMIES_COUNT);
            case "Turret":
                times = (int) Math.ceil(times * EnemiesStat.BASE_ENEMIES_COUNT);
            case "Cart":
                times = (int) Math.ceil(times * EnemiesStat.TURRET_ENEMIES_COUNT);
        }
        int randElement = rand.nextInt(times);

        if(!enemyArray.getPoolSet().isEmpty()){
            Enemy currentEnemy = enemyArray.getPoolSet().get(type).get(randElement);
            currentEnemy.reUse(position);
            if(currentEnemy instanceof TurretEnemy){
                currentRoom = ((TurretEnemy) currentEnemy).getRoom();
            }
            enemyArray.add(currentEnemy);
            enemyArray.getPoolSet().get(type).removeIndex(randElement);

        }
    }
    public void update() {


            for (Enemy e :
                enemyArray) {
                if (e.hasToBeDestroyed) {
                    gameController.plusScrap(Utils.getScrapPerEnemy());

                    GameScreen.gameController.plusScore(Scores.SCORE_PER_ENEMY);
                    enemyArray.list.removeValue(e, true);
                    enemyArray.getPoolSet().get(e.getType()).add(e);
                    e.destroy();

                    e.attack.done = true;
                }
            }

        if (!isThereEnemies()) {
            room.setBattling(false);
        }
    }
    boolean isThereEnemies(){
        return !enemyArray.list.isEmpty();

    }
    public Vector2 getRandomFreePosition() {
        int index = rand.nextInt(freePositions.size);
        Vector2 currentPos = freePositions.get(index);
        if (freePositions.isEmpty()) {
            return null;
        }
        freePositions.removeIndex(index);

        return currentPos;
    }
    public Array<Vector2> getFreePositions(Room room) {
        freePositions.clear();
        int[][] map = room.map;
        System.out.println(" gen map = " + Arrays.deepToString(map));


        for (int y = 0; y < map.length - 1; y+=2) {
            for (int x = 0; x < map[0].length - 1; x+=2) {
                if (map[y][x] != 1) {
                    freePositions.add(new Vector2((x) * Room.multiplier + (float) Room.multiplier/2 + room.x,
                        (map.length - y - 1) * Room.multiplier  + (float) Room.multiplier/2 + room.y));
                }
            }
        }
       return freePositions;
    }

    public Array<Vector2> getFreePositions() {
        return freePositions;
    }

    public void setFreePositions(Array<Vector2> freePositions) {
        this.freePositions = freePositions;
    }

}
