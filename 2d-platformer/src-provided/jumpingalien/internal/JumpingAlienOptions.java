package jumpingalien.internal;

import jumpingalien.internal.game.JumpingAlienGameOptions;
import jumpingalien.internal.gui.JumpingAlienGUIOptions;


public class JumpingAlienOptions implements JumpingAlienGameOptions, JumpingAlienGUIOptions {

	/**
	 * Number of real seconds for 1 second in the game world (default: 1.0). By
	 * increasing this value, everything moves slower.
	 */
	private double timeScale = 1.0;

	/**
	 * Target framerate (default: 30 frames per second).
	 * 	@note 	overclocked
	 */
	private int targetFPS = 60;

	/**
	 * Print debug info on the screen (default: true).
	 */
	private boolean debugShowInfo = false;

	/**
	 * Draw axes through world origin on screen (default: false).
	 */
	private boolean debugShowAxes = false;

	/**
	 * Paint at larger scale, such that individual pixels become visible (default: false).
	 * Warning: a much smaller part of the world can be drawn on the screen.
	 * The camera will follow the alien.
	 * Probably you'll want the debugShowAlienLocationAndSize option set to true as well.
	 */
	private boolean debugShowPixels = false;
	
	/**
	 * Show the location and size of the alien on screen (default: false).
	 */
	private boolean debugShowAlienLocationAndSize = false;
	
	/**
	 * Paint the history of most recent positions of the alien (default: false).
	 */
	private boolean debugShowHistory = false;
	

	/**
	 * Color-code the different tiles based on their type (default: false).
	 */
	private boolean debugShowTileTypes = false;

	/**
	 * Draw tile gridlines (default: false).
	 */
	private boolean debugShowTileGridlines = false;

	/**
	 * Shade tiles that overlap with Mazub (default: false).
	 */
	private boolean debugShowAlienOverlappingTiles = false;

	/**
	 * Paint result of toString() method of game objects below their sprite
	 * (default: false).
	 */
	private boolean debugShowObjectString = false;

	/**
	 * Show the entire world on screen, scaled (default: false).
	 */
	private boolean debugShowEntireWorld = false;
	
	
	@Override
	public double getTargetFPS() {
		return targetFPS;
	}
	
	public void setTargetFPS(int targetFPS) {
		this.targetFPS = targetFPS;
	}

	@Override
	public double getTimescale() {
		return timeScale;
	}
	
	@Override
	public void setTimescale(double timeScale) {
		this.timeScale = timeScale;
	}

	@Override
	public boolean getDebugShowInfo() {
		return debugShowInfo;
	}
	
	@Override
	public void setDebugShowInfo(boolean debugShowInfo) {
		this.debugShowInfo = debugShowInfo;
	}

	@Override
	public boolean getDebugShowAxes() {
		return debugShowAxes;
	}
	
	@Override
	public void setDebugShowAxes(boolean debugShowAxes) {
		this.debugShowAxes = debugShowAxes;
	}

	@Override
	public boolean getDebugShowObjectLocationAndSize() {
		return debugShowAlienLocationAndSize;
	}
	
	@Override
	public void setDebugShowObjectLocationAndSize(
			boolean debugShowAlienLocationAndSize) {
		this.debugShowAlienLocationAndSize = debugShowAlienLocationAndSize;
	}

	@Override
	public boolean getDebugShowPixels() {
		return debugShowPixels;
	}
	
	@Override
	public void setDebugShowPixels(boolean debugShowPixels) {
		this.debugShowPixels = debugShowPixels;
	}

	@Override
	public boolean getDebugShowHistory() {
		return debugShowHistory;
	}
	
	@Override
	public void setDebugShowHistory(boolean debugShowHistory) {
		this.debugShowHistory = debugShowHistory;
	}
	

	@Override
	public boolean getDebugShowTileTypes() {
		return debugShowTileTypes;
	}

	@Override
	public void setDebugShowTileTypes(boolean debugShowTileTypes) {
		this.debugShowTileTypes = debugShowTileTypes;
	}
	
	@Override
	public boolean getDebugShowTileGridlines() {
		return debugShowTileGridlines;
	}

	@Override
	public void setDebugShowTileGridlines(boolean debugShowTileGridlines) {
		this.debugShowTileGridlines = debugShowTileGridlines;
	}

	@Override
	public boolean getDebugShowAlienOverlappingTiles() {
		return debugShowAlienOverlappingTiles;
	}

	@Override
	public void setDebugShowAlienOverlappingTiles(
			boolean debugShowAlienOverlappingTiles) {
		this.debugShowAlienOverlappingTiles = debugShowAlienOverlappingTiles;
	}

	@Override
	public boolean getDebugShowObjectString() {
		return debugShowObjectString;
	}

	@Override
	public void setDebugShowObjectString(boolean value) {
		this.debugShowObjectString = value;
	}
	
	@Override
	public boolean getDebugShowEntireWorld() {
		return debugShowEntireWorld;
	}

	@Override
	public void setDebugShowEntireWorld(boolean debugShowEntireWorld) {
		this.debugShowEntireWorld = debugShowEntireWorld;
	}

	public static JumpingAlienOptions parse(String[] args) {
		JumpingAlienOptions options = new JumpingAlienOptions();

		for (int i = 0; i < args.length; i++) {
			String arg = args[i];
			switch (arg) {
			case "-targetfps":
				options.targetFPS = Integer.parseInt(args[++i]);
				break;
			case "-timescale":
				options.timeScale = Double.parseDouble(args[++i]);
				break;
			case "-debuginfo":
				options.debugShowInfo = Boolean.parseBoolean(args[++i]);
				break;
			case "-debugaxes":
				options.debugShowAxes = Boolean.parseBoolean(args[++i]);
				break;
			case "-debuglocation":
				options.debugShowAlienLocationAndSize = Boolean
						.parseBoolean(args[++i]);
				break;
			case "-debugpixels":
				options.debugShowPixels = Boolean.parseBoolean(args[++i]);
				break;
			case "-debughistory":
				options.debugShowHistory = Boolean.parseBoolean(args[++i]);
				break;
			case "-showtiletypes":
				options.debugShowTileTypes = Boolean.parseBoolean(args[++i]);
				break;
			}
		}

		return options;
	}
}
