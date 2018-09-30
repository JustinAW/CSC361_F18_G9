package com.packtpub.libgdx.canyonbunny.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.math.MathUtils;

/**
 * @author Justin Study ch. 7
 * implemented as singletion
 * holds and applies users preferences
 */
public class GamePreferences
{
	public static final String TAG = GamePreferences.class.getName();
	
	public static final GamePreferences instance = new GamePreferences();
	
	public boolean sound;
	public boolean music;
	public float volSound;
	public float volMusic;
	public int charSkin;
	public boolean showFpsCounter;
	
	private Preferences prefs;
	
	//singleton: prevent instantiation from other classes
	private GamePreferences() 
	{
		//settings will be loaded and saved in preferences file
		prefs = Gdx.app.getPreferences(Constants.PREFERENCES);
	}
	
	//always tries to find suitable and valid value for settings
	public void load() 
	{
		sound = prefs.getBoolean("sound", true);
		music = prefs.getBoolean("music", true);
		volSound = MathUtils.clamp(prefs.getFloat("volSound", 0.5f), 0.0f, 1.0f);
		volMusic = MathUtils.clamp(prefs.getFloat("volMusic", 0.5f), 0.0f, 1.0f);
		charSkin = MathUtils.clamp(prefs.getInteger("charSkin", 0), 0, 2);
		showFpsCounter = prefs.getBoolean("showFpsCounter", false);
	}
	
	//takes current values of its public variables and puts them into the map of
	//the preferences file. flush is called on preferences file to actually write 
	//changed values into the file
	public void save() 
	{
		prefs.putBoolean("sound", sound);
		prefs.putBoolean("music", music);
		prefs.putFloat("volSound", volSound);
		prefs.putFloat("volMusic", volMusic);
		prefs.putInteger("charSkin", charSkin);
		prefs.putBoolean("showFpsCounter", showFpsCounter);
		prefs.flush();
	}
}
