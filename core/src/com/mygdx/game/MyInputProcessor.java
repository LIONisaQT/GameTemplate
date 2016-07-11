package com.mygdx.game;

import com.badlogic.gdx.InputProcessor;

/**
 * Created by MissionBit on 7/8/16.
 */
public class MyInputProcessor implements InputProcessor {
    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int x, int y, int pointer, int button) {
        Player.minDistance = 0;
        Player.first = y;
        return true;
    }

    @Override
    public boolean touchUp(int x, int y, int pointer, int button) {
        Player.last = y;
        return true;
    }

    @Override
    public boolean touchDragged(int x, int y, int pointer) {
        Player.minDistance += 1;
        return true;
    }

    @Override
    public boolean mouseMoved(int x, int y) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}