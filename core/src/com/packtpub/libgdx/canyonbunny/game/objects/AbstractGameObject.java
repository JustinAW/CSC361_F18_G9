package com.packtpub.libgdx.canyonbunny.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

/**
 * Class stores position dimension origin scale factor and angle of rotation 
 * of a game object.
 * 
 * @author Justin Study ch 5 9/17/18
 */
public abstract class AbstractGameObject 
{
	public Vector2 position;
	public Vector2 dimension;
	public Vector2 origin;
	public Vector2 scale;
	public float rotation;
	
	public AbstractGameObject() 
	{
		position = new Vector2();
		dimension = new Vector2(1,1);
		origin = new Vector2();
		scale = new Vector2(1,1);
		rotation = 0;
	}
	
	//called inside world controller
	//will do nothing right now
	public void update(float deltaTime)
	{
		
	}
	
	//called inside render
	public abstract void render(SpriteBatch batch);
}
