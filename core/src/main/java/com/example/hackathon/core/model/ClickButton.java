package com.example.hackathon.core.model;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

public class ClickButton extends ButtonElement {

    public ClickButton(Vector2 loc, boolean initState, List<TiledMapTile> onTiles,
					   List<TiledMapTile> offTiles, List<TiledMapTileLayer.Cell> coveredCells) {
    	isActivated = initState;
		location = loc;

		this.onTiles = onTiles;
		this.offTiles = offTiles;
		this.coveredCells = coveredCells;
		this.cooldown = 0;
    }

	@Override
	public void update(float deltaTime) {
		cooldown--;
	}

	@Override
	public void collide(World world, Entity entity) {
    	if (cooldown <= 0) {
			isActivated = !isActivated;
			cooldown = 10;
		}
	}
}
