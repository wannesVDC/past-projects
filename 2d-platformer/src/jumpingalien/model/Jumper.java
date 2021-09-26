package jumpingalien.model;

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Immutable;
import be.kuleuven.cs.som.annotate.Raw;
/**
 * A class of jumpers as a special kind of beast. These organisms have the ability to jump.
 * 
 * @Invar	The amount of hitpoints of an jumper is a positive integer
 * 			|thisHP()>=0
 * @Invar 	The maximum amount of hitpoints of a jumper is 500 .
 * 			|this.getMaxHP() == 500
 * 			|this.getHP() =< this.getMaxHP()
 * @Invar 	A jumper does not die of old age, its maximum time it can be alive is -1.
 * 			|this.getMaxTimeAlive() == -1
 * @Invar	The time a jumper is alive is a positive double.
 * 			|this.getTimeAlive() >= 0
 * @Invar	The time an jumper stays in a world after he dies is between 0 and maxTimeDeath.
 * 			|( 0 <= this.getTimeDeath() <= this.getMaxTimeDeath() )
 * @Invar	A jumper does not want to overlap with other organisms.
 * 			|this.CanOverLap() = false
 * @Invar	The possible directions a jumper can have is 
 * 			either Direction.LEFT, Direction.FRONT or Direction.RIGHT .
 * 			|this.getDirection() == Direction.LEFT  or  this.getDirection() == Direction.FRONT  or  this.getDirection() == Direction.RIGHT
 * @Invar	The velocity of a jumper in the vertical direction (going up) can never
 * 			be greater the its jumpspeed.
 * 			|this.getYVelocity() <= this.getJumpSpeed()
 * @Invar	TODO associations
 * 
 * @author 	Wannes Vande Cauter, Nils Van Dessel	--	Fysica
 */
public abstract class Jumper extends Beast {
	 
	private final double jumpSpeed;
	private boolean jumping = false;
	
	/**
	 * Initialize this new jumper based on the given parameters of the subclass that calls the constructor.
	 * 
	 * @param 	maxVelocity
	 * 			The maximum absolute value amount of the velocity the jumper can have
	 * 			for a jumper this is meant for the horizontal direction.
	 * @param 	xAcceleration
	 * 			The maximum absolute value of the amount of the acceleration of the jumper 
	 * 			in the horizontal direction.
	 * @param 	yAcceleration
	 * 			The maximum absolute value of the amount of the acceleration of the jumper 
	 * 			in the horizontal direction.
	 * @param	jumpspeed
	 * 			The amount of vertical velocity the jumper has the moment startJump() is called?
	 * @param 	tileValues
	 * 			This tileInfo object shows how the jumper interacts with water, magma, gas and nonWater.
	 * @post	The jumpspeed gets set.
	 * 			|new.getJumpSpeed() == jumpSpeed
	 */
	@Raw
	protected Jumper(double maxVelocity,double xAcceleration,double yAcceleration,double jumpSpeed,TileInfo tileValues) {
		super(maxVelocity,xAcceleration,yAcceleration,tileValues);
		this.jumpSpeed=jumpSpeed;
	}
	
	/**
	 * Returns the vertical speed that will be set when this.startJump() is called.
	 */
	@Basic
	@Immutable
	public double getJumpSpeed() {
		return this.jumpSpeed;
	}
	
	/**
	 * @param 	bool
	 * 			The new value of jumping.
	 * @post	Jumping will be bool.
	 * 			new.isJumping() == bool
	 */
	public void setJumping(boolean bool) {
		this.jumping = bool;
	}
	
	/**
	 * Returns whether the jumper is jumping.
	 */
	@Basic
	public boolean isJumping() {
		return this.jumping;
	}
	
}
