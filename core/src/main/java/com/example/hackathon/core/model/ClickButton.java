package com.example.hackathon.core.model;

import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;

import java.util.List;

public class ClickButton extends ButtonElement {

    public ClickButton(Vector2 location, boolean initState, List<TiledMapTile> onTiles,
					   List<TiledMapTile> offTiles, List<TiledMapTileLayer.Cell> coveredCells,
					   String scriptActivate, String scriptDeactivate) {
		super(location, new Vector2(2, 2), new Vector2(1, 1), scriptActivate, scriptDeactivate);

		this.isActivated = initState;
		this.onTiles = onTiles;
		this.offTiles = offTiles;
		this.coveredCells = coveredCells;
    }

	@Override
	public void collide(World world, Entity entity) {
		super.collide(world, entity);
		updateTiles();
	}
}
