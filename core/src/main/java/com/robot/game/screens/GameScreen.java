package com.robot.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.robot.game.Resources;
import com.robot.game.attacks.AttackArray;
import com.robot.game.Button;
import com.robot.game.Raycast;
import com.robot.game.FontManager;
import com.robot.game.GameController;
import com.robot.game.GameInputAdapt;
import com.robot.game.Joystick;
import com.robot.game.Main;
import com.robot.game.MyCollisionListener;
import com.robot.game.Obstacle;
import com.robot.game.Player;
import com.robot.game.constants.FilterBits;
import com.robot.game.constants.GameSettings;
import com.robot.game.constants.UI;
import com.robot.game.enemies.Enemy;
import com.robot.game.enums.GameStage;

import java.util.Arrays;

import box2dLight.Light;
import box2dLight.PointLight;
import box2dLight.RayHandler;

public class GameScreen extends ScreenAdapter {
    public static ShapeRenderer shapeRenderer = new ShapeRenderer();

    BitmapFont font;
    Texture staminaIcon;
    private static final float MIN_FRAME_LENGTH = 1f/60f;
    private float timeSinceLastRender = 0;

    Joystick joystick;
    Texture heartIcon;
    Texture scrapIcon;
    public static World world;
    Texture menuTexture;
    Sprite menuSprite;
    public static GameStage gameStage;





    Player player;

    public static AttackArray attackArray;
    Button attack;
    Button pause;
    Button heal;

    Button menuButton;
    Button resumeButton;
    Sprite blackout;


    Enemy enemy;
    Box2DDebugRenderer debugRenderer;
    private final SpriteBatch batch = new SpriteBatch();
    static public Main main;
    static public RayHandler rayHandler;
    public static OrthographicCamera camera;
    public OrthographicCamera hudCam;
    public GameScreen(Main main) {
        GameScreen.main = main;
    }

    static public GameController gameController;

    Array<Button> buttons = new Array<>();
    PointLight pointLight;
    Texture attackButtonTexture;
    TextureRegion healButtonTexture;
    TextureRegion pauseButtonTexture;
    Texture uiMenuTexture;


    public void show() {
        blackout = new Sprite(new Texture(Resources.BLACKOUT));
        blackout.setAlpha(0.7f);
        blackout.setSize(3000, 3000);
        uiMenuTexture = new Texture(Resources.UI_BUTTON_TEXTURE);
        menuTexture = new Texture(Resources.MENU_TEXTURE);
        menuSprite = new Sprite(menuTexture);
        menuSprite.setSize(UI.MENU_SIZE, UI.MENU_SIZE);
        menuSprite.setPosition(GameSettings.SCREEN_WIDTH/2f - UI.MENU_SIZE/2f, GameSettings.SCREEN_HEIGHT/2f - UI.MENU_SIZE/2f);

        resumeButton = new Button(GameSettings.SCREEN_WIDTH/2f - 250, GameSettings.SCREEN_HEIGHT/2f + 50, 500, 200, uiMenuTexture, false);
        menuButton = new Button(GameSettings.SCREEN_WIDTH/2f - 250, GameSettings.SCREEN_HEIGHT/2f - 250, 500, 200, uiMenuTexture, false);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, GameSettings.SCREEN_WIDTH, GameSettings.SCREEN_HEIGHT);
        attackArray = new AttackArray();
        gameStage = GameStage.PLAYING;
        world = new World(new Vector2(0, 0), true);
        rayHandler = new RayHandler(world);
        heartIcon = new Texture(Resources.HEART_ICON);
        scrapIcon = new Texture(Resources.SCRAP_ICON);
        attackButtonTexture = new Texture(Resources.ATTACK_BUTTON);
        healButtonTexture = new TextureRegion(new Texture(Resources.HEAL_BUTTON));
        staminaIcon = new Texture(Resources.STAMINA_ICON);
        pauseButtonTexture = new TextureRegion(new Texture(Resources.PAUSE_BUTTON));

        FontManager fontManager = new FontManager();
        font = fontManager.getFont();


        joystick = new Joystick(50, 50, (int) UI.JOYSTICK_DIAMETER,  main);
        player = new Player(joystick,  800, 600, world);

        gameController = new GameController(main, player);

        hudCam = new OrthographicCamera();
        hudCam.setToOrtho(false, GameSettings.SCREEN_WIDTH, GameSettings.SCREEN_HEIGHT);




        rayHandler.setShadows(true);
        rayHandler.setAmbientLight(0.1f, 0.1f, 0.1f, 0.40f);
        rayHandler.setBlur(true);
        rayHandler.setBlurNum(3);

        pointLight = new PointLight(
            rayHandler,
            400,
            new Color(0.5F, 0.5f, 0.5f, 0.75f),
            14f,
            5f, 10f
        );
        debugRenderer = new Box2DDebugRenderer();





        heal = new Button((UI.HEAL_BUTTON_POS_X - UI.HEAL_BUTTON_DIAMETER), UI.HEAL_BUTTON_POS_Y, UI.HEAL_BUTTON_DIAMETER,
            UI.HEAL_BUTTON_DIAMETER, 100,
            100,healButtonTexture, false);
        attack = new Button(UI.ATTACK_BUTTON_POS_X,
            UI.ATTACK_BUTTON_POS_Y, UI.ATTACK_BUTTON_DIAMETER,
            UI.ATTACK_BUTTON_DIAMETER, attackButtonTexture, false);
        pause = new Button(GameSettings.SCREEN_WIDTH - UI.PAUSE_BUTTON_DIAMETER * 1.2f, GameSettings.SCREEN_HEIGHT - UI.PAUSE_BUTTON_DIAMETER * 1.2f, UI.PAUSE_BUTTON_DIAMETER, UI.PAUSE_BUTTON_DIAMETER, UI.PAUSE_BUTTON_DIAMETER,
            UI.PAUSE_BUTTON_DIAMETER, pauseButtonTexture, false);

        buttons.add(attack);//0
        buttons.add(pause); //1
        buttons.add(heal);  //2

        buttons.add(resumeButton); //3
        buttons.add(menuButton);   //4


        Gdx.input.setInputProcessor(new GameInputAdapt(main, Main.camera, joystick, buttons, player, world));

        world.setContactListener(new MyCollisionListener( world));

        System.out.println(Arrays.deepToString(gameController.getRoom().map));

        Light.setGlobalContactFilter(FilterBits.LIGHT_BITS, (short) 0,  FilterBits.OBSTACLE_BITS);







        shapeRenderer.setAutoShapeType(true);
        camera.position.x = player.getX() + player.getWidth()/2;
        camera.position.y = player.getY() + player.getWidth()/2;

    }

    @Override
    public void render(float delta) {


        if(player.getHp() <=0){
            restartGame();
            return;
        }

        if(gameStage == GameStage.PLAYING){

            if(!gameController.enemyArray.list.isEmpty()) enemy = gameController.enemyArray.getClosestEnemy();

            gameController.update();
            if(!gameController.enemyArray.list.isEmpty() && enemy != null) player.setCanSeeEnemy(
                !Raycast.lightBehindWall(player.body, enemy.body, world) &&
                camera.frustum.pointInFrustum(new Vector3(new Vector2(enemy.getX(), enemy.getY()), 0)));
            else {
                player.setCanSeeEnemy(false);
            }
            for (Obstacle o:
                gameController.room.obstacles.list) {
                o.update();
            }


            attackArray.update();

            player.move();
            pointLight.setPosition(player.body.getPosition());
            player.update(enemy);

            timeSinceLastRender += delta;
            if (timeSinceLastRender >= MIN_FRAME_LENGTH) {
                world.step(1/60f, 5, 5);
                timeSinceLastRender -= MIN_FRAME_LENGTH;
            }



            joystick.normalize();


            camera.position.x = player.getX() + player.getWidth()/2;
            camera.position.y = player.getY() + player.getWidth()/2;
        }

        ScreenUtils.clear(Color.BLACK);
        camera.update();
        batch.begin();
        batch.setProjectionMatrix(camera.combined);


        gameController.getRoom().drawFloor(batch);
        gameController.getRoom().draw(batch);
        gameController.draw(batch);

        player.draw(batch);

        batch.end();
        rayHandler.setCombinedMatrix(camera.combined.cpy().scale(GameSettings.PPM, GameSettings.PPM, 1));
        batch.begin();
        rayHandler.updateAndRender();
        batch.end();

        batch.begin();
        for (Obstacle o:
            gameController.room.obstacles.list) {
            o.drawGlow(batch);
        }
        attackArray.draw(batch);
        batch.end();

        batch.setProjectionMatrix(hudCam.combined);
        batch.begin();


        font.getData().setScale(GameSettings.HP_FONT_SCALE);
        font.setColor(Color.RED);
        font.draw(batch, "" + player.getHp(), GameSettings.ICONS_X + GameSettings.ICONS_X + 100, GameSettings.SCREEN_HEIGHT - 50);
        batch.draw(heartIcon, GameSettings.ICONS_X, GameSettings.SCREEN_HEIGHT - GameSettings.ICONS_Y_OFFSET, GameSettings.ICONS_SIZE, GameSettings.ICONS_SIZE);
        font.getData().setScale(GameSettings.STAMINA_FONT_SCALE);
        font.setColor(Color.SKY);
        batch.draw(staminaIcon, GameSettings.ICONS_X, GameSettings.SCREEN_HEIGHT - GameSettings.ICONS_Y_OFFSET - GameSettings.ICONS_SIZE * 1.2f, GameSettings.ICONS_SIZE, GameSettings.ICONS_SIZE);
        font.draw(batch, "" + player.getRoundedStamina(), GameSettings.ICONS_X + 110, GameSettings.SCREEN_HEIGHT - 150);
        font.setColor(Color.WHITE);
        font.getData().setScale(GameSettings.SCORE_FONT_SCALE);
        font.draw(batch, Long.toString(gameController.getScore()), (float) GameSettings.SCREEN_WIDTH /2 - 25, GameSettings.SCREEN_HEIGHT - 25);
        batch.draw(scrapIcon, GameSettings.ICONS_X, GameSettings.SCREEN_HEIGHT - 300, GameSettings.ICONS_SIZE, GameSettings.ICONS_SIZE);
        font.getData().setScale(GameSettings.SCRAP_FONT_SCALE);
        font.draw(batch, Long.toString(gameController.getScrap()), GameSettings.ICONS_X + GameSettings.ICONS_SIZE*1.5f, GameSettings.SCREEN_HEIGHT - 230);
        batch.end();
        batch.begin();
        buttons.get(2).changeState(GameScreen.gameController.getScrap() >= GameSettings.HEAL_COST);
        if(gameStage == GameStage.PAUSED){
            blackout.draw(batch);
            pause.draw(batch);
            menuSprite.draw(batch);
            resumeButton.draw(batch);
            menuButton.draw(batch);
            font.getData().setScale(0.7f);
            font.draw(batch, "Продолжить",GameSettings.SCREEN_WIDTH/2f - 195, GameSettings.SCREEN_HEIGHT/2f + 175);
            font.getData().setScale(1f);
            font.draw(batch, "Сдаться",GameSettings.SCREEN_WIDTH/2f - 175, GameSettings.SCREEN_HEIGHT/2f - 115);
        }
        else {
            joystick.draw(batch);
            attack.draw(batch);
            pause.draw(batch);
            heal.draw(batch);
            if(gameController.getScrap() < GameSettings.HEAL_COST) font.setColor(Color.RED);
            else font.setColor(Color.WHITE);
            font.draw(batch, Integer.toString(GameSettings.HEAL_COST), UI.HEAL_BUTTON_POS_X - UI.HEAL_BUTTON_DIAMETER/1.2f, UI.HEAL_BUTTON_POS_Y - 10);
            batch.draw(scrapIcon, UI.HEAL_BUTTON_POS_X - UI.HEAL_BUTTON_DIAMETER/3f, UI.HEAL_BUTTON_POS_Y - GameSettings.ICONS_SIZE, GameSettings.ICONS_SIZE, GameSettings.ICONS_SIZE);
        }
        //debugRenderer.render(world, camera.combined.scl(32));
        batch.end();

    }
    public void pauseGame(){
        pause.changeState(true);
        gameStage = GameStage.PAUSED;
    }

    @Override
    public void pause() {
        pauseGame();

    }

    @Override
    public void hide() {
        pauseGame();
    }

    @Override
    public void dispose() {
        font.dispose();
        batch.dispose();

    }
    public static void restartGame(){
        main.setScreen(new GameOverScreen(main, gameController.getScore()));
        gameController.getRoom().destroy();
    }

}
