package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;


/**
 * Created by MissionBit on 7/12/16.
 */

public class HP {
    int health;
    float x, y, width, height, sideWidth;

    ShapeRenderer renderer;
    ShapeRenderer outline;

    public HP() {
        renderer = new ShapeRenderer();
        renderer.setAutoShapeType(true);
        renderer.setColor(Color.GREEN);

        outline = new ShapeRenderer();
        outline.setAutoShapeType(true);
        outline.setColor(Color.RED);

        health = 5;
        width = 350;
        height = 50;
        sideWidth = 350;
        x = MyGdxGame.scrWidth - width - 20;
        y = MyGdxGame.scrHeight- height - 20;

    }

    public void hit(){
        width = width-(width/health);
        health = health - 1;
    }

    public void reset() {
        width = 350;
        health = 5;
    }

    public void draw() {
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.rect(x, y, width, height);
        renderer.end();
        outline.begin();
        outline.rect(x, y, sideWidth, height);
        outline.end();
    }

}
