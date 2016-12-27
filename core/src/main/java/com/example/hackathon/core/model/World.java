package com.example.hackathon.core.model;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class World {
	private List<InteractionElement> interactionElements = new ArrayList<>();

	private TiledMap map;
	private Player player = new Player();

	public static final float INTERACTION_RADIUS = 1;
	Integer[] CLICK_BUTTON_ON_TILE_IDS = {33,34,55,56};
	Integer[] CLICK_BUTTON_OFF_TILE_IDS = {35,36,57,58};

	public static boolean isWalkable(int cellType) {
		return cellType == 2;
	}

	public World(TiledMap map) {
		this.map = map;
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

	public void findInteractionElements() {
		// find ClickButtons and add them to the InteractionElements
		List<TiledMapTile> clickButtonOnTiles = getTilesByIds(CLICK_BUTTON_ON_TILE_IDS);
		List<TiledMapTile> clickButtonOffTiles = getTilesByIds(CLICK_BUTTON_OFF_TILE_IDS);

		int width = ((TiledMapTileLayer) map.getLayers().get(0)).getWidth();
		int height = ((TiledMapTileLayer) map.getLayers().get(0)).getHeight();

		for (int x = 0; x < width; i++) {
			for (int y = 0; y < height; y++) {
				if (Arrays.asList(CLICK_BUTTON_OFF_TILE_IDS).contains(getCellTileId(x,y))) {
					// add Button to matching location (and look, that it is not added multiple times (only on upper left tile?)
				}
			}
		}
	}

	public List<TiledMapTile> getTilesByIds(int[] ids) {
		List<TiledMapTile> back = new ArrayList<>();
		for (int i : ids) {
			back.add(map.getTileSets().getTile(i));
		}
		return back;
	}
}
