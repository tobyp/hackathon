package com.example.hackathon.core.model;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

public class World {
	private List<InteractionElement> interactionElements = new ArrayList<>();

	private TiledMap map;
	private Player player;

	private List<Entity> entities;

	public static final float INTERACTION_RADIUS = 1;
	int[] CLICK_BUTTON_ON_TILE_IDS = { 33, 34, 55, 56 };
	int[] CLICK_BUTTON_OFF_TILE_IDS = { 35, 36, 57, 58 };

	private static boolean isWalkable(int cellType) {
		return true; //cellType == 2;
	}

	public World(TiledMap map) {
		this.map = map;
		MapLayer meta_layer = map.getLayers().get("meta");
		MapObject player_start = meta_layer.getObjects().get("player-start");

		this.player = new Player();
		player.setLocation(new Vector2(player_start.getProperties().get("x", Float.class) / ((TiledMapTileLayer) map.getLayers().get(0)).getTileWidth(), player_start.getProperties().get("y", Float.class) / ((TiledMapTileLayer) map.getLayers().get(0)).getTileWidth()));

		this.entities = new ArrayList<>();
		entities.add(player);
		findInteractionElements();
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

	public List<Entity> getEntities() {
		return entities;
	}

	public void update(float deltaTime) {
		for (Entity e : entities) {
			// Compute the movement for all entities
			Vector2 diff = e.getVelocity().cpy().scl(deltaTime);
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
					cur = (int) e.getLocation().x;
					next = (int) (e.getLocation().x + diff.x);
					step = -1;
				} else {
					cur = (int) (e.getLocation().x + e.getSize().x);
					next = (int) (e.getLocation().x + e.getSize().x + diff.x);
					step = 1;
				}
				outer:
				for (int i = cur; i != next; i += step) {
					// Test if this x coordinate overlaps somewhere in the height of the robot
					int maxJ = (int) Math.ceil(e.getLocation().y + e.getSize().y);
					for (int j = (int) e.getLocation().y; j < maxJ; j++) {
						if (!isWalkable(getCellTileId(i, j))) {
							// Set the coordinate to the border one step backwards
							// because we have a collision.
							diff.x = i - step - e.getLocation().x;
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
					cur = (int) e.getLocation().y;
					next = (int) (e.getLocation().y + diff.y);
					step = -1;
				} else {
					cur = (int) (e.getLocation().y + e.getSize().y);
					next = (int) (e.getLocation().y + e.getSize().y + diff.y);
					step = 1;
				}
				outer:
				for (int i = cur; i != next; i += step) {
					// Test if this y coordinate overlaps somewhere in the height of the robot
					int maxJ = (int) Math.ceil(e.getLocation().x + e.getSize().x);
					for (int j = (int) e.getLocation().x; j < maxJ; j++) {
						if (!isWalkable(getCellTileId(j, i))) {
							// Set the coordinate to the border one step backwards
							// because we have a collision.
							diff.y = i - step - e.getLocation().y;
							break outer;
						}
					}
				}
			}
			e.getLocation().add(diff);

			e.update(deltaTime);
		}
		checkForInteraction();
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

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if (CLICK_BUTTON_ON_TILE_IDS[0] == getCellTileId(x,y)) {
					List<TiledMapTileLayer.Cell> cells = getCellsByCoords(x, y, 2);
					ClickButton cb = new ClickButton(new Vector2(x + 1, y),
							true, clickButtonOnTiles, clickButtonOffTiles, cells);
					addIntercationElement(cb);
				} else if (CLICK_BUTTON_OFF_TILE_IDS[0] == getCellTileId(x,y)) {
					List<TiledMapTileLayer.Cell> cells = getCellsByCoords(x, y, 2);
					ClickButton cb = new ClickButton(new Vector2(x + 1, y),
							false, clickButtonOnTiles, clickButtonOffTiles, cells);
					addIntercationElement(cb);
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

	public List<TiledMapTileLayer.Cell> getCellsByCoords(int x, int y, int radius) {
		List<TiledMapTileLayer.Cell> back = new ArrayList<>();
		for (int i = 0; i < radius; i++) {
			for (int j = 0; j < radius; j++) {
				// go for x to the right and for y down
				back.add(((TiledMapTileLayer)map.getLayers().get(0)).getCell(x+i, y-j));
			}
		}
		return back;
	}
}
