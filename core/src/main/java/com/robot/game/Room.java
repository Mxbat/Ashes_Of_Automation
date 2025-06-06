package com.robot.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.robot.game.enums.Direction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Room {


    public int getWallsWidth() {
        return wallsWidth;
    }

    private final int wallsWidth;

    public float x;
    public float y;
    Texture texture;

    Array<Obstacle> generalObstacles = new Array<>();

    public int getWidth() {
        return width;
    }

    public int width;

    public int getHeight() {
        return height;
    }

    private final int height;

    public void setBattling(boolean battling) {
        this.battling = battling;
    }

    private boolean battling = true;
    int scale = 2;

    public int getEntrancesSize() {
        return entrancesSize;
    }

    int entrancesSize = multiplier * 2 * scale;

    Sprite floor;
    public int[][] map;


    ObstacleArray walls = new ObstacleArray();

    public Direction[] getEntranceLocations() {
        return entranceLocations;
    }

    private final Direction[] entranceLocations;


    public ObstacleArray obstacles = new ObstacleArray();
    public static final short multiplier = 96;

    World world;
    Entrance entrance1;
    Entrance entrance2;
    HashMap<String, Object> textureArray;
    Texture floorTiles;

    public Room(int wallsWidth, float x, float y,
                World world, Direction[] entranceLocations, HashMap<String, Object> textures) {
        this.textureArray = textures;
        floorTiles = (Texture) textures.get("floor");

        this.x = x;
        this.y = y;


        this.world = world;
        this.entranceLocations = entranceLocations;


        this.wallsWidth = wallsWidth;
        map = generateRoom();
        width = map[0].length * multiplier;
        height = map.length * multiplier;
        System.out.println(Arrays.deepToString(map));

        createWalls();
        createFloor();
        createEntrance(entranceLocations[0], entranceLocations[1]);
        System.out.println(Arrays.deepToString(map) + " start");

    }
    public void drawFloor(SpriteBatch batch){
        floor.draw(batch);
    }

    public  void draw(SpriteBatch batch){
        for (Obstacle obstacle:
             generalObstacles) {
            obstacle.draw(batch);

        }
        obstacles.draw(batch);
        walls.draw(batch);
        entrance1.draw(batch);
        entrance2.draw(batch);
    }
    private void createFloor(){
        int floorWidth =  floorTiles.getWidth() * map[0].length / 20;
        int floorHeight =  floorTiles.getHeight() * map.length / 20;
        floor = new Sprite(floorTiles, 0, 0, floorWidth, floorHeight);
                floor.setOriginCenter();
                floor.setPosition(x, y);
                floor.setSize(multiplier * map[0].length, multiplier*map.length);
    }
    private void createEntrance(Direction first, Direction second){
        entrance1 = generateEntrance(first, false);
        entrance2 = generateEntrance(second, true);


    }

    private void createWalls(){


            obstacles.add(new Obstacle(x - wallsWidth, y - wallsWidth, map[0].length * multiplier + wallsWidth*2, wallsWidth, (Texture) textureArray.get("wall")));//down


        obstacles.add(new Obstacle(x - wallsWidth, map.length * multiplier + y, map[0].length * multiplier + wallsWidth * 2, wallsWidth, (Texture) textureArray.get("wall")));



        obstacles.add(new Obstacle(x + map[0].length * multiplier, y, wallsWidth,
                map.length * multiplier + wallsWidth, (Texture) textureArray.get("wall")));

        obstacles.add(new Obstacle(x - wallsWidth, y, wallsWidth, map.length * multiplier + wallsWidth, (Texture) textureArray.get("wall")));



    }
    public  void updateMap(Vector2 start, Vector2 end) {
        System.out.println("start = " + start);
        System.out.println("end = " + end);
        int height = map.length;
        int width = map[0].length;


        int x1 = (int) Math.floor((start.x));
        int y1 = (int) Math.floor(start.y);
        int x2 = (int) Math.floor(end.x);
        int y2 = (int) Math.floor(end.y);

        int y1Index = Math.min(Math.max(height - y1 - 1, 0), height - 1);
        int y2Index = Math.min(Math.max(height - y2 - 1, 0), height - 1);


        int startX = Math.min(x1, x2);
        int endX = Math.max(x1, x2);

        int startY = Math.min(y1Index, y2Index);
        int endY = Math.max(y1Index, y2Index);


        for (int y = startY; y <= endY; y++) {
            for (int x = startX; x <= endX; x++) {
                if (y >= 0 && y < height && x >= 0 && x < width) {
                    map[y][x] = 1;
                    System.out.println("updated" + x + " | " + y);
                }
            }
        }

}

    private static final Random rand = new Random();
    int MIN_GAP = rand.nextInt(1) + 2;
    public void createObstacle(Vector2 lowerLeft, Vector2 upperRight) {

        Vector2 subtractionResult = (upperRight.cpy().sub(lowerLeft.cpy())).add(1, 1);

        if(subtractionResult.equals(new Vector2(1, 1))){
            obstacles.add(obstacles.createObstacle(lowerLeft, upperRight, this, (Texture) textureArray.get("1x1")));
        }
        else  if(subtractionResult.equals(new Vector2(2, 2))){
            obstacles.add(obstacles.createObstacle(lowerLeft, upperRight, this, (Texture) textureArray.get("2x2")));
        }
        else if(subtractionResult.equals(new Vector2(1, 4))){
            obstacles.add(obstacles.createObstacle(lowerLeft, upperRight, this, (TextureRegion) textureArray.get("1x4"), (TextureRegion) textureArray.get("1x4G"), 24, false, (int) subtractionResult.x, (int) subtractionResult.y));
        }
        else if(subtractionResult.equals(new Vector2(4, 1))){
            obstacles.add(obstacles.createObstacle(lowerLeft, upperRight, this, (TextureRegion) textureArray.get("1x4"), (TextureRegion) textureArray.get("1x4G"), 24, true, (int) subtractionResult.x, (int) subtractionResult.y));
        }
        else if(subtractionResult.equals(new Vector2(2, 4))){
            obstacles.add(obstacles.createObstacle(lowerLeft, upperRight, this, (TextureRegion) textureArray.get("2x4"), (TextureRegion) textureArray.get("2x4G"),48, false, (int) subtractionResult.x, (int) subtractionResult.y));
        }
        else if(subtractionResult.equals(new Vector2(4, 2))){
            obstacles.add(obstacles.createObstacle(lowerLeft, upperRight, this, (TextureRegion) textureArray.get("2x4"),(TextureRegion) textureArray.get("2x4G"), 48, true, (int) subtractionResult.x, (int) subtractionResult.y));
        }
        else if(subtractionResult.equals(new Vector2(2, 3))){
            obstacles.add(obstacles.createObstacle(lowerLeft, upperRight, this, (TextureRegion) textureArray.get("2x3"), (TextureRegion) textureArray.get("2x3G"),24, false, (int) subtractionResult.x, (int) subtractionResult.y));
        }
        else if(subtractionResult.equals(new Vector2(3, 2))){
            obstacles.add(obstacles.createObstacle(lowerLeft, upperRight, this, (TextureRegion) textureArray.get("2x3"), (TextureRegion) textureArray.get("2x3G"),24, true, (int) subtractionResult.x, (int) subtractionResult.y));
        }
         updateMap(lowerLeft, upperRight);
    }





    private boolean isValidPosition(int minX, int minY, int w, int h,
                                    int width, int height,
                                    Array<int[]> placed, ArrayList<Vector2> forbiddenZone, ArrayList<Vector2> forbiddenZone1) {

        int maxX = minX + w - 1;
        int maxY = minY + h - 1;



        if (minX < 0 || maxX >= width || minY < 0 || maxY >= height) {
            return false;
        }


        boolean leftGap = minX > 0 && minX < MIN_GAP;
        boolean rightGap = maxX < width - 1 && (width - 1 - maxX) < MIN_GAP;
        boolean bottomGap = minY > 0 && minY < MIN_GAP;
        boolean topGap = maxY < height - 1 && (height - 1 - maxY) < MIN_GAP;

        if (leftGap || rightGap || bottomGap || topGap) {
            return false;
        }





        if (forbiddenZone != null) {
            for (Vector2 point : forbiddenZone) {
                int px = (int) point.x;
                int py = (int) point.y;

                if (px >= minX && px <= maxX && py >= minY && py <= maxY) {
                    return false;
                }
            }
        }

        if (forbiddenZone1 != null) {
            for (Vector2 point : forbiddenZone1) {
                int px = (int) point.x;
                int py = (int) point.y;
                if (px >= minX && px <= maxX && py >= minY && py <= maxY) {
                    return false;
                }
            }
        }

        for (int[] obst : placed) {

            int obstMinX = obst[0] - MIN_GAP;
            int obstMinY = obst[1] - MIN_GAP;
            int obstMaxX = obst[2] + MIN_GAP;
            int obstMaxY = obst[3] + MIN_GAP;

            if (maxX >= obstMinX && minX <= obstMaxX &&
                maxY >= obstMinY && minY <= obstMaxY) {
                return false;
            }
        }

        return true;
    }

    public int[][] generateRoom() {
        MIN_GAP = rand.nextInt(1) + 2;
        int width = rand.nextInt(13) + 7;
        int height = rand.nextInt(13) + 7;
        map = new int[height][width];

        ArrayList<Vector2> forbiddenZone = createForbiddenZone(entranceLocations[0], map);
        ArrayList<Vector2> forbiddenZone1 = createForbiddenZone(entranceLocations[1], map);



        List<int[]> scalableShapes = List.of(new int[]{1, 1});
        List<int[]> fixedShapes = Arrays.asList(new int[]{2, 3},  new int[]{1, 4}, new int[]{2, 4});



        Array<int[]> possibleSizes = new Array<>();



        for (int[] shape : scalableShapes) {
            int w = shape[0], h = shape[1];
            for (int s = 1; s <= 2; s++) {
                int scaledW = w * s, scaledH = h * s;
                if (scaledW < width && scaledH < height) {
                    possibleSizes.add(new int[]{scaledW, scaledH});

                }
                if (w != h) { // Добавление повёрнутой версии
                    int rotW = h * s, rotH = w * s;
                    if (rotW < width && rotH < height) {
                        possibleSizes.add(new int[]{rotW, rotH});

                    }
                }
            }
        }


        for (int[] shape : fixedShapes) {
            int w = shape[0], h = shape[1];
            if (w < width && h < height) {
                possibleSizes.add(new int[]{w, h});
            }
            if (w != h) {
                if (h < width && w < height) {
                    possibleSizes.add(new int[]{h, w});
                }
            }
        }

        Array<int[]> placedObstacles = new Array<>();


        if (placedObstacles.isEmpty()) {
            int index = rand.nextInt(possibleSizes.size);
            int[] size = possibleSizes.get(index);
            int w = size[0], h = size[1];
            int minX = rand.nextInt(width - w + 1);
            int minY = height - h - rand.nextInt(Math.max(1, height / 2));
            if (isValidPosition(minX, minY, w, h, width, height, placedObstacles, forbiddenZone, forbiddenZone1)) {
                int maxX = minX + w - 1;
                int maxY = minY + h - 1;
                int[] newObst = {minX, minY, maxX, maxY};
                placedObstacles.add(newObst);
                createObstacle(new Vector2(minX, minY), new Vector2(maxX, maxY));
            }
        }


        while (true) {
            int index = rand.nextInt(possibleSizes.size);
            int[] size = possibleSizes.get(index);
            int w = size[0], h = size[1];

            Array<int[]> possiblePositions = new Array<>();
            for (int x = 0; x <= width - w; x++) {
                for (int y = 0; y <= height - h; y++) {
                    if (isValidPosition(x, y, w, h, width, height, placedObstacles, forbiddenZone, forbiddenZone1)) {
                        possiblePositions.add(new int[]{x, y});
                    }
                }
            }

            if (!possiblePositions.isEmpty()) {
                int[] pos = possiblePositions.get(rand.nextInt(possiblePositions.size));
                int minX = pos[0], minY = pos[1];
                int maxX = minX + w - 1, maxY = minY + h - 1;
                int[] newObst = {minX, minY, maxX, maxY};
                placedObstacles.add(newObst);
                createObstacle(new Vector2(minX, minY), new Vector2(maxX, maxY));
            } else if (possibleSizes.size > 1) {
                possibleSizes.removeIndex(index);

            } else {
                break;
            }
        }




            System.out.println( " map" + Arrays.deepToString(map));
            System.out.println("generated");
            return map;
    }


    public float getY() {
        return  y;
    }

    public float getX() {
        return  x;
    }

    public boolean isBattling() {
        return battling;
    }
    public void disable() {

        entrance1.body.setActive(false);

        entrance2.body.setActive(false);

        for (Obstacle o:
            obstacles.list) {
            o.getFixture().getBody().setActive(false);

        }

    }
    public void enable() {

        entrance1.body.setActive(true);

        entrance2.body.setActive(true);

        for (Obstacle o:
            obstacles.list) {
           o.body.setActive(true);
        }

    }
    public void destroy() {
        world.destroyBody(entrance1.getFixture().getBody());
        world.destroyBody(entrance2.getFixture().getBody());
        for (Obstacle o:
            obstacles.list) {

            world.destroyBody(o.getFixture().getBody());
        }
        obstacles.list.clear();

    }
    private static ArrayList<Vector2> createForbiddenZone(Direction location, int[][] map) {
        int rows = map.length;
        int cols = map[0].length;
        ArrayList<Vector2> list = new ArrayList<>();

        int start, end;
        switch (location) {
            case RIGHT:
                start = Math.max(0, (rows - 6) / 2);
                end = Math.min(rows, start + 6);
                for (int r = start; r < end; r++) {
                    list.add(new Vector2(cols - 1, r));
                }
                break;
            case LEFT:
                start = Math.max(0, (rows - 6) / 2);
                end = Math.min(rows, start + 6);
                for (int r = start; r < end; r++) {
                    list.add(new Vector2(0, r));
                }
                break;
            case UP:
                start = Math.max(0, (cols - 6) / 2);
                end = Math.min(cols, start + 6);
                for (int c = start; c < end; c++) {
                    list.add(new Vector2(c, rows - 1));
                }
                break;
            case DOWN:
                start = Math.max(0, (cols - 6) / 2);
                end = Math.min(cols, start + 6);
                for (int c = start; c < end; c++) {
                    list.add(new Vector2(c, 0));
                }
                break;
        }
        return list;
    }

    Entrance generateEntrance(Direction location, boolean exit){
        float x, y, width, height;
        texture = new Texture(Resources.ENTRANCE_ANIM);
        int angle = 0;
        switch (location){
            case UP:

                angle = -90;
                x = (float) (getWidth() - getEntrancesSize()) /2;
                y = getHeight() ;
                width = getEntrancesSize();
                height = 100;
                break;
            case DOWN:

                angle = 90;
                x = (float) (getWidth() - getEntrancesSize())/2 ;
                y = -100;
                width = getEntrancesSize();
                height = 100;
                break;
            case RIGHT:
                angle = 180;
                x = getWidth();
                y = (float) (getHeight() - getEntrancesSize()) /2;
                width = 100;
                height = getEntrancesSize();
                break;
            default:
                x = -100;
                y = (float) (getHeight() - getEntrancesSize()) /2;
                width = 100;
                height = getEntrancesSize();
                break;
        }
        return new Entrance(x, y, width, height, (TextureRegion) textureArray.get("entrance"), exit, angle);

    }

    public Direction getExitLocation() {
        return entranceLocations[1];
    }

    public int[][] getMap() {
        return map;
    }

    public Entrance getEntrance1() {
        return entrance1;
    }
    public Entrance getEntrance2() {
        return entrance2;
    }


    static class Entrance extends GameObject{
        boolean exit;
        Animation animation;
        public Entrance(float x, float y, float width, float height, TextureRegion textureRegion, boolean exit, float rotation) {
            super(x, y, width, height, textureRegion, BodyDef.BodyType.StaticBody);

            this.exit = exit;
            height = 384;
            width = 100;




            sprite = new Sprite(textureRegion, 25*11,0,  25, 100);
            if(exit){
                sprite = new Sprite(textureRegion, 0,0,  25, 100);
            }
            sprite.setOrigin(0, 0);

            switch ((int) rotation){
                case -90:
                    y+= width;
                    break;
                case 90:
                    x += height;
                    break;
                case 180:
                    y += height;
                    x += width;
                    break;


            }
            sprite.setPosition(x, y);

            sprite.setRotation(rotation);
            sprite.setSize(width, height);
            animation = new Animation(new TextureRegion(new Texture(Resources.ENTRANCE_ANIM)), sprite, 12, 1, 4);
        }
        public void setBody(Body body){
            this.body = body;
        }
        public void openIfPossible(){
            if(!animation.isCycleEnded()){
                animation.play();
            }
        }
        public void closeIfPossible(){
            if(!animation.isCycleEnded()){
                animation.changeRow(1);
                animation.play();
            }
        }
        public boolean isExit(){
            return exit;
        }
        void draw(SpriteBatch batch){
            sprite.draw(batch);
        }



    }
}

