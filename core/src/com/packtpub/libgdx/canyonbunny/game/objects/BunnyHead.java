/*
 * @author Justin Study
 */

package com.packtpub.libgdx.canyonbunny.game.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.packtpub.libgdx.canyonbunny.game.Assets;
import com.packtpub.libgdx.canyonbunny.util.Constants;
import com.packtpub.libgdx.canyonbunny.util.CharacterSkin;
import com.packtpub.libgdx.canyonbunny.util.GamePreferences;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;

public class BunnyHead extends AbstractGameObject
{
	public static final String TAG = BunnyHead.class.getName();

	private final float JUMP_TIME_MAX = 0.3f;
	private final float JUMP_TIME_MIN = 0.1f;
	private final float JUMP_TIME_OFFSET_FLYING = JUMP_TIME_MAX -0.018f;
	
	public enum VIEW_DIRECTION{LEFT, RIGHT}
    public enum JUMP_STATE {GROUNDED, FALLING, JUMP_RISING, JUMP_FALLING}
    
    private TextureRegion regHead;
    
    public VIEW_DIRECTION viewDirection;
    public float timeJumping;
    public JUMP_STATE jumpState;
    public boolean hasFeatherPowerup;
    public float timeLeftFeatherPowerup;
    public ParticleEffect dustParticles = new ParticleEffect();
    
    public BunnyHead() 
    {
    	init();
    }
    
    /* 
     * initializes bunny head object by setting its physics values
     * deactivates the feather powerup
     */
    public void init() 
    {
    	dimension.set(1,1);
    	regHead = Assets.instance.bunny.head;
    	
    	//center image on game object
    	origin.set(dimension.x/2, dimension.y/2);
    	
    	//Building box for collision detection
    	bounds.set(0, 0, dimension.x, dimension.y);
    	
    	//set physics value
    	terminalVelocity.set(3.0f, 4.0f);
    	friction.set(12.0f, 0.0f);
    	acceleration.set(0.0f, -25.0f);
    	
    	//View direction
    	viewDirection = VIEW_DIRECTION.RIGHT;
    	
    	//jumpState
    	jumpState = JUMP_STATE.FALLING;
    	timeJumping = 0;
    	
    	//Power-ups
    	hasFeatherPowerup = false;
    	timeLeftFeatherPowerup = 0;
    	
    	//particles
    	dustParticles.load(Gdx.files.internal("particles/dust.pfx"), Gdx.files.internal("particles"));
    }
    
    /* 
     * allows us to make the bunny jump. jumpState will decide if its possible to 
     * jump or not or if its a single or multi jump
     */
    public void setJumping(boolean jumpKeyPressed) 
    {
    	switch (jumpState) 
    	{
    		case GROUNDED: //character is standing
    		  if(jumpKeyPressed)
    		  {
    			  //start counting jump time from the beginning
    			  timeJumping = 0;
    			  jumpState = JUMP_STATE.JUMP_RISING;
    		  }
    	 	  break;
    	 	  
    		case JUMP_RISING: //rising in the air
    		  if(!jumpKeyPressed)
    		  jumpState = JUMP_STATE.JUMP_FALLING;
    		  break;
    		  
    		case FALLING: //Falling down
    		case JUMP_FALLING: //Falling down after jump
    		  if(jumpKeyPressed && hasFeatherPowerup)
    		  {
    			  timeJumping = JUMP_TIME_OFFSET_FLYING;
    			  jumpState = JUMP_STATE.JUMP_RISING;
    		  }
    		  break;
    	}
    }
    
    /*
     * allows us to toggle feather powerup effect
     */
    public void setFeatherPowerup(boolean pickedUp) 
    {
    	hasFeatherPowerup = pickedUp;
    	if(pickedUp)
    	{
    		timeLeftFeatherPowerup = Constants.ITEM_FEATHER_POWERUP_DURATION;
    	}
    }
    
    /*
     * find out whether the powerup is still active
     */
    public boolean hasFeatherPowerup()
    {
    	return hasFeatherPowerup && timeLeftFeatherPowerup > 0;
    }
	
    /*
     *handles switching of view direction according to current move direction
     *remaining power up time is checked. If time is up its disabled
    */
	@Override
	public void update (float deltaTime)
	{
		super.update(deltaTime);
		if(velocity.x != 0)
		{
			viewDirection = velocity.x < 0 ? VIEW_DIRECTION.LEFT : VIEW_DIRECTION.RIGHT;
		}
		
		if(timeLeftFeatherPowerup > 0)
		{
			timeLeftFeatherPowerup -= deltaTime;
			if(timeLeftFeatherPowerup < 0)
			{
				//disable powerup
				timeLeftFeatherPowerup = 0;
				setFeatherPowerup(false);
			}
		}
		dustParticles.update(deltaTime);
	}
	
	/*
	 * handles calculations and switching of states that is needed to enable
	 * jumping and falling. also triggers particle effect
	 */
	@Override
	protected void updateMotionY(float deltaTime)
	{
		switch (jumpState)
		{
		  case GROUNDED:
			  jumpState = JUMP_STATE.FALLING;
			  break;
			  
		  case JUMP_RISING:
		  	  //keep track of jump time
		  	  timeJumping += deltaTime;
		  	  //jump time left?
		  	  if(timeJumping <= JUMP_TIME_MAX)
		  	  {
		  		  //still jumping
		  		  velocity.y = terminalVelocity.y;
		  	  }
		  	  break;
		  
		  case FALLING:
		    break;
		  case JUMP_FALLING:
		    //Add delta times to track jump time
		    timeJumping += deltaTime;
		    //jump to minimal height if jump key was pressed too short
		    if(timeJumping > 0 && timeJumping <= JUMP_TIME_MIN)
		    {
		    	//still jumping
		    	velocity.y = terminalVelocity.y;
		    }
		}
		if(jumpState != JUMP_STATE.GROUNDED)
		{
			dustParticles.allowCompletion();
			super.updateMotionY(deltaTime);
		}
	}
	
	/*
	 * handles drawing the bunny head to the screen. also draws the dust particles 
	 * that the bunny leaves. Changes colors of bunny if power up
	 */
	@Override
	public void render(SpriteBatch batch) 
	{
		TextureRegion reg = null;
		
		//Draw Particles
		dustParticles.draw(batch);
		
		//apply skin color
		batch.setColor(CharacterSkin.values()[GamePreferences.instance.charSkin].getColor());
		
		//set special color when game object has a feather powerup
		if(hasFeatherPowerup)
		{
			batch.setColor(1.0f, 0.8f, 0.0f, 1.0f);
		}
		//draw image
		reg = regHead;
		batch.draw(reg.getTexture(), position.x, position.y, origin.x, origin.y, 
				dimension.x, dimension.y, scale.x, scale.y, rotation, 
				reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(),
				reg.getRegionHeight(), viewDirection == VIEW_DIRECTION.LEFT, false);
		
		//reset color to white
		batch.setColor(1,1,1,1);
	}
}

























