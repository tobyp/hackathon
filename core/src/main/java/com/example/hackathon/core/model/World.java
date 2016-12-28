package com.example.hackathon.core.model;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.example.hackathon.core.HackathonGame;
import com.example.hackathon.core.ScriptCommand;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class World {
	private TiledMap map;
	private Player player;
	private TiledMapTileLayer walk_layer;
	private MapLayer meta_layer;

	private List<Entity> entities;

	public static final float INTERACTION_RADIUS = 1;
	int[] CLICK_BUTTON_ON_TILE_IDS = { 228, 229, 260, 261 };
	int[] CLICK_BUTTON_OFF_TILE_IDS = { 226, 227, 258, 259 };

	private boolean isWalkable(int x, int y) {
		return walk_layer.getCell(x, y).getTile().getId() != 1;
	}

	public World(TiledMap map) {
		this.map = map;
		meta_layer = map.getLayers().get("meta");
		walk_layer = (TiledMapTileLayer)map.getLayers().get("walk");

		this.player = new Player();
		MapObject player_start = meta_layer.getObjects().get("player-start");
		player.setLocation(new Vector2(player_start.getProperties().get("x", Float.class) / ((TiledMapTileLayer) map.getLayers().get(0)).getTileWidth(), player_start.getProperties().get("y", Float.class) / ((TiledMapTileLayer) map.getLayers().get(0)).getTileWidth()));

		this.entities = new ArrayList<>();
		entities.add(player);
		findEntitys();
	}

	public TiledMap getMap() {
		return map;
	}

	public Player getPlayer() {
		return player;
	}

	public int getCellTileId(int x, int y) {
		TiledMapTileLayer.Cell cell = ((TiledMapTileLayer) map.getLayers().get("ground")).getCell(x, y);
		if (cell == null)
			return -1;
		return cell.getTile().getId();
	}

	public int getCellTileId(Vector2 v) {
		return getCellTileId((int) v.x, (int) v.y);
	}

	public List<Entity> getEntities() {
		return entities;
	}

	public void update(float deltaTime) {
		for (Entity e_ : entities) {
			if (e_ instanceof DynamicEntity) {
				DynamicEntity e = (DynamicEntity)e_;

				// Compute the movement for all entities
				Vector2 diff = e.getVelocity().cpy().scl(deltaTime);
				// Test for collisions in the newly occupied cells
				// X coordinate
				if (diff.x != 0) {
					// The step to go from cur to next
					int step = (int) Math.copySign(1, diff.x);
					// The current tile coordinate
					int cur =  (int) (e.getLocation().x + step * e.getSize().x / 2);
					// The next coordinate
					int next =  (int) (e.getLocation().x + step * e.getSize().x / 2 + diff.x);
					outer:
					for (int i = cur; i != next + step; i += step) {
						// Test if this x coordinate overlaps somewhere in the height of the robot
						int maxJ = (int) Math.ceil(e.getLocation().y + e.getSize().y / 2);
						for (int j = (int) (e.getLocation().y - e.getSize().y / 2); j < maxJ; j++) {
							//Logger.getAnonymousLogger().info("id: " + walk_layer.getCell(i, j).getTile().getId() + " position: " + i + ", " + j);
							if (!isWalkable(i, j)) {
								// Set the coordinate to the border one step backwards
								// because we have a collision.
								diff.x = i - (step - 1) / 2 - e.getLocation().x - step * e.getSize().x / 2;
								//Logger.getAnonymousLogger().info(/*"diff x changed: " + diff.x + */" position: " + i + ", " + j);
								break outer;
							}
						}
					}
					e.getLocation().x += diff.x;
				}

				// Y coordinate
				if (diff.y != 0) {
					// The step to go from cur to next
					int step = (int) Math.copySign(1, diff.y);
					// The current tile coordinate
					int cur =  (int) (e.getLocation().y + step * e.getSize().y / 2);
					// The next coordinate
					int next =  (int) (e.getLocation().y + step * e.getSize().y / 2 + diff.y);
					outer:
					for (int i = cur; i != next + step; i += step) {
						// Test if this x coordinate overlaps somewhere in the height of the robot
						int maxJ = (int) Math.ceil(e.getLocation().x + e.getSize().x / 2);
						for (int j = (int) (e.getLocation().x - e.getSize().x / 2); j < maxJ; j++) {
							//Logger.getAnonymousLogger().info("id: " + walk_layer.getCell(j, i).getTile().getId() + " position: " + i + ", " + j);
							if (!isWalkable(j, i)) {
								// Set the coordinate to the border one step backwards
								// because we have a collision.
								diff.y = i - (step - 1) / 2 - (e.getLocation().y + step * e.getSize().y / 2);
								//Logger.getAnonymousLogger().info(/*"diff x changed: " + diff.x + */" position: " + i + ", " + j);
								break outer;
							}
						}
					}
					e.getLocation().y += diff.y;
				}
			}
			e_.update(deltaTime);
		}

		entities.stream().filter(
				(Entity e) -> e.location.dst(player.getLocation()) <= INTERACTION_RADIUS)
				.forEach((Entity e) -> e.collide(this, player));

		entities.stream().filter(ie -> ie instanceof ButtonElement).map(c -> (ButtonElement) c).forEach(ButtonElement::updateTiles);
	}


	public void addEntity(Entity e) {
		entities.add(e);
	}

	public void findEntitys() {
		// find ClickButtons and add them to the InteractionElements
		List<TiledMapTile> clickButtonOnTiles = getTilesByIds(CLICK_BUTTON_ON_TILE_IDS);
		List<TiledMapTile> clickButtonOffTiles = getTilesByIds(CLICK_BUTTON_OFF_TILE_IDS);

		int width = ((TiledMapTileLayer) map.getLayers().get("ground")).getWidth();
		int height = ((TiledMapTileLayer) map.getLayers().get("ground")).getHeight();

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				Logger.getAnonymousLogger().info("x: " + x + " y: " + y + " id: " + getCellTileId(x,y));
				if (CLICK_BUTTON_ON_TILE_IDS[0] == getCellTileId(x,y)) {
					List<TiledMapTileLayer.Cell> cells = getCellsByCoords(x, y, 2);
					ClickButton cb = new ClickButton(new Vector2(x + 1, y),
							true, clickButtonOnTiles, clickButtonOffTiles, cells);
					addEntity(cb);
					Logger.getAnonymousLogger().info("!!!!!!!!!!! on button set !!!!!!!!!!!!");
				} else if (CLICK_BUTTON_OFF_TILE_IDS[0] == getCellTileId(x,y)) {
					List<TiledMapTileLayer.Cell> cells = getCellsByCoords(x, y, 2);
					ClickButton cb = new ClickButton(new Vector2(x + 1, y),
							false, clickButtonOnTiles, clickButtonOffTiles, cells);
					addEntity(cb);
					Logger.getAnonymousLogger().info("!!!!!!!!!!! off button set !!!!!!!!!!!!");
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
		for (int j = 0; j < radius; j++) {
			for (int i = 0; i < radius; i++) {
				// go for x to the right and for y down
				back.add(((TiledMapTileLayer)map.getLayers().get("ground")).getCell(x+i, y-j));
			}
		}
		return back;
	}

	public void removeEntity(Entity entity) {
		if (entity == player) {
			HackathonGame.isGameOver = true;
		}
		this.entities.remove(entity);
	}

	private static final Map<String, Method> script_methods = new HashMap<>();

	static {
		for (Method m : World.class.getMethods()) {
			ScriptCommand sca = m.getDeclaredAnnotation(ScriptCommand.class);
			if (sca != null) {
				script_methods.put(m.getName(), m);
			}
		}
	}

	public void runScript(String script) {
		Logger logger = Logger.getLogger("script");
		String[] lines = script.split(";");
		for (String line : lines) {
			String[] cmd_args = line.split(" ", 1);
			Method m = script_methods.getOrDefault(cmd_args[0], null);
			if (m == null) {
				logger.severe("Unknown command " + cmd_args[0]);
				return;
			}

			String[] param_strings;
			if (cmd_args.length > 1) {
				param_strings = cmd_args[1].split(" ");
			}
			else {
				param_strings = new String[0];
			}

			if (cmd_args.length != m.getParameterCount()) {
				logger.severe("Command " + cmd_args[0] + " requires " + m.getParameterCount() + " parameters.");
				return;
			}

			Object[] params = new Object[m.getParameterCount()];
			Class<?>[] param_types = m.getParameterTypes();
			for (int i=0; i<m.getParameterCount(); ++i) {
				Class<?> c = param_types[i];
				String p = param_strings[i];
				if (c == Float.class) {
					params[i] = Float.parseFloat(p);
				}
				else if (c == Integer.class) {
					params[i] = Integer.parseInt(p);
				}
				else if (c == String.class) {
					params[i] = p;
				}
				else {
					logger.severe("Unsupported argument type " + c.toString());
					return;
				}
			}

			try {
				m.invoke(this, params);
			} catch (Exception e) {
				Logger.getLogger("script").severe("Script failed");
			}
		}
	}

	@ScriptCommand
	public void teleport(int x, int y) {
		player.setLocation(new Vector2(x, y));
	}
}
