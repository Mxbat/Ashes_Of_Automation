package com.robot.game;

import static com.robot.game.screens.GameScreen.rayHandler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.robot.game.constants.FilterBits;
import com.robot.game.constants.GameSettings;
import com.robot.game.constants.Scores;
import com.robot.game.enemies.EnemyArray;
import com.robot.game.enums.Direction;

import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

import box2dLight.PointLight;

public class GameController {
    private final AtomicBoolean needToGenerateRoom = new AtomicBoolean(true);
    private volatile Room nextRoom;
    private boolean canEnter = false;
    private Future<Room> roomGenerationFuture;
    private long score = 0;
    private long targetScore = 0;
    World world = Main.world;

    boolean doorSoundPlayed = false;

    Random rand = new Random();

    public EnemyArray enemyArray;

    PointLight entranceLight;


    PointLight exitLight;
    ExecutorService backgroundExecutor = Executors.newFixedThreadPool(1);
    EnemySpawnController spawner;
    Main main;
    Player player;
    HashMap<String, Object> textureMap = new HashMap<>();
    private void createTextures(){
        textureMap.put("1x1", new Texture(Resources._1x1_));
        textureMap.put("2x2",new Texture(Resources._2x2_));
        textureMap.put("1x4",new TextureRegion(new Texture(Resources._1x4_)));
        textureMap.put("2x4",new TextureRegion(new Texture(Resources._2x4_)));
        textureMap.put("2x3",new TextureRegion(new Texture(Resources._2x3_)));
        textureMap.put("floor",new Texture(Resources.FLOOR_TEXTURE_PATH));
        textureMap.put("1x4G", new TextureRegion(new Texture(Resources._1x4_Glow)));
        textureMap.put("2x4G", new TextureRegion(new Texture(Resources._2x4_Glow)));
        textureMap.put("2x3G", new TextureRegion(new Texture(Resources._2x3_Glow)));
        textureMap.put("entrance", new TextureRegion(new Texture(Resources.ENTRANCE_ANIM)));
        textureMap.put("wall", new Texture(Resources.wallTexture));
    }



    public GameController(Main main, Player player) {
        AudioManager.playDoor();
        createTextures();
        this.main = main;
        this.player = player;

        room = new Room(GameSettings.WALLS_WIDTH ,0 ,0, world, getRandomDirections(), textureMap);
        createPhysicalRoom(room);

        enemyArray = new EnemyArray(main, player, room);
        spawner = new EnemySpawnController(enemyArray, room, world);

        spawner.generateEnemies(0);
        spawner.setRoom(room);
        spawnPlayer(room);

        entranceLight = new PointLight(
            rayHandler,
            256,
            Resources.ENTRANCES_LIGHT_COLOR,
            20,
            GameController.getPositionOnEntrance(room.getEntranceLocations()[0], room).scl(1/ GameSettings.PPM).x,
            GameController.getPositionOnEntrance(room.getEntranceLocations()[0], room).scl(1/GameSettings.PPM).y);
        exitLight = new PointLight(
            rayHandler,
            256,
            Resources.ENTRANCES_LIGHT_COLOR,
            20,
            GameController.getPositionOnEntrance(room.getEntranceLocations()[1], room).scl(1/GameSettings.PPM).x,
            GameController.getPositionOnEntrance(room.getEntranceLocations()[1], room).scl(1/GameSettings.PPM).y);
    }

    public Room getRoom() {
        return room;
    }

    public Room room;

    Array<Direction> directions = Array.with(Direction.UP, Direction.DOWN, Direction.RIGHT, Direction.LEFT);

    public void update(){
        if(score < targetScore){
            score++;
        }
        room.getEntrance1().closeIfPossible();
        if(!room.isBattling()){
            room.getEntrance2().openIfPossible();

        }
        if (needToGenerateRoom.getAndSet(false) && (roomGenerationFuture == null || roomGenerationFuture.isDone())) {
            roomGenerationFuture = backgroundExecutor.submit(() -> {
                return new Room(50, 0, 0, world, getNextDirections(room.getExitLocation().invert()), textureMap);
            });
        }
        if (roomGenerationFuture != null && roomGenerationFuture.isDone() && !canEnter) {
            try {
                nextRoom = roomGenerationFuture.get();
                createPhysicalRoom(nextRoom);
                nextRoom.disable();
                canEnter = true;
                roomGenerationFuture = null;
            } catch (Exception e) {
                nextRoom = new Room(50, 0, 0, world, getNextDirections(room.getExitLocation().invert()), textureMap);
            }
        }

        spawner.update();
        enemyArray.update();

        if(!room.isBattling() && !doorSoundPlayed){
            AudioManager.playDoor();
            doorSoundPlayed = true;
        }
        if(!room.isBattling() && player.isTouchingEntrance() && nextRoom != null && canEnter){
            AudioManager.playDoor();
            step();
        }
    }
    public void draw(SpriteBatch batch){
        enemyArray.draw(batch);
    }
    private Direction[] getRandomDirections(){
        Direction d1 = directions.get(rand.nextInt(directions.size-1));
        Direction d2 = getRandomDirection(d1);

        return new Direction[]{d1, d2};
    }
    private Direction getRandomDirection(Direction ban){
        directions.removeValue(ban, true);
        Direction result = directions.get(rand.nextInt(directions.size-1));
        directions.add(ban);
        return result;
    }
    private Direction[] getNextDirections(Direction direction){
        return new Direction[]{direction, getRandomDirection(direction)};

    }
    public void step(){
        plusScore(Scores.SCORE_PER_ROOM);
        doorSoundPlayed = false;
        room.destroy();

        room = nextRoom;

        room.enable();
        entranceLight.setPosition(getPositionOnEntrance(room.getEntranceLocations()[0], room).scl(1/GameSettings.PPM));
        exitLight.setPosition(getPositionOnEntrance(room.getEntranceLocations()[1], room).scl(1/GameSettings.PPM));

        spawner.setRoom(room);
        spawner.setFreePositions(spawner.getFreePositions(room));
        spawner.generateEnemies(getEnemyCount(room));



        spawnPlayer(room);

        needToGenerateRoom.set(true);

        canEnter = false;
    }
    static public Vector2 getPositionOnEntrance(Direction entranceLocation, Room room){
        Vector2 result = new Vector2();
        switch (entranceLocation){
            case DOWN:
                result.x = (float) room.getWidth() /2;
                result.y = 0;
                break;
            case UP:
                result.x = (float) room.getWidth() /2 + room.x;
                result.y = room.getHeight() - room.getWallsWidth();
                break;
            case LEFT:
                result.x = 0;
                result.y = (float) room.getHeight() /2;
                break;
            default:
                result.x = room.getWidth() - room.getWallsWidth();
                result.y = (float) room.getHeight() /2;
                break;
        }
        return result;

    }
    private void spawnPlayer(Room room){
        player.setPosition(getPositionOnEntrance(room.getEntranceLocations()[0], room));
    }
    private int getEnemyCount(Room room){
        int a = room.getMap()[0].length;
        int b = room.getMap().length;
        return (int) (Math.max(a, b)/4.5);
    }
    private void createPhysicalRoom(Room room){
        room.getEntrance1().setBody(room.getEntrance1().createBody(
            room.getEntrance1().bodyType,
            room.getEntrance1().getX(),
            room.getEntrance1().getY(),
            room.getEntrance1().getWidth(),
            room.getEntrance1().getHeight()));
        room.getEntrance1().getFixture().setSensor(true);
        room.getEntrance1().initCategoryBits((short) -1, (short) 0, FilterBits.PLAYER_BITS);

        room.getEntrance2().setBody(room.getEntrance2().createBody(
            room.getEntrance2().bodyType,
            room.getEntrance2().getX(),
            room.getEntrance2().getY(),
            room.getEntrance2().getWidth(),
            room.getEntrance2().getHeight()));
        room.getEntrance2().getFixture().setSensor(true);
        room.getEntrance2().initCategoryBits((short) -1, (short) 0, FilterBits.PLAYER_BITS);


        for (Obstacle o : room.obstacles){
            o.setBody(o.createBody(BodyDef.BodyType.StaticBody, o.getX(), o.getY(),
                o.getWidth(), o.getHeight()));
            o.initCategoryBits(FilterBits.OBSTACLE_BITS, (short) 0, (short) 0xFFFF);
        }
    }


    public long getScore() {
        return score;
    }

    public void plusScore(int value){
        AudioManager.playScore();
        targetScore += value;
    }
}
