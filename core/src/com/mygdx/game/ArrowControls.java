package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by Kat on 7/13/16.
 */
public class ArrowControls {
    Sprite upButton;
    Sprite leftButton;
    Sprite rightButton;

    float size;
    int spriteSpeed;

    public ArrowControls() {
        size = 100;
        spriteSpeed = 10;

        upButton = new Sprite(new Texture("images/arrowUp.png"));
        leftButton = new Sprite(new Texture("images/arrowLeft.png"));
        rightButton = new Sprite(new Texture("images/arrowRight.png"));


        upButton.setBounds(120, 100, size, size);
        leftButton.setBounds(20, 20, size, size);
        rightButton.setBounds(220, 20, size, size);
    }

    public void update(Player player) {
        // set touch positions
        int xTouch = Gdx.input.getX();
        int yTouch = (int)MyGdxGame.scrHeight - Gdx.input.getY();

        if (Gdx.input.isTouched() && leftButton.getBoundingRectangle().contains(xTouch, yTouch)) {
            player.setPosition(player.getPosition().x - spriteSpeed, player.getPosition().y);
        }
        else if (Gdx.input.isTouched() && rightButton.getBoundingRectangle().contains(xTouch, yTouch)) {
            player.setPosition(player.getPosition().x + spriteSpeed, player.getPosition().y);
        }
        else if (Gdx.input.isTouched() && upButton.getBoundingRectangle().contains(xTouch, yTouch)) {
            player.jump();
        }
    }

    public boolean isTouched() {
        // set touch positions
        int xTouch = Gdx.input.getX();
        int yTouch = (int)MyGdxGame.scrHeight - Gdx.input.getY();

        if (Gdx.input.isTouched() && leftButton.getBoundingRectangle().contains(xTouch, yTouch) ||
                Gdx.input.isTouched() && rightButton.getBoundingRectangle().contains(xTouch, yTouch) ||
                Gdx.input.isTouched() && upButton.getBoundingRectangle().contains(xTouch, yTouch)) {
            return true;
        }
        return false;
    }



    public void draw(SpriteBatch batch) {
        upButton.draw(batch);
        leftButton.draw(batch);
        rightButton.draw(batch);
    }
}
