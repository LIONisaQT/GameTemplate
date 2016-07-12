package com.mygdx.game;

/**
 * Created by Ryan on 7/5/2016.
 */
public class StateChanger extends Button {
    public StateChanger(float x, float y) {
        super(x, y);
        //sprite.setSize(MyGdxGame.scrWidth / 2 - 20, MyGdxGame.scrHeight / 10);
        sprite.setSize(100, 100);
        setBounds();
    }

    public void action() {
        if (MyGdxGame.state == MyGdxGame.GameState.START) MyGdxGame.state = MyGdxGame.GameState.IN_GAME;
//        else if (MyGdxGame.state == MyGdxGame.GameState.GAME_OVER) MyGdxGame.state = MyGdxGame.GameState.IN_GAME;
        //else MyGdxGame.state = MyGdxGame.GameState.START;
    }
}
