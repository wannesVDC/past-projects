package gna;

import java.util.Collection;
import java.util.Arrays;
import java.util.ArrayList;

public class Board
{
	private int[][] tiles;
	
	private final int emptyBlock;
	private final int boardSize;

	private Board previousBoard;
	private int numberOfSteps;

	private int hamming;
	private int manhattan;

	private String tilesString;

	public int hamming(){
		return this.hamming;
	}

	public int manhattan(){
		return this.manhattan;
	}



	//TODO: accesability lower
	public void setPreviousBoard(Board oldBoard){
		this.previousBoard = oldBoard;
	}
	public Board getPreviousBoard(){
		return this.previousBoard;
	}

	public void setNumberOfSteps(int steps){
		int difference = steps - this.numberOfSteps;
		this.numberOfSteps = steps;
		
		this.hamming += difference;
		this.manhattan += difference;
	}

	public int getNumberOfSteps(){
		return this.numberOfSteps;
	}

	//returns a clone of tiles
	public int[][] getTiles(){
		if(this.tiles.length > 0){
			int[][] ret = new int[this.tiles.length][this.tiles.length];
			for (int i = 0; i<this.tiles.length; i++){
				for (int j = 0; j<this.tiles.length; j++){
					ret[i][j] = this.tiles[i][j];
				}
			}
			return ret;
		}
		return null;
	}

	private void setTiles(int[][] newTiles){//TODO throw exception when changing sizes
		// ik definier het lege vakje als tiles.length*tiles.length, dit is eenvoudiger mee te werken gezien de formules voor coordinaten dan nog steeds gelden
		// soms wordt er als input het lege vakje als 0 gegeven, dit verander ik hier
		// gezien er nooit een andere 0 dan het lege vakje kan voorkomen hoef ik geen rekening te houden met welke 0
		if(newTiles.length == this.boardSize){	
			this.tiles = new int[this.boardSize][this.boardSize];
			for (int i = 0; i<this.boardSize; i++){
				for (int j = 0; j<this.boardSize; j++){
					int block = newTiles[i][j];
					if (block == 0){
						this.tiles[i][j] = this.emptyBlock;
					}
					else{
						this.tiles[i][j] = block;
					}
				}
			}
		}
	}
	// construct a board from an N-by-N array of tiles
	public Board( int[][] tiles )
	{// TODO: exceptions

		// ik definier het lege vakje als tiles.length*tiles.length, dit is eenvoudiger mee te werken gezien de formules voor coordinaten dan nog steeds gelden
		// soms wordt er als input het lege vakje als 0 gegeven, dit verander ik in setTiles
		this.emptyBlock = tiles.length*tiles.length;
		this.boardSize = tiles.length;

		//makes copy of input 'tiles' and sets this.tiles
		this.setTiles(tiles);
		this.setHamming();
		this.setManhattan();
		this.setString();
	}
	
	// return number of blocks out of place + numberOfSteps
	public void setHamming(){
		int counter = 0;
		for (int i = 0; i<this.boardSize; i++){
			for (int j = 0; j<this.boardSize; j++){// if the number is correct it is equal to n*i+(j+1)
				int block = this.tiles[i][j];
				if (block != this.goalForCoords(new int[]{i,j}) && block != this.emptyBlock){
					counter++;
				}
			}
		}
		this.hamming =  counter+this.getNumberOfSteps();
	}
	
	
	//returns the coordinates of the given block
	public int[] getCoordsBlock(int block){//TODO exceptions
		for (int i = 0; i<this.boardSize; i++){
			for (int j = 0; j<this.boardSize; j++){
				if (this.tiles[i][j] == block){
					return new int[]{i,j};
				}
			}
		}
		return null;
	}

	//returns blocknumber that should end there
	public int goalForCoords(int[] coords){
		return (this.boardSize*coords[0] + coords[1] + 1);
	}

	//returns end coords for the given block
	public int[] goalForBlock(int block){
		int tempBlock = block;
		int i = 0;
		while(tempBlock > this.boardSize){
			tempBlock -= this.boardSize;
			i++;
		}
		int j = tempBlock-1;
		return new int[] {i,j};
	}

	//returns true is the given block is in it's place, false otherwise
	public boolean isInPlace(int block){//TODO exceptions
		int[] temp = this.goalForBlock(block);
		return (this.tiles[temp[0]][temp[1]] == block);
	}

	// return sum of Manhattan distances between blocks and goal and the number of steps taken
	public void setManhattan()
	{
		int distance = 0;
		for (int i = 0; i<this.boardSize; i++){
			for (int j = 0; j<this.boardSize; j++){
				distance += this.singleManhattan(i,j);
			}
		}

		this.manhattan = distance+this.getNumberOfSteps();
	}

	//returns manhattan distance for the given block
	// this is a hulp function for manhattan(), it's sufficient to test manhattan()
	public int singleManhattan(int i, int j){
		int block = this.tiles[i][j];
		if (block < this.emptyBlock){
			int[] goalCoords = this.goalForBlock(block);	
			return (Math.abs(i-goalCoords[0]) + Math.abs(j-goalCoords[1]));
		}
		return 0; 
	}
	
	// Does this board equal y. Two boards are equal when they both were constructed
	// using tiles[][] arrays that contained the same values.
	@Override
	public boolean equals(Object y)
	{
		if ( !(y instanceof Board) )
			return false;

		Board other = (Board)y;
		return Arrays.deepEquals(tiles, other.tiles);
	}

	// Since we override equals(), we must also override hashCode(). When two objects are
	// equal according to equals() they must return the same hashCode. More info:
	// - http://stackoverflow.com/questions/27581/what-issues-should-be-considered-when-overriding-equals-and-hashcode-in-java/27609#27609
	// - http://www.ibm.com/developerworks/library/j-jtp05273/
    @Override
    public int hashCode()
	{
		return Arrays.deepHashCode(tiles);
	}
	
	// return a Collection of all neighboring board positions
	public Collection<Board> neighbors()
	{
		int[] emptyCoords = this.getCoordsBlock(this.emptyBlock);

		int[][] tilesClone = this.getTiles(); //the function getTiles() already clones it
		
		Collection<Board> newBoards = new ArrayList<>();
		for (int k = 0; k<4; k++){//0:up, 1:right, 2:down, 3:left
			
			if (k==0 && (emptyCoords[0]-1 >= 0)){
				//swap blocks
				tilesClone[emptyCoords[0]][emptyCoords[1]] = this.tiles[emptyCoords[0]-1][emptyCoords[1]];
				tilesClone[emptyCoords[0]-1][emptyCoords[1]] = this.tiles[emptyCoords[0]][emptyCoords[1]];

				Board upBoard = new Board(tilesClone);
				newBoards.add(upBoard);

				//revert changes made to tilesclone
				tilesClone[emptyCoords[0]][emptyCoords[1]] = this.tiles[emptyCoords[0]][emptyCoords[1]];
				tilesClone[emptyCoords[0]-1][emptyCoords[1]] = this.tiles[emptyCoords[0]-1][emptyCoords[1]];
			}
			if (k==1 && (emptyCoords[1]+1 < this.tiles[emptyCoords[0]].length)){
				//swap blocks
				tilesClone[emptyCoords[0]][emptyCoords[1]] = this.tiles[emptyCoords[0]][emptyCoords[1]+1];
				tilesClone[emptyCoords[0]][emptyCoords[1]+1] = this.tiles[emptyCoords[0]][emptyCoords[1]];

				Board rightBoard = new Board(tilesClone);
				newBoards.add(rightBoard);

				//revert changes made to tilesclone
				tilesClone[emptyCoords[0]][emptyCoords[1]] = this.tiles[emptyCoords[0]][emptyCoords[1]];
				tilesClone[emptyCoords[0]][emptyCoords[1]+1] = this.tiles[emptyCoords[0]][emptyCoords[1]+1];
			}
			if (k==2 && (emptyCoords[0]+1 < this.tiles.length)){
				//swap blocks
				tilesClone[emptyCoords[0]][emptyCoords[1]] = this.tiles[emptyCoords[0]+1][emptyCoords[1]];
				tilesClone[emptyCoords[0]+1][emptyCoords[1]] = this.tiles[emptyCoords[0]][emptyCoords[1]];
				
				Board downBoard = new Board(tilesClone);
				newBoards.add(downBoard);

				//revert changes made to tilesclone
				tilesClone[emptyCoords[0]][emptyCoords[1]] = this.tiles[emptyCoords[0]][emptyCoords[1]];
				tilesClone[emptyCoords[0]+1][emptyCoords[1]] = this.tiles[emptyCoords[0]+1][emptyCoords[1]];
			}
			if (k==3 && (emptyCoords[1]-1 >= 0)){
				//swap blocks
				tilesClone[emptyCoords[0]][emptyCoords[1]] = this.tiles[emptyCoords[0]][emptyCoords[1]-1];
				tilesClone[emptyCoords[0]][emptyCoords[1]-1] = this.tiles[emptyCoords[0]][emptyCoords[1]];
				
				Board leftBoard = new Board(tilesClone);
				newBoards.add(leftBoard);
			}
		}
		return newBoards;
	}
	
	// return a string representation of the board
	public String toString()
	{
		return this.tilesString;
	}
	
	// set the string value that needs to be returned in toString()
	public void setString(){
		//since the setString function given tiles does nog change the tiles in any way, it's not needed to give a clone of the tiles
		this.tilesString = this.setString(this.tiles);
	}

	//return a string representation of the given tiles
	public String setString(int[][] tiles){
		String ret = "";
		for (int i = 0; i<tiles.length; i++){
			for (int j = 0; j<tiles.length; j++){
				int block = tiles[i][j];
				if (block != this.emptyBlock){
					ret += Integer.toString(block);
				}
				else{
					ret += " ";
				}
			}
		}
		return ret;
	}


	// is the initial board solvable? Note that the empty tile must
	// first be moved to its correct position.
	public boolean isSolvable()
	{
		int[][] tempTiles = this.getTiles();
		//Board tempBoard = new Board(tempTiles);
		int[] goalCoords = this.goalForBlock(this.emptyBlock);
		int[] currentCoords = this.getCoordsBlock(this.emptyBlock);
		int i = currentCoords[0];
		int j = currentCoords[1];
		int iGoal = goalCoords[0];
		int jGoal = goalCoords[1];
		int tempBlock;

		while (i < iGoal){
			
			tempBlock = tempTiles[i+1][j];
			tempTiles[i][j] = tempBlock;
			tempTiles[i+1][j] = this.emptyBlock;
			i++;

			//currentCoords = tempBoard.getCoordsBlock(this.emptyBlock);
		}

		while (j < jGoal){

			tempBlock = tempTiles[i][j+1];
			tempTiles[i][j] = tempBlock;
			tempTiles[i][j+1] = this.emptyBlock;
			j++;

			//currentCoords = tempBoard.getCoordsBlock(this.emptyBlock);
		}
		
		//Board tempBoard = new Board(tempTiles);
		return (new Board(tempTiles)).solvableCorrectPosition();

	}

	//given a board where the empty tile is in it's place already, true if the puzzle is solvable, false otherwise
	public boolean solvableCorrectPosition(){//hulp function, is tested by testing isSolvable()
		float product = 1;

		int upper, lower;
		float total;

		ArrayList<Integer> p = new ArrayList<>(); //acces p(x) door p.get(x-1) 
		for (int j = 1; j<this.emptyBlock; j++){
			p.add(this.p(j));
			for (int i = 1; i<j; i++){
				upper = p.get(j-1) - p.get(i-1); //dit is p(j) - p(i)
				lower = j-i;
				total = (float) upper/ (float) lower;
				
				product *= total;
			}
		}
		return (product >= 0);
		
	}

	//this is a hulp function of solvableCorrectPosition()
	// it's the function p described in the assignement
	public int p(int block){
		int[] coords = this.getCoordsBlock(block);// i*length + j + 1

		return (coords[0]*this.boardSize)+coords[1]+1;
	}

	

	//returns true if the board is a solution, false otherwise
	public boolean isSolution(){// TODO reform to use toString() (easier and more reliable)
		String solution = "";
		for (int i = 1; i<this.emptyBlock; i++) {
			solution += Integer.toString(i);
		}
		solution += " ";
		return (solution.equals(this.toString()));
	}


}
