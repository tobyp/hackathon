package com.example.hackathon.core;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;

import com.example.hackathon.core.model.Movement;
import com.example.hackathon.core.model.Player;

public class HackathonGame implements ApplicationListener, InputProcessor {
	Texture texture;
	SpriteBatch batch;
	float elapsed;
	Player player;
	/**
	 * If the last movement of the player was set by a mouse movement.
	 * false if it was set by the keyboard.
	 */
	boolean movementSetByMouse = false;

	@Override
	public void create () {
		texture = new Texture(Gdx.files.internal("libgdx-logo.png"));
		batch = new SpriteBatch();
		player = new Player();
	}

	@Override
	public void resize (int width, int height) {
	}

	@Override
	public void render () {
		elapsed += Gdx.graphics.getDeltaTime();
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
	}

	@Override
	public void dispose () {
	}

	@Override
	public boolean keyDown(int keycode) {
		Movement m = Movement.None;
		if (keycode == Input.Keys.W)
			m = Movement.Up;
		else if (keycode == Input.Keys.S)
			m = Movement.Down;
		else if (keycode == Input.Keys.A)
			m = Movement.Left;
		else if (keycode == Input.Keys.D)
			m = Movement.Right;

		if (m != Movement.None) {
			player.setMovement(m);
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
			player.setMovement(Movement.None);
			movementSetByMouse = false;
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
