package com.packtpub.libgdx.canyonbunny.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import com.packtpub.libgdx.canyonbunny.util.Constants;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

/**
 * Draws the game
 * @author Justin Study ch 5 9/17/18
 */
public class WorldRenderer implements Disposable
{
	private OrthographicCamera camera;
	private OrthographicCamera cameraGUI;
	private SpriteBatch batch;
	private WorldController worldController;
	
	public WorldRenderer (WorldController worldController) 
	{
		this.worldController = worldController;
		init();
	}
	
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
	
	public void render() 
	{
		renderWorld(batch);
		renderGui(batch);
	}
	
	private void renderWorld(SpriteBatch batch)
	{
		worldController.cameraHelper.applyTo(camera);
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		worldController.level.render(batch);
		batch.end();
	}
	
	private void renderGUI(SpriteBatch batch)
	{
		batch.setProjectionMatrix(cameraGUI.combined);
		batch.begin();
		//draw collected gold coins icon + text to top left edge
		renderGuiScore(batch);
		
		//draw extra lives icon + text on top right
		renderGuiExtraLive(batch);
		
		//draw FPS text on bottom right
		renderGuiFpsCounter(batch);
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
	
	@Override public void dispose() 
	{
		batch.dispose();
	}
}
