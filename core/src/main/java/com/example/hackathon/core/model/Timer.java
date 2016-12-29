package com.example.hackathon.core.model;

import com.badlogic.gdx.math.Vector2;

import java.util.logging.Logger;

/**
 * Created by tobyp on 12/29/16.
 */
public class Timer extends Entity {
    protected String onZero;

    public float getTime() {
        return time;
    }

    public void setTime(float time) {
        this.time = time;
    }

    protected float time;

    public Timer(float time_start, String onZero) {
        super(new Vector2());
        this.time = time_start;
        this.onZero = onZero;
    }

    @Override
    public void update(World world, float deltaTime) {
        if (this.time > 0.f) {
            this.time = Math.max(0, time - deltaTime);
            if (this.time <= 0.f) {
                Logger.getLogger("timer").info("Triggered timer script " + onZero);
                world.runScript(null, onZero);
                this.time = -1.f;
            }
        }
    }
}
