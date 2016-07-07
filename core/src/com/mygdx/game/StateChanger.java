package com.mygdx.game;

/**
 * Created by Ryan on 7/5/2016.
 */
public class StateChanger extends Button {
    public StateChanger(float x, float y) {
        super(x, y);
        sprite.setSize(MyGdxGame.scrWidth / 2 - 20, MyGdxGame.scrHeight / 10);
        setBounds();
    }

    public void action() {
        if (MyGdxGame.state == MyGdxGame.GameState.START) MyGdxGame.state = MyGdxGame.GameState.IN_GAME;
        else if (MyGdxGame.state == MyGdxGame.GameState.IN_GAME) MyGdxGame.state = MyGdxGame.GameState.GAME_OVER;
        //else MyGdxGame.state = MyGdxGame.GameState.START;
    }
}
