package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad.TouchpadStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

/**
 * Created by Kat on 7/10/16.
 */
public class Joystick {
    public Touchpad touchpad;
    private TouchpadStyle touchpadStyle;
    private Skin touchpadSkin;
    private Drawable touchBackground;
    private Drawable touchKnob;

    private Stage stage;
    private float spriteSpeed;

    public Joystick() {
        //Create a touchpad skin
        touchpadSkin = new Skin();
        //Set background image
        touchpadSkin.add("touchBackground", new Texture("images/touchBackground.png"));
        //Set knob image
        touchpadSkin.add("touchKnob", new Texture("images/touchKnob.png"));

        //Create TouchPad Style
        touchpadStyle = new TouchpadStyle();
        //Create Drawable's from TouchPad skin
        touchBackground = touchpadSkin.getDrawable("touchBackground");
        touchKnob = touchpadSkin.getDrawable("touchKnob");
        //Apply the Drawables to the TouchPad Style
        touchpadStyle.background = touchBackground;
        touchpadStyle.knob = touchKnob;

        //Create new TouchPad with the created style
        touchpad = new Touchpad(10, touchpadStyle);
        //setBounds(x,y,width,height)
        touchpad.setBounds(50, 50, 150, 150);

        // Create stage and add touchpad
        stage = new Stage();
        stage.addActor(touchpad);
        Gdx.input.setInputProcessor(stage);

        // Set speed of sprite movement
        spriteSpeed = 10;
    }

    // update sprite position based on joystick movement
    public void update(Player player) {
        player.setPosition(player.getPosition().x + touchpad.getKnobPercentX()*spriteSpeed, player.getPosition().y + touchpad.getKnobPercentY()*spriteSpeed);
    }

    public void draw() {
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }
}
