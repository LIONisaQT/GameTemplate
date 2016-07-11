package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Ryan on 7/4/2016.
 */
public class Bullet {
    private AssetManager manager;
    private float bulletSpeed;
    private Vector2 position, velocity;
    private Rectangle bounds;
    public Sprite sprite;
    private Animation star;


    public Bullet() {
        //memory management -- loading image into manager
        manager = new AssetManager();
        manager.load("images/star1.gif", Texture.class);
        manager.finishLoading();
        sprite = new Sprite(manager.get("images/star1.gif", Texture.class));

        //sprite = new Sprite(new Texture("badlogic.jpg"));
        //sprite.setSize(YOUR WIDTH, YOUR HEIGHT);
        sprite.setScale(sprite.getWidth(), sprite.getHeight());
        position = new Vector2();
        velocity = new Vector2();
        bounds = new Rectangle();
        setBulletSpeed(2000);

        Texture frame1 = new Texture("images/star1.gif");
        frame1.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        Texture frame2 = new Texture("images/star2.gif");
        Texture frame3 = new Texture("images/star3.gif");
        Texture frame4 = new Texture("images/star4.gif");
        Texture frame5 = new Texture("images/star5.gif");
        Texture frame6 = new Texture("images/star6.gif");
        Texture frame7 = new Texture("images/star7.gif");
        Texture frame8 = new Texture("images/star8.gif");

        star = new Animation(0.05f, new TextureRegion(frame1), new TextureRegion(frame2), new TextureRegion(frame3), new TextureRegion(frame4), new TextureRegion(frame5), new TextureRegion(frame6), new TextureRegion(frame7), new TextureRegion(frame8));
        star.setPlayMode(Animation.PlayMode.LOOP);

    }

    public void update() {
        //update position and bounds as the bullet travels
        float deltaTime = Gdx.graphics.getDeltaTime();
        getPosition().mulAdd(getVelocity(), deltaTime);
        setBounds(getPosition().x, getPosition().y);
    }

    public void setBulletSpeed(float speed) {
        bulletSpeed = speed;
    }

    public float getBulletSpeed() {
        return bulletSpeed;
    }

    public void setPosition(float x, float y) {
        position.set(x, y);
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setVelocity(float x, float y) {
        velocity.set(x, y);
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public void setBounds(float x, float y) {
        bounds.set(x, y, sprite.getWidth(), sprite.getHeight());
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void draw(SpriteBatch batch) {
        batch.draw(sprite, position.x, position.y, sprite.getWidth(), sprite.getHeight());
    }

    public void draw(SpriteBatch batch, float time) {
        batch.draw(star.getKeyFrame(time), getPosition().x, getPosition().y, sprite.getWidth(), sprite.getHeight());
    }
}

