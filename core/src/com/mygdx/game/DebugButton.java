package com.mygdx.game;

/**
 * Created by Ryan on 7/5/2016.
 */
public class DebugButton extends Button {
    protected boolean debug;
    public DebugButton(float x, float y) {
        super(x, y);
        sprite.setSize(100, 100);
        setBounds();
        debug = false;
    }

    public void action() {
        debug = !debug;}
}
