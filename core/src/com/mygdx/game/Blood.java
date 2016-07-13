package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Ryan on 7/12/2016.
 */
public class Blood {
    public Sprite sprite;
    private Vector2 position;

    public Blood(float x, float y) {
        sprite = new Sprite(new Texture("images/badlogic.jpg"));
        //sprite.setColor(0, 0, 0, 0.5f);
        sprite.setSize(100, 100);
        sprite.setScale(sprite.getWidth(), sprite.getHeight());
        position = new Vector2();
        setPosition(x, y);
    }

    public void setPosition(float x, float y) {position.set(x, y);}

    public Vector2 getPosition() {return position;}

    public void draw(SpriteBatch batch) {
        batch.setColor(1, 1, 1, 0.5f);
        batch.draw(sprite,getPosition().x, getPosition().y, sprite.getWidth(), sprite.getHeight());
        batch.setColor(1, 1, 1, 1f);
    }
}
