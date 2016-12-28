package com.example.hackathon.core.model;

public class Upgrade {
	// how much the Battery Capacity increases/decreases
	public int batteryCapacityChange;
	// how much Battery is consumed/produced by the Upgrade
	public int batteryConsumptionChange;

	public Upgrade(int capacityChange, int consumptionChange)
	{
		batteryCapacityChange = capacityChange;
		batteryCapacityChange = consumptionChange;
	}
}
