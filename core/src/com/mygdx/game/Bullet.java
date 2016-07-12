package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;

/**
 * Created by Ryan on 7/4/2016.
 */
public class Bullet {
    private static AssetManager manager;
    private float bulletSpeed;
    private Vector2 position, velocity;
    private Rectangle bounds;
    public static Sprite sprite;

    static {
        //memory management -- loading image into manager
        String image = "images/badlogic.jpg";
        manager = new AssetManager();
        manager.load("images/badlogic.jpg", Texture.class);
        manager.finishLoading();
        sprite = new Sprite(manager.get("images/badlogic.jpg", Texture.class));
    }

    public Bullet() {

        //sprite = new Sprite(new Texture("badlogic.jpg"));
        //sprite.setSize(YOUR WIDTH, YOUR HEIGHT);
        sprite.setScale(sprite.getWidth(), sprite.getHeight());
        position = new Vector2();
        velocity = new Vector2();
        bounds = new Rectangle();
        setBulletSpeed(2000);
    }

    public void update() {
            //update position and bounds as the bullet travels
            float deltaTime = Gdx.graphics.getDeltaTime();
            getPosition().mulAdd(getVelocity(), deltaTime);
            setBounds(getPosition().x, getPosition().y);
    }

    public void setBulletSpeed(float speed) {bulletSpeed = speed;}

    public float getBulletSpeed() {return bulletSpeed;}

    public void setPosition(float x, float y) {position.set(x, y);}

    public Vector2 getPosition() {return position;}

    public void setVelocity(float x, float y) {velocity.set(x, y);}

    public Vector2 getVelocity() {return velocity;}

    public void setBounds(float x, float y) {bounds.set(x, y, sprite.getWidth(), sprite.getHeight());}

    public Rectangle getBounds() {return bounds;}

    public void draw(SpriteBatch batch) {batch.draw(sprite, position.x, position.y, sprite.getWidth(), sprite.getHeight());}

}
