package com.example.hackathon.core.model;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by tobyp on 12/28/16.
 */
public abstract class Entity {
    protected int collision_priority = 0;
    protected final Vector2 location = new Vector2();
    protected final Vector2 size = new Vector2(1, 1);
    protected final Vector2 collisionSize = new Vector2(1, 1);
    protected float rotation = 0.f;
    private boolean destroyed = false;

    public Entity(Vector2 location) {
        this.location.set(location);
    }

    public Entity(Vector2 location, Vector2 size) {
        this.location.set(location);
        this.size.set(size);
        this.collisionSize.set(size);
    }

    public Entity(Vector2 location, Vector2 size, Vector2 collisionSize) {
        this.location.set(location);
        this.size.set(size);
        this.collisionSize.set(collisionSize);
    }

    public Vector2 getCollisionSize() {
        return collisionSize;
    }

    public Rectangle getCollisionRect() {
        return new Rectangle(location.x - collisionSize.x / 2, location.y - collisionSize.y / 2, collisionSize.x, collisionSize.y);
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public Vector2 getLocation() {
        return location;
    }

    public Vector2 getSize() {
        return size;
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public void update(World world, float deltaTime) { }
    public void collide(World world, Entity entity) { }
    public void uncollide(World world, Entity entity) { }
    public void render(World world, SpriteBatch batch) { }

    public void destroy() {
        this.destroyed = true;
    }
}
