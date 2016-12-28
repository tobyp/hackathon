package com.example.hackathon.core.model;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;

import java.util.List;

/**
 * A generic Element on the map, that the player can interact with
 */
public abstract class InteractionElement {
	public abstract void interact();

	public Vector2 location;

	public List<TiledMapTileLayer.Cell> coveredCells;
}
