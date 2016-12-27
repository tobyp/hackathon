package com.example.hackathon.core;

import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Matrix4;

import java.util.logging.Logger;

public class HackathonGame implements ApplicationListener {
	SpriteBatch batch;
	float elapsed;
	TiledMap map;
	TiledMapRenderer map_renderer;
	OrthographicCamera camera;
	BitmapFont font;

	@Override
	public void create () {
		font = new BitmapFont();
		batch = new SpriteBatch();

		camera = new OrthographicCamera();

		TmxMapLoader loader = new TmxMapLoader();
		map = loader.load("test.tmx");

		map_renderer = new OrthogonalTiledMapRenderer(map, 1f / 32f);
	}

	@Override
	public void resize (int width, int height) {
		camera.setToOrtho(false, (float)width / height * 10, 10);
	}

	@Override
	public void render () {
		elapsed += Gdx.graphics.getDeltaTime();
		Gdx.gl.glClearColor(0, 0, 1, 0);
		Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);

		camera.update();

		map_renderer.setView(camera);
		map_renderer.render();

		batch.begin();
		font.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 10, 20);
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
		map.dispose();
	}
}
