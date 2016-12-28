package com.example.hackathon.core.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by tobyp on 12/28/16.
 */
public class UpgradeItem extends DynamicEntity {
    public static final int COLLISION_PRIORITY_ITEM = 1;

    private Upgrade upgrade;

    public UpgradeItem(Sprite sprite, Upgrade upgrade) {
        super(sprite);
        this.upgrade = upgrade;
        this.collision_priority = COLLISION_PRIORITY_ITEM;
    }

    @Override
    public void collide(World world, Entity entity) {
        if (entity instanceof Player) {
            ((Player)entity).addUpgrade(this.upgrade);
        }

        world.removeEntity(this);
    }
}
