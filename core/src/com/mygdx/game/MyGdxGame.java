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
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g3d.particles.influencers.ColorInfluencer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.assets.loaders.BulletLoader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class MyGdxGame extends ApplicationAdapter {
    protected static float scrWidth, scrHeight;

    protected enum GameState {START, IN_GAME, GAME_OVER}
    protected static GameState state;


    //stuff you can save here

    protected static Preferences preferences;
    protected static int score, highScore;

    private AssetManager manager; //EXPERIMENTAL SHIT

    private SpriteBatch batch;
    private static Vector3 tap; //holds the position of tap location
    private BitmapFont font;
    private GlyphLayout layout;
    protected static Vector2 gravity;
    private Player player;
    private HP hpBar;
    private List<Bullet> bullets;
    private List<Enemy> enemies;
    private Music music;
    private Music music1;
    private Sound shootSound, matchSound;
    private Sprite background;
    private Sprite bgStart;
    private ArrowControls dpad;
    public static int tapIndex;
    private FreeTypeFontGenerator fontGenerator; //handles .ttf --> .fnt
    private Iterator<Bullet> bulletIterator;
    private Iterator<Enemy> enemyIterator;
    private ArrayList<Blood> blood;


    public static OrthographicCamera camera; //camera is your game world camera
    public static OrthographicCamera uiCamera; //uiCamera is your heads-up display

    private Level currentLevel;

    //buttons stuff
    private DebugButton debug;
    private StateChanger stateChanger;

    private float time = 0;

    @Override
    public void create() {
        scrWidth = Gdx.graphics.getWidth();
        scrHeight = Gdx.graphics.getHeight();
        gravity = new Vector2();
        blood = new ArrayList<Blood>();
        background = new Sprite(new Texture("images/shawdow forrest.jpg"));
        bgStart = new Sprite(new Texture("images/bgstart.jpeg"));

        preferences = new Preferences("Preferences");
        //if theree are no high scores, then make one
        if (preferences.getInteger("highScore", 0) == 0) {
            highScore = 0;
            preferences.putInteger("highScore", highScore);
        } else
            highScore = preferences.getInteger("highScore", 0); //set highScore to saved value

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
        hpBar = new HP(batch);
        tap = new Vector3(); //location of tap
        //FONT STUFF
        fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Chicken Butt.ttf")); //replace font with whatever
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = (int) (90 * (scrWidth / 1196)); //lin did the math and i guess it works
        font = fontGenerator.generateFont(parameter);
        music = Gdx.audio.newMusic(Gdx.files.internal("music/epicAdventure.mp3"));
        music1 = Gdx.audio.newMusic(Gdx.files.internal("music/japanese.mp3"));
        music.setLooping(true);
        music.play();
        matchSound = Gdx.audio.newSound(Gdx.files.internal("sounds/matchStart.wav"));
        shootSound = Gdx.audio.newSound(Gdx.files.internal("sounds/shootStar.mp3"));
        layout = new GlyphLayout();
        player = new Player();
        bullets = Collections.synchronizedList(new ArrayList<Bullet>());
        enemies = Collections.synchronizedList(new ArrayList<Enemy>());
        dpad = new ArrowControls();
        tapIndex = 0;
        bulletIterator = bullets.iterator();
        enemyIterator = enemies.iterator();


        camera = new OrthographicCamera();
        camera.setToOrtho(false, scrWidth, scrHeight);
        uiCamera = new OrthographicCamera();
        uiCamera.setToOrtho(false, scrWidth, scrHeight);
        uiCamera.update();

        debug = new DebugButton(10, 10);
        stateChanger = new StateChanger(scrWidth - 150, 10);


        currentLevel = new Level(1);

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
        blood.clear();
        score=0;
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
        time += deltaTime;
        dpad.update(player);
        player.update();
        for (Enemy enemy : enemies) {
            enemy.update();
        }
        if (state == GameState.START) {
            tapIndex = 0;
            if (debug.isPressed()) debug.action();
            if (stateChanger.isPressed()) {
                matchSound.play();
                stateChanger.action();
                enemies = currentLevel.getEnemies();
            }
        }

        else if (state == GameState.IN_GAME) {
            for (Enemy ninjaEnemy : enemies) {ninjaEnemy.followPlayer(player);}
            if (stateChanger.isPressed()) stateChanger.action();
            // shoot and move on input
            if (Gdx.input.justTouched() && !dpad.isTouched()) {
                tapIndex = 0;
                shootSound.play();
                player.shoot(bullets);
            } else if (Gdx.input.justTouched() && Gdx.input.isTouched(0) && Gdx.input.isTouched(1) && dpad.isTouched()) {
                tapIndex = 1;
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

            // collision
            enemyIterator = enemies.iterator();
            while(enemyIterator.hasNext()) {
                Enemy enemy = enemyIterator.next();
                //player die when collide with enemy
                if (enemy.getBounds().overlaps(player.getBounds())) {
                    hpBar.hit();
                    if (hpBar.health <= 0){
                        score = currentLevel.getLevel();
                        state = GameState.GAME_OVER;
                    }

                }
               //remove bullet and enemy when they collide
                bulletIterator = bullets.iterator();
                while(bulletIterator.hasNext()) {
                    Bullet bullet = bulletIterator.next();
                    if (enemy.getBounds().overlaps(bullet.getBounds())) {
                        blood.add(new Blood(enemy.getPosition().x, enemy.getPosition().y));
                        enemyIterator.remove();
                        bulletIterator.remove();
                        break;
                    }
                }
            }


            //collision
//            for (int j = 0; j < enemies.size(); j++) {
//                //player die when collide with enemy
//                if (enemies.get(j).getBounds().overlaps(player.getBounds())) {
//                    hpBar.hit();
//                    if (hpBar.health <= 0){
//                        score = currentLevel.getLevel() - 1;
//                        state = GameState.GAME_OVER;
//                    }
//
//                }
//                //remove bullet and enemy when they collide
//                for (int i = 0; i < bullets.size(); i++)  {
//                    if (enemies.get(j).getBounds().overlaps(bullets.get(i).getBounds()))  {
//                        enemies.remove(j);
//                        bullets.remove(i);
//
//                    }
//                }
//            }
            if (enemies.size() == 0) {
                currentLevel.setLevel(currentLevel.getLevel()+1);
                enemies = currentLevel.getEnemies();
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
            currentLevel.setLevel(1);
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
        } else if (state == GameState.IN_GAME) {
            for (Blood b : blood) b.draw(batch);
            for (Bullet bullet : bullets) bullet.draw(batch, time);
            player.draw(batch, time);
            for (Enemy enemy : enemies) enemy.draw(batch, time);
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
            batch.draw(bgStart,0,0,scrWidth,scrHeight);
            stateChanger.draw(batch);
            //debug.draw(batch);
            font.setColor(Color.FIREBRICK);
            layout.setText(font, "NINJA SURVIVAL");
            font.getData().setScale(1.46f);
            font.draw(batch, layout, scrWidth / 2 - layout.width / 2, scrHeight - 250);
        } else if (state == GameState.IN_GAME) {
            stateChanger.draw(batch);
            dpad.draw(batch);
            currentLevel.getLevel();
            font.getData().setScale(1f);
            font.setColor(Color.WHITE);
            layout.setText(font, "Level " + currentLevel.getLevel());
            font.draw(batch, layout, 20, MyGdxGame.scrHeight - 20);
        } else if (state == GameState.GAME_OVER) {
            font.getData().setScale(1.7f);
            font.setColor(Color.FIREBRICK);
            layout.setText(font, "Tap to restart!");
            font.draw(batch, layout, scrWidth / 2 - layout.width / 2, scrHeight / 2 + 150);
            font.getData().setScale(1f);
            font.setColor(Color.WHITE);
            layout.setText(font, "Levels Survived: " + score);
            font.draw(batch, layout, scrWidth / 2 - layout.width / 2, scrHeight / 2);
            layout.setText(font, "High score: " + highScore);
            font.draw(batch, layout, scrWidth / 2 - layout.width / 2, scrHeight / 2 - 100);
        }
        batch.end();
        if (state == GameState.IN_GAME) {
            hpBar.draw();
        }
    }
}
