package com.example.hackathon.core.model;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by Laci on 27.12.16.
 * Base class for the players robot and enemy robots
 */
public class Robot {
	public Vector2 location;
    private Movement movement;

    public Movement getMovement() {
        return movement;
    }

    public void setMovement(Movement m) {
        movement = m;
    }

    public void moveTowards(Vector2 goal, int distance)
	{
    	// TODO Distance as int or float or absolute location?
	}
}
