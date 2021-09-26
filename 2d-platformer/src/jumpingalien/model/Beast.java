package jumpingalien.model;


import be.kuleuven.cs.som.annotate.Raw;

import be.kuleuven.cs.som.annotate.Basic;

/**
 * A class of beasts as a special kind of organism. These Organism are able to move in multiple directions.
 * They also don't like to overlap with each other.
 * 
 * @Invar	The amount of hitpoints of an beast is a positive integer
 * 			|thisHP()>=0
 * @Invar 	The maximum amount of hitpoints of a beast is 500 .
 * 			|this.getMaxHP() == 500
 * 			|this.getHP() =< this.getMaxHP()
 * @Invar 	A beast does not die of old age, its maximum time it can be alive is -1.
 * 			|this.getMaxTimeAlive() == -1
 * @Invar	The time a beast is alive is a positive double.
 * 			|this.getTimeAlive() >= 0
 * @Invar	The time an beast stays in a world after he dies is between 0 and maxTimeDeath.
 * 			|( 0 <= this.getTimeDeath() <= this.getMaxTimeDeath() )
 * @Invar	A beast does not want to overlap with other organisms.
 * 			|this.CanOverLap() = false
 * @Invar	The possible directions a beast can have is 
 * 			either Direction.LEFT, Direction.FRONT or Direction.RIGHT .
 * 			|this.getDirection() == Direction.LEFT  or  this.getDirection() == Direction.FRONT  or  this.getDirection() == Direction.RIGHT
 * @Invar	TODO associations
 * 
 * @author	Wannes Vande Cauter, Nils Van Dessel	--	Fysica
 */
public abstract class Beast extends Organism implements Imovement{
	
	private static int maxHP=500;
	private static int maxTimeAlive=-1;
	private static boolean canOverlap=false;
	private Direction direction = Direction.FRONT;
	
	/**
	 * Initialize this new beast based on the given parameters of the subclass that calls the constructor
	 * 
	 * @param 	maxVelocity
	 * 			The maximum absolute value amount of the velocity the beast can have
	 * 			for a beast this is meant for the horizontal direction.
	 * @param 	xAcceleration
	 * 			The maximum absolute value of the amount of the acceleration of the beast 
	 * 			in the horizontal direction.
	 * @param 	yAcceleration
	 * 			The maximum absolute value of the amount of the acceleration of the beast 
	 * 			in the horizontal direction.
	 * @param 	tileValues
	 * 			This tileInfo object shows how the beast interacts with water, magma, gas and nonWater.
	 * @post	The maximum amount of HP of a beast is 500.
	 * 			|this.getMaxHP() == 500 and
	 * 			|this.getHP() =< getMaxHP()
	 * @post	A beast will not die of old age.
	 * 			The maximum time a beast can be alive is set to -1.
	 * 			|this.getMaxTimeAlive() == -1
	 * @post	A beast should not be able to overlap with another organism whose canOverlap() equals false.
	 * 			|this.canOverlap()==false
	 * 			|&&
	 * 			|if org.canOverlap()
	 * 			|then this.canPass(org)==false
	 * @post	A beast begins with a direction of Direction.FRONT
	 * 			|if getTimeAlive()==0
	 * 			|then this.getDirection == Direction.FRONT
	 * @post	The possible directions a beast can have is 
	 * 			either Direction.LEFT, Direction.FRONT or Direction.RIGHT
	 * 			|this.getDirection()==Direction.LEFT || this.getDirection()==Direction.FRONT || this.getDirection()==Direction.RIGHT
	 */
	@Raw
	protected Beast(double maxVelocity,double xAcceleration,double yAcceleration,TileInfo tileValues) {
		super(maxHP,maxVelocity,xAcceleration,yAcceleration,maxTimeAlive,canOverlap,tileValues);
	}
	

	/**
	 * @note	This method is not required to be documented
	 */
	@Override
	public void advanceLoop(double time) {
		this.advanceTimeSinceInteraction(time);
		double timing = 0;
		while (timing < time) {
			if (this instanceof Mazub && this.hasWorld()) {
				this.getWorld().updateGameState();
			}
			
			//PREP
			double[] tempPos = this.getMeterPosition();
			int[] tempPosPixel = this.meterToPixel(tempPos);
			
			double velX = this.getVelocity()[0],velY = this.getVelocity()[1];
			double accX = this.getAcceleration()[0],accY = this.getAcceleration()[1];
			double dt = Math.min(0.01 / (Math.sqrt((velX*velX)+(velY*velY)) + (Math.sqrt((accX*accX) + (accY*accY))*time)),time);
			
			dt = this.alterTime(dt,timing,time)[0];
			timing = this.alterTime(dt,timing,time)[1];
			this.advanceMisc(dt);
			this.advanceAcceleration(dt);
			this.advanceVelocity(0);
			velX = this.getVelocity()[0];
			velY = this.getVelocity()[1];
			this.updateDirection(timing);
			
			double [] testPos = this.getAdvancePositionAmount(dt);
			int [] testPosPixel = this.meterToPixel(testPos);
			
			this.setCollides(testPosPixel[0], testPosPixel[1]);
			
			this.advanceVelocity(dt);
			//COLLISION TILES
			this.advanceForSolids(tempPos, testPos);
			
			this.advanceForOrganism(tempPos,tempPosPixel, dt);
			this.advanceTiles(dt);
		}
	}
	
	/**
	 * @note	This method is not required to be documented.
	 */
	protected void updateDirection(double time) {}

	/**
	 * @note	This method is not required to be documented
	 */
	@Override
	protected void switchDirection() {
		this.setDirection(this.getDirection().getOppositeDirection());
		this.setXVelocity(-this.getXVelocity());
	}
	@Override@Basic
	public Direction getDirection() {
		return this.direction;
	}
	@Override
	public void setDirection(Direction dir) {
		this.direction = dir;
	}
}
