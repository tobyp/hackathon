package com.example.hackathon.core.model;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by tobyp on 12/29/16.
 */
public class Trigger extends Entity {
    protected String onEnter, onExit;

    public Trigger(Vector2 location, Vector2 size, String onEnter, String onExit) {
        super(location, size);
        this.onEnter = onEnter;
        this.onExit = onExit;
    }

    @Override
    public void collide(World world, Entity entity) {
        world.runScript(null, onEnter);
    }

    @Override
    public void uncollide(World world, Entity entity) {
        world.runScript(null, onExit);
    }
}
