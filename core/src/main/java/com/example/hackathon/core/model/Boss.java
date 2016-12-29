package com.example.hackathon.core.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class Boss extends DynamicEntity {
	private boolean isDead = true;

	public Boss(Vector2 location) {
		super(location, new Vector2(6, 6), new Vector2(6, 6),
			new Texture("boss.png"), 0, 0, 128, 128);
		// Handle collisions here and not in the player
		collision_priority = 1;
	}

	public boolean getIsDead() {
		return isDead;
	}

	public void setIsDead(boolean b) {
		isDead = b;
	}

	@Override
	public void update(World world, float deltaTime) {
		super.update(world, deltaTime);
		velocity = (new Vector2((float) (Math.sin(world.getWorldTime() / 2) * Math.sin(world.getWorldTime() / 1f) * Math.random()) * 6,
			(float) (Math.cos(world.getWorldTime()) * Math.cos(world.getWorldTime() / 1.2f) * Math.random()) * 20));
		velocity.scl(0.6f);
	}

	@Override
	public void collide(World world, Entity entity) {
		super.collide(world, entity);

		// Kill the player
		if (entity instanceof Player)
			((Player) entity).setBattery(0);
	}
}
