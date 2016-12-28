package com.example.hackathon.core.model;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by tobyp on 12/28/16.
 */
public class Entity {
    protected Vector2 location = new Vector2();
    protected Vector2 size = new Vector2(1, 1);
    protected Vector2 velocity = new Vector2();
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

    public Vector2 getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector2 velocity) {
        this.velocity = velocity;
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public void update(float deltaTime) {

    }
}
