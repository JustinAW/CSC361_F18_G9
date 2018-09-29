package com.packtpub.libgdx.canyonbunny.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.packtpub.libgdx.canyonbunny.game.Assets;

/**
 * @author Justin Study ch 7
 *
 */
public class AbstractGameScreen implements Screen
{
	protected Game game;
	
	public AbstractGameScreen(Gane game)
	{
		this.game = game;
	}
	
	public abstract void render(float deltaTime);
	public abstract void resize(int width, int height);
	public abstract void show ();
	public abstract void hide ();
	public abstract void pause();
	
	public void resume() 
	{
		Assets.instance.init(new AssetManager());
	}
	
	public void dispose()
	{
		Assets.instance.dispose();
	}
}
