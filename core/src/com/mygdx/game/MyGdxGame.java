package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;


public class MyGdxGame extends ApplicationAdapter {
    protected static float scrWidth;
    protected static float scrHeight;

    protected enum GameState {START, IN_GAME, GAME_OVER}

    protected static GameState state;
    protected static Vector2 gravity;
    protected static Preferences preferences;

    private AssetManager manager; //EXPERIMENTAL SHIT

    Music sound1, sound2;
    boolean playSong;
    public static SpriteBatch batch;
    private static Vector3 tap; //holds the position of tap location
    private BitmapFont font;
    private GlyphLayout layout;
    private Player player;
    private ArrayList<Bullet> bullets;
    private ArrayList<Enemy> enemies;
    private Animation zombies;
    private Music music;
    protected static int score , highScore;
    private Sound shootSound, matchSound;
    public static OrthographicCamera camera; //camera is your game world camera
    public static OrthographicCamera uiCamera; //uiCamera is your heads-up display
    private int zombiesLives;
    private DebugButton debug;
    private StateChanger stateChanger;
    private Background BGanimation;

    @Override
    public void create() {

        BGanimation = new Background();

        sound1 = Gdx.audio.newMusic(Gdx.files.internal("music/1.mp3"));
        sound2 = Gdx.audio.newMusic(Gdx.files.internal("music/2.mp3"));
        sound1.play();
        sound2.play();
        playSong = true;
        sound1.setLooping(true);
        sound2.setLooping(true);
        scrWidth = Gdx.graphics.getWidth();
        scrHeight = Gdx.graphics.getHeight();
        gravity = new Vector2();

        preferences = new Preferences("Preferences");
        //if there are no high scores then make one
        if (preferences.getInteger("highScore", 0) == 0) {
            highScore = 0;
            preferences.putInteger("highScore", highScore);
        }else {
         highScore = preferences.getInteger("highScore", 0);
        //set highscore to set value
        }

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
        font = new BitmapFont(Gdx.files.internal("fonts/arial.fnt"), Gdx.files.internal("fonts/arial.png"), false);
//        music = Gdx.audio.newMusic(Gdx.files.internal("music/bgm1.mp3"));
//        music.setLooping(true);
//        music.play();
        matchSound = Gdx.audio.newSound(Gdx.files.internal("sounds/matchStart.wav"));
        shootSound = Gdx.audio.newSound(Gdx.files.internal("sounds/louderArrowSound.mp3"));
        layout = new GlyphLayout();
        player = new Player();
        bullets = new ArrayList<Bullet>();
        enemies = new ArrayList<Enemy>();
        zombiesLives = 3;
        score = 0;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, scrWidth, scrHeight);
        uiCamera = new OrthographicCamera();
        uiCamera.setToOrtho(false, scrWidth, scrHeight);
        uiCamera.update();

        debug = new DebugButton(10, 10);
        stateChanger = new StateChanger(scrWidth / 2 + 10, 10);

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
        gravity.set(0, -25);
        player.reset();
        bullets.clear();
        enemies.clear();
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
        for (Enemy enemy : enemies) {
            enemy.enemiesStateTime += deltaTime;
            ;
        }

        BGanimation.bgStateTime += deltaTime;;

        player.update();
        for (Enemy enemy : enemies) {
            enemy.update();
        }

        if (state == GameState.START) {
            if (debug.isPressed()) debug.action();
            if (stateChanger.isPressed()) {
                matchSound.play();
                for (int i = 0; i < Enemy.NUM_ENEMIES; i++)
                    enemies.add(new Enemy((float) Math.random() * scrWidth, (float) Math.random() * scrHeight));
                    stateChanger.action();
                }
            } else if (state == GameState.IN_GAME) {
                for (Enemy enemy : enemies) {
                    enemy.followPlayer(player);
                }
                if (stateChanger.isPressed()) stateChanger.action();
                if (Gdx.input.justTouched()) {
                /*
                =====EXPERIMENTAL SHIT=====
                Bullet bullet = manager.get("Bullet.java");
                bullets.add(bullet);
                =====EXPERIMENTAL SHIT=====
                */
                    long id = shootSound.play();
                    shootSound.setVolume(id, 1.0f);
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

                //remove bullet and enemy when they collide
                for (int j = 0; j < enemies.size(); j++) {
                    //player die
//                if (enemies.get(j).getBounds().overlaps(player.getBounds())) {
//                    state = GameState.GAME_OVER;
//                }
                    //remove bullet and enemy when they collide
                    for (int i = 0; i < bullets.size(); i++) {
                        if (enemies.get(j).getBounds().overlaps(bullets.get(i).getBounds())) {
//                            enemies.remove(j);
                            bullets.remove(i);
                            zombiesLives = zombiesLives - 1;
                            if (zombiesLives == 0) {
                                enemies.remove(j);
                                score++;
                                zombiesLives = 3;
                                enemies.add(new Enemy((float) Math.random() * scrWidth, (float) Math.random() * scrHeight));
                                }
                            }
                        }
                    }
            } else { //state is GAME_OVER
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
        BGanimation.draw(batch);
        font.setColor(Color.WHITE);
        if (state == GameState.START) {
            //start shit here
        } else if (state == GameState.IN_GAME) {
            for (Bullet bullet : bullets) bullet.draw(batch);
            player.draw(batch);
//            joystick.draw(batch);
            for (Enemy enemy : enemies) enemy.draw(batch);
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
        }

        if (state == GameState.START) {
            stateChanger.draw(batch);
            debug.draw(batch);
            layout.setText(font, "Tap to start!");
            font.draw(batch, layout, scrWidth / 2 - layout.width / 2, scrHeight / 2);
        } else if (state == GameState.IN_GAME) {
            stateChanger.draw(batch);
            layout.setText(font, "High score: " + highScore);
            font.draw(batch, layout, scrWidth - layout.width - 20, scrHeight - 70);
            layout.setText(font, "Score: " + score);
            font.draw(batch, layout, scrWidth / 2 - layout.width / 2, scrHeight - 10);
        } else { //state == GameState.GAME_OVER
            layout.setText(font, "Tap to restart!");
            font.draw(batch, layout, scrWidth / 2 - layout.width / 2, Gdx.graphics.getHeight() / 2);
            layout.setText(font, "High score: " + highScore);
            font.draw(batch, layout, scrWidth - layout.width - 20, scrHeight - 70);
        }
        batch.end();
    }
}
