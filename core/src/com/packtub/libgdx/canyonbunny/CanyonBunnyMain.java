package com.packtub.libgdx.canyonbunny;

import com.badlogic.gdx.ApplicationListener;
import com.packtub.libgdx.canyonbunny.game.WorldController;
import com.packtub.libgdx.canyonbunny.game.WorldRenderer;

public class CanyonBunnyMain implements ApplicationListener
{
	private static final String TAG =		
			CanyonBunnyMain.class.getName();
	
	private WorldController worldController;
	private WorldRenderer worldRenderer;
	
	@Override public void create () { }
	@Override public void render () { }
	@Override public void resize (int width, int height) { }
	@Override public void pause () { }
	@Override public void resume () { }
	@Override public void dispose () { }
}
