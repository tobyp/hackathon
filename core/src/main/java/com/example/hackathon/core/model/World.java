package com.example.hackathon.core.model;

import java.util.ArrayList;
import java.util.List;

public class World {
	private List<InteractionElement> interactionElements;

	private Player player;

	public static final float INTERACTION_RADIUS = 1;

	public World() {
		interactionElements = new ArrayList<InteractionElement>();
		player = new Player();
	}

	// checks, if there are any interactionElements close to the player & calls the respective interact methods
	public void checkForInteraction() {
		interactionElements.stream().filter((InteractionElement e) -> e.location.dst(player.getLocation()) <= INTERACTION_RADIUS).forEach((InteractionElement::interact));
	}

	public void addIntercationElement(InteractionElement ie) {
		interactionElements.add(ie);
	}
}
