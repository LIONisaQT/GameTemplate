package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;

public class MyGdxGame extends ApplicationAdapter {
    protected static final int NUM_LEVELS = 10;
    protected static float scrWidth, scrHeight;

    protected enum GameState {START, LEVEL_SELECT, IN_GAME, GAME_OVER}
    protected static GameState state;

    //stuff you can save here
    protected static Preferences preferences;
    protected static int score, highScore;

    private SpriteBatch batch;
    private static Vector3 tap; //holds the position of tap location
    private BitmapFont font;
    private GlyphLayout layout;
    protected static Vector2 gravity;
    private Player player;
    private ArrayList<Bullet> bullets;
    private ArrayList<Enemy> enemies;
    private ArrayList<Blood> blood;
    private Music music;
    private Sound shootSound, matchSound;

    public static OrthographicCamera camera; //camera is your game world camera
    public static OrthographicCamera uiCamera; //uiCamera is your heads-up display

    private Level currentLevel;
    private ArrayList<Level> levels;

    //buttons stuff
    private DebugButton debug;
    private StateChanger stateChanger;
    private ArrayList<LevelButton> levelButtons;

    @Override
    public void create() {
        scrWidth = Gdx.graphics.getWidth();
        scrHeight = Gdx.graphics.getHeight();
        gravity = new Vector2();

        preferences = new Preferences("Preferences");
        //if there are no high scores, then make one
        if (preferences.getInteger("highScore", 0) == 0) {
            highScore = 0;
            preferences.putInteger("highScore", highScore);
        }
        else highScore = preferences.getInteger("highScore", 0); //set highScore to saved value

        batch = new SpriteBatch();
        tap = new Vector3(); //location of tap
        font = new BitmapFont(Gdx.files.internal("fonts/arial.fnt"),
                Gdx.files.internal("fonts/arial.png"), false);
        music = Gdx.audio.newMusic(Gdx.files.internal("music/bgm1.mp3"));
        music.setLooping(true);
        music.play();
        matchSound = Gdx.audio.newSound(Gdx.files.internal("sounds/matchStart.wav"));
        shootSound = Gdx.audio.newSound(Gdx.files.internal("sounds/shootSound.wav"));
        layout = new GlyphLayout();
        player = new Player();
        bullets = new ArrayList<Bullet>();
        enemies = new ArrayList<Enemy>();
        blood = new ArrayList<Blood>();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, scrWidth, scrHeight);
        uiCamera = new OrthographicCamera();
        uiCamera.setToOrtho(false, scrWidth, scrHeight);
        uiCamera.update();

        debug = new DebugButton(10, 10);
        stateChanger = new StateChanger(scrWidth / 2 + 10, 10);

        levelButtons = new ArrayList<LevelButton>();
        levels = new ArrayList<Level>();
        for (int i = 0; i < NUM_LEVELS; i++) {
            levelButtons.add(new LevelButton(i * scrWidth / NUM_LEVELS, scrHeight / 2));
            levels.add(new Level(i));
        }
        currentLevel = new Level(0);
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
        blood.clear();
        score = 0;
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
        player.update();

        if (state == GameState.START) {
            if (debug.isPressed()) debug.action();
            if (stateChanger.isPressed()) {
                matchSound.play();
                stateChanger.action();
            }
        }

        else if (state == GameState.LEVEL_SELECT) {
            for (int i = 0; i < NUM_LEVELS; i++) {
                if (levelButtons.get(i).isPressed()) {
                    currentLevel = levels.get(i + 1); //current level is whatever you tapped
                    enemies = currentLevel.getEnemies();
                    levelButtons.get(i).pressedAction();
                }
            }
        }

        else if (state == GameState.IN_GAME) {
            for (Enemy enemy : enemies) {
                enemy.update();
                enemy.followPlayer(player);
            }
            if (stateChanger.isPressed()) stateChanger.action();
            if (Gdx.input.justTouched()) {
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

            //enemy collision stuff
            for (int j = 0; j < enemies.size(); j++) {
                //player die
                if (enemies.get(j).getBounds().overlaps(player.getBounds())) {
                    state = GameState.GAME_OVER;
                }
                //remove bullet and enemy when they collide
                for (int i = 0; i < bullets.size(); i++)  {
                    if (enemies.get(j).getBounds().overlaps(bullets.get(i).getBounds()))  {
                        blood.add(new Blood(enemies.get(j).getPosition().x, enemies.get(j).getPosition().y));
                        enemies.remove(j);
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
        font.setColor(Color.WHITE);
        if (state == GameState.START) {
            //start shit here
        } else if (state == GameState.LEVEL_SELECT) {
            for (LevelButton lvlBtn : levelButtons) {lvlBtn.draw(batch);}
        } else if (state == GameState.IN_GAME) {
            for (Bullet bullet : bullets) bullet.draw(batch);
            player.draw(batch);
            for (Enemy enemy : enemies) enemy.draw(batch);
            for (Blood b : blood) b.draw(batch);
        } else {
            //gameover shit here
        }
        batch.end();

        //game ui camera
        batch.setProjectionMatrix(uiCamera.combined);
        batch.begin();

        if (debug.debug) {
            font.draw(batch, "Game state: " + MyGdxGame.state, 20, scrHeight - 20);
            font.draw(batch, "Bullet count: " + bullets.size(), 20, scrHeight - 70);
            font.draw(batch, "Number of enemies: " + enemies.size(), 20, scrHeight - 120);
            font.draw(batch, "Velocity: " + (int)player.getVelocity().x + ", " + (int)player.getVelocity().y, 20, scrHeight - 170);
            font.draw(batch, "Position: " + (int)player.getPosition().x + ", " + (int)player.getPosition().y, 20, scrHeight - 220);
        }

        if (state == GameState.START) {
            stateChanger.draw(batch);
            debug.draw(batch);
            layout.setText(font, "Tap to start!");
            font.draw(batch, layout, scrWidth / 2 - layout.width / 2, scrHeight / 2);
        } else if (state == GameState.IN_GAME) {
            stateChanger.draw(batch);
            layout.setText(font, "Score: " + score);
            font.draw(batch, layout, scrWidth - layout.width - 20, scrHeight - 20);
            layout.setText(font, "High score: " + highScore);
            font.draw(batch, layout, scrWidth - layout.width - 20, scrHeight - 70);
        } else { //state == GameState.GAME_OVER
            layout.setText(font, "Tap to restart!");
            font.draw(batch, layout, scrWidth / 2 - layout.width / 2, scrHeight / 2);
            layout.setText(font, "Your score: " + score);
            font.draw(batch, layout, scrWidth / 2 - layout.width / 2, scrHeight / 2 - 50);
            layout.setText(font, "High score: " + highScore);
            font.draw(batch, layout, scrWidth / 2 - layout.width / 2, scrHeight / 2 - 100);
        }
        batch.end();
    }
}
