package com.example.hackathon.core.model;

import com.badlogic.gdx.math.Vector2;

/**
 * Base class for the players robot and enemy robots
 */
public class DynamicEntity extends Entity {
    protected Vector2 velocity = new Vector2();
    /**
     * The normalized direction in that the robot is looking.
     */
    private Vector2 direction = Vector2.Y;

    public Movement getDiscreteDirection() {
        if (Math.abs(direction.x) > Math.abs(direction.y)) {
            if (direction.x < 0)
                return Movement.Left;
            return Movement.Right;
        } else {
            if (direction.y < 0)
                return Movement.Left;
            return Movement.Right;

        }
    }

    public void setDirection(Vector2 v) {
        direction = v;
    }

    public void setVelocity(Vector2 v) {
		velocity = v;
		if (v.len2() > 0.1)
			direction = v.nor();
	}

    public Vector2 getVelocity() {
        return velocity;
    }
}
