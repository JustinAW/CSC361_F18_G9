/*
 * Displays a menu to the player
 * 
 * @author Justin Weigle 30-Sept-2018
 */

package com.packtpub.libgdx.canyonbunny.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.packtpub.libgdx.canyonbunny.game.Assets;
import com.packtpub.libgdx.canyonbunny.util.CharacterSkin;
import com.packtpub.libgdx.canyonbunny.util.GamePreferences;
import com.packtpub.libgdx.canyonbunny.util.Constants;

public class MenuScreen extends AbstractGameScreen
{
	private static final String TAG = MenuScreen.class.getName();
	
	public MenuScreen (Game game)
	{
		super(game);
	}
	
	private Stage stage;
	private Skin skinCanyonBunny;
	private Skin skinLibgdx;
	
	// menu
	private Image imgBackground;
	private Image imgLogo;
	private Image imgInfo;
	private Image imgCoins;
	private Image imgBunny;
	private Button btnMenuPlay;
	private Button btnMenuOptions;
	
	// options
	private Window winOptions;
	private TextButton btnWinOptSave;
	private TextButton btnWinOptCancel;
	private CheckBox chkSound;
	private Slider sldSound;
	private CheckBox chkMusic;
	private Slider sldMusic;
	private SelectBox<CharacterSkin> selCharSkin;
	private Image imgCharSkin;
	private CheckBox chkShowFpsCounter;
	
	// debug
	private final float DEBUG_REBUILD_INTERVAL = 5.0f;
	private boolean debugEnabled = false;
	private float debugRebuildStage;
	
	// builds everything that makes up the final scene of the menu screen
	private void rebuildStage ()
	{
		skinCanyonBunny = new Skin(
				Gdx.files.internal(Constants.SKIN_CANYONBUNNY_UI),
				new TextureAtlas(Constants.TEXTURE_ATLAS_UI));
		skinLibgdx = new Skin(
				Gdx.files.internal(Constants.SKIN_CANYONBUNNY_UI),
				new TextureAtlas(Constants.TEXTURE_ATLAS_UI));
		
		// build all layers
		Table layerBackground = buildBackgroundLayer();
		Table layerObjects = buildObjectsLayer();
		Table layerLogos = buildLogosLayer();
		Table layerControls = buildControlsLayer();
		Table layerOptionsWindow = buildOptionsWindowLayer();
		
		// assemble stage for menu screen
		stage.clear();
		Stack stack = new Stack();
		stage.addActor(stack);
		stack.setSize(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT);
		stack.add(layerBackground);
		stack.add(layerObjects);
		stack.add(layerLogos);
		stack.add(layerControls);
		stage.addActor(layerOptionsWindow);
	}
	
	// loads previously set preferences
	private void loadSettings()
	{
		GamePreferences prefs = GamePreferences.instance;
		prefs.load();
		chkSound.setChecked(prefs.sound);
		sldSound.setValue(prefs.volSound);
		chkMusic.setChecked(prefs.music);
		sldMusic.setValue(prefs.volMusic);
		selCharSkn.setSelectedIndex(prefs.charSkin);
		onCharSkinSelected(prefs.charSkin);
		chkShowFpsCounter.setChecked(prefs.showFpsCounter);
	}
	
	// saves newly set preferences when save is clicked
	private void saveSettings()
	{
		GamePreferences prefs = GamePreferences.instance;
		prefs.sound = chkSound.isChecked();
		prefs.volSound = sldSound.getValue();
		prefs.music = chkMusic.isChecked();
		prefs.volMusic = sldMusic.getValue();
		prefs.charSkin = selCharSkin.getSelectedIndex();
		prefs.showFpsCounter = chkShowFpsCounter.isChecked();
		prefs.save();
	}
	
	// sets the character skin to the selected one
	private void onCharSkinSelected(int index)
	{
		CharacterSkin skin = CharacterSkin.values()[index];
		imgCharSkin.setColor(skin.getColor());
	}
	
	// when save is clicked, it calls saveSettings()
	private void onSaveClicked()
	{
		saveSettings();
		onCancelClicked();
	}
	
	// when cancel is clicked, it doesn't save the
	// settings and it displays the Play and Options
	// buttons again and hides the options menu
	private void onCancelClicked()
	{
		btnMenuPlay.setVisible(true);
		btnMenuOptions.setVisible(true);
		winOptions.setVisible(false);
	}

	// draws background image to scene of the menu screen
	private Table buildBackgroundLayer() {
		Table layer = new Table();
		// + Background
		imgBackground = new Image(skinCanyonBunny, "background");
		layer.add(imgBackground);
		return layer;
	}
	
	// image of some coins and bunny head set to explicit
	// locations defined by setPosition() drawn on top of
	// the background layer
	private Table buildObjectsLayer() {
		Table layer = new Table();
		// + Coins
		imgCoins = new Image(skinCanyonBunny, "coins");
		layer.addActor(imgCoins);
		imgCoins.setPosition(135, 80);
		// + Bunny
		imgBunny = new Image(skinCanyonBunny, "bunny");
		layer.addActor(imgBunny);
		imgBunny.setPosition(355, 40);
		return layer;
	}
	
	// adds logo images to screen (anchored top left)
	// add() on a table widget will add a new column
	// row() will add a new row
	// expandY() will expand empty space in a vertical direction,
	// which pushes the new image information to the bottom edge
	// layer.debug() draws debug visuals if debugEnabled = true
	private Table buildLogosLayer() {
		Table layer = new Table();
		layer.left().top();
		// + Game Logo
		imgLogo = new Image(skinCanyonBunny, "logo");
		layer.add(imgLogo);
		layer.row().expandY();
		// + Info Logos
		imgInfo = new Image(skinCanyonBunny, "info");
		layer.add(imgInfo).bottom();
		if (debugEnabled) layer.debug();
		return layer;
	}
	
	// adds control buttons (anchored bottom right)
	// play button and options button created with
	// ChangeListeners to define the action to be taken
	// when a button is pressed
	private Table buildControlsLayer() {
		Table layer = new Table();
		layer.right().bottom();
		// + Play Button
		// uses onPlayClicked method to define action
		btnMenuPlay = new Button(skinCanyonBunny, "play");
		layer.add(btnMenuPlay);
		btnMenuPlay.addListener(new ChangeListener()
		{
			@Override
			public void changed (ChangeEvent event, Actor actor)
			{
				onPlayClicked();
			}
		});
		layer.row();
		// + Options Button
		// uses onOptionsClicked method to define action
		btnMenuOptions = new Button(skinCanyonBunny, "options");
		layer.add(btnMenuOptions);
		btnMenuOptions.addListener(new ChangeListener()
		{
			@Override
			public void changed (ChangeEvent event, Actor actor)
			{
				onOptionsClicked();
			}
		});
		if (debugEnabled) layer.debug();
		return layer;
	}
	
	// switches to game screen
	private void onPlayClicked ()
	{
		game.setScreen(new GameScreen(game));
	}
	
	private void onOptionsClicked () {}
	
	private Table buildOptionsWindowLayer() {
		Table layer = new Table();
		return layer;
	}
	
	// adds checkboxes and sliders for audio options in menu
	private Table buildOptWinAudioSettings()
	{
		Table tbl = new Table();
		// + Title: "Audio"
		tbl.pad(10,10,0,10);
		tbl.add(new Label("Audio", skinLibgdx, "default-font", Color.ORANGE)).colspan(3);
		tbl.row();
		tbl.columnDefaults(0).padRight(10);
		tbl.columnDefaults(1).padRight(10);
		// + Checkbox, "Sound" label, sound volume slider
		chkSound = new CheckBox("", skinLibgdx);
		tbl.add(chkSound);
		tbl.add(new Label("Sound", skinLibgdx));
		sldSound = new Slider(0.0f, 1.0f, 0.1f, false, skinLibgdx);
		tbl.add(sldSound);
		tbl.row();
		// + Checkbox, "Music" label, music volume slider
		chkMusic = new CheckBox("", skinLibgdx);
		tbl.add(chkMusic);
		tbl.add(new Label("Music", skinLibgdx));
		sldMusic = new Slider(0.0f, 1.0f, 0.1f, false, skinLibgdx);
		tbl.add(sldMusic);
		tbl.row();
		return tbl;
	}

	// calls to update and render the stage and enables calling
	// rebuildStage() in intervals defined by DEBUG_REBUILD_INTERVAL
	// if debugEnabled is set to true
	@Override
	public void render (float deltaTime)
	{
		Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		if (debugEnabled)
		{
			debugRebuildStage -= deltaTime;
			if (debugRebuildStage <= 0)
			{
				debugRebuildStage = DEBUG_REBUILD_INTERVAL;
				rebuildStage();
			}
		}
		stage.act(deltaTime);
		stage.draw();
		stage.setDebugAll(true);
	}
	
	// stes the viewport size of the stage
	@Override public void resize (int width, int height) 
	{
		stage.getViewport().update(width, height, true);
	}
	
	// called when screen is shown. Initializes the stage,
	// sets it as LibGDX's current input processor, and the
	// stage is rebuilt
	@Override public void show () 
	{
		stage = new Stage(new StretchViewport(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT));
		Gdx.input.setInputProcessor(stage);
		rebuildStage();
	}
	
	// frees allocated resources when the screen is hidden
	@Override public void hide () 
	{
		stage.dispose();
		skinCanyonBunny.dispose();
		skinLibgdx.dispose();
	}
	
	@Override public void pause () {}
}