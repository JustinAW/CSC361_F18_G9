package com.packtpub.libgdx.canyonbunny.util;

/**
 * @author Justin Study Ch.4 9/17/18
 * Justin Study ch 5 9/17/18
 * Justin Study ch 6
 * 
 * @edits
 * 		Justin Weigle 23-Sept-18
 * 		Justin Study ch. 7
 */
public class Constants
{
    //Visible  game  world  is  5  meters  wide
	public  static  final  float  VIEWPORT_WIDTH  =  5.0f;
	//Visible  game  world  is  5  meters  tall
	public  static  final  float  VIEWPORT_HEIGHT  =  5.0f;
	
	//GUI Width
	public static final float VIEWPORT_GUI_WIDTH = 800.0f;
	//GUI Height
	public static final float VIEWPORT_GUI_HEIGHT = 480.0f;
	
	//Location of description file for texture atlas
	public static final String TEXTURE_ATLAS_OBJECTS = "images/canyonbunny.pack.atlas";
	
	// Location of image file for level 01
	public static final String LEVEL_01 = "levels/level-01.png";
	
	//Amount of extra lives at level start
	public static final int LIVES_START = 3; 
	
	//duration of feather power-up in seconds
	public static final float ITEM_FEATHER_POWERUP_DURATION = 9;
	
	//delay after game over to post message and restart game
	public static final float TIME_DELAY_GAME_OVER = 3;
	
	//texture atlas for UI location
	public static final String TEXTURE_ATLAS_UI = "images/canyonbunny-ui.pack";
	public static final String TEXTURE_ATLAS_LIBGDX_UI = "images/uiskin.atlas";
	
	//location of description file for skins
	public static final String SKIN_LIBGDX_UI = "images/uiskin.json";
	public static final String SKIN_CANYONBUNNY_UI = "images/canyonbunny-ui.json";
}
