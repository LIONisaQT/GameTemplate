package com.mygdx.game.assets.loaders;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;

import com.mygdx.game.Bullet;

/**
 * Created by Ryan on 7/6/2016.
 */
public class BulletLoader extends AsynchronousAssetLoader<Bullet, BulletLoader.BulletParameter>{
    public BulletLoader(FileHandleResolver resolver) {
        super(resolver);
    }

    Bullet bullet;

    @Override
    public void loadAsync(AssetManager manager, String filename, FileHandle file, BulletParameter parameter) {
        this.bullet = null;
        this.bullet = new Bullet();
    }

    @Override
    public Bullet loadSync(AssetManager manager, String fileName, FileHandle file, BulletParameter parameter) {
        Bullet bullet = this.bullet;
        this.bullet = null;

        return bullet;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, BulletParameter parameter) {
        return null;
    }

    public static class BulletParameter extends AssetLoaderParameters<Bullet> {

    }
}
