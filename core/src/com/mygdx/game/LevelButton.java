package com.mygdx.game;

/**
 * Created by Ryan on 7/14/2016.
 */
public class LevelButton extends Button {
    public LevelButton(float x, float y) {
        super(x, y);
        setBounds();
    }

    public void pressedAction() {MyGdxGame.state = MyGdxGame.GameState.IN_GAME;}
}
