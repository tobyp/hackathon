package com.example.hackathon.core.model;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by tobyp on 12/28/16.
 */
public class Entity {
    protected int collision_priority = 0;
    protected Vector2 location = new Vector2();
    protected Vector2 size = new Vector2(1, 1);
    protected float rotation = 0.f;

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

    public void update(float deltaTime) {

    }

    public void collide(World world, Entity entity) {

    }
}
