package com.example.hackathon.core.model;

import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.example.hackathon.core.HackathonGame;

import java.util.List;

abstract class ButtonElement extends Entity {
	protected List<TiledMapTile> onTiles;
	protected List<TiledMapTileLayer.Cell> coveredCells;
	protected List<TiledMapTile> offTiles;

	protected boolean isActivated;
	private String scriptActivate, scriptDeactivate;

	private static final int COLLISION_PRIORITY_BUTTON = 1;

	ButtonElement(Vector2 location, Vector2 size, Vector2 collisionSize, String scriptActivate, String scriptDeactivate) {
		super(location, size, collisionSize);
		collision_priority = COLLISION_PRIORITY_BUTTON;
		this.scriptActivate = scriptActivate;
		this.scriptDeactivate = scriptDeactivate;
	}

	public void updateTiles() {
		List<TiledMapTile> target_tiles = isActivated ? onTiles : offTiles;
		int i=0;
		for (TiledMapTileLayer.Cell cell : coveredCells) {
			cell.setTile(target_tiles.get(i));
			i++;
		}
	}

	@Override
	public void collide(World world, Entity entity) {
		isActivated = !isActivated;
		world.runScript(null, isActivated ? scriptActivate : scriptDeactivate);
	}
}
