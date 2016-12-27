package com.example.hackathon.core.model;

/**
 * Created by Laci on 27.12.16.
 */
public class ClickButton extends InteractionElement{
	public boolean isActivated;

    public ClickButton(boolean initState) {
    	isActivated = initState;
    }

    @Override
	public void interact() {
    	isActivated = !isActivated;
    }
}
