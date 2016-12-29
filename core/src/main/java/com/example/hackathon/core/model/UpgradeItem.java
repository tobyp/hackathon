package com.example.hackathon.core.model;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class UpgradeItem extends DynamicEntity {
    private static final int COLLISION_PRIORITY_ITEM = 1;
    private static final float ITEM_BOUNCE_AMPLITURE = 2.f;
    private static final float ITEM_BOUNCE_PERIOD = 1.f;

    private final Upgrade upgrade;

    public UpgradeItem(Vector2 location, Sprite sprite, Upgrade upgrade) {
        super(location, new Vector2(1, 1), new Vector2(0.5f, 0.5f), sprite);
        this.upgrade = upgrade;
        this.collision_priority = COLLISION_PRIORITY_ITEM;
    }

    @Override
    public void collide(World world, Entity entity) {
        if (entity instanceof Player) {
            ((Player)entity).addUpgrade(this.upgrade);
        }

        destroy();
    }

    @Override
    public void render(World world, SpriteBatch batch) {
        float bounce = (float)Math.sin(world.getWorldTime() / (2.f * Math.PI * ITEM_BOUNCE_PERIOD));
        getLocation().add(0, bounce);
        super.render(world, batch);
        getLocation().sub(0, bounce);
    }
}
