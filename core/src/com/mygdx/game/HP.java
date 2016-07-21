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
        health = 100;
        width = MyGdxGame.scrWidth / 3;
        height = MyGdxGame.scrHeight / 10;
        sideWidth = MyGdxGame.scrWidth / 3;
        x = MyGdxGame.scrWidth - width - 20;
        y = MyGdxGame.scrHeight- height - 20;
    }

    public void hit(){
        width = width-(width/health);
        health = health - 1;
    }

    public void reset() {
        width = MyGdxGame.scrWidth / 3;
        health = 100;
    }

    public void draw() {
        //draw outline
        renderer.begin(ShapeRenderer.ShapeType.Line);
        renderer.setColor(Color.RED);
        renderer.rect(x, y, sideWidth, height);
        renderer.end();
        // draw fill
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        if (health > 50) renderer.setColor(Color.GREEN);
        else if (health <= 50 && health > 15) renderer.setColor(Color.YELLOW);
        else renderer.setColor(Color.RED);
        renderer.rect(x, y, width, height);
        renderer.end();
    }

}
