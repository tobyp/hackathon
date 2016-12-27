package com.example.hackathon.core.model;

import com.badlogic.gdx.math.Vector2;

public class ClickButton extends InteractionElement{
	public boolean isActivated;

    public ClickButton(Vector2 loc, boolean initState) {
    	isActivated = initState;
		location = loc;
    }

    @Override
	public void interact() {
    	isActivated = !isActivated;
    }
}
