/*
 * Class responsible for loading levels
 * 
 * @author Justin Weigle 16-Sept-18
 */

package com.packtpub.libgdx.canyonbunny.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.packtpub.libgdx.canyonbunny.game.objects.AbstractGameObject;
import com.packtpub.libgdx.canyonbunny.game.objects.Clouds;
import com.packtpub.libgdx.canyonbunny.game.objects.Mountains;
import com.packtpub.libgdx.canyonbunny.game.objects.Rock;
import com.packtpub.libgdx.canyonbunny.game.objects.WaterOverlay;

public class Level 
{
	public static final String TAG = Level.class.getName();
	 
	public enum BLOCK_TYPE 
	{
		EMPTY(0, 0, 0), // black
	    ROCK(0, 255, 0), // green
	    PLAYER_SPAWNPOINT(255, 255, 255), // white
	    ITEM_FEATHER(255, 0, 255), // purple
	    GOAL(255, 0, 0), // red
	    ITEM_GOLD_COIN(255, 255, 0); // yellow

	    private int color;

	    BLOCK_TYPE(int r, int g, int b) 
	    {
	        color = r << 24 | g << 16 | b << 8 | 0xff;
	    }

	    public boolean sameColor(int color) 
	    {
	        return this.color == color;
	    }

	    public int getColor() 
	    {
	        return color;
	    }
	}
	
	// objects
	public Array<Rock> rocks;
	
	// decoration
	public Clouds clouds;
	public Mountains mountains;
	public WaterOverlay waterOverlay;
	
	public Level (String filename)
	{
		init(filename);
	}
	
	private void init (String filename) {}
	public void render (SpriteBatch batch) {}
}
}
