package com.example.hackathon.core.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

/**
 * Base class for the players robot and enemy robots
 */
public class DynamicEntity extends Entity {
    protected Vector2 velocity = new Vector2();
    protected Sprite sprite;

    /**
     * The normalized direction in that the robot is looking.
     */
    protected Vector2 direction = Vector2.Y;

    public DynamicEntity(Sprite sprite) {
    	this.sprite = sprite;
    }

	public DynamicEntity(Texture texture, int srcX, int srcY, int srcW, int srcH) {
		this(new Sprite(texture, 128, 128));
	}

    public Movement getDiscreteDirection() {
        if (Math.abs(direction.x) > Math.abs(direction.y)) {
            if (direction.x < 0)
                return Movement.Left;
            return Movement.Right;
        } else {
            if (direction.y < 0)
                return Movement.Down;
            return Movement.Up;

        }
    }

    public void setDirection(Vector2 v) {
        direction = v;
    }

    public void setVelocity(Vector2 v) {
		velocity = v;
		if (v.len2() > 0.01)
			setDirection(v.cpy().nor());
	}

    public Vector2 getVelocity() {
        return velocity;
    }

	@Override
	public void collide(World world, Entity entity) {

	}

	public void collide(World world, Vector2 pos) { }

	@Override
	public void update(World world, float deltaTime) {
		super.update(world, deltaTime);

		// Compute physics
		Vector2 collidedWith = null;

		Vector2 halfSize = size.cpy().scl(0.5f);
		// Compute the movement for all entities
		Vector2 diff = velocity.cpy().scl(deltaTime);
		// Test for collisions in the newly occupied cells
		// X coordinate
		if (diff.x != 0) {
			// The step to go from cur to next
			int step = (int) Math.copySign(1, diff.x);
			// The current tile coordinate
			int cur =  (int) (location.x + step * halfSize.x);
			// The next coordinate
			int next =  (int) (location.x + step * halfSize.x + diff.x);
			outer:
			for (int i = cur; i != next + step; i += step) {
				// Test if this x coordinate overlaps somewhere in the height of the robot
				int maxJ = (int) Math.ceil(location.y + halfSize.y);
				for (int j = (int) (location.y - halfSize.y); j < maxJ; j++) {
					if (!world.isWalkable(i, j)) {
						collidedWith = new Vector2(i, j);
						// Set the coordinate to the border one step backwards
						// because we have a collision.
						diff.x = i - (step - 1) / 2 - (location.x + step * halfSize.x);
						break outer;
					}
				}
			}
			location.x += diff.x;
		}

		// Y coordinate
		if (diff.y != 0) {
			// The step to go from cur to next
			int step = (int) Math.copySign(1, diff.y);
			// The current tile coordinate
			int cur =  (int) (location.y + step * halfSize.y);
			// The next coordinate
			int next =  (int) (location.y + step * halfSize.y + diff.y);
			outer:
			for (int i = cur; i != next + step; i += step) {
				// Test if this x coordinate overlaps somewhere in the height of the robot
				int maxJ = (int) Math.ceil(location.x + halfSize.x);
				for (int j = (int) (location.x - halfSize.x); j < maxJ; j++) {
					if (!world.isWalkable(j, i)) {
						collidedWith = new Vector2(j, i);
						// Set the coordinate to the border one step backwards
						// because we have a collision.
						diff.y = i - (step - 1) / 2 - (location.y + step * halfSize.y);
						break outer;
					}
				}
			}
			location.y += diff.y;
		}
		if (collidedWith != null)
			collide(world, collidedWith);
	}

	public void render(SpriteBatch batch) {
		Vector2 p = size.cpy().scl(-0.5f).add(location);
		sprite.setPosition(p.x, p.y);
		sprite.setSize(size.x, size.y);
		sprite.draw(batch);
	}
}
