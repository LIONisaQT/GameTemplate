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

    float width, height;
    float spriteSpeed;

    public ArrowControls() {
        width = MyGdxGame.scrWidth / 10;
        height = MyGdxGame.scrHeight / 6;
        spriteSpeed = 10;

        upButton = new Sprite(new Texture("images/arrowUp.png"));
        leftButton = new Sprite(new Texture("images/arrowLeft.png"));
        rightButton = new Sprite(new Texture("images/arrowRight.png"));


        upButton.setSize(width, height);
        leftButton.setSize(width, height);
        rightButton.setSize(width, height);

        upButton.setPosition(width + 20, height);
        leftButton.setPosition(20, 20);
        rightButton.setPosition(width * 2 + 20, 20);
        upButton.setAlpha(0.5f);
        leftButton.setAlpha(0.5f);
        rightButton.setAlpha(0.5f);
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
