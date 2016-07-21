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
        sprite.setSize(MyGdxGame.scrWidth/5, MyGdxGame.scrHeight/7);
        sprite.setScale(MyGdxGame.scrWidth/5, MyGdxGame.scrHeight/7);
        setBounds();
        stopButton = new Sprite(new Texture("images/stopButton.png"));
    }

    public void action() {
        if (MyGdxGame.state == MyGdxGame.GameState.START) MyGdxGame.state = MyGdxGame.GameState.IN_GAME;
        else if (MyGdxGame.state == MyGdxGame.GameState.IN_GAME) MyGdxGame.state = MyGdxGame.GameState.GAME_OVER;
        //else MyGdxGame.state = MyGdxGame.GameState.START;
    }

    public void draw(SpriteBatch batch) {
        if (MyGdxGame.state == MyGdxGame.GameState.START) {
            setPosition(MyGdxGame.scrWidth - sprite.getWidth() - 20, 20);
            setBounds();
            batch.draw(sprite, position.x, position.y, sprite.getWidth(), sprite.getHeight());
        }
        else if (MyGdxGame.state == MyGdxGame.GameState.IN_GAME) {
            setPosition(250, MyGdxGame.scrHeight - 80 );
            setBounds();
            batch.draw(stopButton, 250, MyGdxGame.scrHeight - sprite.getHeight() - 20, stopButton.getWidth(), stopButton.getHeight());

        }

    }

}
