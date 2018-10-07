/**
 * @author Justin Weigle 7-Oct-18
 */

package com.packtpub.libgdx.canyonbunny.util;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

/**
 * singleton class that allows playing music and sounds using overloading
 * to make some parameters optional
 */
public class AudioManager
{
	public static final AudioManager instance = new AudioManager();
	
	private Music playingMusic;
	
	// singleton: prevent instantiation from other classes
	private AudioManager() {}
	
	/**
	 * play sound
	 * @param sound
	 */
	public void play (Sound sound)
	{
		play(sound, 1);
	}
	
	/**
	 * play sound at volume
	 * @param sound
	 * @param volume
	 */
	public void play (Sound sound, float volume)
	{
		play(sound, volume, 1);
	}
	
	/**
	 * play sound at volume and pitch
	 * @param sound
	 * @param volume
	 * @param pitch
	 */
	public void play (Sound sound, float volume, float pitch)
	{
		play(sound, volume, pitch, 0);
	}
	
	/**
	 * play sound at volume and pitch panned left or right
	 * @param sound
	 * @param volume
	 * @param pitch
	 * @param pan
	 */
	public void play (Sound sound, float volume, float pitch, float pan)
	{
		if (!GamePreferences.instance.sound) return;
		sound.play(GamePreferences.instance.volSound * volume, pitch, pan);
	}
	
	/**
	 * play music
	 * @param music
	 */
	public void play (Music music)
	{
		stopMusic();
		playingMusic = music;
		if (GamePreferences.instance.music)
		{
			music.setLooping(true);
			music.setVolume(GamePreferences.instance.volMusic);
			music.play();
		}
	}
	
	/**
	 * stop playing music
	 */
	public void stopMusic()
	{
		if (playingMusic != null) playingMusic.stop();
	}
	
	/**
	 * allows options menu to inform AudioManager when settings have changed
	 */
	public void onSettingsUpdated ()
	{
		if (playingMusic == null) return;
		playingMusic.setVolume(GamePreferences.instance.volMusic);
		if (GamePreferences.instance.music)
		{
			if (!playingMusic.isPlaying()) playingMusic.play();
		}
		else
		{
			playingMusic.pause();
		}
	}
}
