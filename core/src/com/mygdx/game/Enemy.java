package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;


import javafx.concurrent.Task;
import java.util.ArrayList;

/**
 * Created by Ryan on 7/5/2016.
 */
public class  Enemy {
    private static AssetManager manager;

    protected static final int NUM_ENEMIES = 5;
    private float speed;
    private Vector2 position, velocity;
    private Rectangle bounds;
    public Sprite sprite;
    private static Animation ninjaEnemy;
    private Sprite ninjaEnemy1;
    private Sprite ninjaEnemy2;
    private Sprite ninjaEnemy3;
    private Sprite ninjaEnemy4;
    private Sprite ninjaEnemy5;
    private static float objWidth;
    private static float objHeight;

    static {
        objWidth = 90;
        objHeight = 100;
        manager = new AssetManager();
        manager.load("images/Ninja_Enemy(1).png", Texture.class);
        manager.load("images/Ninja_Enemy(2).png", Texture.class);
        manager.load("images/Ninja_Enemy(3).png", Texture.class);
        manager.load("images/Ninja_Enemy(4).png", Texture.class);
        manager.load("images/Ninja_Enemy(5).png", Texture.class);
        manager.finishLoading();

    }

    public Enemy(float x, float y) {
        sprite = new Sprite(new Texture("images/Ninja_Enemy(1).png"));
        //sprite.setSize(YOUR WIDTH, YOUR HEIGHT);
        position = new Vector2();
        setPosition(x, y);
        velocity = new Vector2();
        bounds = new Rectangle();
        setSpeed(100);

        sprite.setScale(objWidth, objHeight);

        ninjaEnemy1 = new Sprite(manager.get("images/Ninja_Enemy(1).png", Texture.class));
        ninjaEnemy2 = new Sprite(manager.get("images/Ninja_Enemy(2).png", Texture.class));
        ninjaEnemy3 = new Sprite(manager.get("images/Ninja_Enemy(3).png", Texture.class));
        ninjaEnemy4 = new Sprite(manager.get("images/Ninja_Enemy(4).png", Texture.class));
        ninjaEnemy5 = new Sprite(manager.get("images/Ninja_Enemy(5).png", Texture.class));
        ninjaEnemy1.setScale(MyGdxGame.scrWidth / 15, MyGdxGame.scrHeight / 7);
        ninjaEnemy2.setScale(MyGdxGame.scrWidth / 15, MyGdxGame.scrHeight / 7);
        ninjaEnemy3.setScale(MyGdxGame.scrWidth / 15, MyGdxGame.scrHeight / 7);
        ninjaEnemy4.setScale(MyGdxGame.scrWidth / 15, MyGdxGame.scrHeight / 7);
        ninjaEnemy5.setScale(MyGdxGame.scrWidth / 15, MyGdxGame.scrHeight / 7);
        ninjaEnemy1.setSize(MyGdxGame.scrWidth / 15, MyGdxGame.scrHeight / 7);
        ninjaEnemy2.setSize(MyGdxGame.scrWidth / 15, MyGdxGame.scrHeight / 7);
        ninjaEnemy3.setSize(MyGdxGame.scrWidth / 15, MyGdxGame.scrHeight / 7);
        ninjaEnemy4.setSize(MyGdxGame.scrWidth / 15, MyGdxGame.scrHeight / 7);
        ninjaEnemy5.setSize(MyGdxGame.scrWidth / 15, MyGdxGame.scrHeight / 7);
        ninjaEnemy = new Animation(0.05f, new TextureRegion(ninjaEnemy1), new TextureRegion(ninjaEnemy2), new TextureRegion(ninjaEnemy3), new TextureRegion(ninjaEnemy4), new TextureRegion(ninjaEnemy5));
        ninjaEnemy.setPlayMode(Animation.PlayMode.LOOP);
    }

    public void move() {}

    public void followPlayer(Player player) {
        float deltaTime = Gdx.graphics.getDeltaTime();
        float deltaX = player.getPosition().x - getPosition().x;
        float deltaY = player.getPosition().y - getPosition().y;
        float rotation = MathUtils.atan2(deltaY, deltaX) / MathUtils.PI * 180;

        setVelocity(MathUtils.cos(rotation / 180 * MathUtils.PI) * getSpeed(),
                MathUtils.sin(rotation / 180 * MathUtils.PI) * getSpeed());
        getPosition().mulAdd(getVelocity(), deltaTime);
    }

    public void respawn()  {
        setPosition((float) Math.random() * MyGdxGame.scrWidth, (float) Math.random() * MyGdxGame.scrHeight + 250);

    }


    public void update() {
        setBounds();
    }

    public void setSpeed(float spd) {speed = spd;}

    public float getSpeed() {return speed;}

    public void setPosition(float x, float y) {position.set(x, y);}

    public Vector2 getPosition() {return position;}

    public void setVelocity(float x, float y) {velocity.set(x, y);}

    public Vector2 getVelocity() {return velocity;}

    public void setBounds() {bounds.set(getPosition().x, getPosition().y, ninjaEnemy1.getWidth(), ninjaEnemy1.getHeight());}

    public Rectangle getBounds() {return bounds;}

    public void draw(SpriteBatch batch, float time) {
        batch.draw(ninjaEnemy.getKeyFrame(time), getPosition().x, getPosition().y, ninjaEnemy1.getWidth(), ninjaEnemy1.getHeight());
    }
}
