package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Ryan on 7/5/2016.
 */
public class Button {
    protected Sprite sprite;
    protected Vector2 position;
    protected Rectangle bounds;

    public Button(float x, float y) {
        sprite = new Sprite(new Texture("images/zombieButton.png"));
        //sprite.setSize(YOUR WIDTH, YOUR HEIGHT);
        sprite.setScale(sprite.getWidth(), sprite.getHeight());
        position = new Vector2();
        setPosition(x, y);
        bounds = new Rectangle();
        setBounds();
    }

    //make buttons do whatever you want here
    public void action() {}

    public boolean isPressed() {return (Gdx.input.justTouched() && this.getBounds().contains(MyGdxGame.getTapPosition().x, MyGdxGame.getTapPosition().y));}

    public void setPosition(float x, float y) {position.set(x, y);}

    public Vector2 getPosition() {return position;}

    public void setBounds() {bounds.set(getPosition().x, getPosition().y, sprite.getWidth(), sprite.getHeight());}

    public Rectangle getBounds() {return bounds;}

    public void draw(SpriteBatch batch) {batch.draw(sprite, position.x, position.y, sprite.getWidth(), sprite.getHeight());}
}