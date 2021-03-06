/**
 * @author Justin Study ch 5 9/17/18
 * @edits
 * 		Justin Study ch 6
 * 		Justin Study ch 11
 * 		Justin Weigle 23-Oct-18
 */

package com.packtpub.libgdx.canyonbunny.game.objects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * Class stores position dimension origin scale factor and angle of rotation 
 * of a game object.
 */
public abstract class AbstractGameObject 
{
	//instance variables to keep track of objects
	public Vector2 position;
	public Vector2 dimension;
	public Vector2 origin;
	public Vector2 scale;
	public float rotation;
	
	//physics variables for object
	public Vector2 velocity;
	public Vector2 terminalVelocity;
	public Vector2 friction;
	public Vector2 acceleration;
	public Rectangle bounds;
	
	public Body body;
	
	// animation variables
	public float stateTime;
	public Animation animation;
	
	/**
	 * creates a new object and places it in the screen
	 */
	public AbstractGameObject() 
	{
		position = new Vector2();
		dimension = new Vector2(1,1);
		origin = new Vector2();
		scale = new Vector2(1,1);
		rotation = 0;
		velocity = new Vector2();
		terminalVelocity = new Vector2(1,1);
		friction = new Vector2();
		acceleration = new Vector2();
		bounds = new Rectangle();
	}
	
	/**
	 * Sets the current animation and resets stateTime to 0
	 */
	public void setAnimation (Animation animation)
	{
		this.animation = animation;
		stateTime = 0;
	}
	
	/*
	 * called on every update cycle to calculate the next x component of the objects
	 * velocity in terms of given delta time
	 */
	 protected void updateMotionX (float deltaTime) 
	 {
		 if(velocity.x != 0)
		 {
			 //Apply friction
			 if(velocity.x > 0)
			 {
				 velocity.x = Math.max(velocity.x - friction.x * deltaTime, 0);
			 } else
			 {
				 velocity.x = Math.min(velocity.x + friction.x * deltaTime, 0);
			 }
		 }
		 
		 //apply acceleration
		 velocity.x += acceleration.x * deltaTime;
		 //make sure object's velocity does not exceed the 
		 //positive or negative terminal velocity
		 velocity.x = MathUtils.clamp(velocity.x, -terminalVelocity.x, terminalVelocity.x);
	 }
	
	/* 
	 * called on every update cycle to calculate the next y component of the objects
	 * velocity in terms of given delta time
	 */
	protected void updateMotionY (float deltaTime)
	{
		if(velocity.y != 0)
		{
			//apply friction
			if(velocity.y > 0)
			{
				velocity.y = Math.max(velocity.y -friction.y * deltaTime, 0);
				}else
				{
				velocity.y = Math.min(velocity.y + friction.y * deltaTime, 0);
			}
		}
		//apply acceleration
		velocity.y += acceleration.y * deltaTime;
		//Make sure the object's velocity does not exceed the
		//positive or negative terminal velocity
		velocity.y = MathUtils.clamp(velocity.y, -terminalVelocity.y, terminalVelocity.y);
	}
	 
	/*
	 * called inside world controller
	 */
	public void update(float deltaTime)
	{
		stateTime += deltaTime;
		
		if(body == null)
		{		
			updateMotionX(deltaTime);
			updateMotionY(deltaTime);
			//Move to new position
			position.x += velocity.x * deltaTime;
			position.y += velocity.y * deltaTime;
		} else {
			position.set(body.getPosition());
			rotation = body.getAngle() * MathUtils.radiansToDegrees;
		}
	}
	
	/*
	 * called inside render
	 */
	public abstract void render(SpriteBatch batch);
}
