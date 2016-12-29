package com.example.hackathon.core.model;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public abstract class Entity {
    int collision_priority = 0;
    protected final Vector2 location = new Vector2();
    protected final Vector2 size = new Vector2(1, 1);
    protected final Vector2 collisionSize = new Vector2(1, 1);
    private float rotation = 0.f;
    protected Sprite sprite = null;
    protected float animation_period = 0.f;
    private boolean destroyed = false;

    public Entity(Vector2 location) {
        this.location.set(location);
    }

    public Entity(Vector2 location, Vector2 size) {
        this.location.set(location);
        this.size.set(size);
        this.collisionSize.set(size);
    }

    Entity(Vector2 location, Vector2 size, Vector2 collisionSize) {
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
    public void render(World world, SpriteBatch batch) {
        if (sprite != null) {
            if (animation_period > 0.f) {
                float animation_progress = world.getWorldTime() % animation_period / animation_period;
                int animation_frame = (int)(animation_progress * sprite.getTexture().getHeight() / sprite.getRegionHeight());
                int y_offset = animation_frame * sprite.getRegionHeight();
                sprite.setRegionY(y_offset);
            }
            Vector2 p = location.cpy().mulAdd(size, -0.5f);
            sprite.setPosition(p.x, p.y);
            sprite.setSize(size.x, size.y);
            sprite.draw(batch);
        }
    }

    void destroy() {
        this.destroyed = true;
    }
}
