package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Created by Ryan on 7/9/2016.
 */
public class Preferences {
    private static final String NEWLINE = "\n";
    private static final String DELIM = " : ";

    private FileHandle file;
    private Map<String, Integer> data;

    public Preferences(String name) {
        this.file = Gdx.files.local(name);
        this.data = new HashMap<String, Integer>();
        if (this.file.exists())
            load();
    }

    public void clear() {
        data.clear();
        flush();
    }

    public int getInteger(String key, int def) {
        if (data.containsKey(key))
            return data.get(key);
        else
            return def;
    }

    public void putInteger(String key, int value) {
        data.put(key, value);
    }

    public void flush() {
        String contents = "";

        for (String key : data.keySet()) {
            contents += key + DELIM + data.get(key) + NEWLINE;
        }

        file.writeString(contents, false);
    }

    private void load() {
        String contents = file.readString();
        StringTokenizer st = new StringTokenizer(contents, NEWLINE);

        while(st.hasMoreTokens()) {
            String[] entry = st.nextToken().split(DELIM);
            if (entry.length >= 2) {
                data.put(entry[0], Integer.parseInt(entry[1]));
            }
        }
    }
}

