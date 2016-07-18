package com.mygdx.game;

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
        if (getLevel() == 0) {
           for (int i = 0; i < Enemy.NUM_ENEMIES; i++) {
               enemies.add(new Enemy((float)Math.random() * MyGdxGame.scrWidth, (float)Math.random() * MyGdxGame.scrHeight));
           }
        } else if (getLevel() == 1) {
            for (int k = 0; k < Enemy.NUM_ENEMIES; k++) {
                enemies.add(new Enemy((float)Math.random() * MyGdxGame.scrWidth, (float)Math.random() * MyGdxGame.scrHeight));
                enemies.get(k).setSpeed(400);
//                enemies.add(new Enemy((float)Math.random() * MyGdxGame.scrWidth, (float)Math.random() * MyGdxGame.scrHeight));
            }
        }
        //ADD MOAR LEVELS
        return enemies;
    }
}