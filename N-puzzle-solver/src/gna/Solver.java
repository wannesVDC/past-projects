package gna;

import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import libpract.PriorityFunc;

public class Solver
{
	
	
	private Board initialBoard;
	private Board solutionBoard;
	
	//adding to this list will put the boards in order
	private List<Board> priorityList = new ArrayList<>();
	private List<String> previousBoards = new ArrayList<>();

	//returns a copy of the solutionBoard
	public Board getSolutionBoard() {
		Board ret = new Board(this.solutionBoard.getTiles());
		ret.setNumberOfSteps(this.solutionBoard.getNumberOfSteps());
		ret.setPreviousBoard(this.solutionBoard.getPreviousBoard());
		return ret;
	}
	
	
	//returns copy of priorityList
	public List<Board> getPriorityList(){
		List<Board> retList = new ArrayList<>();

		for (int i = 0; i<this.priorityList.size(); i++){
			retList.add(i,this.priorityList.get(i));
		}
		return retList;
	}

	//returns copy of previousBoards
	public List<String> getPreviousBoards(){
		List<String> retList = new ArrayList<>();

		for (int i = 0; i<this.previousBoards.size(); i++){
			retList.add(i,this.previousBoards.get(i));
		}
		return retList;
	}


	//add a board to the priority list
	public void addToPriorityList(Board addBoard, PriorityFunc priority){
		boolean canAdd = true;
		for (int i = 0; i<this.previousBoards.size(); i++){
			String prev = this.previousBoards.get(i);
			if(addBoard.toString().equals(prev)){
				canAdd = false;
				i = this.previousBoards.size()+1;//end the loop when it's found
			}
		}
		if (canAdd){
			for (int i = 0; i<this.priorityList.size(); i++){
				Board b = this.priorityList.get(i);
				if(addBoard.equals(b)){
					canAdd = false;
					i = this.priorityList.size()+1;//end the loop when it's found
				}
			}
		}	
		if (canAdd){
			if(this.priorityList.size() == 0){
				this.priorityList.add(addBoard);
			}
			else{
				Boolean added = false;
				if(priority == PriorityFunc.HAMMING){
					for (int i = 0; i<this.priorityList.size(); i++) {
						if (this.priorityList.get(i).hamming() <= addBoard.hamming()) {
							this.priorityList.add(i, addBoard);
							i = this.priorityList.size()+1;
							added = true;
						}
					}
				}
				else if (priority == PriorityFunc.MANHATTAN){
					for (int i = 0; i<this.priorityList.size(); i++) {
						if (this.priorityList.get(i).manhattan() <= addBoard.manhattan()) {
							this.priorityList.add(i, addBoard);
							i = this.priorityList.size();
							added = true;
						}
					}
				}
				if (!added) {//if the priority of the boards in the list are never smaller than addBoards, add it on the end of the list
					this.priorityList.add(this.priorityList.size(), addBoard);
				}
			}
		}
	}

	//1 step towards the solution
	public void advanceSolution(PriorityFunc priority){
		if (this.priorityList.size() > 0){
			Board last = this.priorityList.get(this.priorityList.size()-1);
			//add the previous one to the previousBoards list and remove it from priorityList
			this.previousBoards.add(last.toString());
			this.priorityList.remove(last);
			//find neighbors:
			Collection<Board> neighbors = last.neighbors();
			for (Board n : neighbors){
				n.setPreviousBoard(last);
				n.setNumberOfSteps(last.getNumberOfSteps()+1);
				this.addToPriorityList(n, priority);
			}
		}
	}

	//true if the puzzle is solved, false otherwise
	public boolean isSolved(){
		//the priority list is ordered so that the last one in the list will be closest to the solution at all times
		// thus if the puzzle is solved, the last one contains the solution and we only need to check that one
		if (this.priorityList.size() > 0){
			return this.priorityList.get(this.priorityList.size()-1).isSolution();
		}
		return false;
	}

	/**
	 * Finds a solution to the initial board.
	 *
	 * @param priority is either PriorityFunc.HAMMING or PriorityFunc.MANHATTAN
	 */
	public Solver(Board initial, PriorityFunc priority)
	{
		this.initialBoard = initial;
		this.initialBoard.setPreviousBoard(null);
		this.initialBoard.setNumberOfSteps(0);
		this.priorityList.add(initial);
		
		//while it's not been solved, keep progressing one step towards the solution
		if (initial.isSolvable()){
			while (!this.isSolved()){
				this.advanceSolution(priority);
			}
			this.solutionBoard = this.priorityList.get(this.priorityList.size()-1);
		}
		else{
			System.out.println("niet solvable");
			//TODO throw exception ofzo
		}
		
	}
	
	/**
	 * Returns a List of board positions as the solution. It should contain the initial
	 * Board as well as the solution (if these are equal only one Board is returned).
	 */
	public List<Board> solution()
	{	
		//the total number of boards is one higher than the number of steps (initial with step 0)
		//to get an ordered list starting at initial, put every board at the index equal to their number of steps
		
		List<Board> ret = new ArrayList<>();
		ret.add(0, this.initialBoard);
		
		for (int i = 1; i<=this.solutionBoard.getNumberOfSteps(); i++) {
			ret.add(i,null); //so that I can fill the boards in in reverse order
		}
		if (!this.solutionBoard.equals(this.initialBoard)){
			Board current = this.solutionBoard;
			while (current.getPreviousBoard() != null){
				ret.set(current.getNumberOfSteps(),current);
				current = current.getPreviousBoard();
			}
		}
		return ret;
	}

	//alternate constructor of Solver that does not find a solution
	//this is used for testing the functions easier
	public Solver(Board initial){
		this.initialBoard = initial;
		this.initialBoard.setPreviousBoard(null);
		this.initialBoard.setNumberOfSteps(0);
		this.priorityList.add(initial);
	}
}
