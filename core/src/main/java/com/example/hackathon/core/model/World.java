package com.example.hackathon.core.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.example.hackathon.core.HackathonGame;
import com.example.hackathon.core.ScriptCommand;

import java.lang.reflect.Method;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class World {
	private TiledMap map;
	private Player player;
	private Boss boss;
	private TiledMapTileLayer walkLayer;
	private TiledMapTileLayer buttonLayer;
	private MapLayer metaLayer;

	private List<Entity> entities;
	private Map<String, Entity> entity_names;

	private float worldTime;

	private static final int[] CLICK_BUTTON_OFF_TILE_IDS = { 226, 227, 258, 259 };
	private static final int[] CLICK_BUTTON_ON_TILE_IDS = { 228, 229, 260, 261 };
	private List<TiledMapTile> clickButtonOnTiles;
	private List<TiledMapTile> clickButtonOffTiles;

	/**
	 * Get the mid point of a meta map object.
	 *
	 * @param name The name of the object in the meta layer.
	 * @return The mid point of the object.
	 */
	public Vector2 getObjectLocation(String name) {
		MapObject obj = metaLayer.getObjects().get(name);
		RectangleMapObject rmo = (RectangleMapObject)obj;
		Vector2 center = new Vector2();
		rmo.getRectangle().getCenter(center);
		return center;
	}

	public World(TiledMap map) {
		this.map = map;
		metaLayer = map.getLayers().get("meta");
		walkLayer = (TiledMapTileLayer)map.getLayers().get("walk");
		buttonLayer = (TiledMapTileLayer)map.getLayers().get("buttons");

		clickButtonOnTiles = getTilesByIds(CLICK_BUTTON_ON_TILE_IDS);
		clickButtonOffTiles = getTilesByIds(CLICK_BUTTON_OFF_TILE_IDS);

		player = new Player(getObjectLocation("player-start"));
		boss = new Boss(getObjectLocation("boss-start"));

		this.entities = new ArrayList<>();
		entities.add(player);
		entities.add(boss);

		for (MapObject mo : metaLayer.getObjects()) {
			if (mo.getProperties().containsKey("on-load")) {
				runScript(mo, mo.getProperties().get("on-load", String.class));
			}
		}
	}

	public float getWorldTime() {
		return worldTime;
	}

	public boolean isWalkable(int x, int y) {
		TiledMapTileLayer.Cell cell = walkLayer.getCell(x, y);
		if (cell == null) return false;
		return cell.getTile().getId() != 1;
	}

	public TiledMap getMap() {
		return map;
	}

	public Player getPlayer() {
		return player;
	}

	private int getCellTileId(int x, int y) {
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

	private final Set<Entity> collision_cache = new HashSet<>();

	public void update(float deltaTime) {
		worldTime += deltaTime;
		entities.forEach(e -> e.update(this, deltaTime));

		Rectangle player_rect = player.getCollisionRect();
		entities.forEach((Entity e) -> {
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
		entities.removeIf(Entity::isDestroyed);

		spawnRandomBattery(deltaTime);
	}

	public void spawnRandomBattery(float deltaTime) {
		int width = ((TiledMapTileLayer)map.getLayers().get("walk")).getWidth();
		int height = ((TiledMapTileLayer)map.getLayers().get("walk")).getHeight();
		List<Vector2> walkables = new ArrayList<>();
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if (isWalkable(x, y)) {
					walkables.add(new Vector2(x, y));
				}
			}
		}
		if (Math.random() < 0.02) {
			Vector2 w = walkables.get((int) (Math.random()*walkables.size()));
			Upgrade upgrade = new Upgrade(10, 0.1f);
			Sprite batterySprite = new Sprite(new Texture("items/battery.png"), 32, 32);

			UpgradeItem ue = new UpgradeItem(new Vector2(w.x + 0.5f, w.y + 0.5f), batterySprite, upgrade);
			entities.add(ue);
			Logger.getLogger("script").info("Spawned Battery at (" + w.x + ", " + w.y + ") cap=" + 10 + ", drain=" + 0.1f);
		}
	}

	public void addEntity(Entity e) {
		entities.add(e);
	}

	private List<TiledMapTile> getTilesByIds(int[] ids) {
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

	private void runScript(MapObject mo, String script) {
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
				logger.log(Level.SEVERE, "Script failed: " + script, e);
			}
		}
	}

	@ScriptCommand
	public void teleport(MapObject mo, int x, int y) {
		player.getLocation().set(x, y);
	}

	@ScriptCommand
	public void battery(MapObject mo, float capacity, float consumption) {
		RectangleMapObject rmo = (RectangleMapObject)mo;
		Vector2 center = new Vector2();
		rmo.getRectangle().getCenter(center);

		Upgrade upgrade = new Upgrade(capacity, consumption);
		Sprite batterySprite = new Sprite(new Texture("items/battery.png"), 32, 32);

		UpgradeItem ue = new UpgradeItem(center, batterySprite, upgrade);
		entities.add(ue);
		Logger.getLogger("script").info("Spawned Battery at " + center + " cap=" + capacity + ", drain=" + consumption);
	}

	@ScriptCommand
	public void barrierToggle(MapObject mo, String wall_name) {

	}

	@ScriptCommand
	public void barrier(MapObject mo, boolean on, float toggle_period) {
		/*RectangleMapObject rmo = (RectangleMapObject)mo;
		Vector2 center = new Vector2(), size = new Vector2();
		rmo.getRectangle().getCenter(center);
		rmo.getRectangle().getSize(size);
		Barrier b = new Barrier(center, size, on, toggle_period);
		entities.add(b);
		Logger.getLogger("script").info("Spawned barrier at " + center);*/
	}

	@ScriptCommand
	public void button(MapObject mo, boolean on) {
		RectangleMapObject rmo = (RectangleMapObject)mo;
		Vector2 center = new Vector2(), size = new Vector2();
		rmo.getRectangle().getCenter(center);
		rmo.getRectangle().getSize(size);

		List<TiledMapTileLayer.Cell> cells = new ArrayList<>();
		for (int y = (int)rmo.getRectangle().getY() + 1; y > (int)rmo.getRectangle().getY() - 1; y--) {
			for (int x = (int)rmo.getRectangle().getX(); x < (int)rmo.getRectangle().getX() + 2; x++) {
				TiledMapTileLayer.Cell cell = buttonLayer.getCell(x, y);
				cells.add(cell);
			}
		}
		ButtonElement button = new ClickButton(center, on, clickButtonOnTiles, clickButtonOffTiles, cells);
		button.updateTiles();
		entities.add(button);
		Logger.getLogger("script").info("Spawned button at " + center + " state="+on);
	}

	@ScriptCommand
	public void coil(MapObject mo, boolean on) {

	}

	@ScriptCommand
	public void coilOn(MapObject mo, boolean on) {

	}

	@ScriptCommand
	public void mob(MapObject mo) {

	}
}
