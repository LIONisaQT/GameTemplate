package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

/**
 * Created by Ryan on 7/4/2016.
 */
public class Player {
    private float xFactor, yFactor; //how much lean you need to move
    private Vector2 position, velocity, accel;
    private Rectangle bounds;
    public Sprite sprite;

    public Player() {
        sprite = new Sprite(new Texture("player3.png"));
        //sprite.setSize(YOUR WIDTH, YOUR HEIGHT);
        sprite.setScale(sprite.getWidth(), sprite.getHeight());
        position = new Vector2();
        velocity = new Vector2();
        accel = new Vector2();
        bounds = new Rectangle();
        xFactor = -100; //play with this value
        yFactor = -100; //play with this value
    }

    //shoot bullets from the player!
    public void shoot(ArrayList<Bullet> bullets) {
        //ew math, don't need to touch this at all
        float deltaX = MyGdxGame.getTapPosition().x - getPosition().x;
        float deltaY = MyGdxGame.getTapPosition().y - getPosition().y;
        float rotation = MathUtils.atan2(deltaY, deltaX) / MathUtils.PI * 180;

        //bullet spawning
        Bullet bullet = new Bullet();
        bullet.setPosition(getPosition().x + 74, getPosition().y + 90);
        bullet.setVelocity(MathUtils.cos(rotation / 180 * MathUtils.PI) * bullet.getBulletSpeed(),
                MathUtils.sin(rotation / 180 * MathUtils.PI) * bullet.getBulletSpeed());
        bullets.add(bullet);
    }

    //EXPERIMENTAL SHIT
    public void shoot(Bullet bullet) {
        //ew math, don't need to touch this at all
        float deltaX = MyGdxGame.getTapPosition().x - getPosition().x;
        float deltaY = MyGdxGame.getTapPosition().y - getPosition().y;
        float rotation = MathUtils.atan2(deltaY, deltaX) / MathUtils.PI * 180;

        bullet.setPosition(getPosition().x, getPosition().y);
        bullet.setVelocity(MathUtils.cos(rotation / 180 * MathUtils.PI) * bullet.getBulletSpeed(),
                MathUtils.sin(rotation / 180 * MathUtils.PI) * bullet.getBulletSpeed());
    }

    //all movement code here
//    public void tiltControls() {
//        float deltaTime = Gdx.graphics.getDeltaTime();
//        setAccel(-Gdx.input.getAccelerometerY(), Gdx.input.getAccelerometerX(), xFactor, yFactor);
//
//        //makes movement feel snappier, comment out for sluggish turning
//        if (Gdx.input.getAccelerometerX() > 0 || Gdx.input.getAccelerometerX() < 0) getVelocity().x = 0;
//        if (Gdx.input.getAccelerometerY() > 0 || Gdx.input.getAccelerometerY() < 0) getVelocity().y = 0;
//
//        getVelocity().add(getAccel().x, getAccel().y);
//        getPosition().mulAdd(getVelocity(), deltaTime);
//    }

    //make player wrap around screen
    public void wrap() {
        //left and right warping
        if (getPosition().x > MyGdxGame.scrWidth) {
            setPosition(0 - getBounds().getWidth(), getPosition().y);
        } else if (getPosition().x < 0) {
            setPosition(0, getPosition().y);
        }

        //top and bottom warping
        if (getPosition().y > MyGdxGame.scrHeight) {
            setPosition(getPosition().x, 0 - getBounds().getHeight());
        } else if (getPosition().y < 0 - getBounds().getHeight()) {
            setPosition(getPosition().x, MyGdxGame.scrHeight);
        }
    }

    public void reset() {
//        setPosition(MyGdxGame.scrWidth / 2 - getBounds().getWidth() / 2, MyGdxGame.scrHeight / 2);
        setPosition(0 - getBounds().getWidth() / 2, MyGdxGame.scrHeight / 2 - 90);
        setVelocity(0, 0);
    }

    //turn on shit in here
    public void update() {
        setBounds();
        if (MyGdxGame.state == MyGdxGame.GameState.IN_GAME) {
//            tiltControls();
            wrap();
        }
    }

    //one dimensional movement, third parameter controls up-down or left-right
    public void setAccel(float acc, float multiplier, char c) {
        if (c == 'x') accel.set(acc * multiplier, 0);
        else if (c == 'y') accel.set(0, acc * multiplier);
    }

    //two dimensional movement
    public void setAccel(float x, float y, float xMultiplier, float yMultiplier) {accel.set(x * xMultiplier, y * yMultiplier);}

    public Vector2 getAccel() {return accel;}

    public void setPosition(float x, float y) {position.set(x, y);}

    public Vector2 getPosition() {return position;}

    public void setVelocity(float x, float y) {velocity.set(x, y);}

    public Vector2 getVelocity() {return velocity;}

    public void setBounds() {bounds.set(getPosition().x, getPosition().y, sprite.getWidth(), sprite.getHeight());}

    public Rectangle getBounds() {return bounds;}

    public void draw(SpriteBatch batch) {batch.draw(sprite, getPosition().x, getPosition().y, sprite.getWidth(), sprite.getHeight());}
}
