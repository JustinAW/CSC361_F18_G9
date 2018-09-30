package com.packtpub.libgdx.canyonbunny.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.packtpub.libgdx.canyonbunny.util.Constants;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.packtpub.libgdx.canyonbunny.util.GamePreferences;

/**
 * Draws the game
 * @author Justin Study ch 5 9/17/18
 * 		   Justin Study ch 6
 */
public class WorldRenderer implements Disposable
{
	private OrthographicCamera camera;
	private OrthographicCamera cameraGUI;
	private SpriteBatch batch;
	private WorldController worldController;
	
	//initializes an instance of worldrenderer
	public WorldRenderer (WorldController worldController) 
	{
		this.worldController = worldController;
		init();
	}
	
	//sets up cameras to view the game world
	private void init() 
	{
		batch = new SpriteBatch();
		camera = new OrthographicCamera(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
		camera.position.set(0, 0, 0);
		camera.update();
		
		//set up gui camera
		cameraGUI = new OrthographicCamera(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
		cameraGUI.position.set(0,0,0);
		cameraGUI.setToOrtho(true); //flip y-axis
		cameraGUI.update();
	}
	//calls method that draws on screen
	public void render() 
	{
		renderWorld(batch);
		renderGui(batch);
	}
	
	//render world and objects within the world
	private void renderWorld(SpriteBatch batch)
	{
		worldController.cameraHelper.applyTo(camera);
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		worldController.level.render(batch);
		batch.end();
	}
	
	//renders gui and calls other methods that render specific parts of the gui like fps counter and coin amount
	private void renderGui(SpriteBatch batch)
	{
		batch.setProjectionMatrix(cameraGUI.combined);
		batch.begin();
		//draw collected gold coins icon + text to top left edge
		renderGuiScore(batch);
		
		//draw collected feather icon anchored to top left edge
		renderGuiFeatherPowerup(batch);
		
		//draw extra lives icon + text on top right
		renderGuiExtraLive(batch);
		
		//draw FPS text on bottom right only if box is checked by user.
		if(GamePreferences.instance.showFpsCounter)
		{	
			renderGuiFpsCounter(batch);
		}
		
		//draw game over text
		renderGuiGameOverMessage(batch);
		
		batch.end();
	}
	
	//draws score in top left
	private void renderGuiScore (SpriteBatch batch)
	{
		float x = -15;
		float y = -15;
		batch.draw(Assets.instance.goldCoin.goldCoin, x, y, 50, 50, 100, 100, 0.35f, -0.35f, 0);
		Assets.instance.fonts.defaultBig.draw(batch, "" + worldController.score, x + 75, y +37);
	}
	
	//draws lives indicator in top right
	private void renderGuiExtraLive(SpriteBatch batch)
	{
		float x = cameraGUI.viewportWidth - 50 - Constants.LIVES_START * 50;
		float y = -15;
		for(int i = 0; i < Constants.LIVES_START; i++)
		{
			if(worldController.lives <= i)
				batch.setColor(0.5f, 0.5f, 0.5f, 0.5f);
			
			batch.draw(Assets.instance.bunny.head, x+i*50, y, 50, 50, 120, 100, 0.35f, -0.35f, 0);
			batch.setColor(1,1,1,1);
		}
	}
	
	//draws fps counter in bottom right
	private void renderGuiFpsCounter (SpriteBatch batch)
	{
		float x = cameraGUI.viewportWidth - 55;
		float y = cameraGUI.viewportHeight -15;
		int fps = Gdx.graphics.getFramesPerSecond();
		BitmapFont fpsFont = Assets.instance.fonts.defaultNormal;
		if(fps >= 45)
		{
			//45 or more fps show in green
			fpsFont.setColor(0,1,0,1);
		}
		if(fps >= 30)
		{
			//30 or more fps show in yello
			fpsFont.setColor(1,0,0,1);
		}
		else
		{
			//less than 30 shows in red
			fpsFont.setColor(1,0,0,1);
		}
		
		fpsFont.draw(batch, "FPS: " + fps, x, y);
		fpsFont.setColor(1,1,1,1); //white
	}
	
	//adds game over text and feather icon to the game
	private void renderGuiGameOverMessage(SpriteBatch batch)
	{
		//calculates center of gui camera viewport. 
		float x = cameraGUI.viewportWidth/2;
		float y = cameraGUI.viewportHeight/2;
		if(worldController.isGameOver())
		{
			BitmapFont fontGameOver = Assets.instance.fonts.defaultBig;
			//used to draw font
			fontGameOver.draw(batch, "GAME OVER", x, y, 1, Align.center, false);
			//change color
			fontGameOver.setColor(1,1,1,1);
		}
	}
	
	//draws feather power up. checks if time is still left on power up. if so, draw feather
	//in upper left corner. small number with time left is drawn next to it. blinks if 
	//less than 4 seconds left
	private void renderGuiFeatherPowerup (SpriteBatch batch)
	{
		float x = -15;
		float y = 30;
		float timeLeftFeatherPowerup = worldController.level.bunnyHead.timeLeftFeatherPowerup;
		
		if(timeLeftFeatherPowerup > 0)
		{
			//start icon fade in/out if time less than 4 seconds. 5 changes per second
			if(timeLeftFeatherPowerup < 4)
			{
				if((((int)timeLeftFeatherPowerup * 5) % 2) != 0)
				{
					batch.setColor(1, 1, 1, 0.5f);
				}
			}
			batch.draw(Assets.instance.feather.feather, x, y, 50, 50, 100, 100,
											0.35f, 0.35f, 0);
			batch.setColor(1,1,1,1);
			Assets.instance.fonts.defaultSmall.draw(batch, ""+ (int)timeLeftFeatherPowerup, x+60, y+57);
		}
	}
	
	//handles resizing of the game window
	public void resize(int width, int height) 
	{
		camera.viewportWidth = (Constants.VIEWPORT_HEIGHT/height) * width;
		camera.update();
		
		//resize gui camera
		cameraGUI.viewportHeight = Constants.VIEWPORT_GUI_HEIGHT;
		cameraGUI.viewportWidth = (Constants.VIEWPORT_GUI_HEIGHT/(float)height) * (float)width;
		cameraGUI.position.set(cameraGUI.viewportWidth/2, cameraGUI.viewportHeight/2, 0);
		cameraGUI.update();
	}
	
	//clears unused memory
	@Override public void dispose() 
	{
		batch.dispose();
	}
}
