package jumpingalien.model;

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Immutable;
import be.kuleuven.cs.som.annotate.Value;

/**
 * An enumeration of the direction of an object.
 *    In its current definition, the class distinguishes between
 *    left, front, right, up and down.
 * 
 * @authors Wannes Vande Cauter, Nils Van Dessel	--	Fysica
 */
@Value
public enum Direction {
	
	FRONT,LEFT,RIGHT,DOWN,UP;
	
	/**
	 * A direction has an opposite direction.
	 */
	private Direction opposite;
	static {
		FRONT.opposite = FRONT;
		LEFT.opposite  = RIGHT;
		RIGHT.opposite = LEFT;
		DOWN.opposite  = UP;
		UP.opposite    = DOWN;
	}
	/**
	 * Returns the opposite direction of this direction.
	 * @note This method is used for the switchDirection() in plants.
	 */
	@Basic
	@Immutable
	public Direction getOppositeDirection() {
		return this.opposite;
	}
	
	
	/**
	 * A direction has a number thats is associated with the sign of speed.
	 */
	private int number;
	static {
		FRONT.number = 0;
		LEFT.number  = -1;
		RIGHT.number = 1;
		DOWN.number  = -1;
		UP.number    = 1;
	}
	/**
	 * Returns the integer associated with this direction.
	 * @note this method is used for the setVelocity in plants.
	 */
	@Basic
	@Immutable
	public int dirToInt() {
		return this.number;
	}
}