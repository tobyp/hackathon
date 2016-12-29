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
    private String victim;
    private Sprite sprite = null;
    private Entity victim_entity = null;

    protected List<TiledMapTile> onTiles;
    protected List<TiledMapTileLayer.Cell> coveredCells;
    protected List<TiledMapTile> offTiles;

    public Coil(Vector2 location, boolean active, String victim, List<TiledMapTile> onTiles,
                List<TiledMapTile> offTiles, List<TiledMapTileLayer.Cell> coveredCells,
                String onActivate, String onDeactivate) {
        super(location, new Vector2(2, 2));
        this.active = active;
        this.victim = victim;
        this.onTiles = onTiles;
        this.offTiles = offTiles;
        this.coveredCells = coveredCells;
        sprite = new Sprite(new Texture("coil_lightning.png"), 0, 0, 96, 32);
    }

    @Override
    public void update(World world, float deltaTime) {
        if (victim != null && victim_entity == null) {
            victim_entity = world.getEntity(victim);
        }
    }

    @Override
    public void render(World world, SpriteBatch batch) {
        if (active && victim_entity != null) {
            sprite.draw(batch);
        }
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

    public void setActive(boolean active) {
        this.active = active;
        if (active) {

        }
        updateTiles();
    }
}
