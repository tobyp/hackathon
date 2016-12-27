package com.example.hackathon.core.model;

import com.badlogic.gdx.math.Vector2;

/**
 * Base class for the players robot and enemy robots
 */
public class Robot {
	private Vector2 location = Vector2.Zero;

    /**
     * The normalized direction in that the robot is looking.
     */
    private Vector2 direction = Vector2.Y;
    private Vector2 velocity = Vector2.Zero;

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

    public Vector2 getLocation() {
        return location;
    }

    public void setLocation(Vector2 v) {
        location = v;
	}
}
