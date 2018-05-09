package net.cserny.game;

import com.badlogic.gdx.Game;

public class FlapeeBeeGame extends Game {

	@Override
	public void create() {
		setScreen(new StartScreen(this));
	}
}
