package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

import java.util.ArrayList;

/**
 * Created by Ryan on 7/14/2016.
 */
public class Level {
    private int level;

    public Level(int lvl) {
        setLevel(lvl);
    }

    public void setLevel(int lvl) {level = lvl;}

    public int getLevel() {return level;}



    /*
        put enemies inside arraylist here
        number of enemies depends on how many you want per level
        number of levels is controlled by an int inside MyGdxGame.java
     */
    public ArrayList<Enemy> getEnemies() {
        ArrayList<Enemy> enemies = new ArrayList<Enemy>();
        for (int i = 0; i < level * 5; i++) {
            Enemy ninja = new Enemy((float) Math.random() * MyGdxGame.scrWidth, (float) Math.random() * MyGdxGame.scrHeight + MyGdxGame.scrHeight);
            enemies.add(ninja);
            ninja.setSpeed(level * 5 + 90);


        }
        return enemies;
    }
}
