package com.example.hackathon.core.model;

import com.badlogic.gdx.math.Vector2;

import java.util.logging.Logger;

/**
 * Created by tobyp on 12/29/16.
 */
public class Counter extends Entity {
    protected String onZero;
    protected int count;

    public Counter(int init_count, String onZero) {
        super(new Vector2());
        this.count = init_count;
        this.onZero = onZero;
    }

    public void dec(World world) {
        if (this.count > 0) {
            this.count--;
            Logger.getLogger("counter").info("Counter is at " + count);
            if (this.count == 0) {
                Logger.getLogger("counter").info("triggered script " + onZero);
                world.runScript(null, onZero);
            }
        }
    }

    public void inc() {
        this.count++;
    }
}
