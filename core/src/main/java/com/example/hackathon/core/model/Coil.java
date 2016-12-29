package com.example.hackathon.core.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;

import java.util.List;

/**
 * Created by tobyp on 12/29/16.
 */
public class Coil extends Entity {
    private boolean active;

    protected List<TiledMapTile> onTiles;
    protected List<TiledMapTileLayer.Cell> coveredCells;
    protected List<TiledMapTile> offTiles;

    protected String onActivate;
    protected String onDeactivate;

    public Coil(Vector2 location, boolean active, String victim, List<TiledMapTile> onTiles,
                List<TiledMapTile> offTiles, List<TiledMapTileLayer.Cell> coveredCells,
                String onActivate, String onDeactivate) {
        super(location, new Vector2(2, 2));
        this.active = active;
        this.onTiles = onTiles;
        this.offTiles = offTiles;
        this.coveredCells = coveredCells;

        this.onActivate = onActivate;
        this.onDeactivate = onDeactivate;
    }

    public void updateTiles() {
        List<TiledMapTile> target_tiles = active ? onTiles : offTiles;
        int i=0;
        for (TiledMapTileLayer.Cell cell : coveredCells) {
            cell.setTile(target_tiles.get(i));
            i++;
        }
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(World world, boolean active) {
        this.active = active;
        world.runScript(null, active ? onActivate : onDeactivate);
        updateTiles();
    }
}
