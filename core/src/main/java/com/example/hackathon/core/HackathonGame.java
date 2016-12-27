package com.example.hackathon.core;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

import com.badlogic.gdx.math.Vector3;
import com.example.hackathon.core.model.Player;

public class HackathonGame implements ApplicationListener, InputProcessor {
	private Texture texture;
	private SpriteBatch batch;
	private float elapsed;
	private Player player;
	/**
	 * If the player movement can be set by a mouse movement.
	 * false if it is set by the keyboard.
	 * The mouse will only set the movement if this variable is set to true.
	 */
	private boolean movementSetByMouse;
	private Camera camera = new OrthographicCamera();

	/**
	 * Update the movement by the mouse depending on the player position.
	 */
	private void updateMouseInput() {
		if (!movementSetByMouse)
			return;
		Vector3 pos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
		camera.unproject(pos);
		// Camera - Player
		Vector2 diff = player.getLocation().sub(pos.x, pos.y);
		if (diff.len2() < 1)
			diff.nor();
		player.setVelocity(diff);
	}

	private void updateMovement(float deltaTime) {
		// TODO Compute movement for all robots
		player.getLocation().mulAdd(player.getVelocity(), deltaTime);
	}

	@Override
	public void create () {
		texture = new Texture(Gdx.files.internal("libgdx-logo.png"));
		batch = new SpriteBatch();
		player = new Player();
		movementSetByMouse = true;
	}

	@Override
	public void resize (int width, int height) {
	}

	@Override
	public void render () {
		float deltaTime = Gdx.graphics.getDeltaTime();
		elapsed += deltaTime;

		// Update input
		updateMouseInput();

		// Update logic
		updateMovement(deltaTime);

		// Render
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(texture, 100+100*(float)Math.cos(elapsed), 100+25*(float)Math.sin(elapsed));
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
			player.setVelocity(m);
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
			player.setVelocity(Vector2.Zero);
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
