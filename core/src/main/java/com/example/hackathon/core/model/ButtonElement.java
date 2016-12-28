package com.example.hackathon.core.model;

import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class ButtonElement extends Entity {
	public List<TiledMapTile> onTiles;
	public List<TiledMapTileLayer.Cell> coveredCells;
	public List<TiledMapTile> offTiles;

	public boolean isActivated;

	protected int cooldown;

	public void updateTiles() {
		if (isActivated) {
			for (TiledMapTileLayer.Cell cell : coveredCells) {
				cell.setTile(onTiles.get(coveredCells.indexOf(cell)));
			}
		} else {
			for (TiledMapTileLayer.Cell cell : coveredCells) {
				cell.setTile(offTiles.get(coveredCells.indexOf(cell)));
			}
		}
	}
}
