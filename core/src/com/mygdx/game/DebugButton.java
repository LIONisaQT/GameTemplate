package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by Ryan on 7/5/2016.
 */
public class DebugButton extends Button {
    protected boolean debug;
    protected Sprite sprite;


    public DebugButton(float x, float y) {
        super(x, y);
        sprite = new Sprite(new Texture("images/RedPlayButton.png"));
        sprite.setSize(sprite.getWidth(), sprite.getHeight());
        setBounds();
        debug = false;
    }

    public void action() {
        debug = !debug;
    }
}
