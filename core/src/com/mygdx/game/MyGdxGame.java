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
    protected static final int NUM_LEVELS = 2;
    protected static float scrWidth, scrHeight;

    protected enum GameState {START, LEVEL_SELECT, IN_GAME, GAME_OVER}


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
//    private ArrayList<Blood> blood;
    private Animation zombies;
    private Music music;
    protected static int score, easyHighScore, hardHighScore;
    private Sound shootSound, matchSound;
    public static OrthographicCamera camera; //camera is your game world camera
    public static OrthographicCamera uiCamera; //uiCamera is your heads-up display
    private Level currentLevel;
    private ArrayList<Level> levels;
    private int zombiesLives;
    private DebugButton debug;
    private StateChanger stateChanger;
    private ArrayList<LevelButton> levelButtons;
    private Background BGanimation;
    private Joystick joystick;
    private static int tapIndex;

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
        joystick = new Joystick();
        tapIndex = 0;

        preferences = new Preferences("Preferences");
        //if there are no high scores then make one
        if (preferences.getInteger("easyHighScore", 0) == 0) {
            easyHighScore = 0;
            preferences.putInteger("easyHighScore", easyHighScore);
        }else {
         easyHighScore = preferences.getInteger("easyHighScore", 0);
        //set highscore to set value
        }
        if (preferences.getInteger("hardHighScore", 0) == 0) {
            hardHighScore = 0;
            preferences.putInteger("hardHighScore", hardHighScore);
        }else {
            hardHighScore = preferences.getInteger("hardHighScore", 0);
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
//        blood = new ArrayList<Blood>();
        zombiesLives = 3;
        score = 0;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, scrWidth, scrHeight);
        uiCamera = new OrthographicCamera();
        uiCamera.setToOrtho(false, scrWidth, scrHeight);
        uiCamera.update();

        debug = new DebugButton(10, 10);
//        debug = new DebugButton(scrWidth / 2 - 50, scrHeight / 2 - 50);
        stateChanger = new StateChanger(scrWidth / 2 - 50, scrHeight / 2 - 50);

        levelButtons = new ArrayList<LevelButton>();
        levels = new ArrayList<Level>();
        for (int i = 0; i < NUM_LEVELS; i++) {
            levelButtons.add(new LevelButton(i * scrWidth / NUM_LEVELS + 170, scrHeight / 2));
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
        gravity.set(0, -25);
        player.reset();
        bullets.clear();
//        blood.clear();
        enemies.clear();
    }

    /*
      - gets and translates coordinates of tap to game world coordinates
      - you don't need to touch this at all
    */
    public static Vector3 getTapPosition() { //gets and translates coordinates of tap to game world coordinates
        tap.set(Gdx.input.getX(tapIndex), Gdx.input.getY(tapIndex), 0);
        return camera.unproject(tap);
    }

    private void updateGame() {
        float deltaTime = Gdx.graphics.getDeltaTime();
        for (Enemy enemy : enemies) {
            enemy.enemiesStateTime += deltaTime;
        }

        BGanimation.bgStateTime += deltaTime;;

        player.update();
        joystick.update(player);
        for (Enemy enemy : enemies) {
            enemy.update();
        }

        if (state == GameState.START) {
            score = 0;
            tapIndex = 0;
            if (debug.isPressed()) debug.action();
            if (stateChanger.isPressed()) {
                stateChanger.action();
                matchSound.play();
                for (int i = 0; i < Enemy.NUM_ENEMIES; i++)
                    enemies.add(new Enemy((float) Math.random() * scrWidth, (float) Math.random() * scrHeight));

                }
            } else if (state == GameState.LEVEL_SELECT) {
            for (int i = 0; i < NUM_LEVELS; i++) {
                if (levelButtons.get(i).isPressed()) {
                    currentLevel = levels.get(i); //current level is whatever you tapped
                    enemies = currentLevel.getEnemies();
                    levelButtons.get(i).pressedAction();
                }
            }
        } else if (state == GameState.IN_GAME) {
                for (Enemy enemy : enemies) {
                    enemy.followPlayer(player);
                }
                if (currentLevel.getLevel() == 0) {
                    if (score > easyHighScore) {
                        easyHighScore = score;
                        preferences.putInteger("easyHighScore", score);
                    }
                    preferences.flush();
                }
                if (currentLevel.getLevel() == 1) {
                    if (score > hardHighScore) {
                        hardHighScore = score;
                        preferences.putInteger("hardHighScore", score);
                    }
                    preferences.flush();
                }
//                if (stateChanger.isPressed()) stateChanger.action();
                // check for tap index and shoot bullets
//                if (Gdx.input.justTouched()) {
//                    shootSound.play();
//                    player.shoot(bullets);
//                    tapIndex = 0;
//                }
                if (joystick.touchpad.isTouched()) {
                    tapIndex = 0;
                    player.update();
                }
                if (Gdx.input.justTouched() && !joystick.touchpad.isTouched()) {
                    tapIndex = 0;
                    shootSound.play();
                    player.shoot(bullets);
                    player.update();
                }
//                if (joystick.touchpad.isTouched() && !Gdx.input.justTouched()) {
//                    player.update();
//                }
//                if (joystick.touchpad.isTouched() && Gdx.input.justTouched()) {
//                    tapIndex = 0;
//                    player.update();
//                    player.shoot(bullets);
//                }
//                if (Gdx.input.justTouched() && joystick.touchpad.isTouched()) {
//                    player.update();
//                }
                else if (Gdx.input.justTouched() && Gdx.input.isTouched(0) && joystick.touchpad.isTouched() && Gdx.input.isTouched(1)) {
                    tapIndex = 1;
                    player.update();
                    shootSound.play();
                    player.shoot(bullets);
                }
//                if (joystick.touchpad.isTouched() && !Gdx.input.justTouched()) {
//                    tapIndex = 0;
//                    player.update();
//                }
//                if (Gdx.input.justTouched()) {
//                    long id = shootSound.play();
//                    shootSound.setVolume(id, 1.0f);
//                    player.shoot(bullets);
//                }

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
                    if (enemies.get(j).getBounds().overlaps(player.getBounds())) {
                        state = GameState.GAME_OVER;
                    }
                    //remove bullet and enemy when they collide
                    for (int i = 0; i < bullets.size(); i++) {
                        if (enemies.get(j).getBounds().overlaps(bullets.get(i).getBounds())) {
//                            enemies.remove(j);
                            bullets.remove(i);
                            zombiesLives = zombiesLives - 1;
                            if (zombiesLives == 0) {
                                enemies.remove(j);
//                                blood.add(new Blood(enemies.get(j).getPosition().x, enemies.get(j).getPosition().y));
                                score++;
                                zombiesLives = 3;
                                Enemy zombies;
                                zombies = new Enemy((float) Math.random() * scrWidth, (float) Math.random() * scrHeight);
                                enemies.add(zombies);
                                if (currentLevel.getLevel() == 1) {
                                    zombies.setSpeed(400);
                                    }
                                }
                            }
                        }
                    }


            } else { //state is GAME_OVER
                if (currentLevel.getLevel() == 0) {
                    if (score > easyHighScore) {
                        easyHighScore = score;
                        preferences.putInteger("easyHighScore", score);
                    }
                    preferences.flush(); //saves
                }
                if (currentLevel.getLevel() == 1) {
                    if (score > hardHighScore) {
                        hardHighScore = score;
                        preferences.putInteger("hardHighScore", score);
                    }
                    preferences.flush(); //saves
                }

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
        } else if (state == GameState.LEVEL_SELECT) {
            for (LevelButton lvlBtn : levelButtons) {lvlBtn.draw(batch);}
        } else if (state == GameState.IN_GAME) {
            for (Bullet bullet : bullets) bullet.draw(batch);
            player.draw(batch);
//            joystick.draw(batch);
            for (Enemy enemy : enemies) enemy.draw(batch);
//            for (Blood b : blood) b.draw(batch);
        } else {
            //gameover shit here
        }
        batch.end();

        // draw virtual joystick in game only
        if (state == GameState.IN_GAME) {
            joystick.draw();
        }

        //game ui camera
        batch.setProjectionMatrix(uiCamera.combined);
        batch.begin();

//        if (debug.debug) {
//            font.draw(batch, "Game state: " + MyGdxGame.state, 20, MyGdxGame.scrHeight - 20);
//            font.draw(batch, "Bullet count: " + bullets.size(), 20, MyGdxGame.scrHeight - 70);
//            font.draw(batch, "Number of enemies: " + enemies.size(), 20, MyGdxGame.scrHeight - 120);
//            font.draw(batch, "Tap Index: " + tapIndex, 20, MyGdxGame.scrHeight - 170);
//        }

        if (state == GameState.START) {
            stateChanger.draw(batch);
            //debug.draw(batch);
            layout.setText(font, "Press The Zombie If Ready");
            font.draw(batch, layout, scrWidth / 2 - layout.width / 2, scrHeight - 100);
        } else if (state == GameState.IN_GAME) {
            if (currentLevel.getLevel() == 0) {
                layout.setText(font, "High Score: " + easyHighScore);
                font.draw(batch, layout, scrWidth - layout.width - 150, scrHeight - 10);
                layout.setText(font, "Score: " + score);
                font.draw(batch, layout, scrWidth / 2 - layout.width - 60, scrHeight - 10);
            }
            if (currentLevel.getLevel() == 1) {
                layout.setText(font, "High Score: " + hardHighScore);
                font.draw(batch, layout, scrWidth - layout.width - 150, scrHeight - 10);
                layout.setText(font, "Score: " + score);
                font.draw(batch, layout, scrWidth / 2 - layout.width - 60, scrHeight - 10);
            }
        } else if (state == GameState.LEVEL_SELECT) {
            layout.setText(font, "Choose a Level to Start!");
            font.draw(batch, layout, scrWidth - layout.width - 230, scrHeight - 10);
            layout.setText(font, "Easy Level");
            font.draw(batch, layout,  100, scrHeight - 300);
            layout.setText(font, "Hard Level");
            font.draw(batch, layout, scrWidth - 370, scrHeight - 300);
        } else { //state == GameState.GAME_OVER
            layout.setText(font, "Tap to restart!");
            font.draw(batch, layout, scrWidth / 2 - layout.width / 2, Gdx.graphics.getHeight() / 2);
            if (currentLevel.getLevel() == 0) {
                layout.setText(font, "High Score: " + easyHighScore);
                font.draw(batch, layout, scrWidth / 2 - layout.width / 2, scrHeight - 150);
                layout.setText(font, "Score: " + score);
                font.draw(batch, layout, scrWidth / 2 - layout.width / 2, scrHeight - 215);
            }
            if (currentLevel.getLevel() == 1) {
                layout.setText(font, "High Score: " + hardHighScore);
                font.draw(batch, layout, scrWidth / 2 - layout.width / 2, scrHeight - 150);
                layout.setText(font, "Score: " + score);
                font.draw(batch, layout, scrWidth / 2 - layout.width / 2, scrHeight - 215);
            }
        }
        batch.end();
    }
}
