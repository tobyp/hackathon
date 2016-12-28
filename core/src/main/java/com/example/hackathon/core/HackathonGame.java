package com.example.hackathon.core;

import java.util.logging.Logger;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import com.example.hackathon.core.model.Player;
import com.example.hackathon.core.model.World;

public class HackathonGame implements ApplicationListener, InputProcessor {
	private SpriteBatch batch;
	private TiledMapRenderer map_renderer;
	private OrthographicCamera camera;
	private BitmapFont font;
	private Texture tew_texture;
	private Sprite tew_sprite;

	private World world;

	private Vector2 camera_pos;

	private final float TILES_PER_SCREEN_Y = 10f;
	private final float CAMERA_MOVE_MARGIN = 0.05f;

	/**
	 * If the player movement can be set by a mouse movement.
	 * false if it is set by the keyboard.
	 * The mouse will only set the movement if this variable is set to true.
	 */
	private boolean movementSetByMouse;

	/**
	 * Update the movement by the mouse depending on the player position.
	 */
	private void updateMouseInput() {
		if (!movementSetByMouse)
			return;
		Vector3 pos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
		camera.unproject(pos);
		// Camera - Player
		Player player = world.getPlayer();
		Vector2 diff = player.getLocation().sub(pos.x, pos.y);
		if (diff.len2() < 1)
			diff.nor();
		player.setVelocity(diff);
	}

	private void updateMovement(float deltaTime) {
		// TODO Compute movement for all robots
		Vector2 displacement = world.getPlayer().getVelocity().cpy().scl(deltaTime);
		float move_margin = CAMERA_MOVE_MARGIN * Gdx.graphics.getWidth();
		if (Gdx.input.getX() < move_margin || Gdx.input.getY() < move_margin
				|| Gdx.input.getX() > Gdx.graphics.getWidth() - move_margin
				|| Gdx.input.getY() > Gdx.graphics.getHeight() - move_margin) {
			camera.translate(displacement);
		}
		world.getPlayer().getVelocity().add(displacement);
	}

	@Override
	public void create () {
		font = new BitmapFont();
		batch = new SpriteBatch();

		movementSetByMouse = true;

		camera = new OrthographicCamera();

		tew_texture = new Texture("tew.png");
		tew_sprite = new Sprite(tew_texture, 64, 64);

		TmxMapLoader loader = new TmxMapLoader();
		TiledMap map = loader.load("test.tmx");

		world = new World(map);
		camera.translate(world.getPlayer().getLocation());

		map_renderer = new OrthogonalTiledMapRenderer(map, 1f / 32f);
	}

	@Override
	public void resize (int width, int height) {
		camera.setToOrtho(false, (float)width / height * TILES_PER_SCREEN_Y, TILES_PER_SCREEN_Y);
	}

	@Override
	public void render () {
		float deltaTime = Gdx.graphics.getDeltaTime();

		// Update input
		updateMouseInput();

		// Update logic
		//world.updateMovement(deltaTime);
		updateMovement(deltaTime);

		// Render
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);

		camera.update();

		map_renderer.setView(camera);
		map_renderer.render();

		tew_sprite.setPosition(world.getPlayer().getLocation().x, world.getPlayer().getLocation().y);

		batch.begin();
		tew_sprite.draw(batch);
		font.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 10, 20);
		batch.end();
	}

	@Override
	public void pause () {
	}

	@Override
	public void resume () {
		movementSetByMouse = true;
	}

	@Override
	public void dispose () {
		world.getMap().dispose();
	}

	@Override
	public boolean keyDown(int keycode) {
		Vector2 m = Vector2.Zero;
		if (keycode == Input.Keys.W)
			m = Vector2.Y;
		else if (keycode == Input.Keys.S)
			m = new Vector2(0, -1);
		else if (keycode == Input.Keys.A)
			m = new Vector2(-1, 0);
		else if (keycode == Input.Keys.D)
			m = Vector2.X;

		if (m != Vector2.Zero) {
			world.getPlayer().setVelocity(m);
			movementSetByMouse = false;
		}
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		if (keycode == Input.Keys.W ||
			keycode == Input.Keys.S ||
			keycode == Input.Keys.A ||
			keycode == Input.Keys.D) {
			world.getPlayer().setVelocity(Vector2.Zero);
			// Let the mouse control the player again
			movementSetByMouse = true;
		}
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}
}
