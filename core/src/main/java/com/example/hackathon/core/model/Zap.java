package com.example.hackathon.core.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by tobyp on 12/29/16.
 */
public class Zap extends Entity {
    private Entity source;
    private Entity target;
    private float timer;

    public Zap(Entity source, Entity target) {
        super(source.getLocation().cpy().add(target.getLocation()).scl(0.5f), new Vector2(source.getLocation().dst(target.getLocation()), source.getLocation().dst(target.getLocation()) / 3.f));

        this.source = source;
        this.target = target;
        this.timer = 2.f;
        this.animation_period = 0.32f;
        sprite = new Sprite(new Texture("barrier_h.png"), 0, 0, 96, 32);

    }

    @Override
    public void update(World world, float deltaTime) {
        this.timer = Math.max(0.f, timer - deltaTime);
        if (this.timer > 0.f) {
            location.set(source.getLocation().cpy().add(target.getLocation()).scl(0.5f));
            float distance = source.getLocation().dst(target.getLocation());
            size.set(distance, 2.f);
            sprite.setOrigin(size.x / 2, size.y / 2);
            Vector2 relative = target.getLocation().cpy().sub(source.getLocation());
            sprite.setRotation(relative.angle());
            getCollisionSize().set(size);
        }
        else {
            destroy();
        }
    }

    @Override
    public void render(World world, SpriteBatch batch) {
        if (timer > 0.f) {
            super.render(world, batch);
        }
    }
}
