package com.example.hackathon.core.model;

class Upgrade {
	// how much the Battery Capacity increases/decreases
	private final float batteryCapacityChange;
	// how much Battery is consumed/produced by the Upgrade
	private final float batteryConsumptionChange;

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
