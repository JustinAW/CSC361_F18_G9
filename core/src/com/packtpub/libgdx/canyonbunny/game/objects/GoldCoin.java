/** 
 * @author Justin Weigle 23-Sept-18
 * @edits
 * 		Justin Weigle 23-Oct-18
 */

package com.packtpub.libgdx.canyonbunny.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.packtpub.libgdx.canyonbunny.game.Assets;

/**
 * Gold coin object that must be walked through by the player
 * to collect it. If collected it will turn invisible for the
 * rest of the game and award the player 100 points.
 */
public class GoldCoin extends AbstractGameObject
{
	private TextureRegion regGoldCoin;
	
	public boolean collected;
	
	public GoldCoin()
	{
		init();
	}
	
	/**
	 *  initializes coin as not collected
	 */
	private void init ()
	{
		dimension.set(0.5f, 0.5f);
		
		setAnimation(Assets.instance.goldCoin.animGoldCoin);
		stateTime = MathUtils.random(0.0f, 1.0f);
		
		// Set bounding box for collision detection
		bounds.set(0, 0, dimension.x, dimension.y);
		
		collected = false;
	}
	
	/**
	 * if the coin has not been collected, will be rendered on the screen
	 */
	public void render (SpriteBatch batch)
	{
		if (collected) return;
		
		TextureRegion reg = null;
		reg = (TextureRegion) animation.getKeyFrame(stateTime, true);
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
		return 100;
	}
}
