package gna;

import java.util.*;
import libpract.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Implement the methods stitch, seam and floodfill.
 */
public class Stitcher
{
	private PriorityQueue<Node> priorityList = new PriorityQueue<Node>(new Comparator<Node>() {
		@Override
		public int compare(Node o1, Node o2) {
			return (o1.getCurrentCost() - o2.getCurrentCost());
		}
	});

	private HashSet<Node> previousNodes = new HashSet<Node>();

	private void addToPriorityList(Node n){
		//because the node with the lowest cost is chosen everytime, when a node is added for the first time, it is added with the lowest possible cost
		// this is because the cost of a node itself is constant (regardless of path), so if any other path gets to that node with a lower cost, it would have had a lower cost
		// than the first path before the new node was found, and the first path wouldn't have been chosen to calculate in
		if (!this.previousNodes.contains(n)){
			this.priorityList.add(n);
			this.previousNodes.add(n);
		}
	}

	/**
	 * Return the sequence of positions on the seam. The first position in the
	 * sequence is (0, 0) and the last is (width - 1, height - 1). Each position
	 * on the seam must be adjacent to its predecessor and successor (if any).
	 * Positions that are diagonally adjacent are considered adjacent.
	 * 
	 * image1 and image2 are both non-null and have equal dimensions.
	 *
	 * Remark: Here we use the default computer graphics coordinate system,
	 *   illustrated in the following image:
	 * 
	 *        +-------------> X
	 *        |  +---+---+
	 *        |  | A | B |
	 *        |  +---+---+
	 *        |  | C | D |
	 *        |  +---+---+
	 *      Y v 
	 * 
	 *   The historical reasons behind using this layout is explained on the following
	 *   website: http://programarcadegames.com/index.php?chapter=introduction_to_graphics
	 * 
	 *   Position (y, x) corresponds to the pixels image1[y][x] and image2[y][x]. This
	 *   convention also means that, when an automated test mentioned that it used the array
	 *   {{A,B},{C,D}} as a test image, this corresponds to the image layout as shown in
	 *   the illustration above.
	 */
	public List<Position> seam(int[][] image1, int[][] image2) {

		//implement dijkstras shortest path through priority Queue
		// sort highest priority as the elements with the shortest path yet and shortest way to go to end
		// (this means the value will be the current cost + the taxicab distance of the current to the goal)

		// for this I will create a class (Noda.java) which holds the coordinates of the current node, and a function to calculate it's priority
		// if we have the lowest cost solution, the top element of priorityList will be a node equal the last node in the list

		// the withd and heigt of the 2 images must be the same
		int width = image1[0].length;
		int heigth = image1.length;

		int[] endCoords = new int[]{heigth-1,width-1};
		int[] startCoords = new int[]{0, 0};

		Node startNode = new Node(startCoords,endCoords,ImageCompositor.pixelSqDistance(image1[0][0], image2[0][0]));
		Node endNode = new Node(endCoords,endCoords,0); //currentcost will not matter, as this is used to check for a solution and will be overwritten with the solution that has the right cost
		this.addToPriorityList(startNode);

		Node currentNode = this.priorityList.poll();
		while (!currentNode.equals(endNode)){
			Collection<Node> addNodes = currentNode.neighbors();
			for (Node addNode : addNodes){
				int[] co = addNode.getCoords();
				int i = co[0];
				int j = co[1];
				if (i >= 0 && j >= 0 && i < heigth && j < width){
					//set the current cost of a node:
					int newCost = ImageCompositor.pixelSqDistance(image1[i][j], image2[i][j]);
					addNode.setCurrentCost(addNode.getCurrentCost() + newCost);
					this.addToPriorityList(addNode);
				}
			}
			currentNode = this.priorityList.poll();
		}
		endNode = currentNode;

		//after this loop has ended we have a line of solutions
		List<Position> retList = new ArrayList<>();

		while(currentNode != null){
			int[] addCoords = currentNode.getCoords();
			Position addPos = new Position(addCoords[0],addCoords[1]);
			retList.add(addPos);

			currentNode = currentNode.getPreviousNode();
		}
		Collections.reverse(retList);
		return retList;
	}

	/**
	 * Apply the floodfill algorithm described in the assignment to mask. You can assume the mask
	 * contains a seam from the upper left corner to the bottom right corner. The seam is represented
	 * using Stitch.SEAM and all other positions contain the default value Stitch.EMPTY. So your
	 * algorithm must replace all Stitch.EMPTY values with either Stitch.IMAGE1 or Stitch.IMAGE2.
	 *
	 * Positions left to the seam should contain Stitch.IMAGE1, and those right to the seam
	 * should contain Stitch.IMAGE2. You can run `ant test` for a basic (but not complete) test
	 * to check whether your implementation does this properly.
	 */
	public void floodfill(Stitch[][] mask) {

		//assume mask is a field describing what image is used where
		// the seam is the fields in wich the value is Stitch.SEAM

		List<Position> todoImage1 = new ArrayList<Position>();
		List<Position> todoImage2 = new ArrayList<Position>();


		// the borders can easily be filled in: the upper and right border are always image 2 (except for seam elements)
		// and the left and lower border are always image 1 (except for seam elements)

		for (int j = 0; j<mask[0].length; j++){
			if (mask[0][j] == Stitch.EMPTY) {// upper border
				todoImage2.add(new Position(0,j));
			}
			if (mask[mask.length-1][j] == Stitch.EMPTY){// lower border
				todoImage1.add(new Position(mask.length-1,j));
			}
		}

		for (int i = 0; i<mask.length; i++){
			if (mask[i][0] == Stitch.EMPTY) {// left border
				todoImage1.add(new Position(i,0));
			}
			if (mask[i][mask[0].length-1] == Stitch.EMPTY){// right border
				todoImage2.add(new Position(i, mask[0].length-1));
			}
		}

		while (todoImage1.size() > 0){
			Position pos = todoImage1.get(0);
			mask[pos.getY()][pos.getX()] = Stitch.IMAGE1;
			todoImage1.remove(pos);
			//now add its neighbors
			Collection<Position> neighbors = this.directNeigbors(pos);
			for (Position p : neighbors){
				if (!todoImage1.contains(p)){
					if (canAddToTodo(p,mask)){
						todoImage1.add(p);
					}
				}
			}
		}

		while (todoImage2.size() > 0){
			Position pos = todoImage2.get(0);
			mask[pos.getY()][pos.getX()] = Stitch.IMAGE2;
			todoImage2.remove(pos);
			//now add its neighbors
			Collection<Position> neighbors = this.directNeigbors(pos);
			for (Position p : neighbors){
				if (!todoImage2.contains(p)){
					if (canAddToTodo(p,mask)){
						todoImage2.add(p);
					}
				}
			}
		}
	}

	public boolean canAddToTodo(Position p, Stitch[][] mask){
		if (p.getY() >= 0 && p.getY() < mask.length){
			if (p.getX() >= 0 && p.getX() < mask[0].length){
				if (mask[p.getY()][p.getX()] == Stitch.EMPTY){
					return true;
				}
			}
		}
		return false;
	}

	public Collection<Position> directNeigbors(Position pos){
		Collection<Position> retList = new ArrayList<Position>();

		retList.add(new Position(pos.getY()-1, pos.getX()));
		retList.add(new Position(pos.getY()+1, pos.getX()));
		retList.add(new Position(pos.getY(), pos.getX()-1));
		retList.add(new Position(pos.getY(), pos.getX()+1));

		return retList;
	}

	/**
	 * Return the mask to stitch two images together. The seam runs from the upper
	 * left to the lower right corner, where in general the rightmost part comes from
	 * the second image (but remember that the seam can be complex, see the spiral example
	 * in the assignment). A pixel in the mask is Stitch.IMAGE1 on the places where
	 * image1 should be used, and Stitch.IMAGE2 where image2 should be used. On the seam
	 * record a value of Stitch.SEAM.
	 * 
	 * ImageCompositor will only call this method (not seam and floodfill) to
	 * stitch two images.
	 * 
	 * image1 and image2 are both non-null and have equal dimensions.
	 */
	public Stitch[][] stitch(int[][] image1, int[][] image2) {

		// use seam and floodfill to implement this method

		List<Position> seam = this.seam(image1,image2);

		Stitch[][] mask = new Stitch[image1.length][image1[0].length];
		for (int i = 0; i<mask.length; i++){
			for (int j = 0; j<mask[0].length; j++){
				mask[i][j] = Stitch.EMPTY;
			}
		}

		for (Position seamElement : seam){
			mask[seamElement.getY()][seamElement.getX()] = Stitch.SEAM;
		}

		this.floodfill(mask);

		return mask;
	}
}


