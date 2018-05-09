package net.cserny.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;

public class FlapeeBeeGame extends Game {

	private final AssetManager assetManager = new AssetManager();

	@Override
	public void create() {
		setScreen(new StartScreen(this));
	}

	public AssetManager getAssetManager() {
		return assetManager;
	}
}
