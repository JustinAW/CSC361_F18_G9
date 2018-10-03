/*
 * Feather object that must be walked through by the player
 * to collect it. If collected it will turn invisible for the
 * rest of the game and award the player 250 points.
 * 
 * @author Justin Weigle 23-Sept-18
 */

package com.packtpub.libgdx.canyonbunny.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.packtpub.libgdx.canyonbunny.game.Assets;

public class Feather extends AbstractGameObject
{
	private TextureRegion regFeather;
	
	public boolean collected;
	
	public Feather()
	{
		init();
	}
	
	/*
	 *  initializes feather as not collected
	 */
	private void init ()
	{
		dimension.set(0.5f, 0.5f);
		
		regFeather = Assets.instance.feather.feather;
		
		// Set bounding box for collision detection
		bounds.set(0, 0, dimension.x, dimension.y);
		
		collected = false;
	}
	
	/*
	 * if the feather has not been collected, will be rendered on the screen
	 */
	public void render (SpriteBatch batch)
	{
		if (collected) return;
		
		TextureRegion reg = null;
		reg = regFeather;
		batch.draw(reg.getTexture(),
				position.x, position.y,
				origin.x, origin.y,
				dimension.x, dimension.y,
				scale.x, scale.y,
				rotation,
				reg.getRegionX(), reg.getRegionY(),
				reg.getRegionWidth(), reg.getRegionHeight(),
				false, false);
	}
	
	public int getScore()
	{
		return 250;
	}
}
