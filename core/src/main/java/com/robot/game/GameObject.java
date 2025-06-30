package com.robot.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.robot.game.constants.GameSettings;
import com.robot.game.screens.GameScreen;

public class GameObject {
    public void setX(float x) {
        this.x = x;
    }

    private float x;

    public void setY(float y) {
        this.y = y;
    }

    private float y;



    private float width;

    public void setHeight(float height) {
        this.height = height;
    }

    private float height;
    public Texture texture;
    public BodyDef.BodyType bodyType;
    public Sprite sprite;
    public Fixture fixture;
    public Body body;
    public World world = GameScreen.world;
    public Filter filter = new Filter();
    public TextureRegion textureRegion;


    public GameObject(float x, float y, float width, float height, Texture texture, BodyDef.BodyType bodyType) {
        this.x = x;
        this.y = y;
        this.bodyType = bodyType;
        this.width = width;
        this.height = height;
        this.texture = texture;
    }
    public GameObject(float x, float y, float width, float height, TextureRegion textureRegion, BodyDef.BodyType bodyType) {
        this.x = x;
        this.y = y;
        this.bodyType = bodyType;
        this.width = width;
        this.height = height;
        this.textureRegion = textureRegion;
    }
    public Body createBody(BodyDef.BodyType bodyType, float x, float y, float width, float height){
        Body body;
        BodyDef bodyDef = new BodyDef();
        FixtureDef fixtureDef;

            bodyDef.type = bodyType;
            bodyDef.position.set(x/ GameSettings.PPM, y/GameSettings.PPM);

            body = world.createBody(bodyDef);

            PolygonShape shape = new PolygonShape();
            shape.setAsBox(
            (width / 2) / GameSettings.PPM,
            (height / 2) / GameSettings.PPM, new Vector2(width /2/GameSettings.PPM, height /2/GameSettings.PPM), 0
            );

            fixtureDef = new FixtureDef();

            fixtureDef.shape = shape;

            fixture = body.createFixture(fixtureDef);
            fixture.setUserData(this);
            shape.dispose();
            return body;}


    public Body createBody(BodyDef.BodyType bodyType, float x, float y, float diameter, boolean isActor, boolean hasBiggerSprite) {
        Body body;
        BodyDef bodyDef = new BodyDef();
        FixtureDef fixtureDef;


        bodyDef.type = bodyType;
        bodyDef.position.set(x / GameSettings.PPM, y / GameSettings.PPM);

        body = world.createBody(bodyDef);
        diameter = hasBiggerSprite? diameter/2 : diameter;
        CircleShape shape = new CircleShape();
        float diameterDivider = isActor ? 3 : 2;
        shape.setRadius(diameter / GameSettings.PPM / diameterDivider);

        fixtureDef = new FixtureDef();

        fixtureDef.shape = shape;

        fixture = body.createFixture(fixtureDef);
        fixture.setUserData(this);
        shape.dispose();
        return body;
    }
    public void initCategoryBits(short categoryBits, short groupIndex, short maskBits){
        filter.categoryBits = categoryBits;
        filter.maskBits = maskBits;
        filter.groupIndex = groupIndex;

        getFixture().setFilterData(filter);

    }
    public Fixture getFixture(){
        return fixture;
    }
    public void setUserData(Object object){
        fixture.setUserData(object);
    }


    public float getX() {
        return x;
    }
    public float getY() {
        return y;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public Body getBody() {
        return body;
    }
}
