package gna;

import java.util.Collection;
import java.util.Arrays;
import java.util.ArrayList;

public class Node{

    private final int[] coords;
    private final int[] endCoords;
    private int currentCost;
    private Node previousNode = null;

    public void setPreviousNode(Node n){
        this.previousNode = n;
    }

    public Node getPreviousNode(){
        return this.previousNode;
    }

    public int[] getCoords(){
        return new int[]{this.coords[0], this.coords[1]};
    }

    public Node(int[] coords, int[] endCoords, int currentCost){
        //we do not need to copy the coords as it will not be changed in this class and a copy of it is returned upon request
        this.coords = coords;
        this.endCoords = endCoords;
        this.currentCost = currentCost;
    }

    public void setCurrentCost(int cost){
        this.currentCost = cost;
    }

    public int getCurrentCost(){
        return this.currentCost;
    }

    //return a list of the adjacent nodes of a given node
    public Collection<Node> neighbors(){
        Collection<Node> newNodes = new ArrayList<>();

        //find adjacent nodes:
        for (int i = -1; i<2; i++){
            for (int j = -1; j<2; j++){
                if ((i!=0 || j!=0) && !this.equals(this.previousNode)){
                    int[] newCoords = new int[]{this.coords[0]+i, this.coords[1]+j};
                    //int newCost = ImageCompositor.pixelSqDistance(newCoords[0], newCoords[1]);
                    Node newNode = new Node(newCoords,this.endCoords, this.currentCost);
                    newNode.setPreviousNode(this);
                    newNodes.add(newNode);
                }
            }
        }

            return newNodes;
    }


    // Does this node equal y. they are considered equal if they have the same values for coords
    @Override
    public boolean equals(Object y){
        if ( !(y instanceof Node) )
            return false;

        if (y == null)
            return false;

        Node other = (Node)y;
        return Arrays.equals(this.coords, other.coords);
    }

    // Since we override equals(), we must also override hashCode(). When two objects are
    // equal according to equals() they must return the same hashCode. More info:
    // - http://stackoverflow.com/questions/27581/what-issues-should-be-considered-when-overriding-equals-and-hashcode-in-java/27609#27609
    // - http://www.ibm.com/developerworks/library/j-jtp05273/
    @Override
    public int hashCode(){
        return Arrays.hashCode( this.coords);
    }

}