package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Created by MissionBit on 7/12/16.
 */
public class Pika extends Enemy {
    public Pika(float x,float y) {
        super(x, y);
        sprite=new Sprite(new Texture("images/zombie animmal.png"));
        setSpeed(100);
        sprite.setSize(180,350);

    }
}
