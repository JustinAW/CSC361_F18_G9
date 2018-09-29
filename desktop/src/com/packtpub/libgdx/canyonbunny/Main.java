package com.packtpub.libgdx.canyonbunny;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.badlogic.gdx.tools.texturepacker.TexturePacker.Settings;
import com.packtpub.libgdx.canyonbunny.CanyonBunnyMain;

/**
 * Launches the game on desktop
 * @author Justin Study Chap 4 9/17/18
 * @edit Justin Study ch.7
 */
public class Main 
{
	private static boolean rebuildAtlas = false;
	private static boolean drawDebugOutline = true;
	
	public static void main (String[] args) 
	{   
		//rebuilds texture atlas every time the game is run on desktop
		if(rebuildAtlas)
		{
			Settings settings = new Settings();
			settings.maxWidth = 1024;
			settings.maxHeight = 1024;
			settings.debug = drawDebugOutline;
			TexturePacker.process(settings, "assets-raw/images", "../core/assets/images",
					"canyonbunny.pack");
			TexturePacker.process(settings, "assets-raw/images-ui", "../core/assets/images", 
					"canyonbunny-ui.pack");
		}
		
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "CanyonBunny";
		config.width = 800;
		config.height = 480;
		
		new LwjglApplication(new CanyonBunnyMain(), config);
	}
}
