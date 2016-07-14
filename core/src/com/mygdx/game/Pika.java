package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by MissionBit on 7/12/16.
 */
public class Pika extends Enemy {
    public Pika(float x,float y) {
        super(x, y);
        sprite=new Sprite(new Texture("images/zombie_animmal.png"));
        setSpeed(100);
        sprite.setSize(180,350);

    }
    public void draw(SpriteBatch batch) {
        batch.draw(sprite, getPosition().x, getPosition().y);
    }
}
