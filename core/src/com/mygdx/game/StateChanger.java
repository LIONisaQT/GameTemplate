package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by Ryan on 7/5/2016.
 */
public class StateChanger extends Button {
    Sprite stopButton;


    public StateChanger(float x, float y) {
        super(x, y);
        sprite = new Sprite(new Texture("images/RedPlayButton.png"));
        sprite.setSize(sprite.getWidth(), sprite.getHeight());
        setBounds();
        stopButton = new Sprite(new Texture("images/stopButton.png"));


    }

    public void action() {
        if (MyGdxGame.state == MyGdxGame.GameState.START) MyGdxGame.state = MyGdxGame.GameState.LEVEL_SELECT;
        else if (MyGdxGame.state == MyGdxGame.GameState.LEVEL_SELECT) MyGdxGame.state = MyGdxGame.GameState.IN_GAME;
        else if (MyGdxGame.state == MyGdxGame.GameState.IN_GAME) MyGdxGame.state = MyGdxGame.GameState.GAME_OVER;
        //else MyGdxGame.state = MyGdxGame.GameState.START;
    }

    public void draw(SpriteBatch batch) {
        if (MyGdxGame.state == MyGdxGame.GameState.START) {
            setPosition(MyGdxGame.scrWidth - 150, 10);
            setBounds();
            batch.draw(sprite, position.x, position.y, sprite.getWidth(), sprite.getHeight());
        }
        else if (MyGdxGame.state == MyGdxGame.GameState.IN_GAME) {
            batch.draw(stopButton, 250, MyGdxGame.scrHeight - 80, stopButton.getWidth(), stopButton.getHeight());
            setPosition(250, MyGdxGame.scrHeight - 80 );
            setBounds();

        }

    }

}
