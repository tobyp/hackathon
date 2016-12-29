package com.example.hackathon.core.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class Boss extends DynamicEntity {
	public Boss(Vector2 location) {
		super(location, new Vector2(6, 6), new Vector2(6, 6),
			new Texture("boss.png"), 0, 0, 128, 128);
	}
}
