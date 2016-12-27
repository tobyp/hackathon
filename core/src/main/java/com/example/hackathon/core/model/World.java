package com.example.hackathon.core.model;

import java.util.ArrayList;
import java.util.List;

public class World {
	private List<InteractionElement> interactionElements = new ArrayList<>();

	private Player player = new Player();

	public static final float INTERACTION_RADIUS = 1;

	public World() {
	}

	public Player getPlayer() {
		return player;
	}

	// checks, if there are any interactionElements close to the player & calls the respective interact methods
	public void checkForInteraction() {
		interactionElements.stream().filter((InteractionElement e) -> e.location.dst(player.getLocation()) <= INTERACTION_RADIUS).forEach((InteractionElement::interact));
	}

	public void addIntercationElement(InteractionElement ie) {
		interactionElements.add(ie);
	}
}
