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

    public ClickButton(Vector2 location, boolean initState, List<TiledMapTile> onTiles,
					   List<TiledMapTile> offTiles, List<TiledMapTileLayer.Cell> coveredCells) {
		super(location, new Vector2(2, 2), new Vector2(1, 1));

		this.isActivated = initState;
		this.onTiles = onTiles;
		this.offTiles = offTiles;
		this.coveredCells = coveredCells;
    }

	@Override
	public void collide(World world, Entity entity) {
    	isActivated = !isActivated;
    	updateTiles();
	}
}
