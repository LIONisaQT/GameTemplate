package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Player;

/**
 * Created by Ryan on 7/5/2016.
 */
public class Background {
    public Animation bgAnim;
    public float bgStateTime;

    public Background() {
        Texture frame1 = new Texture("Background/BG1.gif");
        frame1.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        Texture frame2 = new Texture("Background/BG2.gif");
        Texture frame3 = new Texture("Background/BG3.gif");
        Texture frame4 = new Texture("Background/BG4.gif");
        Texture frame5 = new Texture("Background/BG5.gif");
        Texture frame6 = new Texture("Background/BG6.gif");
        Texture frame7 = new Texture("Background/BG7.gif");
        Texture frame8 = new Texture("Background/BG8.gif");

        bgAnim = new Animation(0.1f, new TextureRegion(frame1), new TextureRegion(frame2), new TextureRegion(frame3), new TextureRegion(frame4), new TextureRegion(frame5), new TextureRegion(frame6), new TextureRegion(frame7), new TextureRegion(frame8));
        bgAnim.setPlayMode(Animation.PlayMode.LOOP);
        bgStateTime = 0;
        //sprite.setSize(YOUR WIDTH, YOUR HEIGHT);
        //enemies.setScale(enemies.getWidth(), enemies.getHeight());
    }


    public void draw(SpriteBatch batch) { batch.draw(bgAnim.getKeyFrame(bgStateTime), 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }
}
