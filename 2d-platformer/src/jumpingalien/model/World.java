package jumpingalien.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import java.lang.Math;

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Immutable;
import be.kuleuven.cs.som.annotate.Raw;
import jumpingalien.util.ModelException;

/**
 * A class of worlds which contain a player and other organisms, it also has a structure of tiles.
 * 
 * @Invar 	the maximum x-coordinate of a pixel is tileSize*nbTilesX-1
 * 			|this.getMaxPixelX() == this.getTileSize() * this.getNbTilesX() -1
 * @Invar	the maximum y-coordinate of a pixel is tileSize*nbTilesY-1
 * 			|this.getMaxPixelY() == this.getTileSize() * this.getNbTilesY() -1
 * @Invar 	the maximum number of game objects is 101
 * 			|this.getMaxNbOfObjects == 101
 * @Invar 	the number of game objects is greater than 0 and lower or equal than maxNbOfObjects
 * 			|0<this.getNbOfObjects() <= getMaxNbOfObjects()
 * @Invar	TODO associations
 * 
 * @authors  	Wannes Vande Cauter, Nils Van Dessel	--	Fysica
 */
public class World {

	private final int maxNumberOfObjects = 100;
	
	private int maxPixelX;
	private int maxPixelY;
	private int tileSize;
	private int nbOfTilesX;
	private int nbOfTilesY;
	private int maxPixelsWindowX, maxPixelsWindowY;
	private int[] targetTileCoordinate;
	private Set<Organism> objectList = new HashSet<>();
	private int[] lowerLeftPixelWindow = new int[2];
	private Mazub player = null;
	private GameState currentGameState = GameState.PREPARATION;
	private GeologicalFeature[] tileTypes;
	private static final GeologicalFeature defaultGeoFeat = GeologicalFeature.AIR; 
	private static final List<GeologicalFeature> allGeoList = new ArrayList<>(List.of(GeologicalFeature.AIR , GeologicalFeature.SOLID,GeologicalFeature.WATER,GeologicalFeature.MAGMA,GeologicalFeature.ICE,GeologicalFeature.GAS)) ;
	private Set<School> schoolList = new HashSet<>();
	private static int maxNbOfSchools = 10;
	private int[] returnTargetTile;
	
	/**
	 * Initialize a new world with give tileSize, number of tiles in the x direction, number of tiles in the x direction, number of tiles in the y direction,
	 * a coordinate for the target tile, the width of the visible window, the height of the visible window
	 * 
	 * @param 	tileSize
	 * 			The size of a tile (number of pixels)
	 * @param 	nbTilesX
	 * 			The number of tiles in each horizontal row
	 * @param 	nbTilesY
	 * 			The number of tiles in each vertical column
	 * @param 	targetTileCoordinate
	 * 			The coordinates of the target tile [pixelsX,pixelsY]
	 * @param 	visibleWindowWidth
	 * 			The width of the visible window (number of pixels)
	 * @param 	visibleWindowHeight
	 * 			The height of the visible window (number of pixels)
	 * @param 	geologicalFeatures
	 * 			A list that defines the geological features of the tiles (in order), all integers
	 * 			0 : AIR
	 * 			1 : SOLID_GROUND
	 * 			2 : WATER
	 * 			3 : MAGMA
	 * 			4 : ICE
	 * 			5 : GAS
	 * 			Not specified : AIR
	 * @post	TODO
	 * @throws	ModelException
	 * 			TODO
	 * 
	 */
	@Raw
	public World(int tileSize, int nbTilesX, int nbTilesY, int[] targetTileCoordinate,
			int visibleWindowWidth, int visibleWindowHeight, int... geologicalFeatures) throws ModelException{
		if (targetTileCoordinate == null) {
			throw new ModelException("targetTile can't be null");
		}
		else {
			this.nbOfTilesX = Math.abs(nbTilesX);
			this.nbOfTilesY = Math.abs(nbTilesY);
			this.tileSize = Math.abs(tileSize);
			this.maxPixelX = Math.abs((tileSize*nbTilesX) );
			this.maxPixelY = Math.abs((tileSize*nbTilesY) );
			this.setReturnTargetTile(targetTileCoordinate);
			int[] targetTile = new int[] {targetTileCoordinate[0],targetTileCoordinate[1]};
			this.setTargetTileCoord(targetTile);
			this.maxPixelsWindowX = visibleWindowWidth;
			this.maxPixelsWindowY = visibleWindowHeight;
			
			this.setGeoFeat(geologicalFeatures);
			
			if(targetTileCoordinate.length != 2) {
				throw new ModelException("...");
			}
			if (visibleWindowWidth > Math.abs(tileSize*nbTilesX) || visibleWindowHeight > Math.abs(tileSize*nbTilesY)) {
				throw new ModelException("window too large");
			}
			if (visibleWindowWidth < 0 || visibleWindowHeight < 0) {
				throw new ModelException("windowSize can't be negative");
			}
		}
	}
	
	/**
	 * Returns the integer array of the returnTargetTile.
	 */
	@Basic
	public int[] getReturnTargetTile() {
		return this.returnTargetTile;
	}
	/**
	 * Sets the new coordinates of the returnsTargetTile
	 * 
	 * @param 	coord
	 * 			The new coordinates of the returnsTargetTile
	 * @post	The returnTargetTile is now the given coordinate.
	 * 			|new.getReturnTargetTile() == coord
	 */
	public void setReturnTargetTile(int[] coord) {
		this.returnTargetTile = coord.clone();
	}
	
	/**
	 * Returns the maximum amount of gameObjects that can inhabit the world.
	 */
	@Basic
	@Immutable
	public int getMaxNumberOfObjects() {
		return this.maxNumberOfObjects;
	}
	
	/**
	 * Returns the amount of organisms in the world.
	 * 
	 * @return 	Returns the amount of objects in this.getOrganism().
	 * 			|this.getOrganism().size()
	 */
	public int getNbOfObjects() {
		return this.getOrganism().size();
	}
	
	/**
	 * Returns an object set of all organisms in this world.
	 * 
	 * @return	A set of all the objects in the world.
	 */
	public Set<Object> getOrganism() {
		Set<Object> ret = new HashSet<>();
		ret.addAll(this.objectList);
		return ret;
	}
	
	/**
	 * Returns the amount of pixels in the horizontal direction of this world.
	 */
	@Basic
	@Immutable
	public int getMaxPixelX() {
		return this.maxPixelX;
	}
	

	/**
	 * Returns the amount of pixels in the vertical direction of this world.
	 */
	@Basic
	@Immutable
	public int getMaxPixelY() {
		return this.maxPixelY;
	}
	
	/**
	 * Returns the size of tiles in the world.
	 */
	@Basic
	@Immutable
	public int getTileSize() {
		return this.tileSize;
	}
	
	/**
	 * Returns the amount of tiles in the horizontal direction of this world.
	 */
	@Basic
	@Immutable
	public int getNbOfTilesX() {
		return this.nbOfTilesX;
	}
	
	/**
	 * Returns the amount of tiles in the horizontal direction of this world.
	 */
	@Basic
	@Immutable
	public int getNbOfTilesY() {
		return this.nbOfTilesY;
	}
	
	/**
	 * Returns the total amount of tiles of this world.
	 * 
	 * @return	Returns the multiplication of this.getNbOfTilesX() and this.getNbOfTilesY()
	 * 			|result == (this.getNbOfTilesX() * this.getNbOfTilesY())
	 */
	@Immutable
	public int getNbOfTiles() {
		return (this.getNbOfTilesX() * this.getNbOfTilesY());
	}
	
	/**
	 * Returns the maximum amount of pixels the visible window can be wide.
	 */
	@Basic
	@Immutable
	public int getMaxPixelsWindowX() {
		return this.maxPixelsWindowX;
	}
	
	/**
	 * Returns the maximum amount of pixels the visible window can be high.
	 */
	@Basic
	@Immutable
	public int getMaxPixelsWindowY() {
		return this.maxPixelsWindowY;
	}
	
	/**
	 * Returns the coordinate of the target tile.
	 */
	@Basic
	public int[] getTargetTileCoordinate() {
		return this.targetTileCoordinate;
	}
		
	/**
	 * Returns the single mazub that acts the player in this world.
	 */
	@Basic
	public Mazub getPlayer() {
		return this.player;
	}
		
	/**
	 * Returns the current game state of the world.
	 */
	@Basic
	private GameState getCurrentGameState() {
		return this.currentGameState;
	}
	
	/**
	 * Returns whether the current gamestate is GameState.RUNNING
	 * 
	 * @return	Returns true if the current gamestate is GameState.RUNNING.
	 * 			|result == (this.getCurrentGameState() == GameState.GAMEOVER)
	 */
	public boolean isRunning() {
		return (this.getCurrentGameState() == GameState.RUNNING);
	}
	
	/**
	 * Returns whether the current gamestate is GameState.GAMEOVER
	 * 
	 * @return	Returns true if the current gamestate is GameState.GAMEOVER .
	 * 			|result == (this.getCurrentGameState() == GameState.GAMEOVER)
	 */
	public boolean isGameOver() {
		return this.getCurrentGameState() == GameState.GAMEOVER;
	}
	
	/**
	 * Returns whether the current gamestate is GameState.VICTORY
	 * 
	 * @return	Returns true if the current gamestate is GameState.VICTORY
	 * 			|result == (this.getCurrentGameState() == GameState.VICTORY)
	 */
	public boolean isGameWon() {
		return this.getCurrentGameState() == GameState.VICTORY;
	}
	
	/**
	 * Sets the current game state of the world.
	 * 
	 * 	@param	newGameState
	 * 			The new gamestate of this world.
	 * 	@post	The gameState will change to the given gamestate.
	 * 			|new.currentGameState == newGameState
	 */
	private void setGameState(GameState newGameState) {
		this.currentGameState = newGameState;
	}
	
	/**
	 * Start the game.
	 * 
	 * @effect	the gameState will change to the GameState.RUNNING
	 * 			|if (!this.isRunning() and this.hasPlayer())
	 * 			|then this.setGameState(GameState.RUNNING)
	 * @throws 	ModelException
	 * 			A new ModelException gets throw if the game is running or if there is no available player.
	 * 			|if (this.isRunning() || !this.hasPlayer())
	 * 			|then throw new ModelException()
	 */
	public void startGame() throws ModelException{
		if (this.isRunning() || !this.hasPlayer()) {
			throw new ModelException("Already started or no Player selected");
		}
		else {
			this.setGameState(GameState.RUNNING);
		}
	}
	
	/**
	 * Change the current game state to GameState.GAMEOVER.
	 * 
	 * 	@effect	The gameState will change to GameState.GAMEOVER.
	 * 			|setGameState(GameState.GAMEOVER)
	 */
	public void gameOver() {
		this.setGameState(GameState.GAMEOVER);
	}
	
	/**
	 * Change the current game state to GameState.VICTORY
	 * 
	 * 	@effect	The gameState will change to the GameState.VICTORY.
	 * 			|setGameState(GameState.VICTORY)
	 */
	public void gameWin() {
		this.setGameState(GameState.VICTORY);
	}
	
	/**
	 * @note	This method is not required to be documented.
	 * Update The game state.
	 * 
	 * @effect	The game is over if this.hasPlayer and  not this.getPlayer().isAlive()
	 * 			|if (this.hasPlayer() and not this.getPlayer().isAlive())
	 * 			|then this.gameOver()
	 * @effect	The game is won when the player reaches the target tile.
	 * 			|if (this.hasPlayer() and this.targetTileReached)
	 * 			|then this.gameWin()
	 */
	public void updateGameState() {
		if (this.hasPlayer()) {
			if (!this.getPlayer().isAlive()) {
				this.gameOver();
			}
			else if (this.targetTileReached()) {
				this.gameWin();
			}
		}
	}
	
	/**
	 * @note	This method is not required to be documented.
	 */
	private boolean targetTileReached() {
		int[] pos = this.getPlayer().getPosition();
		int[] target = this.getTargetTileCoordinate();
		
		int sizeX1 = this.getPlayer().getCurrentSize()[0];
		int sizeX2 = this.getTileSize();
		int sizeXTotal = sizeX1 + sizeX2;
		int XM1 = pos[0] + sizeX1/2;
		int XM2 = target[0] + sizeX2/2;
		int dX = Math.abs(XM1 - XM2);
		int sizeY1 = this.getPlayer().getCurrentSize()[1];
		int sizeY2 = this.getTileSize();
		int sizeYTotal = sizeY1 + sizeY2;
		int YM1 = pos[1] + sizeY1/2;
		int YM2 = target[1] + sizeY2/2;
		int dY = Math.abs(YM1 - YM2);

		return (dX<=sizeXTotal/2 && dY<=sizeYTotal/2);
	}
	
	/**
	 * Returns whether the world contains a player.
	 * 
	 * @return 	Returns true if this.getPlayer() == null.
	 * 			|result == this.getPlayer() != null 
	 */
	public boolean hasPlayer() {
		return !(this.getPlayer()==null);
	}
	
	/** 
	 * Returns whether the world is terminated.
	 * 
	 * @return	Returns true if the world is terminated .
	 * 			|(this.getCurrentGameState() == GameState.TERMINATED)
	 */
	public boolean isTerminated() {
		return (this.getCurrentGameState() == GameState.TERMINATED);
	}
	
	/**
	 * The world gets terminated and removes all of the organisms living in it.
	 * 
	 * @effect	This world gets removed from every organism that was living in it.
	 * 			|for organism in this.getOrganism()
	 * 			|	organism.removeFromWorld()
	 * @effect 	The gamestate is updated to GameState.TERMINATED .
	 * 			|this.setGameState(GameState.TERMINATED)
	 */
	public void terminate() {
		Set<Object> objListTemp = Set.copyOf(this.getOrganism());
		for (Object obj : objListTemp) {
			Organism org = (Organism) obj;
			org.removeFromWorld();
		}
		this.setGameState(GameState.TERMINATED);
	}
	

	/**
	 * Returns the tile types in this world as an array of GeologicalFeature s.
	 */
	@Basic
	@Immutable
	public GeologicalFeature[] getTileTypes() {
		return this.tileTypes;
	}
	
	/**
	 * Returns the lower left pixel of the visible window.
	 */
	@Basic
	public int[] getLowerLeftPixelWindow() {
		return this.lowerLeftPixelWindow;
	}
	/**
	 * The lower left pixel of the visible window gets set to the given coordinate.
	 * 
	 * @param 	Coord
	 * 			the new coordinates of the lower left pixel of the window
	 * @post	the coordinates of the lower left pixel of the window get changed to Coord
	 * 			|this.lowerLeftPixel == Coord
	 */
	public void setLowerLeftPixelWindow(int[] Coord) {
		this.lowerLeftPixelWindow = Coord;
	}
	/**
	 * Returns get upper right pixel of the visible window.
	 * 
	 * @return	The coordinates of the upper right pixel of the visible window
	 * 			|result.getUpperRightPixelWindow() ==
	 * 			|[ this.getLowerLeftPixelWindow()[0] + this.getMaxPixelsWindowX(),
	 * 			|  this.getLowerLeftPixelWindow()[1] + this.getMaxPixelsWindowY()]
	 */
	public int[] getUpperRightPixelWindow() {
		int xCo = this.getLowerLeftPixelWindow()[0] + this.getMaxPixelsWindowX();
		int yCo = this.getLowerLeftPixelWindow()[1] + this.getMaxPixelsWindowY();
		int[] Co = {xCo,yCo};
		return Co;
	}

	/**
	 * Returns the default geological feature.
	 */
	@Basic
	@Immutable
	private static GeologicalFeature getDefaultGeoFeat() {
		return World.defaultGeoFeat;
	}
	
	/**
	 * Returns the amount of possible geological features.
	 */
	@Immutable
	private static int getNBOffGeoFeats() {
		return World.getAllGeoFeats().size();
	}
	
	/**
	 * Returns an arraylist of the possible geological geological features.
	 */
	@Basic
	@Immutable
	private static List<GeologicalFeature> getAllGeoFeats() {
		return World.allGeoList;
	}
	
	
	/**
	 * Translate an integer to a geological feature.
	 * 
	 * @param 	integer
	 * 			The integer that needs to be translated.
	 * @Pre		Integer has to be greater then 0 and smaller then this.getAllGeoFeats().
	 * 			|(integer>=0 && integer<this.getNBOffGeoFeats())
	 * @return	The integer gets translated based on the given integer and the possible geological features.
	 * 			|result == (this.getAllGeoFeats().get(integer))
	 */
	public static GeologicalFeature intToGeo(int integer) {
		assert (integer>=0 && integer<World.getNBOffGeoFeats());
		return World.getAllGeoFeats().get(integer);
	}
	




	/**
	 * The world gets set based on the given integer array.
	 * 
	 * @note	This method is not required to be documented.
	 */
	private void setGeoFeat(int[] intList) {
		List<GeologicalFeature> geoList = World.getAllGeoFeats();
		List<GeologicalFeature> typeList = new ArrayList<>();
		if (!this.containsNull(intList)) {
			for (int integer:intList) {
				if ((0 <= integer && integer < World.getNBOffGeoFeats()) && geoList.get(integer)!=null) {
					typeList.add(geoList.get(integer));
					}
				else if (integer < 0){
					typeList.add(geoList.get(0));
				}
			}
		}
		int total = this.getNbOfTiles();
		while (typeList.size() < total) {
			typeList.add(World.getDefaultGeoFeat());
		}
		this.tileTypes = new GeologicalFeature[typeList.size()];
		for (int i =0 ; i < typeList.size() ; i++)
			this.tileTypes[i] = (GeologicalFeature) typeList.get(i);
		
	}
	
	/**
	 * Returns whether the given integer array contains a null.
	 * 
	 * @param 	test
	 * 			The integer array that needs to be checked for containing a null.
	 * @return	Returns true if any elements of the array matches null.
	 * 			|result == (Arrays.asList(test)).stream().anyMatch(element->element==null)
	 */
	public boolean containsNull(int[] test) {
		return (Arrays.asList(test)).stream().anyMatch(element->element==null);
	}
	
	

	/**
	 *Returns whether the the given position is inside of the world borders.
	 *
	 * @param 	x
	 * 			The x coordinate (in pixels) of the given position.
	 * @param 	y
	 * 			The y coordinate (in pixels) of the given position.
	 * @return	Whether the given position is a valid one.
	 * 			|result == 
	 * 			|(((x >= 0) && (x <= this.getMaxPixelX())) && ((y >= 0) && (y <= this.getMaxPixelY())))
	 */
	public boolean isValidPosition(int x, int y) {
		return (((x >= 0) && (x <= this.getMaxPixelX())) && ((y >= 0) && (y <= this.getMaxPixelY())));
	}
	
	/**
	 * Returns whether the amount of game objects in the world is allowed.
	 * 
	 * @return	Whether the amount of objects in the world is valid
	 * 			|result.isValidAmountOfObjects() = 
	 * 			|(this.getNbOfObjects() >= 0 && this.getNbOfObjects() <= this.getMaxNumberOfObjects())
	 */
	public boolean isValidAmountOfObjects(){
		return (this.getNbOfObjects() >= 0 && this.getNbOfObjects() <= this.getMaxNumberOfObjects());
	}
	
	/**
	 * Returns whether a given coordinate (in tiles) is the target tile.
	 * 
	 * @param 	tileCoord
	 * 			The coordinates of the tile to check
	 * @return	Whether the tile is the target tile or not (true or false)
	 * 			|result.isTargetTileCoord(tileCoord) = 
	 * 			|(tileCoord == this.getTargetTileCoordinate())
	 */
	public boolean isTargetTileCoord(int[] tileCoord) {
		return (tileCoord == this.getTargetTileCoordinate());
	}
	
	/**
	 * Set the target tile coordinate of this world to a given coordinate.
	 * 
	 * @param 	coord
	 * 			The new coordinate of the target tile.
	 * @throws 	ModelException
	 * 			If coord equals null, a new ModelException will be thrown.
	 * 			|if (coord == null)
	 * 			|then throw new ModelException
	 */
	public void setTargetTileCoord(int[] coord) throws ModelException{
		if (coord == null) {
			throw new ModelException("targetTile can't be null");
		}
		else {
			int[] co = new int[] {this.getTileSize()*coord[0],this.getTileSize()*coord[1]};
			this.targetTileCoordinate = co.clone();
		}
	}
	/**
	 * Returns whether the given tile number corresponds with the tile number of the target tile.
	 * 
	 * @param 	Nb
	 * 			The number of the tile to check.
	 * @return	Returns true if Nb equals this.getTileNumber(this.getTargetTileCoordinate()).
	 * 			|result == 
	 * 			|(Nb == this.getTileNumber(this.getTargetTileCoordinate()))
	 */
	public boolean isTargetTileNb(int Nb) {
		return (Nb == this.getTileNumber(this.getTargetTileCoordinate()));
	}
	
	/**
	 * Returns the pixel coordinate of the lower left pixel based on the tilenumber.
	 * 
	 * @param 	N
	 * 			The tile number of which you want the pixel coordinate.
	 * @return	Returns the pixelcoordinate corresponding to the tilenumber.
	 * 			|result == int[] {(N/this.getNbOfTilesX())*this.getTileSize() , (N/this.getNbOfTilesY())*this.getTileSize()}
	 */
	public int[] getTilePixelCoord(int N) {
		int xCo = N / this.getNbOfTilesX();
		int yCo = N - (xCo*this.getNbOfTilesX());
		xCo *= this.getTileSize();
		yCo *= this.getTileSize();
		int[] list = {xCo,yCo};
		return list;
	}
	
	/**
	 * Translate a tile coordinate to a tileNumber.
	 * 
	 * @param 	coord
	 * 			The coordinates of the tile.
	 * @return	The index number of the tile if the given coordinate is valid.
	 * 			|if (((coord[0] >= 0) and (coord[0] < this.getNbOfTileX())) and ((coord[1] >= 0) and (coord[1] < this.getNbOfTilesY())))
	 * 			|then result == (coord[1]*this.getNbOfTilesX() + coord[0])
	 * @throws 	IllegalArgumentException
	 * 			The coordinates given are out of range.
	 * 			|if not (((coord[0] >= 0) and (coord[0] < this.getNbOfTileX())) and ((coord[1] >= 0) and (coord[1] < this.getNbOfTilesY())))
	 * 			|then throw new IllegalArgumentException
	 */
	public int getTileNumber(int[] coord) throws IllegalArgumentException{
		int xLen = this.getNbOfTilesX();
		int yLen = this.getNbOfTilesY();
		if (((coord[0] >= 0) && (coord[0] < xLen)) && ((coord[1] >= 0) && (coord[1] < yLen))) {
			return (coord[1]*xLen + coord[0]);
		}
		else {
			throw new IllegalArgumentException("invalid coordinates");
		}
	}
	
	/**
	 * Returns whether a given pixel coordinate is in the world borders.
	 * 
	 * @param 	x
	 * 			The x value of the coordinate that needs to be checked.
	 * @param 	y
	 * 			The y value of the coordinate that needs to be checked.
	 * @return	Returns false if x < 0 or x > this.getMaxPixelX() or y<0 or y> this.getMaxPixelY() .
	 * 			|result == (x>= 0 and x < this.getMaxPixelX() and y>= 0 and y<this.getMaxPixelY())
	 */
	public boolean isValidCoordinate(int x, int y) {
		return (x>= 0 && x < this.getMaxPixelX() && y>= 0 && y<this.getMaxPixelY());
	}
	
	/**
	 * Translate a tile coordinate to a pixel coordinate.
	 * 
	 * @param 	Coord
	 * 			The coordinate that needs to be translated from pixel coordinates to tile coordinates.
	 * @return 	Returns the translation of the given coordinate if it is valid.
	 * 			|if this.isValidCoordinate(Coord[0],Coord[1])
	 * 			|then result == int[] {Coord[0]/this.getTileSize() , Coord[1]/this.getTileSize()}
	 * @return 	Returns an integer array of zeros if the given coordinate is not valid.
	 * 			|if not this.isValidCoordinate(Coord[0],Coord[1])
	 * 			|then result == int[] {0,0}		
	 */
	public int[] getTileCoordFromPixel(int[] Coord) {
		if (this.isValidCoordinate(Coord[0], Coord[1])) {
			int xCo = Coord[0] / this.getTileSize();
			int yCo = Coord[1] / this.getTileSize();
			int[] Co = {xCo,yCo};
			return Co;
		}
		else {
			return new int[] {0,0}; 
		}
	}
	
	/**
	 * Translate an integer to a geological feature.
	 * 
	 * @param 	N 
	 * 			The tile index.
	 * @return	This returns the type of a tile in the form of an GeologicalFeature if the given N is valid.
	 * 			|if (N < this.getNbOfTiles() and N > 0 and this.getTileTypes().length)
	 * 			|then result == this.getTileTypes()[N]
	 * @return	This returns the default geological feature if the given N is valid in terms of the number of tiles but not in terms of the current amount of tiles.
	 * 			|if (N < this.getNbOfTiles() and N > 0 and not this.getTileTypes().length)
	 * 			|then result == this.getDefaultGeoFeat()
	 * @throws	IllegalArgumentException
	 * 			The number given is out of index range
	 * 			|if (N >= this.getNbOfTiles() || N < 0)
	 * 			|then throw new IllegalArgumentException()
	 */
	public GeologicalFeature getTypeTile(int N) throws IllegalArgumentException{
		if (N >= this.getNbOfTiles() || N < 0) {
			throw new IllegalArgumentException("invalid tile index");
		}
		else {
			if (N >= this.getTileTypes().length) {
				return World.getDefaultGeoFeat();
			}
			else {
				return this.getTileTypes()[N];
			}
		}
	}
	
	/**
	 * Set the tile types based on a given number and an integer.
	 * 
	 * @param 	N
	 * 			The number that corresponds to a geological feature.
	 * @param 	integer
	 * 			The tilenumber that should be set.
	 * 
	 * @throws 	IllegalArgumentException
	 * 			An IllegalArgumentException gets thrown if 
	 */
	public void setTypeTile(int N, int integer) throws IllegalArgumentException{
		if (integer >= 0 && integer < World.getNBOffGeoFeats()) {
			if (N >= this.getNbOfTiles() || N < 0) {
				throw new IllegalArgumentException("invalid tile index");
			}
			else {
				if (N >= this.getTileTypes().length) {
			
					int start = this.getTileTypes().length;
					for (int i = start ; i < N ; i++) {
						this.tileTypes[i] = World.getDefaultGeoFeat();
					}
					GeologicalFeature type = World.intToGeo(integer);
					this.tileTypes[N] = type;
				}
				else {
					this.tileTypes[N] = World.intToGeo(integer);
				}
			}
		}
	}
	
	/**
	 * @note	This method is not required to be documented.
	 */
	public void advanceTime(double time) {
		Set<Object> objListTemp = Set.copyOf(this.getOrganism());
		for (Object obj : objListTemp){
			if (objListTemp.contains(obj)) {
				((Organism) obj).advanceTime(time);
			}
		}
		this.advanceWindow();
	}
	
	/**
	 * @note	This method is not required to be documented.
	 */
	public void advanceWindow() {
		if (player != null) {
			// attempt to keep Mazub in the middle of the screen
			int middleMazubX = (player.getCurrentSize()[0] / 2) + (player.getPosition()[0]);
			int middleMazubY = (player.getCurrentSize()[1] / 2) + (player.getPosition()[1]);
			int windowX = middleMazubX - (this.getMaxPixelsWindowX()/2);
			int windowY = middleMazubY - (this.getMaxPixelsWindowY()/2);
			// exceptions (total)
			if (windowX < 0) {
				windowX = 0;
			}
			if (windowY < 0) {
				windowY = 0;
			}
			if (windowX >= (this.getMaxPixelX() - this.getMaxPixelsWindowX())) {
				windowX = this.getMaxPixelX() - this.getMaxPixelsWindowX();
			}
			if (windowY >= (this.getMaxPixelY() - this.getMaxPixelsWindowY())) {
				windowY = (this.getMaxPixelY() - this.getMaxPixelsWindowY());
			}
			int[] Co = {windowX,windowY};
			this.setLowerLeftPixelWindow(Co);
		}
	}
	
	
	/**
	 * TODO
	 * @param 	object
	 * @pre 	
	 * @pre		
	 */
	public void addObject(Organism object) {
		assert(this.getCurrentGameState() != GameState.RUNNING && this.getCurrentGameState() != GameState.TERMINATED);
		assert(this.isValidObject(object));
		if ((this.isValidObject(object)) && (this.getNbOfObjects() < this.getMaxNumberOfObjects())) {
			this.objectList.add(object);
			((Organism) object).addWorld(this);
			if (!this.hasPlayer() && object instanceof Mazub) {
				this.player = (Mazub) object;
				this.advanceWindow();
			}
			((Organism) object).setCurrentSize();
		}
		
	}
	
	/**
	 * Returns the schools that are in this world.
	 */
	@Basic
	public Set<School> getSchools(){
		return this.schoolList;
	}
	
	/**
	 * Returns the maximum amount of schools a world can have.
	 */
	@Basic
	@Immutable
	public static int getMaxNbOfSchools() {
		return World.maxNbOfSchools;
	}
	
	/**
	 * TODO
	 * @param 	school
	 * @throws 	AssertionError
	 */
	public void addSchool(School<?> school) throws AssertionError{
		if (this.getSchools().size() < World.getMaxNbOfSchools()) {
			if (! this.getSchools().contains(school)) {
				this.schoolList.add(school);
				school.addWorld(this);
			}
			else {
				throw new AssertionError("Is already in world");
			}
		}
		else {
			throw new AssertionError("World already has the max number of schools");
		}
	}
	
	public void terminateSchool(School<?> school) {
		this.schoolList.remove(school);
	}
	
	/**
	 * Remove an object from this world
	 * 
	 * @param 	organism
	 * 			The organism which should be removed from the world.
	 * @post	The organism will be removed from the world if he was in the world.
	 * 			|if this.hasAsGameObject(organism)
	 * 			|then not new.hasAsGameObject(organism);
	 * @post	If the organism was a player, the player will be set to null.
	 * 			|if this.hasAsGameObject(organism) and this.getPlayer() == organism
	 * 			|then new.getPlayer == null
	 * @throws	ModelException
	 * 			|A ModelException gets thrown if organism is not in the world.
	 */
	public void removeObject(Object organism) throws ModelException{
		if (this.hasAsGameObject(organism))
			this.objectList.remove(organism);
		else {
			throw new ModelException("this isn't in game");
		}
		if(this.getPlayer() == organism) {
			this.player = null;
		}
			
	}
	
	/**
	 * TDO
	 * 
	 * @param 	object
	 * 			The object to check for being valid
	 * @return	Whether the object is valid
	 * 			|result.isValidObject(object) == false if (!object instanceof Organism) 
	 * 				|| (object.hasWorld
	 * 			|else: result.isValidObject(object) == true
	 */
	private boolean isValidObject(Object object) {
		if (object instanceof Organism){
			return !(((Organism) object).hasWorld() || ((Organism) object).isEliminated());
		}
		return false;
	}
	
	
	/**
	 * @note	This method is not required to be documented.
	 * 
	 * Checks whether a given object is allowed to unduck.
	 * 
	 * @param 	organism
	 * 			an organism which might want to unduck
	 * @return	an integer list (of length 3) of tiles the organism would touch when it would unduck
	 * 			[amount of solid ground, amount of water, amount of lava]
	 */
	public boolean canUnduck(Mazub organism) {
		if (organism.isDucking()) {
			organism.setDuck(false);
			organism.setCurrentSprite(0);
			int height = organism.getCurrentSprite().getHeight();
			int width = organism.getCurrentSprite().getWidth();
			organism.setDuck(true);
			organism.setCurrentSprite(0);
			int[] position = organism.getPosition();
			boolean bool1 = analyseHLine(position[1]+height,position[0],position[0]+width-1,GeologicalFeature.SOLID);
			boolean bool2 = analyseHLine(position[1]+height,position[0],position[0]+width-1,GeologicalFeature.ICE);
			boolean ret = false;
			for (Object obj : this.getOrganism()) {
				if (obj != organism) {
					ret = ((Organism) obj).overlap(organism, organism.getPosX(), organism.getPosY(), false);
				}
			}
			return !(bool1 || bool2 || ret);
		}
		else {
			return true;
		}
	}

	/**
	 * @note	This method is not required to be documented.
	 */
	protected boolean analyseHLine(int y, int x1, int x2,GeologicalFeature type) {
		x2 -= 1;
		double width=this.getTileSize();
		int number = -1;
		for(int x=x1 ; x<x2 ; x += width) {
			int[] l = {x,y};
			number = this.getTileNumber(this.getTileCoordFromPixel(l));
			GeologicalFeature typeT = getTypeTile(number);
			if (typeT==type) {
				return true;
			}
		}
		int[] l = {x2,y};
		int numberLastTile = this.getTileNumber(this.getTileCoordFromPixel(l));
		GeologicalFeature typeT = getTypeTile(numberLastTile);
		if (typeT==type) {
			return true;
		}
		return false;
	}
		
	/**
	 * @note	This method is not required to be documented.
	 */
	protected boolean analyseVLine(int x, int y1, int y2,GeologicalFeature type) {
		y2 -= 1;
		double height=this.getTileSize();
		int number = -1;
		for(int y=y1 ; y<y2 ; y += height) {
			int[] l = {x,y};
			number = this.getTileNumber(this.getTileCoordFromPixel(l));
			GeologicalFeature typeT = getTypeTile(number);
			if (typeT==type) {
				return true;
			}
		}
		int[] l = {x,y2};
		int numberLastTile = this.getTileNumber(this.getTileCoordFromPixel(l));
		GeologicalFeature typeT = getTypeTile(numberLastTile);
		if (typeT==type) {
			return true;
		}
		return false;
	}
	
	/**
	 * @note	This method is not required to be documented.
	 */
	public boolean[] hitTile(Organism org, int x0, int y0, GeologicalFeature type) {
		boolean[] list = {false,false,false,false};
		int sizeX = org.getSizeX();
		int sizeY = org.getSizeY();
		int x1 = x0 + sizeX -1;
		int y1 = y0 + sizeY -1;
		list[0]=(this.analyseHLine(y1,x0+1,x1-1,type));
		list[1]=(this.analyseVLine(x1,y0+1,y1-1,type));
		list[2]=(this.analyseHLine(y0,x0+1,x1-1,type));
		list[3]=(this.analyseVLine(x0,y0+1,y1-1,type));
		return list;
		}
	
	/**
	 * @note	This method is not required to be documented.
	 */
	public boolean[] colidesSolid(Organism org,int x0, int y0) {
		boolean[] listGROUND = this.hitTile(org,x0,y0+1,GeologicalFeature.SOLID);
		boolean[] listICE = this.hitTile(org,x0,y0+1,GeologicalFeature.ICE);
		boolean [] listResult = new boolean[4];
		for (int i=0 ; i<Math.min( listGROUND.length , listICE.length) ; i++ ) {
			listResult[i] = listGROUND[i] || listICE[i]; }
		return listResult;
	}
	
	/**
	 * @note	This method is not required to be documented.
	 */
	public boolean[] hitWater(Organism org,int x0, int y0) {
		return this.hitTile(org,x0,y0,GeologicalFeature.WATER);
	}
	
	/**
	 * @note	This method is not required to be documented.
	 */
	public boolean[] hitLava(Organism org,int x0, int y0) {
		return this.hitTile(org,x0,y0,GeologicalFeature.MAGMA);
	}
	
	/**
	 * @note	This method is not required to be documented.
	 */
	public boolean overlapTile(Organism org,int x0, int y0, GeologicalFeature type) {
		int sizeT = this.getTileSize();
		int sizeX = org.getCurrentSize()[0];
		int sizeY = org.getCurrentSize()[1];
		int x1 = x0 + sizeX -1;
		int y1 = y0 + sizeY -2;
		for (int i=0;i<sizeY;i+=sizeT) {
			int y = y0 + i;
			if (this.analyseHLine(y, x0, x1, type)){
				return true;
			}
		}

		return (this.analyseHLine(y1, x0, x1, type));
	}
	
	/**
	 * @note	This method is not required to be documented.
	 */
	public boolean overlapsSolid(Organism org,int x0, int y0) {
		return (this.overlapTile(org,x0,y0+1,GeologicalFeature.SOLID) || this.overlapTile(org, x0, y0+1, GeologicalFeature.ICE));
	}
	
	/**
	 * @note	This method is not required to be documented.
	 */
	public boolean overlapsWater(Organism org,int x0, int y0) {
		return this.overlapTile(org,x0,y0,GeologicalFeature.WATER);
	}
	
	/**
	 * @note	This method is not required to be documented.
	 */
	public boolean overlapsLava(Organism org,int x0, int y0) {
		return this.overlapTile(org,x0,y0,GeologicalFeature.MAGMA);
	}
	
	/**
	 * @note	This method is not required to be documented.
	 */
	public boolean overlapsGas(Organism org, int x0, int y0) {
		return this.overlapTile(org, x0, y0, GeologicalFeature.GAS);
	}
	
	/**
	 * Returns whether a given object is in this world.
	 * 
	 * @param 	organism
	 * 			The organism of which its precence needs to be checked.
	 * @return	Returns true if this.getOrganism().contains(organism).
	 * 			|result == this.getOrganism().contains(organism)
	 */
	public boolean hasAsGameObject(Object organism) {
		return this.getOrganism().contains(organism);
	}
	
	/**
	 * Returns whether this world has schools.
	 * 
	 * @return	Returns true if this.getSchools() != null and this.getSchools().size() > 0
	 * 			|result == (this.getSchools() != null and this.getSchools().size() > 0)
	 */
	public boolean hasSchools() {
		if(this.getSchools() == null) {
			return false;
		}
		return (this.getSchools().size() > 0);			
	}

	
	
}
