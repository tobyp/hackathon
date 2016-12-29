package com.example.hackathon.core.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.example.hackathon.core.HackathonGame;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;
import java.util.Random;

public class Player extends DynamicEntity {
	private static final float PLAYER_SPEED = 1.f;
	// how much energy is still left in the battery, between 0 and batteryMax
	private float battery;
	// how much energy the battery can hold
	private float batteryMax;
	// how much battery is consumed per second
	private float consumption;

	private Vector2 target;

	private List<Upgrade> upgrades;

	private float soundStartTime = 0;
	private Sound collideSound = Gdx.audio.newSound(Gdx.files.internal("sound/donk.wav"));
	private Random rand = new Random();

	public Player(Vector2 location) {
		super(location, new Vector2(2, 2), new Vector2(2, 2), new Texture("tew.png"), 0, 0, 128, 128);
		batteryMax = 1.f;
		battery = batteryMax;
		consumption = 0.0f;
		upgrades = new ArrayList<>();
		size.set(2, 2);
	}

	public void addUpgrade(Upgrade u) {
		upgrades.add(u);
		recalculateStats();
	}

	// updates batteryMax and consumption (for new upgrades)
	private void recalculateStats() {
		float old_battery_max = getBatteryMax();
		batteryMax = 1.f;
		consumption = 0.0f;
		for (Upgrade u : upgrades) {
			u.apply(this);
		}
		float delta_battery_max = getBatteryMax() - old_battery_max;
		battery = Math.min(batteryMax, battery + delta_battery_max);
	}

	public float getBattery() {
		return battery;
	}

	public void setBattery(float battery) {
		this.battery = Math.max(0.f, Math.min(battery, batteryMax)); //die on update()
	}

	public float getBatteryMax() {
		return batteryMax;
	}

	public void setBatteryMax(float batteryMax) {
		this.batteryMax = Math.max(1.f, batteryMax);
		this.battery = Math.min(battery, this.batteryMax); //die on update()
	}

	public float getConsumption() {
		return consumption;
	}

	public void setConsumption(float consumption) {
		this.consumption = Math.max(0.f, consumption);
	}

	public Vector2 getTarget() {
		return target;
	}

	public void setTarget(Vector2 target) {
		this.target = target;
	}

	@Override
	public void setDirection(Vector2 v) {
		super.setDirection(v);
		// Change the sprite
		sprite.setRegionX(getDiscreteDirection().ordinal() * 128);
		sprite.setRegionWidth(128);
	}

	@Override
	public void update(World world, float deltaTime) {
		battery = Math.max(Math.min(batteryMax, battery + consumption * deltaTime), 0.f);
		if (battery <= 0.f) {
			HackathonGame.isGameOver = true;
		}
		setVelocity(target.sub(location).scl(PLAYER_SPEED));
	}

	@Override
	public void collide(World world, Entity entity) {
		super.collide(world, entity);
	}

	@Override
	public void collide(World world, Vector2 pos) {
		super.collide(world, pos);
		if (world.getWorldTime() - soundStartTime > rand.nextInt(15) / 10f) {
			soundStartTime = world.getWorldTime();
			collideSound.play();
		}
	}
}
