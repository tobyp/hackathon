package com.example.hackathon.core.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by tobyp on 12/29/16.
 */
public class Barrier extends Entity {
    private Sprite sprite_on;

    private boolean active = false;
    private Player victim = null;
    private static final float BARRIER_DAMAGE = 0.1f;
    private float toggle_period;
    private float toggle_timer;

    public Barrier(Vector2 location, Vector2 size, boolean active, float toggle_period) {
        super(location, size, size);
        this.active = active;
        this.toggle_period = toggle_period;
        this.toggle_timer = active ? toggle_period : 0.f;
        if (size.x > size.y) {
            this.sprite_on = new Sprite(new Texture("barrier_h.png"), 96, 32);
        }
        else {
            this.sprite_on = new Sprite(new Texture("barrier_v.png"), 32, 96);
        }
        this.sprite = active ? sprite_on : null;
    }

    @Override
    public void collide(World world, Entity entity) {
        if (!active) return;
        if (entity instanceof Player) {
            victim = (Player)entity;
        }
    }

    @Override
    public void uncollide(World world, Entity entity) {
        if (victim == entity) victim = null;
    }

    @Override
    public void update(World world, float deltaTime) {
        if (active && victim != null) {
            victim.setBattery(victim.getBattery() - BARRIER_DAMAGE * deltaTime);
        }
        if (toggle_period > 0.f) {
            toggle_timer = (toggle_timer + deltaTime) % (toggle_period * 2);
            setActive(toggle_timer > toggle_period);
        }
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
        this.sprite = this.active ? sprite_on : null;
    }
}
