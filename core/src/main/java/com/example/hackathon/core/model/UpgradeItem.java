package com.example.hackathon.core.model;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public class UpgradeItem extends DynamicEntity {
    private static final int COLLISION_PRIORITY_ITEM = 1;

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
}
