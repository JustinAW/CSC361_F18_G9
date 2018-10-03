/*
 * Class that determines the size of Mountains. Also draws the mountains in the background and handles scrolling speed
 * 
 * @author Justin Weigle 16-Sept-18
 * @edits Justin Study ch. 8
 */

package com.packtpub.libgdx.canyonbunny.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.packtpub.libgdx.canyonbunny.game.Assets;
import com.badlogic.gdx.math.Vector2;

public class Mountains extends AbstractGameObject 
{
	private TextureRegion regMountainLeft;
	private TextureRegion regMountainRight;
	
	private int length;
	
	public Mountains (int length)
	{
		this.length = length;
		init();
	}
	
	/*
	 * method that sets mountains to be 10m wide by 2 meters high. imports texture from 
	 * Assets class. origin of mountains id offset in negative direction and width is doubled
	 */
	private void init ()
	{
		dimension.set(10, 2);
		
		regMountainLeft = Assets.instance.levelDecoration.mountainLeft;
		regMountainRight = Assets.instance.levelDecoration.mountainRight;
		
		// shift mountain and extend length
		origin.x = -dimension.x * 2;
		length += dimension.x * 2;
	}
	
	/*
	 * method that sets the position of the mountains in the background. Takes input from render() below and 
	 * applies calculations to shift and offset the mountains apart from each other to give appearance of depth. 
	 * parallaxSpeedX used to give depth illusion. objects in background will scroll slower than objects up close.
	 */
	private void drawMountain (SpriteBatch batch, float offsetX, float offsetY, float tintColor, float parallaxSpeedX)
	{
		TextureRegion reg = null;
		batch.setColor(tintColor, tintColor, tintColor, 1);
		float xRel = dimension.x * offsetX;
		float yRel = dimension.y * offsetY;
		
		// mountains span the whole level
		int mountainLength = 0;
		mountainLength += MathUtils.ceil(length / (2 * dimension.x) * (1 - parallaxSpeedX));
		mountainLength += MathUtils.ceil(0.5f + offsetX);
		for (int i = 0; i < mountainLength; i++)
		{
			// mountain left
			reg = regMountainLeft;
			batch.draw(reg.getTexture(),
					origin.x + xRel + position.x * parallaxSpeedX, 
					origin.y + yRel + position.y,
					origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y, rotation,
					reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(),
					false, false);
			xRel += dimension.x;
			
			// mountain right
			reg = regMountainRight;
			batch.draw(reg.getTexture(), 
					origin.x + xRel + position.x * parallaxSpeedX, 
					origin.y + yRel + position.y, 
					origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y, rotation,
					reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(),
					false, false);
			xRel += dimension.x;
		}
		// reset color to white
		batch.setColor(1, 1, 1, 1);
	}
	
	public void updateScrollPosition(Vector2 camPosition)
	{
		position.set(camPosition.x, position.y);
	}
	
	/*
	 * uses the previous method to draw the mountains to the screen. uses inputs sprite batch, offsetX for a 
	 * horizontal shift. uses offset Y for vertical shift, uses tintColor to change mountains from black to white,
	 * uses parallax speed to slow scrolling of distant mountains to give realism. 
	 */ 
	@Override
	public void render (SpriteBatch batch)
	{
		// 80% parallax speed distant mountains (dark gray)
		drawMountain(batch, 0.5f, 0.5f, 0.5f, 0.8f);
		// 50% parallax speed distant mountains (gray)
		drawMountain(batch, 0.25f, 0.25f, 0.7f, 0.5f);
		// 30& parallax speed distant mountains (light gray)
		drawMountain(batch, 0.0f, 0.0f, 0.9f, 0.3f);
	}
}
