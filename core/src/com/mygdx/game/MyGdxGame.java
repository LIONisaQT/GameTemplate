package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.particles.influencers.ColorInfluencer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.assets.loaders.BulletLoader;

import java.util.ArrayList;
import java.util.Random;

public class MyGdxGame extends ApplicationAdapter {
    protected static float scrWidth;
    protected static float scrHeight;

    protected enum GameState {START, IN_GAME, GAME_OVER}

    protected static GameState state;
    protected static Vector2 gravity;

    protected static Preferences preferences;
    protected static int score, highScore;

    private AssetManager manager; //EXPERIMENTAL SHIT

    private SpriteBatch batch;
    private static Vector3 tap; //holds the position of tap location
    private BitmapFont font;
    private GlyphLayout layout;
    private Player player;
    private HP hpBar;
    private ArrayList<Bullet> bullets;
    private ArrayList<Enemy> enemies;
    private Music music;
    private Music music1;
    private Sound shootSound, matchSound;
    private Sprite background;


    public static OrthographicCamera camera; //camera is your game world camera
    public static OrthographicCamera uiCamera; //uiCamera is your heads-up display

    private DebugButton debug;
    private StateChanger stateChanger;

    private float time = 0;

    @Override
    public void create() {
        hpBar = new HP();
        scrWidth = Gdx.graphics.getWidth();
        scrHeight = Gdx.graphics.getHeight();
        gravity = new Vector2();
        background=new Sprite(new Texture("images/shawdow forrest.jpg"));

        preferences = new Preferences("Preferences");
        //if theree are no high scores, then make one
        if (preferences.getInteger("highScore", 0) == 0) {
            highScore = 0;
            preferences.putInteger("highScore", highScore);
        } else
            highScore = preferences.getInteger("highScore", 0); //set highScore to saved value

        /*
        =====EXPERIMENTAL SHIT=====
        manager = new AssetManager();
        manager.setLoader(Bullet.class, new BulletLoader(new InternalFileHandleResolver()));
        manager.load("Bullet.java", Bullet.class);
        manager.finishLoading();
        //manager.load(new AssetDescriptor<Bullet>("Bullet.java", Bullet.class, new BulletLoader.BulletParameter()));
        =====EXPERIMENTAL SHIT=====
        */

        batch = new SpriteBatch();
        tap = new Vector3(); //location of tap
        font = new BitmapFont(Gdx.files.internal("fonts/arial.fnt"),
                Gdx.files.internal("fonts/arial.png"), false);
        music = Gdx.audio.newMusic(Gdx.files.internal("music/epicAdventure.mp3"));
        music1 = Gdx.audio.newMusic(Gdx.files.internal("music/japanese.mp3"));
        music.setLooping(true);
        music.play();
        matchSound = Gdx.audio.newSound(Gdx.files.internal("sounds/matchStart.wav"));
        shootSound = Gdx.audio.newSound(Gdx.files.internal("sounds/shootStar.mp3"));
        layout = new GlyphLayout();
        player = new Player();
        bullets = new ArrayList<Bullet>();
        enemies = new ArrayList<Enemy>();


        camera = new OrthographicCamera();
        camera.setToOrtho(false, scrWidth, scrHeight);
        uiCamera = new OrthographicCamera();
        uiCamera.setToOrtho(false, scrWidth, scrHeight);
        uiCamera.update();

        debug = new DebugButton(10, 10);
        stateChanger = new StateChanger(scrWidth / 2 + 10, 10);

        Random randomNum = new Random();
        int songNum =randomNum.nextInt(2) + 1;
        if(songNum == 1) {
        music.play();
            music.setLooping(true);
        }

        if(songNum == 2){
            music1.play();
            music.setLooping(true);
        }

        resetGame();


    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        updateGame();
        drawGame();
    }

    private void resetGame() {
        state = GameState.START;
        gravity.set(0, -50);
        player.reset();
        bullets.clear();
        enemies.clear();
        hpBar.reset();


    }



    /*
      - gets and translates coordinates of tap to game world coordinates
      - you don't need to touch this at all
    */
    public static Vector3 getTapPosition() { //gets and translates coordinates of tap to game world coordinates
        tap.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        return camera.unproject(tap);
    }

    private void updateGame() {
        float deltaTime = Gdx.graphics.getDeltaTime();
        time += deltaTime;
        player.update();
        for (Enemy enemy : enemies) {
            enemy.update();
        }

        if (state == GameState.START) {
            if (debug.isPressed()) debug.action();
            if (stateChanger.isPressed()) {
                matchSound.play();
                for (int i = 0; i < Enemy.NUM_ENEMIES; i++)
                    enemies.add(new Enemy((float)Math.random() * scrWidth, (float)Math.random() * scrHeight + 250));
                stateChanger.action();
            }
        }

        else if (state == GameState.IN_GAME) {
            for (Enemy enemy : enemies) {enemy.followPlayer(player);}
            if (stateChanger.isPressed()) stateChanger.action();
            if (Gdx.input.justTouched()) {
                /*
                =====EXPERIMENTAL SHIT=====
                Bullet bullet = manager.get("Bullet.java");
                bullets.add(bullet);
                =====EXPERIMENTAL SHIT=====
                */
                shootSound.play();
                player.shoot(bullets);
            }

            //bullet-only codes
            for (int i = 0; i < bullets.size(); i++) {
                bullets.get(i).update();
                //removes bullets from memory if they go off screen
                if (bullets.get(i).getPosition().x > scrWidth
                        || bullets.get(i).getPosition().x < 0 - bullets.get(i).getBounds().getWidth()
                        || bullets.get(i).getPosition().y > scrHeight
                        || bullets.get(i).getPosition().y < 0 - bullets.get(i).getBounds().getHeight()) {
                    bullets.remove(i);
                }
            }

            //collision
            for (int j = 0; j < enemies.size(); j++) {
                //player die when collide with enemy
                if (enemies.get(j).getBounds().overlaps(player.getBounds())) {
                    enemies.get(j).respawn();
                    hpBar.hit();
                    if (hpBar.health <= 0){
                        state = GameState.GAME_OVER;
                    }

                }
                //remove bullet and enemy when they collide
                for (int i = 0; i < bullets.size(); i++)  {
                    if (enemies.get(j).getBounds().overlaps(bullets.get(i).getBounds()))  {
                        enemies.get(j).respawn();
                        bullets.remove(i);

                    }
                }
            }
        }

        else { //state is GAME_OVER
            //sets score to high score if you beat highScore
            if (score > highScore) {
                highScore = score;
                preferences.putInteger("highScore", score);
            }
            preferences.flush(); //saves
            if (Gdx.input.justTouched()) {
                resetGame();

            }
        }
    }

	private void drawGame() {
        //game world camera
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(background,0,0,scrWidth,scrHeight);

        font.setColor(Color.WHITE);
        if (state == GameState.START) {
            //start shit here
            font.draw(batch, "text  ", 20, MyGdxGame.scrHeight - 20);
        } else if (state == GameState.IN_GAME) {
            for (Bullet bullet : bullets) bullet.draw(batch, time);
            player.draw(batch, time);
            for (Enemy enemy : enemies) enemy.draw(batch);
            hpBar.draw();
        } else {
            //gameover shit here
        }
        batch.end();

        //game ui camera
        batch.setProjectionMatrix(uiCamera.combined);
        batch.begin();
        if (debug.debug) {
            font.draw(batch, "Game state: " + MyGdxGame.state, 20, MyGdxGame.scrHeight - 20);
            font.draw(batch, "Bullet count: " + bullets.size(), 20, MyGdxGame.scrHeight - 70);
            font.draw(batch, "Number of enemies: " + enemies.size(), 20, MyGdxGame.scrHeight - 120);
            font.draw(batch, "Velocity: " + (int)player.getVelocity().x + ", " + (int)player.getVelocity().y, 20, MyGdxGame.scrHeight - 170);
            font.draw(batch, "Position: " + (int)player.getPosition().x + ", " + (int)player.getPosition().y, 20, MyGdxGame.scrHeight - 220);
            font.draw(batch, "HP: " + hpBar.health, 20, MyGdxGame.scrHeight - 270);

        }


        if (state == GameState.START) {
            stateChanger.draw(batch);
            debug.draw(batch);
            layout.setText(font, "Tap to start!");
            font.draw(batch, layout, scrWidth / 2 - layout.width / 2, scrHeight / 2);
        } else if (state == GameState.IN_GAME) {
            stateChanger.draw(batch);
        } else { //state == GameState.GAME_OVER
            layout.setText(font, "Tap to restart!");
            font.draw(batch, layout, scrWidth / 2 - layout.width / 2, Gdx.graphics.getHeight() / 2);
        }
        batch.end();
    }
}
