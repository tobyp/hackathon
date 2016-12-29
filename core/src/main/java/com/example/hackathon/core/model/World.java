package com.example.hackathon.core.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.example.hackathon.core.HackathonGame;
import com.example.hackathon.core.ScriptCommand;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class World {
	private TiledMap map;
	private Player player;
	private TiledMapTileLayer walkLayer;
	private TiledMapTileLayer buttonLayer;
	private MapLayer metaLayer;
	private float tileSize;

	private List<Entity> entities;

	public float getWorldTime() {
		return worldTime;
	}

	private float worldTime;

	public static final int[] CLICK_BUTTON_OFF_TILE_IDS = { 226, 227, 258, 259 };
	public static final int[] CLICK_BUTTON_ON_TILE_IDS = { 228, 229, 260, 261 };
	private List<TiledMapTile> clickButtonOnTiles;
	private List<TiledMapTile> clickButtonOffTiles;

	private boolean isWalkable(int x, int y) {
		TiledMapTileLayer.Cell cell = walkLayer.getCell(x, y);
		if (cell == null) return false;
		return cell.getTile().getId() != 1;
	}

	public World(TiledMap map) {
		this.map = map;
		metaLayer = map.getLayers().get("meta");
		walkLayer = (TiledMapTileLayer)map.getLayers().get("walk");
		buttonLayer = (TiledMapTileLayer)map.getLayers().get("buttons");
		tileSize=  walkLayer.getTileWidth();

		clickButtonOnTiles = getTilesByIds(CLICK_BUTTON_ON_TILE_IDS);
		clickButtonOffTiles = getTilesByIds(CLICK_BUTTON_OFF_TILE_IDS);

		MapObject player_start_mo = metaLayer.getObjects().get("player-start");
		Vector2 player_start = new Vector2(player_start_mo.getProperties().get("x", Float.class) / tileSize, player_start_mo.getProperties().get("y", Float.class) / tileSize);
		this.player = new Player(player_start);

		this.entities = new ArrayList<>();
		entities.add(player);

		for (MapObject mo : metaLayer.getObjects()) {
			if (mo.getProperties().containsKey("on-load")) {
				runScript(mo, mo.getProperties().get("on-load", String.class));
			}
		}
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

	private Set<Entity> collision_cache = new HashSet<Entity>();

	public void update(float deltaTime) {
		worldTime += deltaTime;
		for (Entity e_ : entities) {
			if (e_ instanceof DynamicEntity) {
				DynamicEntity e = (DynamicEntity)e_;
				Vector2 collidedWith = null;

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
								collidedWith = new Vector2(i, j);
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
								collidedWith = new Vector2(j, i);
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
				if (collidedWith != null)
					e.collide(this, collidedWith);
			}
			e_.update(this, deltaTime);
		}

		Rectangle player_rect = player.getCollisionRect();
		entities.stream().forEach((Entity e) -> {
					Rectangle entity_rect = e.getCollisionRect();
					boolean collided = player_rect.overlaps(entity_rect);
					boolean precollided = collision_cache.contains(e);
					Entity higher_prio = e.collision_priority > player.collision_priority ? e : player;
					Entity lower_prio = e.collision_priority > player.collision_priority ? player : e;
					if (collided && !precollided) {
						higher_prio.collide(this, lower_prio);
						collision_cache.add(e);
					}
					else if (!collided && precollided) {
						higher_prio.uncollide(this, lower_prio);
						collision_cache.remove(e);
					}
				});

		if (player.isDestroyed()) {
			HackathonGame.isGameOver = true;
		}
		entities.removeIf((Entity e) -> e.isDestroyed());
	}


	public void addEntity(Entity e) {
		entities.add(e);
	}

	public List<TiledMapTile> getTilesByIds(int[] ids) {
		List<TiledMapTile> back = new ArrayList<>();
		for (int i : ids) {
			back.add(map.getTileSets().getTile(i));
		}
		return back;
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

	public void runScript(MapObject mo, String script) {
		Logger logger = Logger.getLogger("script");
		String[] lines = script.split(";");
		for (String line : lines) {
			String[] cmd_args = line.split(" ", 2);
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

			if (param_strings.length + 1 != m.getParameterCount()) {
				logger.severe("Command " + cmd_args[0] + " requires " + (m.getParameterCount() - 2) + " parameters.");
				return;
			}

			Object[] params = new Object[m.getParameterCount()];
			params[0] = mo;
			Class<?>[] param_types = m.getParameterTypes();
			for (int i=1; i < m.getParameterCount(); i++) {
				Class<?> c = param_types[i];
				String p = param_strings[i-1];
				if (c == float.class) {
					params[i] = Float.parseFloat(p);
				}
				else if (c == int.class) {
					params[i] = Integer.parseInt(p);
				}
				else if (c == String.class) {
					params[i] = p;
				}
				else if (c == boolean.class) {
					params[i] = (p.equalsIgnoreCase("true") || p.equalsIgnoreCase("on") || p.equalsIgnoreCase("yes") || p.equalsIgnoreCase("1"));
				}
				else {
					logger.severe("Unsupported argument type " + c.toString());
					return;
				}
			}

			try {
				m.invoke(this, params);
			} catch (Exception e) {
				logger.log(Level.SEVERE, "Script failed", e);
			}
		}
	}

	@ScriptCommand
	public void teleport(MapObject mo, int x, int y) {
		player.getLocation().set(x, y);
	}

	@ScriptCommand
	public void battery(MapObject mo, float capacity, float consumption) {
		Upgrade upgrade = new Upgrade(capacity, consumption);
		Sprite batterySprite = new Sprite(new Texture("items/battery.png"), 32, 32);
		int cell_x = (int)(mo.getProperties().get("x", Float.class).floatValue() / tileSize);
		int cell_y = (int)(mo.getProperties().get("y", Float.class).floatValue() / tileSize);

		UpgradeItem ue = new UpgradeItem(new Vector2(cell_x + 0.5f, cell_y + 0.5f), batterySprite, upgrade);
		entities.add(ue);
		Logger.getLogger("script").info("Spawned Battery at (" + cell_x + ", " + cell_y + ") cap=" + capacity + ", drain=" + consumption);
	}
	@ScriptCommand
	public void wallToggle(MapObject mo, String wall_name) {

	}

	@ScriptCommand
	public void wall(MapObject mo, boolean on) {

	}

	@ScriptCommand
	public void button(MapObject mo, boolean on) {
		int cell_x = (int)(mo.getProperties().get("x", Float.class).floatValue() / tileSize);
		int cell_y = (int)(mo.getProperties().get("y", Float.class).floatValue() / tileSize);

		List<TiledMapTileLayer.Cell> cells = new ArrayList<>();
		for (int y = cell_y + 1; y > cell_y - 1; y--) {
			for (int x = cell_x; x < cell_x + 2; x++) {
				cells.add(buttonLayer.getCell(x, y));
			}
		}
		ButtonElement button = new ClickButton(new Vector2(cell_x + 1, cell_y + 1), on, clickButtonOnTiles, clickButtonOffTiles, cells);
		button.getCollisionSize().set(0.5f, 0.5f);
		button.updateTiles();
		entities.add(button);
		Logger.getLogger("script").info("Spawned button at (" + cell_x + ", " + cell_y + ") state="+on);
	}
}
