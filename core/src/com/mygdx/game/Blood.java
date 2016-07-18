package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Ryan on 7/12/2016.
 */
public class Blood {
    private static float FADE_TIME = 4;
    private static AssetManager manager;
    private float fadeStart, deltaTime;
    public Sprite sprite;
    public Texture texture;
    private Vector2 position;

    static {
        //memory management -- loading image into manager
        String image = "images/blood.png";
        manager = new AssetManager();
        manager.load(image, Texture.class);
        manager.finishLoading();
    }

    public Blood(float x, float y) {
        sprite = new Sprite(manager.get("images/blood.png", Texture.class));
        sprite.setSize(100, 100);
        sprite.setScale(sprite.getWidth(), sprite.getHeight());
        position = new Vector2();
        setPosition(x, y);
        deltaTime = Gdx.graphics.getDeltaTime();
    }

    public void setPosition(float x, float y) {position.set(x, y);}

    public Vector2 getPosition() {return position;}

    public void draw(SpriteBatch batch) {
        fadeStart += deltaTime;
        float alpha = 1 - (fadeStart / FADE_TIME);
        if (alpha < 0) {
            alpha = 0;
//            texture.dispose();
        }

        batch.setColor(1, 1, 1, alpha);
        batch.draw(sprite, getPosition().x, getPosition().y, sprite.getWidth(), sprite.getHeight());

        //reset alpha so everything else remains opaque
        batch.setColor(1, 1, 1, 1f);
    }
}
