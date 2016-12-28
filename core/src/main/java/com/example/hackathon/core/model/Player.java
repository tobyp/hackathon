package com.example.hackathon.core.model;

import com.badlogic.gdx.math.Vector2;

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
		upgrades = new ArrayList<Upgrade>();
	}

	public void addUpgrade(Upgrade u) {
		upgrades.add(u);
		recalculateStats();
	}

	// updates batteryMax and consumption (for new upgrades)
	private void recalculateStats() {
		for (Upgrade u : upgrades) {
			setBatteryMax(batteryMax + u.batteryCapacityChange);
			setConsumption(consumption + u.batteryConsumptionChange);
		}
	}

	/// should be called once per elapsed timestep, decreases battery according to consumption
	public void timeStep() {
		setBattery(battery - consumption);
	}

	public int getBattery() {
		return battery;
	}

	public void setBattery(int battery) {
		if (battery < 0) {
			this.battery = 0;
			// TODO dead?
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
			// TODO dead?
		}
		this.batteryMax = batteryMax;
	}

	public int getConsumption() {
		return consumption;
	}

	public void setConsumption(int consumption) {
		this.consumption = consumption;
	}

	public void update(float deltaTime) { }

	public Vector2 getTarget() {
		return target;
	}

	public void setTarget(Vector2 target) {
		this.target = target;
	}
}
