/**
 * Handles input from user to control the game world
 * 
 * @author Justin Weigle 16-Sept-18
 * @edits
 * 		Justin Weigle 23-Sept-18
 *		Justin Weigle 25-Sept-18
 *      Justin Study ch. 7
 *      Justin Study ch. 10
 *      Justin Weigle 17-Oct-18
 *changes to utilize screens
 */

package com.packtpub.libgdx.canyonbunny.game;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.packtpub.libgdx.canyonbunny.game.objects.BunnyHead;
import com.packtpub.libgdx.canyonbunny.game.objects.BunnyHead.JUMP_STATE;
import com.packtpub.libgdx.canyonbunny.game.objects.Carrot;
import com.packtpub.libgdx.canyonbunny.game.objects.Feather;
import com.packtpub.libgdx.canyonbunny.game.objects.GoldCoin;
import com.packtpub.libgdx.canyonbunny.game.objects.Rock;
import com.packtpub.libgdx.canyonbunny.screens.MenuScreen;
import com.packtpub.libgdx.canyonbunny.util.AudioManager;
import com.packtpub.libgdx.canyonbunny.util.CameraHelper;
import com.packtpub.libgdx.canyonbunny.util.Constants;

public class WorldController extends InputAdapter
{
	/**
	 * allows us to save a reference to game instance. enables us to switch to 
	 * another screen
	 */
	private Game game;
	private static final String TAG = WorldController.class.getName();
	public CameraHelper cameraHelper;
	
	private float timeLeftGameOverDelay;
	
	public Level level;
	public int lives;
	public int score;
	//used for animations with score and lives on GUI
	public float livesVisual;
	public float scoreVisual;
	
	/**
	 * for initPhysics
	 */
	private boolean goalReached;
	public World b2world;
	
	
	/**
	 * constructs game instance and stores game variable
	 */
	public WorldController (Game game)
	{
		this.game = game;
		init();
	}
	
	private void init ()
	{
		Gdx.input.setInputProcessor(this);
		cameraHelper = new CameraHelper();
		lives = Constants.LIVES_START;
		livesVisual = lives;
		timeLeftGameOverDelay = 0;
		initLevel();
	}
	
	private void initLevel ()
	{
		score = 0;
		scoreVisual = score;
		goalReached = false;
		level = new Level(Constants.LEVEL_01);
		cameraHelper.setTarget(level.bunnyHead);
		initPhysics();
	}
	
	/**
	 * uses goalReached to keep track of whether or not the player has
	 * already reached the goal.
	 * Initializes the physics for a gravity of 9.81 meters per second squared,
	 * just like on earth
	 */
	private void initPhysics ()
	{
		if (b2world != null) b2world.dispose();
		b2world = new World(new Vector2(0, -9.81f), true);
		// Rocks
		Vector2 origin = new Vector2();
		for (Rock rock : level.rocks)
		{
			BodyDef bodyDef = new BodyDef();
			bodyDef.type = BodyType.KinematicBody;
			bodyDef.position.set(rock.position);
			Body body = b2world.createBody(bodyDef);
			rock.body = body;
			PolygonShape polygonShape = new PolygonShape();
			origin.x = rock.bounds.width / 2.0f;
			origin.y = rock.bounds.height / 2.0f;
			polygonShape.setAsBox(
					rock.bounds.width / 2.0f, rock.bounds.height / 2.0f, 
					origin, 0);
			FixtureDef fixtureDef = new FixtureDef();
			fixtureDef.shape = polygonShape;
			body.createFixture(fixtureDef);
			polygonShape.dispose();
		}
	}
	
	/**
	 * creates a variable (numCarrots) number of carrots at a location (pos)
	 * in the game world and distributed in a certain area (radius) around
	 * that center
	 * @param pos
	 * @param numCarrots
	 * @param radius
	 */
	private void spawnCarrots (Vector2 pos, int numCarrots, float radius)
	{
		float carrotShapeScale = 0.5f;
		// create carrots with box2d body and fixture
		for (int i = 0; i < numCarrots; i++)
		{
			Carrot carrot = new Carrot();
			//calculate random spawn position, rotation, and scale
			float x = MathUtils.random(-radius, radius);
			float y = MathUtils.random(5.0f, 15.0f);
			float rotation = MathUtils.random(0.0f, 360.0f) *
					MathUtils.degreesToRadians;
			float carrotScale = MathUtils.random(0.5f, 1.5f);
			carrot.scale.set(carrotScale, carrotScale);
			// create box2d body for carrot with start position
			// and angle of rotation
			BodyDef bodyDef = new BodyDef();
			bodyDef.position.set(pos);
			bodyDef.position.add(x, y);
			bodyDef.angle = rotation;
			Body body = b2world.createBody(bodyDef);
			body.setType(BodyType.DynamicBody);
			carrot.body = body;
			// create rectangular shape for carrot to allow
			// interactions (collisions) with other objects;
			PolygonShape polygonShape = new PolygonShape();
			float halfWidth = carrot.bounds.width / 2.0f * carrotScale;
			float halfHeight = carrot.bounds.height / 2.0f * carrotScale;
			polygonShape.setAsBox(halfWidth * carrotShapeScale,
					halfHeight * carrotShapeScale);
			// set physics attributes
			FixtureDef fixtureDef = new FixtureDef();
			fixtureDef.shape = polygonShape;
			fixtureDef.density = 50;
			fixtureDef.restitution = 0.5f;
			fixtureDef.friction = 0.5f;
			body.createFixture(fixtureDef);
			polygonShape.dispose();
			// finally, add new carrot to list for updating/rendering
			level.carrots.add(carrot);
		}
	}
	
	/**
	 * draws squares for testing game code
	 * no longer used
	 */
	private Pixmap createProceduralPixmap (int width, int height)
	{
		Pixmap pixmap = new Pixmap(width, height, Format.RGBA8888);
		// Fill square with red color at 50% opacity
		pixmap.setColor(1, 0, 0, 0.5f);
		pixmap.fill();
		// Draw a yellow-colored X shape on square
		pixmap.setColor(1, 1, 0, 1);
		pixmap.drawLine(0, 0, width, height);
		pixmap.drawLine(width, 0, 0, height);
		// Draw a cyan-colored border around square
		pixmap.setColor(0, 1, 1, 1);
		pixmap.drawRectangle(0, 0, width, height);
		return pixmap;
	}
	
	public void update (float deltaTime)
	{
		handleDebugInput(deltaTime);
		if (isGameOver())
		{
			timeLeftGameOverDelay -= deltaTime;
			//replaced init with back to menu method to return to menu
			//instead of creating new game world
			if (timeLeftGameOverDelay < 0) backToMenu();
		} else {
			handleInputGame(deltaTime);
		}
		
		level.update(deltaTime);
		testCollisions(deltaTime);
		b2world.step(deltaTime,  8,  3);
		cameraHelper.update(deltaTime);
		
		if (!isGameOver() && isPlayerInWater())
		{
			AudioManager.instance.play(Assets.instance.sounds.liveLost);
			lives--;
		
		    if (isGameOver())
		    {
			    timeLeftGameOverDelay = Constants.TIME_DELAY_GAME_OVER;
		    } 
		    else 
		    {
			    initLevel();
		    }
		}
		
		//enables mountains to scroll at different speeds
		level.mountains.updateScrollPosition(cameraHelper.getPosition());
		//slowly decrements livesVisual so we can play an animation until it equals lives variable
		if(livesVisual > lives)
		{
			livesVisual = Math.max(lives,  livesVisual - 1 * deltaTime);
		}
		//slowly increments scoreVisual to play animation until it equals score
		if(scoreVisual < score)
		{
			scoreVisual = Math.min(score, scoreVisual + 250 * deltaTime);		
		}
	}
	
	/**
	 * Captures input from the keyboard to move the camera up, down, left, right
	 * and zoom in and out.
	 * @param deltaTime 
	 */
	private void handleDebugInput (float deltaTime)
	{
		if (Gdx.app.getType() != ApplicationType.Desktop) return;
		
		if (!cameraHelper.hasTarget(level.bunnyHead))
		{
		// Camera Controls (move)
			float camMoveSpeed = 5 * deltaTime;
			float camMoveSpeedAccelerationFactor = 5;
			if(Gdx.input.isKeyPressed(Keys.SHIFT_LEFT))camMoveSpeed *= camMoveSpeedAccelerationFactor;
			if(Gdx.input.isKeyPressed(Keys.LEFT)) moveCamera(-camMoveSpeed, 0);
			if(Gdx.input.isKeyPressed(Keys.RIGHT)) moveCamera(camMoveSpeed, 0);
			if(Gdx.input.isKeyPressed(Keys.UP)) moveCamera(0, camMoveSpeed);
			if(Gdx.input.isKeyPressed(Keys.DOWN)) moveCamera(0, -camMoveSpeed);
			if(Gdx.input.isKeyPressed(Keys.BACKSPACE)) cameraHelper.setPosition(0, 0);
		}
		
		// Camera Controls (zoom)
		float camZoomSpeed = 1 * deltaTime;
		float camZoomSpeedAccelerationFactor = 5;
		if(Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)) camZoomSpeed *= camZoomSpeedAccelerationFactor;
		if(Gdx.input.isKeyPressed(Keys.COMMA)) cameraHelper.addZoom(camZoomSpeed);
		if(Gdx.input.isKeyPressed(Keys.PERIOD)) cameraHelper.addZoom(-camZoomSpeed);
		if(Gdx.input.isKeyPressed(Keys.SLASH)) cameraHelper.setZoom(1);
	}
	
	/**
	 * moves the camera by a specified amount
	 * @param x horizontal movement of camera
	 * @param y vertical movement of camera
	 */
	private void moveCamera (float x, float y)
	{
		x += cameraHelper.getPosition().x;
		y += cameraHelper.getPosition().y;
		cameraHelper.setPosition(x,  y);
	}
	
	/**
	 * handles the bunny head main character moving left and right and dettects if 
	 * the character is jumping then changes the jump state
	 * @param deltaTime
	 */
	private void handleInputGame (float deltaTime)
	{
		if (cameraHelper.hasTarget(level.bunnyHead))
		{
			// Player movement
			if (Gdx.input.isKeyPressed(Keys.LEFT))
			{
				level.bunnyHead.velocity.x = -level.bunnyHead.terminalVelocity.x;
			}
			else if (Gdx.input.isKeyPressed(Keys.RIGHT))
			{
				level.bunnyHead.velocity.x = level.bunnyHead.terminalVelocity.x;
			} else {
				// Execute auto-forward movement on non-desktop platform
				if (Gdx.app.getType() != ApplicationType.Desktop)
				{
					level.bunnyHead.velocity.x = level.bunnyHead.terminalVelocity.x;
				}
			}
			
			//Bunny Jump
			if (Gdx.input.isTouched() || Gdx.input.isKeyPressed(Keys.SPACE))
			{
				level.bunnyHead.setJumping(true);
			} else {
				level.bunnyHead.setJumping(false);
			}
		}
	}
	
	/**
	 * checks how many lives the player has left
	 */
	public boolean isGameOver ()
	{
		return lives < 0;
	}
	
	/**
	 * checks if the bunny is in the water
	 */
	public boolean isPlayerInWater ()
	{
		return level.bunnyHead.position.y < -5;
	}
	
	/**
	 * handles what to do when the bunny collides with a rock
	 */
	private void onCollisionBunnyHeadWithRock(Rock rock) 
	{
		BunnyHead bunnyHead = level.bunnyHead;
		float heightDifference = Math.abs(bunnyHead.position.y 
				- (rock.position.y + rock.bounds.height));
		if (heightDifference > 0.25f)
		{
			boolean hitRightEdge = bunnyHead.position.x >
			(rock.position.x + rock.bounds.width / 2.0f);
			if(hitRightEdge)
			{
				bunnyHead.position.x = rock.position.x + rock.bounds.width;
			} else {
				bunnyHead.position.x = rock.position.x - bunnyHead.bounds.width;
			}
			return;
		}
		
		switch (bunnyHead.jumpState)
		{
		case GROUNDED:
			break;
		case FALLING:
		case JUMP_FALLING:
			bunnyHead.position.y = rock.position.y +
			bunnyHead.bounds.height + bunnyHead.origin.y;
			bunnyHead.jumpState = JUMP_STATE.GROUNDED;
			break;
		case JUMP_RISING:
			bunnyHead.position.y = rock.position.y +
			bunnyHead.bounds.height + bunnyHead.origin.y;
			break;
		}
	}
	
	/**
	 * handles what to do when the bunny collides with a gold coin
	 */
	private void onCollisionBunnyWithGoldCoin(GoldCoin goldCoin) 
	{
		goldCoin.collected = true;
		AudioManager.instance.play(Assets.instance.sounds.pickupCoin);
		score += goldCoin.getScore();
		Gdx.app.log(TAG,  "Gold coin collected");		
	}
	
	/**
	 * handles what to do when the bunny collides with a feather
	 */
	private void onCollisionBunnyWithFeather(Feather feather) 
	{
		feather.collected = true;
		AudioManager.instance.play(Assets.instance.sounds.pickupFeather);
		score += feather.getScore();
		level.bunnyHead.setFeatherPowerup(true);
		Gdx.app.log(TAG, "Feather collected");
	}
	
	/**
	 * handles what to do when the bunny collides with the goal
	 */
	private void onCollisionBunnyWithGoal ()
	{
		goalReached = true;
		timeLeftGameOverDelay = Constants.TIME_DELAY_GAME_FINISHED;
		Vector2 centerPosBunnyHead = 
				new Vector2(level.bunnyHead.position);
		centerPosBunnyHead.x += level.bunnyHead.bounds.width;
		spawnCarrots(centerPosBunnyHead, Constants.CARROTS_SPAWN_MAX, 
				Constants.CARROTS_SPAWN_RADIUS);
	}
	
	// Rectangles for collision detection
	private Rectangle r1 = new Rectangle();
	private Rectangle r2 = new Rectangle();
	
	/**
	 * tests collisions of all objects that can collide
	 */
	private void testCollisions (float deltaTime)
	{
		r1.set(level.bunnyHead.position.x, level.bunnyHead.position.y,
				level.bunnyHead.bounds.width, level.bunnyHead.bounds.height);
		
		// Test collision: Bunny Head <-> Rocks
		for (Rock rock : level.rocks)
		{
			r2.set(rock.position.x, rock.position.y,
					rock.bounds.width, rock.bounds.height);
			if (!r1.overlaps(r2)) continue;
			onCollisionBunnyHeadWithRock(rock);
			// IMPORTANT: must do all collisions for valid edge testing on rocks
		}
		
		// Test collision: Bunny Head <-> Gold Coins
		for (GoldCoin goldcoin : level.goldcoins)
		{
			if (goldcoin.collected) continue;
			r2.set(goldcoin.position.x, goldcoin.position.y,
					goldcoin.bounds.width, goldcoin.bounds.height);
			if (!r1.overlaps(r2)) continue;
			onCollisionBunnyWithGoldCoin(goldcoin);
			break;
		}
		
		// Test collision: Bunny Head <-> Feather
		for (Feather feather : level.feathers)
		{
			if (feather.collected) continue;
			r2.set(feather.position.x, feather.position.y,
					feather.bounds.width, feather.bounds.height);
			if (!r1.overlaps(r2)) continue;
			onCollisionBunnyWithFeather(feather);
			break;
		}
		
		// Test collision: Bunny Head <-> Goal
		if (!goalReached)
		{
			r2.set(level.goal.bounds);
			r2.x += level.goal.position.x;
			r2.y += level.goal.position.y;
			if (r1.overlaps(r2))
			{
				onCollisionBunnyWithGoal();
			}
		}
	}
	
	@Override
	public boolean keyUp (int keycode)
	{
		// Reset game world
		if (keycode == Keys.R)
		{
			init();
			Gdx.app.debug(TAG,  "Game world reset");
		}
		// Toggle camera follow
		else if (keycode == Keys.ENTER) 
		{
			cameraHelper.setTarget(cameraHelper.hasTarget() ? null: level.bunnyHead);
			Gdx.app.debug(TAG, "Camera follow enabled: " + cameraHelper.hasTarget());
		}
		//back to menu
		//goes back to menu if back is pressed on android or escape is pressed on PC
		else if(keycode == Keys.ESCAPE || keycode == Keys.BACK)
		{
			backToMenu();
		}
		return false;
	}
	
	/**
	 * new method to implement screens
	 * will switch to menu screen when called.
	 */
	private void backToMenu()
	{
		//switch to menu screen
		game.setScreen(new MenuScreen(game));
	}
	
}
