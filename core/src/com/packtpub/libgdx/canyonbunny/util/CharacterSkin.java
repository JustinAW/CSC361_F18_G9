/**
 * @author Justin Study
 */

package com.packtpub.libgdx.canyonbunny.util;

import com.badlogic.gdx.graphics.Color;

/**
 * abstracts all selectable character skins
 */
public enum CharacterSkin
{
	//3 color options
	WHITE("White", 1.0f, 1.0f, 1.0f),
	GRAY("Gray", 0.7f, 0.7f, 0.7f),
	BROWN("Brown", 0.7f, 0.5f, 0.3f);
	
	private String name;
	private Color color = new Color();
	
	/**
	 * stores character skin name and color once chosen by the user
	 */
	private CharacterSkin(String name, float r, float g, float b)
	{
		this.name = name;
		color.set(r, g, b, 1.0f);
	}
	
	/**
	 * returns name of skin
	 */
	@Override
	public String toString()
	{
		return name;
	}
	
	/**
	 * return color of skin
	 */
	public Color getColor()
	{
		return color;
	}
}
