package gna;

import java.util.Collection;
import java.util.List;
import libpract.PriorityFunc;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 * A number of JUnit tests for Solver.
 *
 * Feel free to modify these to automatically test puzzles or other functionality
 */
public class UnitTests {
 
	/*
  public void test() {
    this.boardTests();
    this.solverTests();
  }

  // Board.java tests
  public void boardTests(){
    this.boardConstructionTestBasic();
    this.boardConstrucionLeakTest();
    this.hammingTests();
    this.manhattanTests();
    this.getCoordsBlockTest();
    this.goalForCoordsTest();
    this.goalForBlockTest();
    this.isInPlaceTest();
    this.neighborsTest();
    this.toStringTest();
    this.isSolvableTest();
  }
  */
	
	private boolean doBigBoardTest = true; //only used in Board.java tests because Solver.java never uses the size of the tiles.
	
    //constructor tests
      //getTiles is a non-trivial function used in these tests, however, if a bug persists in 
      //the constructor of getTiles, the test will fail, thus both of these functions are tested here
      //since all the constructor does is calling setTiles, testing it is equivalent to testing setTiles

  //tests wether tiles is set properly
  @Test
  public void boardConstructionTestBasic(){

    int[][] testTiles = this.getRandomTiles();

    Board test = new Board(testTiles);
    //System.out.println(test);
    this.assertTilesCopyIsEqual(testTiles, test.getTiles());
  }

  //tests that a clone of the given tiles list is used and not the pointer
  @Test
  public void boardConstrucionLeakTest(){
    int[][] testTiles = this.getRandomTiles();

    Board test = new Board(testTiles);

    int max = testTiles.length*testTiles[0].length;
    testTiles[0][1] = max+1;//tiles can never be generated with a number higher than the sizes
    // if tiles in the board has been changed, it failed
    assertFalse("Board(int[][] tiles) does not clone the tiles",testTiles[0][1] == test.getTiles()[0][1]);
  }

    //hamming() tests
      //expected: the number of tiles in the wrong position, plus the number of moves done to get there
  @Test
  public void hammingTests(){
    //I will make some example Boards with given number of steps to check for the correct value
    //Considering I will need these boards for manhattan as well I will make them in a hulp function
    Board b1 = this.exampleBoard1();
    assertEquals("hamming() van exampleBoard1() onjuist", 10, b1.hamming());
    Board b2 = this.exampleBoard2();
    assertEquals("hamming() van exampleBoard2() onjuist", 12, b2.hamming());
    
    if (this.doBigBoardTest) {
    	Board big = this.exampleBoardBig();
    	assertEquals("hamming() van exampleBoardBig() onjuist", 23, big.hamming());
    }
  }

    //manhattan() tests
      //expected: the sum of displacement on the blocks plus the number of moves done to get there
  @Test
  public void manhattanTests(){
    Board b1 = this.exampleBoard1();
    assertEquals("manhattan() van exampleBoard1() onjuist", 13, b1.manhattan());
    Board b2 = this.exampleBoard2();
    assertEquals("manhattan() van exampleBoard2() onjuist", 15, b2.manhattan());
    if (this.doBigBoardTest) {
    	Board big = this.exampleBoardBig();
    	assertEquals("hamming() van exampleBoardBig() onjuist", 70, big.manhattan());
    }
  }
  
    //getCoordsBlock(int block) tests
  @Test
    public void getCoordsBlockTest(){
      Board b1 = this.exampleBoard1();
      Board b2 = this.exampleBoard2();
      //test for blocks 1,3,7,5,9
      
      int[] b11 = b1.getCoordsBlock(1);//[0,0]
      int[] b21 = b2.getCoordsBlock(1);//[0,1]
      assertEquals("block 1 van example 1 onjuist", 0, b11[0]);
      assertEquals("block 1 van example 1 onjuist", 0, b11[1]);
      assertEquals("block 1 van example 2 onjuist", 0, b21[0]);
      assertEquals("block 1 van example 2 onjuist", 1, b21[1]);

      int[] b13 = b1.getCoordsBlock(3);//[0,2]
      int[] b23 = b2.getCoordsBlock(3);//[2,0]
      assertEquals("block 3 van example 1 onjuist", 0, b13[0]);
      assertEquals("block 3 van example 1 onjuist", 2, b13[1]);
      assertEquals("block 3 van example 2 onjuist", 2, b23[0]);
      assertEquals("block 3 van example 2 onjuist", 0, b23[1]);

      int[] b15 = b1.getCoordsBlock(5);//[1,0]
      int[] b25 = b2.getCoordsBlock(5);//[1,0]
      assertEquals("block 5 van example 1 onjuist", 1, b15[0]);
      assertEquals("block 5 van example 1 onjuist", 0, b15[1]);
      assertEquals("block 5 van example 2 onjuist", 1, b25[0]);
      assertEquals("block 5 van example 2 onjuist", 0, b25[1]);

      int[] b17 = b1.getCoordsBlock(7);//[1,2]
      int[] b27 = b2.getCoordsBlock(7);//[2,1]
      assertEquals("block 7 van example 1 onjuist", 1, b17[0]);
      assertEquals("block 7 van example 1 onjuist", 2, b17[1]);
      assertEquals("block 7 van example 2 onjuist", 2, b27[0]);
      assertEquals("block 7 van example 2 onjuist", 1, b27[1]);

      int[] b19 = b1.getCoordsBlock(9);//[2,0]
      int[] b29 = b2.getCoordsBlock(9);//[1,2]
      assertEquals("block 9 (lege vak) van example 1 onjuist", 2, b19[0]);
      assertEquals("block 9 (lege vak) van example 1 onjuist", 0, b19[1]);
      assertEquals("block 9 (lege vak) van example 2 onjuist", 1, b29[0]);
      assertEquals("block 9 (lege vak) van example 2 onjuist", 2, b29[1]);
      
      if (this.doBigBoardTest) {
      	Board big = this.exampleBoardBig();
      	
      	// for 16, 21, 23
      	//	[1,2], [2,0], [4,3]
      	
      	int[] big16 = big.getCoordsBlock(16);
      	assertEquals("block 16 van example big onjuist", 1, big16[0]);
      	assertEquals("block 16 van example big onjuist", 2, big16[1]);
      	
      	int[] big21 = big.getCoordsBlock(21);
      	assertEquals("block 21 van example big onjuist", 2, big21[0]);
      	assertEquals("block 21 van example big onjuist", 0, big21[1]);
      	
      	int[] big23 = big.getCoordsBlock(23);
      	assertEquals("block 23 van example big onjuist", 4, big23[0]);
      	assertEquals("block 23 van example big onjuist", 3, big23[1]);
      }
    }

    //goalForCoords(int[] coords) tests
  @Test
    public void goalForCoordsTest(){
      //hier maakt het niet uit welk example ik gebruik, enkel tiles.lengt wordt gebruikt
      Board board = this.exampleBoard1();

      // test voor [0,0], [2,2], [0,2] [1,0] [2,1]
      //            1       9     3     4     8

      assertEquals("goalForCoords(new int[] {0,0}) fout", 1, board.goalForCoords(new int[] {0,0}));
      assertEquals("goalForCoords(new int[] {2,2}) fout", 9, board.goalForCoords(new int[] {2,2}));
      assertEquals("goalForCoords(new int[] {0,2}) fout", 3, board.goalForCoords(new int[] {0,2}));
      assertEquals("goalForCoords(new int[] {1,0}) fout", 4, board.goalForCoords(new int[] {1,0}));
      assertEquals("goalForCoords(new int[] {2,1}) fout", 8, board.goalForCoords(new int[] {2,1}));
      
      
      if(this.doBigBoardTest) {
    	  Board big = this.exampleBoardBig();
    	  
    	  // test voor [0,0], [4,4], [3,0], [0,1], [2,3]
    	  //			1		25    16	 2		14
    	  assertEquals("goalForCoords(new int[] {0,0}) fout (big board)", 1, big.goalForCoords(new int[] {0,0}));
    	  assertEquals("goalForCoords(new int[] {4,4}) fout (big board)", 25, big.goalForCoords(new int[] {4,4}));
    	  assertEquals("goalForCoords(new int[] {3,0}) fout (big board)", 16, big.goalForCoords(new int[] {3,0}));
    	  assertEquals("goalForCoords(new int[] {0,1}) fout (big board)", 2, big.goalForCoords(new int[] {0,1}));
    	  assertEquals("goalForCoords(new int[] {2,3}) fout (big board)", 14, big.goalForCoords(new int[] {2,3}));
    	  
      }
    }

    //goalForBlock(int block) tests
  @Test
    public void goalForBlockTest(){
      // ook hier maakt niet uit welke exampe
      Board board = this.exampleBoard1();

      //test voor 9, 3, 4, 5, 1

      int[] board9 = board.goalForBlock(9);//[2,2]
      int[] board3 = board.goalForBlock(3);//[0,2]
      int[] board4 = board.goalForBlock(4);//[1,0]
      int[] board5 = board.goalForBlock(5);//[1,1]
      int[] board1 = board.goalForBlock(1);//[0,0]

      assertEquals("goalForBlock(9) fout", 2,board9[0]);
      assertEquals("goalForBlock(9) fout", 2,board9[1]);

      assertEquals("goalForBlock(3) fout", 0,board3[0]);
      assertEquals("goalForBlock(3) fout", 2,board3[1]);

      assertEquals("goalForBlock(4) fout", 1,board4[0]);
      assertEquals("goalForBlock(4) fout", 0,board4[1]);

      assertEquals("goalForBlock(5) fout", 1,board5[0]);
      assertEquals("goalForBlock(5) fout", 1,board5[1]);

      assertEquals("goalForBlock(1) fout", 0,board1[0]);
      assertEquals("goalForBlock(1) fout", 0,board1[1]);
      
      if (this.doBigBoardTest) {
        	Board big = this.exampleBoardBig();
        	
        	// for 16, 21, 23
        	//	[3,0], [4,0], [4,2]
        	
        	int[] big16 = big.goalForBlock(16);
        	assertEquals("block 16 van example big onjuist", 3, big16[0]);
        	assertEquals("block 16 van example big onjuist", 0, big16[1]);
        	
        	int[] big21 = big.goalForBlock(21);
        	assertEquals("block 21 van example big onjuist", 4, big21[0]);
        	assertEquals("block 21 van example big onjuist", 0, big21[1]);
        	
        	int[] big23 = big.goalForBlock(23);
        	assertEquals("block 23 van example big onjuist", 4, big23[0]);
        	assertEquals("block 23 van example big onjuist", 2, big23[1]);
        }
    }


    //isInPlace(int block) tests
  @Test
    public void isInPlaceTest(){
      // ik maak een opgelost board als voorbeeld en test het voor elk block
      //example 2 heeft geen juiste blocken, hierbij test ik ook voor elk block
      Board solution = this.exampleBoardSolution();
      Board wrong = this.exampleBoard2();

      for (int i = 1; i<10; i++){
        assertTrue(String.format("isInPlace(%d) returned false, expected true",i), solution.isInPlace(i));
        assertFalse(String.format("isInPlace(%d) returned true, expected false",i), wrong.isInPlace(i));
      }
      
      if (this.doBigBoardTest) {
    	  solution = this.exampleBoardBigSolution();
    	  wrong = this.exampleBoardBig();//alles behavle block 1 staat fout, dus blok 1 doe ik apart
    	  
    	  assertTrue("isInPlace(1) returned false, expected true (big board)", solution.isInPlace(1));
    	  for (int i = 2; i<26; i++) {
    		  assertTrue(String.format("isInPlace(%d) returned false, expected true (big board)",i), solution.isInPlace(i));
    		  assertFalse(String.format("isInPlace(%d) returned true, expected false (big board)",i), wrong.isInPlace(i));
    	  }
      }
    }


    //neighbors() tests
  @Test
    public void neighborsTest(){
      // i will use the 3 example boards I've constructed to test this
      Board b1 = this.exampleBoard1();
      Board b2 = this.exampleBoard2();
      Board b3 = this.exampleBoardSolution();

      //in b1, the empty space is left-bottom and can move to the right or the top (2 neighbors)
      Collection<Board> neighbors1 = b1.neighbors();
      assertEquals("example 1 has the wrong number of neighbors", 2, neighbors1.size());

      int[][] tiles = new int[][]{ {1,2,3}, {5,4,7}, {6,9,8} };
      Board b1right = new Board(tiles);
      tiles = new int[][]{ {1,2,3}, {9,4,7}, {5,6,8} };
      Board b1up = new Board(tiles);

      for (Board b : neighbors1){
        if (!(b.equals(b1right) || b.equals(b1up))){
          int[] co = b.getCoordsBlock(9);
          fail(String.format("example 1 has incorrect neighbor (empty tile in [%d,%d])",co[0],co[1]));
        }
      }

      //in b2, the empty space is right-middle and can move to the left or the top or the bottom (3 neighbors)
      Collection<Board> neighbors2 = b2.neighbors();
      assertEquals("example 2 has the wrong number of neighbors", 3, neighbors2.size());
      
      tiles = new int[][]{ {2,1,6}, {5,4,8}, {3,7,9} };
      Board b2down = new Board(tiles);
      tiles = new int[][]{ {2,1,9}, {5,4,6}, {3,7,8} };
      Board b2up = new Board(tiles);
      tiles = new int[][]{ {2,1,6}, {5,9,4}, {3,7,8} };
      Board b2left = new Board(tiles);
      
      for (Board b : neighbors2){
        if (!(b.equals(b2left) || b.equals(b2up) || b.equals(b2down))){
          int[] co = b.getCoordsBlock(9);
          fail(String.format("example 2 has incorrect neighbor (empty tile in [%d,%d])",co[0],co[1]));
        }
      }


      //in b3, the empty space is right-bottom and can move to the left or the top (2 neighbors)
      Collection<Board> neighbors3 = b3.neighbors();
      assertEquals("example 3 has the wrong number of neighbors", 2, neighbors3.size());

      tiles = new int[][]{ {1,2,3}, {4,5,6}, {7,9,8} };
      Board b3left = new Board(tiles);
      tiles = new int[][]{ {1,2,3}, {4,5,9}, {7,8,6} };
      Board b3up = new Board(tiles);

      for (Board b : neighbors3){
        if (!(b.equals(b3left) || b.equals(b3up))){
          int[] co = b.getCoordsBlock(9);
          fail(String.format("example 3 has incorrect neighbor (empty tile in [%d,%d])",co[0],co[1]));
        }
      }
      
      if (this.doBigBoardTest) {
    	  Board b = this.exampleBoardBig();
    	  
    	  Collection<Board> neighbors = b.neighbors();
          assertEquals("example big has the wrong number of neighbors", 4, neighbors.size());
          
          int[] emptyCo = b.getCoordsBlock(25);
          
          for (Board n : neighbors) {
        	  int block = n.getTiles()[emptyCo[0]][emptyCo[1]];
        	  if (block != 10 && block != 24 && block != 16 && block != 6) {
        		  fail("big board has incorrect neighbor");
        	  }
        	  else {
        		  int[] newEmptyCo = b.getCoordsBlock(block);
        		  assertEquals("big board has incorrect neighbor", 25, n.getTiles()[newEmptyCo[0]][newEmptyCo[1]]);
        	  }
          }
      }
      
    }

    //toString() tests
  @Test
    public void toStringTest(){
      //I will once again use the 3 examples already made
      Board b1 = this.exampleBoard1();
      Board b2 = this.exampleBoard2();
      Board b3 = this.exampleBoardSolution();

      String string1 = "123547 68";
      String string2 = "21654 378";
      String string3 = "12345678 ";

      assertEquals("example 1 toString()", string1, b1.toString());      
      assertEquals("example 2 toString()", string2, b2.toString());      
      assertEquals("example 3 toString()", string3, b3.toString());      
      
      
      if (this.doBigBoardTest) {
    	  Board b = this.exampleBoardBig();
    	  String s = "1171310719516 24219126202218814113422315";
    	  
    	  assertEquals("example big toString()", s, b.toString());
      }
    }

    //isSolvable() tests
  @Test
    public void isSolvableTest(){
      //hier gebruik ik enkele voorbeelden meegegeven bij de opgave, waarvan ik de oplosbaarheid ken
      int[][] tiles = new int[][] { {1,2,3}, {4,6,5}, {7,8,9} };
      Board unsolvable = new Board(tiles);
      assertFalse("isSolvable() returned true, expected false", unsolvable.isSolvable());

      tiles = new int[][] { {9,1,3}, {4,2,5}, {7,8,6} };
      Board solvable1 = new Board(tiles);
      assertTrue("isSolvable() returned false, expected true (1)", solvable1.isSolvable());

      tiles = new int[][] { {8,4,7}, {1,5,6}, {3,2,9} };
      Board solvable2 = new Board(tiles);
      assertTrue("isSolvable() returned false, expected true (2)", solvable2.isSolvable());

      tiles = new int[][] { {6,9,7,4}, {2,5,10,8}, {3,11,1,12}, {13,14,15,16} };
      Board solvable3 = new Board(tiles);
      assertTrue("isSolvable() returned false, expected true (3)", solvable3.isSolvable());
    }


/*
//Solver.java tests
public void solverTests(){
  this.addToPriorityListTest();
  this.advanceSolutionTest();
  this.SolverTestHamming();
  this.SolverTestManhattan();
  this.solutionTest();
}
*/
  
  // addToPriorityList tests
@Test
  public void addToPriorityListTest(){
    // I will add the 3 examples for both priority functions and see that it is ordered correctly
    // lowest priority number will be the latest element, this should be closest to the solution

    Board b1 = this.exampleBoard1();
    Board b2 = this.exampleBoard2();
    Board b3 = this.exampleBoardSolution();
    b3.setNumberOfSteps(0);

    //hamming: b2,b1,b3
    Solver s = new Solver(b1);

    s.addToPriorityList(b2,PriorityFunc.HAMMING);
    s.addToPriorityList(b3,PriorityFunc.HAMMING);

    assertEquals("addToPriorityList() with hamming gives wrong size priorityList (1)",3,s.getPriorityList().size());

    assertEquals("addToPriorityList with hamming gives wrong Board at index 0 (1)", b2,s.getPriorityList().get(0));
    assertEquals("addToPriorityList with hamming gives wrong Board at index 1 (1)", b1,s.getPriorityList().get(1));
    assertEquals("addToPriorityList with hamming gives wrong Board at index 2 (1)", b3,s.getPriorityList().get(2));

    //manhattan: b2,b1,b3
    s = new Solver(b1);

    s.addToPriorityList(b2,PriorityFunc.MANHATTAN);
    s.addToPriorityList(b3,PriorityFunc.MANHATTAN);

    assertEquals("addToPriorityList() with manhattan gives wrong size priorityList (1)",3,s.getPriorityList().size());

    assertEquals("addToPriorityList with manhattan gives wrong Board at index 0 (1)", b2,s.getPriorityList().get(0));
    assertEquals("addToPriorityList with manhattan gives wrong Board at index 1 (1)", b1,s.getPriorityList().get(1));
    assertEquals("addToPriorityList with manhattan gives wrong Board at index 2 (1)", b3,s.getPriorityList().get(2));


    //try with solution 20 steps, so it has the highest number in both
    b3.setNumberOfSteps(20);
    
    //hamming: b3,b2,b1
    s = new Solver(b1);

    s.addToPriorityList(b2,PriorityFunc.HAMMING);
    s.addToPriorityList(b3,PriorityFunc.HAMMING);

    assertEquals("addToPriorityList() with hamming gives wrong size priorityList (2)",3,s.getPriorityList().size());

    assertEquals("addToPriorityList with hamming gives wrong Board at index 0 (2)", b3,s.getPriorityList().get(0));
    assertEquals("addToPriorityList with hamming gives wrong Board at index 1 (2)", b2,s.getPriorityList().get(1));
    assertEquals("addToPriorityList with hamming gives wrong Board at index 2 (2)", b1,s.getPriorityList().get(2));

    //manhattan: b3,b2,b1
    s = new Solver(b1);

    s.addToPriorityList(b2,PriorityFunc.MANHATTAN);
    s.addToPriorityList(b3,PriorityFunc.MANHATTAN);

    assertEquals("addToPriorityList() with manhattan gives wrong size priorityList (2)",3,s.getPriorityList().size());

    assertEquals("addToPriorityList with manhattan gives wrong Board at index 0 (2)", b3,s.getPriorityList().get(0));
    assertEquals("addToPriorityList with manhattan gives wrong Board at index 1 (2)", b2,s.getPriorityList().get(1));
    assertEquals("addToPriorityList with manhattan gives wrong Board at index 2 (2)", b1,s.getPriorityList().get(2));

    // try adding the same board again, the list should not change
    Board b1Again = this.exampleBoard1();
    s.addToPriorityList(b1Again,PriorityFunc.HAMMING);
    assertEquals("addToPriorityList() with hamming changes size when same board is added twice",3,s.getPriorityList().size());

    assertEquals("addToPriorityList with hamming gives wrong Board at index 0 when same board is added twice", b3,s.getPriorityList().get(0));
    assertEquals("addToPriorityList with hamming gives wrong Board at index 1 when same board is added twice", b2,s.getPriorityList().get(1));
    assertEquals("addToPriorityList with hamming gives wrong Board at index 2 when same board is added twice", b1,s.getPriorityList().get(2));
    s.addToPriorityList(b1,PriorityFunc.MANHATTAN);
    assertEquals("addToPriorityList() with manhattan changes size when same board is added twice",3,s.getPriorityList().size());

    assertEquals("addToPriorityList with manhattan gives wrong Board at index 0 when same board is added twice", b3,s.getPriorityList().get(0));
    assertEquals("addToPriorityList with manhattan gives wrong Board at index 1 when same board is added twice", b2,s.getPriorityList().get(1));
    assertEquals("addToPriorityList with manhattan gives wrong Board at index 2 when same board is added twice", b1,s.getPriorityList().get(2));
  }

  // advanceSolution() tests
@Test
  public void advanceSolutionTest(){
    //test that new boards have the correct previousBoard and numberOfSteps
    //test that previousBoards has the old board and priorityList does not

    //priority is only used to call on addToPriorityList(), so testing with either one is sufficient
    
    //use exampleBoards 1 and 2 to check
    Board b1 = this.exampleBoard1();//steps will be set to 0 in constructor because it is the initialBoard
    Board b2 = this.exampleBoard2();
    
    Solver s1 = new Solver(b1);
    Solver s2 = new Solver(b2);

    s1.advanceSolution(PriorityFunc.HAMMING);
    s2.advanceSolution(PriorityFunc.HAMMING);

    List<String> prevb1 = s1.getPreviousBoards();
    List<String> prevb2 = s2.getPreviousBoards();

    List<Board> prio1 = s1.getPriorityList();
    List<Board> prio2 = s2.getPriorityList();

    //because we only added 1 board to the priorityList, after checking the old board is removed, we know only the new boards remain
    // checks for oldBoard first

    assertTrue("advanceSolution() previousBoards of example 1 does not contain the old Board", this.doesStringListContain(prevb1,b1));
    assertTrue("advanceSolution() previousBoards of example 2 does not contain the old Board", this.doesStringListContain(prevb2,b2));

    assertFalse("advanceSolution() priorityList of example 1 still contains the old Board", this.doesBoardListContain(prio1,b1));
    assertFalse("advanceSolution() priorityList of example 2 still contains the old Board", this.doesBoardListContain(prio2,b2));

    //now priorityList should be only the new Boards, I will iterate through it to test steps and previousboard
    for (Board b : prio1){
      assertEquals("advanceSolution() new Board does not have correct number of steps",1, b.getNumberOfSteps());
      assertEquals("advanceSolution() new Board does not have correct previousBoard",b1,b.getPreviousBoard());
    }
    for (Board b : prio2){
      assertEquals("advanceSolution() new Board does not have correct number of steps",1, b.getNumberOfSteps());
      assertEquals("advanceSolution() new Board does not have correct previousBoard",b2,b.getPreviousBoard());
    }

    //test for a second iteration with other boards in the list
    this.advanceSolutionTest2(s1,s2);
  }

  //advanceSolution second iteration test (called in advanceSolutionTest())
  public void advanceSolutionTest2(Solver s1, Solver s2){
    List<Board> priorityBefore1 = s1.getPriorityList();
    List<Board> priorityBefore2 = s2.getPriorityList();

    s1.advanceSolution(PriorityFunc.HAMMING);
    s2.advanceSolution(PriorityFunc.HAMMING);

    Board b1 = priorityBefore1.get(priorityBefore1.size()-1);
    Board b2 = priorityBefore2.get(priorityBefore2.size()-1);

    List<Board> prio1 = s1.getPriorityList();
    List<Board> prio2 = s2.getPriorityList();

    List<String> prevb1 = s1.getPreviousBoards();
    List<String> prevb2 = s2.getPreviousBoards();
    // perform the same tests, but a little more complicated

    assertTrue("2. advanceSolution() previousBoards of example 1 does not contain the old Board", this.doesStringListContain(prevb1,b1));
    assertTrue("2. advanceSolution() previousBoards of example 2 does not contain the old Board", this.doesStringListContain(prevb2,b2));

    assertFalse("2. advanceSolution() priorityList of example 1 still contains the old Board", this.doesBoardListContain(prio1,b1));
    assertFalse("2. advanceSolution() priorityList of example 2 still contains the old Board", this.doesBoardListContain(prio2,b2));

    // priorityList now contains the new Boards among others
    // I will compare to priorityBefore lists to find the new Boards to check
    for (Board b : prio1){
      if (!this.doesBoardListContain(priorityBefore1,b)){// only check for the new
        assertEquals("advanceSolution() new Board does not have correct number of steps",2, b.getNumberOfSteps());
        assertEquals("advanceSolution() new Board does not have correct previousBoard",b1,b.getPreviousBoard());
      }
    }
    for (Board b : prio2){
      if (!this.doesBoardListContain(priorityBefore2,b)){
        assertEquals("advanceSolution() new Board does not have correct number of steps",2, b.getNumberOfSteps());
        assertEquals("advanceSolution() new Board does not have correct previousBoard",b2,b.getPreviousBoard());
      }
    }
  }

@Test
  public void SolverTestHamming(){
    Board b1 = this.exampleBoard1();
    Solver s = new Solver(b1,PriorityFunc.HAMMING);
    
    //solutionboard should contain the solution, with a number of steps higher than 0
    // is should have previousboards with one step less for each one
    // when number of steps reaches 0, it should be the initial board (b1) and have 0 steps and null as previous
    Board b1Solution = s.getSolutionBoard();
    
    assertTrue("Solver: given solution is not a solution", b1Solution.isSolution());
    
    Board current = b1Solution;
    for (int i = b1Solution.getNumberOfSteps(); i>0; i--) {
    	assertEquals("numberOfSteps incorrect", i, current.getNumberOfSteps());
    	current = current.getPreviousBoard();
    }
    //after this loop, current is the initial board gotten through previousBoards
    assertEquals(0, current.getNumberOfSteps());
    assertNull("initial board from solution has previous board", current.getPreviousBoard());
    assertTrue("initial board from solutino is not actual initial board", b1.equals(current));
  }

@Test
  public void SolverTestManhattan() {
	    Board b1 = this.exampleBoard1();
	    Solver s = new Solver(b1,PriorityFunc.MANHATTAN);
	    
	    //solutionboard should contain the solution, with a number of steps higher than 0
	    // is should have previousboards with one step less for each one
	    // when number of steps reaches 0, it should be the initial board (b1) and have 0 steps and null as previous
	    Board b1Solution = s.getSolutionBoard();
	    
	    assertTrue("Solver: given solution is not a solution", b1Solution.isSolution());
	    
	    Board current = b1Solution;
	    for (int i = b1Solution.getNumberOfSteps(); i>0; i--) {
	    	assertEquals("numberOfSteps incorrect", i, current.getNumberOfSteps());
	    	current = current.getPreviousBoard();
	    }
	    //after this loop, current is the initial board gotten through previousBoards
	    assertEquals(0, current.getNumberOfSteps());
	    assertNull("initial board from solution has previous board", current.getPreviousBoard());
	    assertTrue("initial board from solutino is not actual initial board", b1.equals(current));
  }
  
  //solution() tests
@Test
  public void solutionTest() {
	  Board b1 = this.exampleBoard1();
	  Solver s = new Solver(b1,PriorityFunc.HAMMING);
	  
	  //returns the list of all the boards, from initial to solution
	  //test: first item is inital, last item is a solution, items next to eachother are neighbors
	  //test: every item's index is equal to their numberOfSteps
	  //test: the previousBoard of an item is the board one item earlier in the list
	  
	  List<Board> solutionList = s.solution();
	  
	  assertTrue("first Board in solution() is not the initial board", b1.equals(solutionList.get(0)));
	  assertTrue("last Board in solution() is not a solution", solutionList.get(solutionList.size()-1).isSolution());
	  
	  for (int i = 0; i<solutionList.size(); i++) {
		  assertEquals("number of steps for item in solution() incorrect", i, solutionList.get(i).getNumberOfSteps());
		  if (i>0) {
			  assertTrue(String.format("Board.previousBoard is not the previous element in solution (at index %d)",i), solutionList.get(i).getPreviousBoard().equals(solutionList.get(i-1)));
		  }
		  else {
			  assertNull("initial board in solution() has previousboard", solutionList.get(0).getPreviousBoard());
		  }
		  
		  if (i<solutionList.size()-1) {
			  //neighbors?
			  Collection<Board> neighbors = solutionList.get(i).neighbors();
			  
			  boolean isNeighbor = false;
			  Board up = solutionList.get(i+1);
			  for (Board b : neighbors) {
				  if(b.equals(up)) {
					  isNeighbor = true;
				  }
			  }
			  assertTrue(String.format("Elements of solution() next to eachother are not neighbors (at index %d and %d",i,i+1),isNeighbor);
		  }
	  }
  }

  //hulp functions

  private int[][] getRandomTiles(){
    int[] intList = new int[9];

    Random r = new Random();
    for (int i = 0; i < 9; i++){
      int rand = r.nextInt(9)+1;
      boolean set = true;
      for (int j : intList){
        if (j == rand){
          i -= 1;
          set = false;
        }  
      }
      if (set){
        intList[i] = rand;
      }
    }

    int[][] randTiles = new int[3][3];
    for (int i = 0; i<randTiles.length; i++){
      for (int j = 0; j<randTiles.length; j++){
        randTiles[i][j] = intList[(i*randTiles.length)+j];
      }
    }
    return randTiles;
  }

  private void assertTilesCopyIsEqual(int[][] tiles, int[][] tilesCopy){
    assertEquals("tiles and tilesCopy are not the same size", tiles.length, tilesCopy.length);
    if (tiles.length>0){
      for (int i = 0; i<tiles.length; i++){
        assertEquals(String.format("tiles[%d] and tilesCopy[%d] are not the same size",i,i),tiles[i].length, tilesCopy[i].length);
        for (int j = 0; j<tiles[i].length; j++){
          assertEquals(String.format("tiles[%d][%d] is not equal to tilesCopy[%d][%d]",i,j,i,j),tiles[i][j], tilesCopy[i][j]);
        }
      }
    }
  }

  private Board exampleBoard1(){
    //5 tiles out of place
    // 5 steps in
    int[][] tiles = new int[][]{ {1,2,3}, {5,4,7}, {9,6,8} };
    //displacement: 0,0 0,0 0,0 1,0 1,0 2,1 emptyblock 1,1 1,0
    // total: 1 + 1 + 2+1 + 1+1 + 1 = 8
    // hamming = 10
    // manhattan = 13

    Board b = new Board(tiles);
    b.setNumberOfSteps(5);
    return b;
  }

  private Board exampleBoard2(){
    // 8 tiles out of place
    // 4 steps in
    int[][] tiles = new int[][]{ {2,1,6}, {5,4,9}, {3,7,8} };
    //displacement: 1,0 1,0 0,1 1,0 1,0 emptyblock 2,2 1,0 1,0 := 8,3
    //total: 11
    //hamming = 12
    //manhattan = 15

    Board b = new Board(tiles);
    b.setNumberOfSteps(4);
    return b;
  }

  private Board exampleBoardSolution(){
    int[][] tiles = new int[][]{ {1,2,3}, {4,5,6}, {7,8,9} };
    //hamming = 0
    //manhattan = 0


    Board b = new Board(tiles);
    return b;
  }
  
  private Board exampleBoardBig() {
	  int[][] tiles = new int[][]{ {1, 17, 13, 10, 7} , {19, 5, 16, 25, 24} , {21, 9, 12, 6, 20} , {22, 18, 8, 14, 11} , {3, 4, 2, 23, 15} };
	  //number of steps is 0
	  
	  Board b = new Board(tiles);
	  b.setNumberOfSteps(0);
	  
	  b.setHamming(); //23
	  b.setManhattan();//70
	  
	  //int manhattan = 0 + 3 + 2 + 2 + 4		+ 5 + 4 + 4 + 4		+ 2 + 3 + 1 + 4 + 1		+ 2 + 1 + 2 + 1 + 5		+ 6 + 6 + 5 + 1 + 2;
	  //System.out.println(String.format("the actual manhattan is : %d", manhattan));
	  return b;
  }
  
  private Board exampleBoardBigSolution() {
	  int[][] tiles = new int[][]{ {1, 2, 3, 4, 5} , {6, 7, 8, 9, 10} , {11, 12, 13, 14, 15} , {16, 17, 18, 19, 20} , {21, 22, 23, 24, 25} };
	  Board b = new Board(tiles);
	  return b;
  }
  
  public void printBoard(Board b) {
	  System.out.println(b.isSolvable() ? "this puzzle is solvable." : "this puzzle is unsolvable");
	  int[][] tiles = b.getTiles();
	  System.out.print("i coordinate j:   ");
	  for (int k = 0; k<tiles.length; k++) {
		  System.out.print(k);
		  System.out.print("  ");
	  }
	  System.out.println("                                 endline");
	  for (int i = 0; i<tiles.length; i++) {
		  System.out.print(String.format("i coordinate %d:   ", i));
		  for (int j = 0; j<tiles.length; j++) {
			  System.out.print(tiles[i][j]);
			  System.out.print(" ");
			  if (tiles[i][j] < 10) {//formatting to spacing, for every size that stays under 100 (max 9x9)
				  System.out.print(" ");
			  }
		  }
		  System.out.print("   ");
		  for (int j = 0; j<tiles.length; j++) {
			  int goal = tiles.length*i + j + 1;
			  System.out.print(goal);
			  System.out.print(" ");
			  if (goal < 10) {//formatting to spacing, for every size that stays under 100 (max 9x9)
				  System.out.print(" ");
			  }
		  }
		  
		  System.out.println("               endline");
	  }
	  System.out.println(String.format("number of steps: %d  hamming: %d  manhattan: %d", b.getNumberOfSteps(), b.hamming(), b.manhattan()));
  }

  private boolean doesBoardListContain(List<Board> list, Board checking){
    boolean ret = false;
    for (Board b : list){
      if(b.equals(checking)){
        ret = true;
      }
    }
    return ret;
  }

  //one for a String list of boards, needed for previousBoards
  private boolean doesStringListContain(List<String> list, Board checking){
      boolean ret = false;
      for (String s : list){
          if(s.equals(checking.toString())){
              ret = true;
          }
      }
      return ret;
  }


}
