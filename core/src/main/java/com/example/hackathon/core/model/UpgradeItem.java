package com.example.hackathon.core.model;

import com.badlogic.gdx.graphics.Texture;

/**
 * Created by tobyp on 12/28/16.
 */
public class UpgradeItem extends Item {
    private Upgrade upgrade;

    public final int COLLISION_PRIORITY_ITEM = 1;
    public UpgradeItem(Texture texture, int srcX, int srcY, int srcW, int srcH, Upgrade upgrade) {
        super(texture, srcX, srcY, srcW, srcH);
        this.upgrade = upgrade;
        this.collision_priority = COLLISION_PRIORITY_ITEM;
    }

    @Override
    public void collide(World world, Entity entity) {
        if (entity instanceof Player) {
            ((Player)entity).addUpgrade(this.upgrade);
        }

        world.remove(this);
    }
}
