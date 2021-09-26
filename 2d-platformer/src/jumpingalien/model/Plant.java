package jumpingalien.model;

import be.kuleuven.cs.som.annotate.*;

/**
 * A class of plants as a special kind of organism. These organisms do not mind overlapping.
 * They are not effected (meaning damaged or healed) by geological features.
 * 
 * @Invar	the amount of hitpoints of an plant is a positive intiger
 * 			|this.getHP()>=0
 * @Invar	The amount of hitpoints of an plant is smaller than (or equal to) it maximum amount of HP.
 * 			|this.getHP <= this.getMaxHP()
 * @Invar 	A plant has a lifetime ranging between 0 and this.getMaxTimeAlive().
 * 			|if this.isAlive()
 * 			|then 0 =< this.getTimeAlive() < this.getMaxTimeAlive()
 * @Invar	the time a plant stays in a world after he dies is between 0 and this.MaxTimeDeath.
 * 			|( 0 <= this.getTimeDeath() <= this.getMaxTimeDeath() )
 * @Invar	A plant moves with a constant velocity (it is 0.5) in the x direction.
 * 			|this.getMaxXacceleration() == 0 and
 * 			|this.getMaxVelocity() == 0.5
 * @Invar	A plant does not jump or fall.
 * 			|this.getGravity() == 0
 * @Invar	A plant does not heal nor take damage from geological features. 
 * 			|this.getTileValues().getGasDamage() == 0		and
 * 			|this.getTileValues().getWaterDamage() == 0		and
 *  		|this.getTileValues().getLavaDamage() == 0		and
 *   		|this.getTileValues().getnonWaterDamage() == 0	and
 * 			|this.getTileValues().getGasTime() == 1			and
 * 			|this.getTileValues().getWaterTime() == 1		and
 * 			|this.getTileValues().getLavaTime() == 1		and
 * 			|this.getTileValues().getnonWaterTime() == 1
 * @Invar	A plant can overlap with other organisms.
 * 			|this.CanOverLap() = true
 * @Invar	A plant can not be look to the front.
 * 			|this.getDirection() != Direction.FRONT
 * @Invar	TODO associations
 * 
 * @authors Wannes Vande Cauter, Nils Van Dessel	--	Fysica
 */
public abstract class Plant extends Organism{
	//subclass-specific variables
	
	private static double xAcceleration = 0;
	private static double yAcceleration = 0;
	private static boolean canOverlap = true;

	private static double maxVelocity = 0.5;

	private static int gasDelta = 0;
	private static int waterDelta = 0;
	private static int lavaDelta = 0;
	private static int nonWaterDelta = 0;
	private static int[] tileDeltas = new int[] {gasDelta,waterDelta,lavaDelta,nonWaterDelta};
	private static double[] tileTimes = new double[] {1,1,1,1};
	private static TileInfo tileValues = new TileInfo(tileDeltas, tileTimes);
	
	protected int width;
	
	/**
	 * Initialize this new plant based on the parameters given by the subclass.
	 * 
	 * @param 	pixelLeftX
	 * 			The x-coordinate of the lower left pixel of the plant.
	 * @param 	pixelBottomY
	 * 			The y-coordinate of the lower left pixel of the plant.
	 * @param 	maxHP
	 * 			The maximum amount of HP of the plant.
	 * @param 	maxTimeAlive
	 * 			The maximum time this plant can be alive.
	 * @post	The maximum amount of HP of the plant is maxHP.
	 * 			|this.getMaxHP() == maxHP
	 * @post	The maximum time the plant can be alive is maxTimeAlive.
	 * 			|this.getMaxTimeAlive() == maxTimeAlive
	 */
	@Raw
	protected Plant(int pixelLeftX, int pixelBottomY,int maxHP, double maxTimeAlive) {
		super(maxHP,maxVelocity,xAcceleration,yAcceleration,maxTimeAlive,canOverlap,tileValues);
		double meterX = (double) pixelLeftX;
		meterX /= 100;
		double meterY = (double) pixelBottomY;
		meterY /= 100;
		this.setMeterPosition(meterX,meterY);
	}
	
	/**
	 * An interaction from plant to the organism happens.
	 * 
	 * @param	organism
	 * 			The organism on which plant interacts.
	 * @effect	The organism gets interacted on by this plant.
	 * 			|organism.interactSlime(this)
	 * @note	The organism does not retaliate in this method.
	 */
	@Override
	protected void interact(Organism organism) {
		organism.interactPlant(this.isAlive());

	}
	
	/**
	 * 	@post	Plant gets terminated when it hits Mazub (regardless of wheter Mazub is alive)
	 * 			|this.terminate()
	 */
	@Override
	protected void interactMazub(Mazub mazub) {
		if ( mazub.getHP() < mazub.getMaxHP() ) {
			this.terminate();
		}	
	}
	
	/**
	 * Nothing
	 */
	@Override
	protected void interactShark(boolean sharkAlive) {/* Nothing */}
	
	/**
	 *Nothing
	 */
	@Override
	protected void interactSlime(Slime slime) {/* Nothing */}
	
	/**
	 * Nothing
	 */
	@Override
	protected void interactPlant(boolean plantAlive) {/* Nothing */}

	/**
	 * @note 	This method is not required to be documented.
	 */
	public abstract void advanceVelocity(double time);
	
	/**
	 * @note 	This method is not required to be documented.
	 */
	public abstract double[] getAdvancePositionAmount(double time);
	
	/**
	 * This plant advances his state.
	 * 
	 * @note This method is not required to be documented.
	 */
	@Override
	public void advanceLoop(double time) {
		this.advanceMisc(time);
		this.advanceTimeSinceInteraction(time);
		double timing = 0;
		while (timing < time) {
			//PREP
			double[] tempPos = this.getMeterPosition();
			int[] tempPosPixel = this.meterToPixel(tempPos);
			
			double dt = Math.min(0.01 / Plant.maxVelocity,time);
			
			dt = this.alterTime(dt,timing,time)[0];
			timing = this.alterTime(dt,timing,time)[1];
			this.advanceVelocity(0);
			
			double [] testPos = this.getAdvancePositionAmount(dt);
			int [] testPosPixel = this.meterToPixel(testPos);
			
			this.setCollides(testPosPixel[0], testPosPixel[1]);
			
			this.advanceVelocity(dt);
			//COLLISION TILES
			this.advanceForSolids(tempPos, testPos);
			
			this.advanceForOrganism(tempPos,tempPosPixel, time);
		}
	}
	
	/**
	 * @note This method is not required to be documented.
	 */
	@Override
	public double alterTypeTime(double dt, double timing) {
			double timeCheck = this.getTimeAlive()+timing;
			while (timeCheck > 1)
				timeCheck -= 1;
			if (timeCheck < 0.5 && timeCheck+dt > 0.5)
				dt = 0.5 - timeCheck;
			if (timeCheck > 0.5 && timeCheck < 1 && timeCheck+dt > 1)
				dt = 1 - timeCheck;
		return dt;
	}
	
	/**
	 * @note	This method is not required to be documented.
	 */
	@Override
	public void advanceTiles(double time) {	
	}
}
	
	
	
	
	
