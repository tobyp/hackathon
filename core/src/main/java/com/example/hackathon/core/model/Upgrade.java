package com.example.hackathon.core.model;

import java.util.logging.Logger;

public class Upgrade {
	// how much the Battery Capacity increases/decreases
	private float batteryCapacityChange;
	// how much Battery is consumed/produced by the Upgrade
	private float batteryConsumptionChange;

	public Upgrade(float capacityChange, float consumptionChange)
	{
		batteryCapacityChange = capacityChange;
		batteryConsumptionChange = consumptionChange;
	}

	public void apply(Player player) {
		player.setBatteryMax(player.getBatteryMax() + batteryCapacityChange);
		player.setConsumption(player.getConsumption() + batteryConsumptionChange);
	}
}
