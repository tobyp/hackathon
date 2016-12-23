package com.example.hackathon.html;

import com.example.hackathon.core.HackathonGame;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;

public class HackathonGameHtml extends GwtApplication {
	@Override
	public ApplicationListener getApplicationListener () {
		return new HackathonGame();
	}
	
	@Override
	public GwtApplicationConfiguration getConfig () {
		return new GwtApplicationConfiguration(480, 320);
	}
}
