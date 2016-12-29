package com.example.hackathon.core.model;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by tobyp on 12/28/16.
 */
public abstract class Entity {
    protected int collision_priority = 0;
    protected Vector2 location = new Vector2();
    protected Vector2 size = new Vector2(1, 1);
    protected float rotation = 0.f;
    private boolean destroyed = false;

    public boolean isDestroyed() {
        return destroyed;
    }

    public Vector2 getLocation() {
        return location;
    }

    public void setLocation(Vector2 location) {
        this.location = location;
    }

    public Vector2 getSize() {
        return size;
    }

    public void setSize(Vector2 size) {
        this.size = size;
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public void update(World world, float deltaTime) { }
    public abstract void collide(World world, Entity entity);
    public void render(World world, SpriteBatch batch) { }

    public void destroy() {
        this.destroyed = true;
    }
}
