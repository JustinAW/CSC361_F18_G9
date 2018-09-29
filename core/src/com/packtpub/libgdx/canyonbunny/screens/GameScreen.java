package com.packtpub.libgdx.canyonbunny.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.packtpub.libgdx.canyonbunny.game.WorldController;
import com.packtpub.libgdx.canyonbunny.game.WorldRenderer;

/**
 * @author Justin Study ch 7
 *
 */
public class GameScreen extends AbstractGameScreen
{
	private static final String TAG = GameScreen.class.getName();
	private WorldController worldControler;
	private WorldRenderer worldRenderer;
	
	private boolean paused;
	
	public GameScreen(Game game)
	{
		super(game);
	}
	
	@Override
	public void render(float deltaTime)
	{
		if(!paused)
		{
			//update game world by the time that has passed 
			//since last rendered frame
			worldController.update(deltaTime);
		}
		//sets the clear screen color to cornflower blue
		Gdx.gl.glClearColor(0x64/255.0f,  0x95/255.0f,  0xed/255.0f, 0xff/255.0f);
		//clears the screen
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		//render game world to screen
		worldRenderer.render();
	}
	
	@Override
	public void show()
	{
		worldController = new WorldController(game);
		worldRenderer = new WorldRenderer(worldController);
		Gdx.input.setCatchBackKey(true);
	}
	
	@Override
	public void hide()
	{
		worldRenderer.dispose();
		Gdx.input.setCatchBackKey(false);
	}
	
	@Override
	public void pause()
	{
		paused = true;
	}
	
	@Override
	public void resume()
	{
		super.resume();
		//Only called on Android
		paused = false;
	}
	
}



















