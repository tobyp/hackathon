package com.example.hackathon.core.model;

import com.badlogic.gdx.math.Vector2;
import com.example.hackathon.core.HackathonGame;

import java.util.ArrayList;
import java.util.List;

public class Player extends DynamicEntity {
	// how much energy is still left in the battery, between 0 and batteryMax
	private float battery;
	// how much energy the battery can hold
	private float batteryMax;
	// how much battery is consumed per timestep
	private float consumption;

	private Vector2 target;

	private List<Upgrade> upgrades;

	public Player() {
		batteryMax = 1.f;
		battery = batteryMax;
		consumption = 0.0f;
		upgrades = new ArrayList<Upgrade>();
		velocity = new Vector2(-0.5f,-0.5f);
	}

	public void addUpgrade(Upgrade u) {
		upgrades.add(u);
		recalculateStats();
	}

	// updates batteryMax and consumption (for new upgrades)
	private void recalculateStats() {
		batteryMax = 1.f;
		consumption = 0.0f;
		for (Upgrade u : upgrades) {
			u.apply(this);
		}
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
		this.consumption = consumption;
	}

	public Vector2 getTarget() {
		return target;
	}

	public void setTarget(Vector2 target) {
		this.target = target;
	}

	@Override
	public void update(float deltaTime) {
		battery -= consumption * deltaTime;
		if (battery <= 0.f) {
			HackathonGame.isGameOver = true;
		}
		velocity = target.sub(location).scl(0.2f);
	}
}
