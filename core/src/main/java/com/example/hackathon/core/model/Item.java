package com.example.hackathon.core.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by tobyp on 12/28/16.
 */
public class Item extends Entity {
    private final float BOUNCE_AMPLITUDE = 2.0f;
    private float bounce_time = 0.f;

    private Sprite sprite;

    public Item(Texture texture, int srcX, int srcY, int srcW, int srcH) {
        sprite = new Sprite(texture, srcX, srcY, srcW, srcH);
    }

    @Override
    public void update(float deltaTime) {
        bounce_time += deltaTime;
        super.update(deltaTime);
    }

    public void render(SpriteBatch batch) {
        float bounce_offset = BOUNCE_AMPLITUDE * (float)Math.sin(bounce_time);
        sprite.translateY(bounce_offset);
        sprite.draw(batch);
        sprite.translateY(-bounce_offset);
    }
}
