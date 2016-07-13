package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Ryan on 7/5/2016.
 */
public class Enemy {
    protected static final int NUM_ENEMIES = 5;
    private float speed;
    private Vector2 position, velocity;
    private Rectangle bounds;
    public Sprite sprite;
    private Animation ninjaEnemy;
    private Sprite ninjaEnemy1;
    private Sprite ninjaEnemy2;
    private Sprite ninjaEnemy3;
    private Sprite ninjaEnemy4;
    private Sprite ninjaEnemy5;
    private float objWidth;
    private float objHeight;
public Enemy(){}

    public Enemy(float x, float y) {
        sprite = new Sprite(new Texture("images/Ninja_Enemy(1).png"));
        //sprite.setSize(YOUR WIDTH, YOUR HEIGHT);
        position = new Vector2();
        setPosition(x, y);
        velocity = new Vector2();
        bounds = new Rectangle();
        setSpeed(100);
        objWidth = 90;
        objHeight = 100;
        sprite.setScale(objWidth, objHeight);

        //Enemy animation
        Texture frame1 = new Texture("images/Ninja_Enemy(1).png");
        frame1.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        Texture frame2 = new Texture("images/Ninja_Enemy(2).png");
        Texture frame3 = new Texture("images/Ninja_Enemy(3).png");
        Texture frame4 = new Texture("images/Ninja_Enemy(4).png");
        Texture frame5 = new Texture("images/Ninja_Enemy(5).png");
        ninjaEnemy1 = new Sprite(frame1);
        ninjaEnemy2 = new Sprite(frame2);
        ninjaEnemy3 = new Sprite(frame3);
        ninjaEnemy4 = new Sprite(frame4);
        ninjaEnemy5 = new Sprite(frame5);
        ninjaEnemy1.setScale(objWidth, objHeight);
        ninjaEnemy2.setScale(objWidth, objHeight);
        ninjaEnemy3.setScale(objWidth, objHeight);
        ninjaEnemy4.setScale(objWidth, objHeight);
        ninjaEnemy5.setScale(objWidth, objHeight);
        ninjaEnemy1.setSize(objWidth, objHeight);
        ninjaEnemy2.setSize(objWidth, objHeight);
        ninjaEnemy3.setSize(objWidth, objHeight);
        ninjaEnemy4.setSize(objWidth, objHeight);
        ninjaEnemy5.setSize(objWidth, objHeight);

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

    public void update() {
        setBounds();
    }

    public void setSpeed(float spd) {speed = spd;}

    public float getSpeed() {return speed;}

    public void setPosition(float x, float y) {position.set(x, y);}

    public Vector2 getPosition() {return position;}

    public void setVelocity(float x, float y) {velocity.set(x, y);}

    public Vector2 getVelocity() {return velocity;}

    public void setBounds() {bounds.set(getPosition().x, getPosition().y, sprite.getWidth(), sprite.getHeight());}

    public Rectangle getBounds() {return bounds;}

    public void draw(SpriteBatch batch) {batch.draw(sprite, getPosition().x, getPosition().y, objWidth, objHeight);}
}
