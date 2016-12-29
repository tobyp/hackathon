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

	private interface WalkableTester {
		boolean isWalkable(int x, int y);
	}

    /**
     * The normalized direction in that the robot is looking.
     */
	private Vector2 direction = Vector2.Y;

    DynamicEntity(Vector2 location, Vector2 size, Vector2 collisionSize, Sprite sprite) {
        super(location, size, collisionSize);
    	this.sprite = sprite;
    }

	DynamicEntity(Vector2 location, Vector2 size, Vector2 collisionSize, Texture texture, int srcX, int srcY, int srcW, int srcH) {
		this(location, size, collisionSize, new Sprite(texture, srcX, srcY, srcW, srcH));
	}

    Movement getDiscreteDirection() {
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

    void setDirection(Vector2 v) {
        direction = v;
    }

    void setVelocity(Vector2 v) {
		velocity = v;
		if (v.len2() > 0.01)
			setDirection(v.cpy().nor());
	}

    public Vector2 getVelocity() {
        return velocity;
    }

	@Override
	public void collide(World world, Entity entity) { }

	/**
	 * Collided with the environment.
	 */
	void collide(World world) { }

	/**
	 * Test collision for one coordinate.
	 * The entity is going to the right or left from prevMid ± halfSize (otherHalfSize is "up and down").
	 * The next position is `new Vector2(prevmid.x + distance, prevmid.y)`.
	 *
	 * @return null if no collision occured, otherwise the distance of the collision.
	 */
	private float testCollision(Vector2 prevMid, float halfSize, float otherHalfSize, float distance, WalkableTester walkable) {
		if (distance == 0)
			return distance;

		// The step to go from cur to next
		int step = (int) Math.copySign(1, distance);
		prevMid.x += step * halfSize;
		// The current tile coordinate
		int cur =  (int) prevMid.x;
		// The next coordinate
		int next =  (int) (prevMid.x + distance);
		// We don't need to check the last tile if we only touch it
		if (next == prevMid.x + distance)
			next--;
		int jStart = (int) (prevMid.y - otherHalfSize);
		int jEnd = (int) Math.ceil(prevMid.y + otherHalfSize);
		for (int i = cur; i != next + step; i += step) {
			// Test if this x coordinate overlaps somewhere in the height of the robot
			for (int j = jStart; j < jEnd; j++) {
				if (!walkable.isWalkable(i, j)) {
					// Set the coordinate to the border one step backwards
					// because we have a collision.
					// Add step if it is -1, otherwise do nothing (because the wall tile ends at i + 1)
					float newDistance = i - (step - 1) / 2 - prevMid.x;
					// Can't touch it…
					/*if (step == 1) {
						if (newDistance < 0 || distance < newDistance)
							throw new UnsupportedOperationException("Something went wrong");
					} else {
						if (newDistance > 0 || distance > newDistance)
							throw new UnsupportedOperationException("Something went wrong");
					}*/
					return newDistance;
				}
			}
		}

		return distance;
	}

	@Override
	public void update(World world, float deltaTime) {
		super.update(world, deltaTime);

		// Compute physics
		boolean collided = false;

		Vector2 halfSize = size.cpy().scl(0.5f);
		// Compute the movement for all entities
		Vector2 diff = velocity.cpy().scl(deltaTime);
		// Test for collisions in the newly occupied cells
		// X coordinate
		float distance = testCollision(location.cpy(), halfSize.x,
			halfSize.y, diff.x, world::isWalkable);
		if (distance != diff.x)
			collided = true;
		location.x += distance;

		// Y coordinate, switch x and y
		distance = testCollision(new Vector2(location.y, location.x), halfSize.y,
			halfSize.x, diff.y, (x, y) -> world.isWalkable(y, x));
		if (distance != diff.y)
			collided = true;
		location.y += distance;

		if (collided)
			collide(world);
	}
}
