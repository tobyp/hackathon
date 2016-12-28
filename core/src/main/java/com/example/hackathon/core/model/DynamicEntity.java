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

	public void render(SpriteBatch batch) {
		Vector2 p = size.cpy().scl(-0.5f).add(location);
		sprite.setPosition(p.x, p.y);
		sprite.setSize(size.x, size.y);
		sprite.draw(batch);
	}
}
