package com.example.hackathon.core.model;

import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;

import java.util.List;

abstract class ButtonElement extends Entity {
	List<TiledMapTile> onTiles;
	List<TiledMapTileLayer.Cell> coveredCells;
	List<TiledMapTile> offTiles;

	boolean isActivated;

	private static final int COLLISION_PRIORITY_BUTTON = 1;

	ButtonElement(Vector2 location, Vector2 size, Vector2 collisionSize) {
		super(location, size, collisionSize);
		collision_priority = COLLISION_PRIORITY_BUTTON;
	}

	public void updateTiles() {
		List<TiledMapTile> target_tiles = isActivated ? onTiles : offTiles;
		int i=0;
		for (TiledMapTileLayer.Cell cell : coveredCells) {
			cell.setTile(target_tiles.get(i));
			i++;
		}
	}
}
