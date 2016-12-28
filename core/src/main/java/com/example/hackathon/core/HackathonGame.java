package com.example.hackathon.core;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import com.example.hackathon.core.model.Entity;
import com.example.hackathon.core.model.World;

public class HackathonGame implements ApplicationListener, InputProcessor {
	private SpriteBatch batch;
	private SpriteBatch gameBatch;
	private TiledMapRenderer map_renderer;
	private OrthographicCamera camera;
	private BitmapFont font;
	private Texture tew_texture;
	private Sprite tew_sprite;

	private World world;

	private Vector2 camera_pos;

	private final float TILES_PER_SCREEN_Y = 10f;
	private final float CAMERA_MOVE_MARGIN = 0.05f;

	public static boolean isGameOver = false;

	@Override
	public void create () {
		font = new BitmapFont();
		batch = new SpriteBatch();
		gameBatch = new SpriteBatch();

		camera = new OrthographicCamera();

		tew_texture = new Texture("tew.png");
		tew_sprite = new Sprite(tew_texture, 128, 128);

		TmxMapLoader loader = new TmxMapLoader();
		TiledMap map = loader.load("test.tmx");

		world = new World(map);
		camera.translate(world.getPlayer().getLocation());

		map_renderer = new OrthogonalTiledMapRenderer(map, 1f / 32f);
	}

	@Override
	public void resize (int width, int height) {
		camera.setToOrtho(false, (float)width / height * TILES_PER_SCREEN_Y, TILES_PER_SCREEN_Y);
		batch.getProjectionMatrix().setToOrtho2D(0, 0, width, height);
	}

	@Override
	public void render () {
		float deltaTime = Gdx.graphics.getDeltaTime();

		Vector3 pos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
		camera.unproject(pos);
		world.getPlayer().setTarget(new Vector2(pos.x, pos.y));

		// Update logic
		world.update(deltaTime);

		// Render
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);

		camera.position.set(world.getPlayer().getLocation(), 0);
		camera.update();
		gameBatch.setTransformMatrix(camera.view);
		gameBatch.setProjectionMatrix(camera.projection);
		//Vector2 pl = world.getPlayer().getLocation();
		//float width = (float) Gdx.graphics.getWidth() / Gdx.graphics.getHeight() * TILES_PER_SCREEN_Y;
		//float height = TILES_PER_SCREEN_Y;
		//gameBatch.getProjectionMatrix().setToOrtho2D(v2.x - width / 2, v2.y - height / 2, width, height);

		map_renderer.setView(camera);
		map_renderer.render();


		gameBatch.begin();
		for (Entity e : world.getEntities()) {
			/*Vector3 p = new Vector3(e.getLocation(), 0);
			// Manually center the texture
			//tew_sprite.setPosition(p.x - tew_sprite.getWidth() / 2.0f, p.y - tew_sprite.getHeight() / 2.0f);
			Matrix4 m = gameBatch.getProjectionMatrix().cpy().mul(gameBatch.getTransformMatrix());
			//System.out.println("Before: " + p);
			//System.out.println("2:      " + p.cpy().prj(camera.combined));
			//p.mul(m);
			//System.out.println("After:  " + p);
			tew_sprite.setPosition(e.getLocation().x, e.getLocation().y);
			//gameBatch.draw(tew_sprite, e.getLocation().x, e.getLocation().y, 2, 2);
			tew_sprite.setSize(2, 2);
			tew_sprite.draw(gameBatch);*/

			Vector2 p = e.getSize().cpy().scl(-0.5f).add(e.getLocation());
			tew_sprite.setPosition(p.x, p.y);
			tew_sprite.setSize(e.getSize().x, e.getSize().y);
			tew_sprite.draw(gameBatch);
		}
		gameBatch.end();

		batch.begin();
		font.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 10, 20);
		font.draw(batch, "Energy: " + world.getPlayer().getBattery() + "/" + world.getPlayer().getBatteryMax(), 100, 20);
		if (isGameOver) {
			Gdx.gl.glClearColor(0, 0, 0, 0);
			Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
			font.setColor(1.0f, 1.0f, 1.0f, 1.0f);
			font.draw(batch, "Game Over", 10, 40);
			// TODO warten
			isGameOver = false;
		}

		batch.end();
	}

	@Override
	public void pause () {
	}

	@Override
	public void resume () {

	}

	@Override
	public void dispose () {
		world.getMap().dispose();
		batch.dispose();
	}

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
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
		Vector3 pos = new Vector3(screenX, screenY, 0);
		camera.unproject(pos);
		world.getPlayer().setTarget(new Vector2(pos.x, pos.y));
		return true;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}
}
