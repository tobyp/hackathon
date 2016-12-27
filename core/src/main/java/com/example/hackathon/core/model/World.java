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

	private static boolean isWalkable(int cellType) {
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
			// X coordinate
			if (diff.x != 0) {
				// The current tile coordinate
				int cur;
				// The next coordinate
				int next;
				// The step to go from cur to next
				int step;
				if (diff.x < 0) {
					cur = (int) r.getLocation().x;
					next = (int) (r.getLocation().x + diff.x);
					step = -1;
				} else {
					cur = (int) (r.getLocation().x + r.getSize().x);
					next = (int) (r.getLocation().x + r.getSize().x + diff.x);
					step = 1;
				}
			outer:
				for (int i = cur; i != next; i += step) {
					// Test if this x coordinate overlaps somewhere in the height of the robot
					int maxJ = (int) Math.ceil(r.getLocation().y + r.getSize().y);
					for (int j = (int) r.getLocation().y; j < maxJ; j++) {
						if (!isWalkable(getCellTileId(i, j))) {
							// Set the coordinate to the border one step backwards
							// because we have a collision.
							diff.x = i - step - r.getLocation().x;
							break outer;
						}
					}
				}
			}
			// Y coordinate
			if (diff.y != 0) {
				// The current tile coordinate
				int cur;
				// The next coordinate
				int next;
				// The step to go from cur to next
				int step;
				if (diff.y < 0) {
					cur = (int) r.getLocation().y;
					next = (int) (r.getLocation().y + diff.y);
					step = -1;
				} else {
					cur = (int) (r.getLocation().y + r.getSize().y);
					next = (int) (r.getLocation().y + r.getSize().y + diff.y);
					step = 1;
				}
				outer:
				for (int i = cur; i != next; i += step) {
					// Test if this y coordinate overlaps somewhere in the height of the robot
					int maxJ = (int) Math.ceil(r.getLocation().x + r.getSize().x);
					for (int j = (int) r.getLocation().x; j < maxJ; j++) {
						if (!isWalkable(getCellTileId(j, i))) {
							// Set the coordinate to the border one step backwards
							// because we have a collision.
							diff.y = i - step - r.getLocation().y;
							break outer;
						}
					}
				}
			}
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
