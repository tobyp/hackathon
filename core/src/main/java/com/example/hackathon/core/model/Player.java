package com.example.hackathon.core.model;

import com.badlogic.gdx.math.Vector2;
import com.example.hackathon.core.HackathonGame;

import java.util.ArrayList;
import java.util.List;

public class Player extends Robot {
	// how much energy is still left in the battery, between 0 and batteryMax
	private int battery;
	// how much energy the battery can hold
	private int batteryMax;
	// how much battery is consumed per timestep
	private int consumption;

	private Vector2 target;

	private List<Upgrade> upgrades;

	public Player() {
		batteryMax = 10;
		battery = batteryMax;
		consumption = 1;
		upgrades = new ArrayList<>();
	}

	public void addUpgrade(Upgrade u) {
		upgrades.add(u);
		recalculateStats();
	}

	/**
	 * Updates batteryMax and consumption (for new upgrades)
	 */
	private void recalculateStats() {
		for (Upgrade u : upgrades) {
			setBatteryMax(batteryMax + u.batteryCapacityChange);
			setConsumption(consumption + u.batteryConsumptionChange);
		}
	}

	public int getBattery() {
		return battery;
	}

	public void setBattery(int battery) {
		if (battery < 0) {
			this.battery = 0;
			HackathonGame.isGameOver = true;
		} else if (battery > batteryMax) {
			this.battery = batteryMax;
		} else {
			this.battery = battery;
		}

	}

	public int getBatteryMax() {
		return batteryMax;
	}

	public void setBatteryMax(int batteryMax) {
		if (batteryMax <= 0) {
			HackathonGame.isGameOver = true;
		}
		this.batteryMax = batteryMax;
	}

	public int getConsumption() {
		return consumption;
	}

	public void setConsumption(int consumption) {
		this.consumption = consumption;
	}

	public Vector2 getTarget() {
		return target;
	}

	public void setTarget(Vector2 v) {
		target = v;
	}

	public void update(float deltaTime) {
		// Consume battery
		setBattery(battery - (int) (consumption * deltaTime));
		// Walk to the target
		if (target != null) {
			// Target - Player
			Vector2 diff = location.cpy().sub(target.x, target.y);
			// Clamp the velocity
			diff.clamp(1, 1.5f);
			setVelocity(diff);
		}
	}
}
