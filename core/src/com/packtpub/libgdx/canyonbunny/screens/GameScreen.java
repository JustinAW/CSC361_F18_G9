/**
 * @author Justin Study ch 7
 */

package com.packtpub.libgdx.canyonbunny.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.packtpub.libgdx.canyonbunny.game.WorldController;
import com.packtpub.libgdx.canyonbunny.game.WorldRenderer;
import com.packtpub.libgdx.canyonbunny.util.GamePreferences;
import com.packtpub.libgdx.canyonbunny.util.CharacterSkin;
import com.packtpub.libgdx.canyonbunny.util.GamePreferences;

/**
 * displays the game screen to the user
 */
public class GameScreen extends AbstractGameScreen
{
	private static final String TAG = GameScreen.class.getName();
	private WorldController worldController;
	private WorldRenderer worldRenderer;
	
	private boolean paused;
	
	public GameScreen(Game game)
	{
		super(game);
	}
	
	/**
	 * render method to draw game world to the screen
	 */
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
	
	/**
	 * code that was in create() of canyonbunnymain to accomodate screen interface
	 */
	@Override
	public void show()
	{
		GamePreferences.instance.load();
		worldController = new WorldController(game);
		worldRenderer = new WorldRenderer(worldController);
		Gdx.input.setCatchBackKey(true);
	}
	
	/**
	 * code that was in dispos() of canyonbunnymain to accomodate screen interface
	 */
	@Override
	public void hide()
	{
		worldRenderer.dispose();
		Gdx.input.setCatchBackKey(false);
	}
	
	/**
	 * pause and resume for switching between applications
	 */
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

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}
	
}



















