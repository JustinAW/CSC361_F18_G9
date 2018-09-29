package com.packtpub.libgdx.canyonbunny;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.packtpub.libgdx.canyonbunny.game.Assets;
import com.packtpub.libgdx.canyonbunny.screens.MenuScreen;
/**
 * @author Justin Study ch. 7
 * deleted old code and replaced with just the create method
 */
public class CanyonBunnyMain extends Game
{
	//platform independent entry point for the game. LibGdx instructed to through 
	//setScreen() method by Game class to change current screen. call menuscreen to 
	//start the game
	@Override
	public void create()
	{
		//Set Libgdx log level
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		//Load Assets
		Assets.instance.init(new AssetManager());
		//Start game at menu screen
		setScreen(new MenuScreen(this));
	}
}

