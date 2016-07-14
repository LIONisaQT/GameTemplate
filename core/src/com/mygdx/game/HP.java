package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;


/**
 * Created by MissionBit on 7/12/16.
 */

public class HP {
    int health;
    float x, y, width, height, sideWidth;

    ShapeRenderer renderer;

    public HP(SpriteBatch batch) {
        renderer = new ShapeRenderer();
        renderer.setAutoShapeType(true);
        renderer.setProjectionMatrix(batch.getProjectionMatrix());
        renderer.setTransformMatrix(batch.getTransformMatrix());
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
        //draw outline
        renderer.begin(ShapeRenderer.ShapeType.Line);
        renderer.setColor(Color.RED);
        renderer.rect(x, y, sideWidth, height);
        renderer.end();
        // draw fill
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(Color.YELLOW);
        renderer.rect(x, y, width, height);
        renderer.end();
    }

}
