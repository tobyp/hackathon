package com.example.hackathon.core.model;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

public class World {
	private List<InteractionElement> interactionElements = new ArrayList<>();

	private TiledMap map;
	private Player player = new Player();

	public static final float INTERACTION_RADIUS = 1;

	public static boolean isWalkable(int cellType) {
		return cellType == 2;
	}

	public World(TiledMap map) {
		this.map = map;
		MapLayer meta_layer = map.getLayers().get("meta");
		MapObject player_start = meta_layer.getObjects().get("player-start");

		player.setLocation(new Vector2(player_start.getProperties().get("x", Float.class), player_start.getProperties().get("y", Float.class)));
	}

	public TiledMap getMap() {
		return map;
	}

	public Player getPlayer() {
		return player;
	}

	public int getCellTileId(int x, int y) {
		return ((TiledMapTileLayer) map.getLayers().get(0)).getCell(0, 0).getTile().getId();
	}

	public int getCellTileId(Vector2 v) {
		return getCellTileId((int) v.x, (int) v.y);
	}

	public void updateMovement(float deltaTime) {
		// Compute the movement for all robots
		List<Robot> robots = new ArrayList<>();
		robots.add(player);
		for (Robot r : robots) {
			Vector2 diff = r.getVelocity().cpy().scl(deltaTime);
			// Test for collisions in the newly occupied cells
			// Test if the x coordinate overlaps somewhere in the height of the robot
			if (diff.x != 0) {
				// The current tile coordinate
				int cur;
				// The next coordinate
				int next;

				int xDiff = diff.x < 0 ? -1 : 1;
				int x = xDiff + (int) r.getLocation().x;
				int y = (int) r.getLocation().y;
				for (int i = 0; i < Math.ceil(r.getSize().y); i++) {
					if (!isWalkable(getCellTileId(x, y + i))) {
						// Set the x coordinate to the border because we have a collision
						diff.x = xDiff == 1 ? (float) Math.floor(diff.x) : 0;
						break;
					}
				}
			}
			int yDiff = diff.y < 0 ? -1 : (diff.y > 0 ? 1 : 0);
			r.getLocation().add(diff);
		}
	}

	/**
	 * Checks, if there are any interactionElements close to the player & calls the respective interact methods.
 	 */
	public void checkForInteraction() {
		interactionElements.stream().filter(
			(InteractionElement e) -> e.location.dst(player.getLocation()) <= INTERACTION_RADIUS)
			.forEach(InteractionElement::interact);
	}

	public void addIntercationElement(InteractionElement ie) {
		interactionElements.add(ie);
	}
}
