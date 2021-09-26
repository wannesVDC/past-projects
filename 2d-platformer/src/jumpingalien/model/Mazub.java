// Wannes Vande Cauter en Nils Vandessel	-- Fysica (beide)	-- https://github.com/KUL-ogp/ogp1819-project-vandecauter-vandessel.git
package jumpingalien.model;

import java.util.Arrays;

import be.kuleuven.cs.som.annotate.*;
import jumpingalien.util.ModelException;
import jumpingalien.util.Sprite;

/**
 * A class of mazubs as a special kind of jumpers. Appart from being able to jump, 
 * they are also able to duck, which allows them to crawl through small spaces.
 * Mazubs like to eat plants, but only alive plants.
 * 
 * @Invar	The amount of hitpoints of an mazub is a positive integer
 * 			|thisHP()>=0
 * @Invar 	The maximum amount of hitpoints of a mazub is 500 .
 * 			|this.getMaxHP() == 500
 * 			|this.getHP() =< this.getMaxHP()
 * @Invar 	A mazub does not die of old age, its maximum time it can be alive is -1.
 * 			|this.getMaxTimeAlive() == -1
 * @Invar	The time a mazub is alive is a positive double.
 * 			|this.getTimeAlive() >= 0
 * @Invar	The time an mazub stays in a world after he dies is between 0 and maxTimeDeath.
 * 			|( 0 <= this.getTimeDeath() <= this.getMaxTimeDeath() )
 * @Invar	The maximum absolute value of the acceleration in the horizontal direction of
 * 			a mazub is 0.9 .
 * 			|this.getMaxXAcceleration() == 0.9
 * @Invar	The vertical free fall acceleration of a mazub is -10 .
 * 			|this.getGravity() == -10
 * @Invar	The minimal absolute value of mazub's non-zero velocity == 1
 * 			|this.getMinVelocity() == 1
 * @Invar	The maximum velocity of a mazub that is not ducking is 3
 * 			|if !this.isDucking()
 * 			|then this.getMaxVelocity() == 3
 * @Invar	The maximum velocity of a mazub that is ducking is its minimal velocity.
 * 			|if this.isDucking()
 * 			|then this.getMaxVelocity() == this.getMinVelocity()
 * @Invar	A mazub jumps with a speed of 8
 * 			|This.jumpspeed == 8
 * @Invar	Mazub's damage for touching gas equals GeologicalFeature.GAS.getDamage().
 * 			|this.getTileValues().getGasDelta() == -GeologicalFeature.GAS.getDamage()
 * @Invar	Mazub's damage for touching water equals GeologicalFeature.WATER.getDamage().
 * 			|this.getTileValues().getWaterDelta() == -GeologicalFeature.WATER.getDamage()
 * @Invar	Mazub's damage for touching lava equals GeologicalFeature.MAGMA.getDamage().
 * 			|this.getTileValues().getLavaDelta() == -GeologicalFeature.MAGMA.getDamage()
 * @Invar	Mazub's damage for not touching water equals 0 .
 * 			|this.getTileValues().getNonWaterDelta() == 0
 * @Invar	Mazub takes damage from tiles every 0.2 seconds.
 * 			|this.getTileValues().getGasTime() == 0.2 	and
 * 			|this.getTileValues().getWaterTime() == 0.2 and
 * 			|this.getTileValues().getLavaTime() == 0.2 	and
 * 			|this.getTileValues().getNonWaterTime() == 0.2
 * @Invar	Mazub height while ducking is the smallest height of the sprites in his spritelist
 * 			|this.getDuckHeight() == min(list[i].GetHeight())
 * @Invar	Mazub's damage for touching a shark that is alive is 50 .
 * 			|Mazub.getSharkDamage() == 50
 * @Invar	Mazub's damage for touching a slime that is alive is 20 .
 * 			|Mazub.getSlimeDamage() == 20
 * @Invar	Mazub gains 50 HP for touching a plant that is alive is 50 .
 * 			|Mazub.getPlantHeal() == 50
 * @Invar	Mazub's damage for touching a plant that is dead is 20 .
 * 			|Mazub.getPlantDamage() == 20
 * @Invar	Mazub's timecount for how long he is running can not be negative.
 * 			|this.getRunningTime()>=0
 * @Invar	Mazub's timecount for how long he hasn't moved can not be negative.
 * 			|this.getTimeSinceMove()>=0
 * @Invar	Mazub's old direction (which is used for sprites) is either Direction.LEFT, Direction.FRONT or Direction.RIGHT
 * 			|this.getOldDirection()==Direction.LEFT || this.getDirection()==Direction.FRONT || this.getDirection()==Direction.RIGHT
 * @Invar	A mazub does not want to overlap with other organisms.
 * 			|this.CanOverLap() = false
 * @Invar	The possible directions a mazub can have is 
 * 			either Direction.LEFT, Direction.FRONT or Direction.RIGHT .
 * 			|this.getDirection() == Direction.LEFT  or  this.getDirection() == Direction.FRONT  or  this.getDirection() == Direction.RIGHT
 * @Invar	The velocity of a mazub in the vertical direction (going up) can never
 * 			be greater the its jumpspeed which is 8 .
 * 			|this.getYVelocity() <= this.getJumpSpeed() == 8
 * @Invar	TODO associations
 * 
 * @authors Wannes Vande Cauter, Nils Van Dessel	--	Fysica
 */
public class Mazub extends Jumper{
	
	private Sprite[] list;
	Sprite sp = null;
	//private static int PassableMultiplier = 1;
	private static final double xAcceleration = 0.9;
	private static final double yAcceleration = -10;
	private static final double maxVelocity = 3;
	
	private static int gasDelta = -GeologicalFeature.GAS.getDamage();
	private static int waterDelta = -GeologicalFeature.WATER.getDamage();
	private static int lavaDelta = -GeologicalFeature.MAGMA.getDamage();
	private static int nonWaterDelta = 0;
	private static int[] tileDeltas = new int[] {gasDelta,waterDelta,lavaDelta,nonWaterDelta};
	private static double[] tileTimes = new double[] {0.2,0.2,0.2,0.2};
	private static TileInfo tileValues = new TileInfo(tileDeltas, tileTimes);
	
	protected int width;
	private int normalHeight = 0;
	private int duckHeight = 200;	
	
	//mazub specific variables
	public static final int sharkDamage = 50;
	public static final int slimeDamage = 20;
	public static final int plantHeal = 50;
	public static final int plantDamage = 20;
	public static final double jumpSpeed = 8;
	private boolean duckRequest=false;
	protected boolean isDucking = false;
	private boolean moving = false;
	private boolean doubleRunning = false;
	private double runningtime = 0;
	private double timeSinceMove = 1;
	private Direction oldDirection = Direction.FRONT;
	
	@Basic
	@Immutable
	public static int getSharkDamage() {
		return Mazub.sharkDamage;
	}
	@Basic
	@Immutable
	public static int getSlimeDamage() {
		return Mazub.slimeDamage;
	}
	@Basic
	@Immutable
	public static int getPlantHeal() {
		return Mazub.plantHeal;
	}
	@Basic
	@Immutable
	public static int getPlantDamage() {
		return Mazub.plantDamage;
	}
	
	/**
	 * Initialize this new mazub with an x-coordinate, an y-coordinate and a list of sprites.
	 * 
	 * @param 	pixelLeftX
	 * 			The x-coordinate of the lower left pixel of the mazub.
	 * @param 	pixelBottomY
	 * 			The y-coordinate of the lower left pixel of the mazub.
	 * @param 	sprites
	 * 			A list of sprites representing the appearance of mazub.
	 * @post	TODO
	 * @throws	
	 * @throws
	 * @throws
	 * @throws
	 */
	@Raw
	public Mazub(int x, int y,Sprite... sprites) throws ModelException {
		super(maxVelocity,xAcceleration,yAcceleration,jumpSpeed,tileValues);
		if (sprites != null) {
			Sprite[] sprit = Arrays.copyOf(sprites, sprites.length);
			int len = sprites.length;
			if (len%2 != 0) {
				throw new ModelException("sprite list can't be odd");
			}
			if (len < 10) {
				throw new ModelException("sprite list too short");
			}
			if (this.containsNull(sprit)) {
				throw new ModelException("sprite list invalid");
			}
			
			this.list = Arrays.copyOf(sprites, sprites.length);
			this.setTimeSinceMove(1);
			this.setCurrentSprite(0);
			}
			else {
				throw new ModelException("sprite list can't be null");
			}
			
			for (Sprite sprite : this.list) {
				this.width = sprite.getWidth();
				if (this.normalHeight < sprite.getHeight()) {
					this.normalHeight = sprite.getHeight();
				}
				if (this.duckHeight > sprite.getHeight()) {
					this.duckHeight = sprite.getHeight();
				}
			}
			if (x < 0 || y < 0) {
				throw new ModelException("position can't be negative");
			}
			double meterX = (double) x;
			meterX /= 100;
			double meterY = (double) y;
			meterY /= 100;
			this.setMeterPosition(meterX,meterY);
			
			this.setCurrentSize();

		}
	/**
	 * @note	This method is not required to be documented
	 */
	@Override
	public void updateDuck() {
		
		if (this.hasDuckRequest()) {
			this.setDuck(true);
			this.setAcceleration(0, this.getAcceleration()[1]);;
			if(this.getVelocity()[0] > this.getMaxVelocity()) {
				this.setVelocity(this.getMaxVelocity(),this.getVelocity()[1]);
			}
			if(-this.getVelocity()[0] > this.getMaxVelocity()) {
				this.setVelocity(-this.getMaxVelocity(),this.getVelocity()[1]);
			}
			
			//HIER CODE VOOR REDUCED DUCKING SIZE
			setCurrentSprite(0);
		}
		else {
			boolean bool = false;
			if (this.hasWorld()) {
				bool = this.getWorld().canUnduck(this);
			}
			if (bool) {
				this.setDuck(false);
				//HIER DE CODE VOOR BACK TO NORMAL DUCKING SIZE
				setCurrentSprite(0);
			}
		}
		this.setCurrentSize();
	
	}
	
	/**
	 * @param	the new state of ducking
	 * @post	|this.isDucking == bool
	 */
	public void setDuck(boolean bool) {
		this.isDucking = bool;
	}
	
	/**
	 * @return 	whether the organism is ducking
	 * 			|result = this.isDucking
	 */
	@Basic
	public boolean isDucking() {
		return this.isDucking;
	}
	
	/**
	 * @post this.duckRequest == true
	 */
	public void startDuck() {
		this.duckRequest = true;
		this.updateDuck();
	}
	/**
	 * @post this.duckRequest == false
	 */
	public void endDuck() {
		this.duckRequest = false;
		this.updateDuck();
		this.advanceAcceleration(0);
	}
	
	/**
	 * Returns whether this mazub has a request to duck.
	 */
	@Basic
	public boolean hasDuckRequest() {
		return this.duckRequest;
	}
	

	/**
	 * TODO
	 * @return
	 */
	public double getMaxDuckVelocity() {
		return this.getMinHorizontalVelocity();
	}

	/**
	 * An interaction from shark to the organism happens.
	 * 
	 * @param	organism
	 * 			The organism on which mazub interacts.
	 * @effect	The organism gets interacted on by this mazub.
	 * 			|organism.interactShark(this.isAlive())
	 * @note	The organism does not retaliate in this method.
	 */
	protected void interact(Organism organism) {
			organism.interactMazub(this);
	}
	
	/**
	 * Nothing
	 */
	@Override
	protected void interactMazub(Mazub mazub) {/* Nothing */}
	
	/**
	 * Mazub feels the interaction of the shark.
	 *	
	 * @effect	Mazub gains Mazub.getPlantHeal() HP for having contact with a living plant.
	 *			|if (plantAlive)
	 *			|then this.gainHP(Mazub.getPlantHeal())
	 * @effect 	Mazub loses Mazub.getPlantDamage() hitpoints for having contact with a living plant.
	 *			|if (plantAlive)
	 *			|then this.gainHP(Mazub.getPlantDamage())
	 */
	@Override
	protected void interactShark(boolean sharkAlive) {
		if (sharkAlive) {
			this.loseHP(Mazub.getSharkDamage()); 
			}
	}
	
	/**
	 *	This shark feels the interaction of a slime, he gains HP from this interaction.
	 *
	 * @param 	slime
	 *			The slime that hits this shark.
	 * @post	If the slime is alive, he will heal this shark with an amount Shark.getSlimeHeal()
	 * 			|if (slime.isalive())
	 * 			|then this.gainHP(Shark.getSlimeHeal())
	 */
	@Override
	protected void interactSlime(Slime slime) {
		if (slime.isAlive())	{
			this.loseHP(Mazub.getSlimeDamage()); }
	}
	
	/**
	 * Mazub feels the interaction of the plant
	 *	
	 * @effect	Mazub gains Mazub.getPlantHeal() HP for having contact with a living plant.
	 *			|if (plantAlive)
	 *			|then this.gainHP(Mazub.getPlantHeal())
	 * @effect 	Mazub loses Mazub.getPlantDamage() hitpoints for having contact with a living plant.
	 *			|if (plantAlive)
	 *			|then this.gainHP(Mazub.getPlantDamage())
	 */
	@Override
	protected void interactPlant(boolean plantAlive) {
		if (plantAlive) {
			this.gainHP(Mazub.getPlantHeal()); 
			}
		else {
			this.loseHP(Mazub.getPlantDamage()); }
	}
	

	@Override
	public double getMaxVelocity() {
		if (this.isDucking()) {
			return this.getMaxDuckVelocity();
		}
		else {
			return super.getMaxVelocity();
		}
	}
	
	
	private static double minHorizontalVelocity = 1;
	
	public double getMinHorizontalVelocity() {
		return minHorizontalVelocity;
	}	
	
	
	
	
	
	public boolean isMoving() {
		return this.moving;
	}
	public void setMoving(boolean m) {
		this.moving = m;
	}

	/**
	 * TODO
	 * @param b
	 */
	private void setDoubleRunning(boolean b) {
		this.doubleRunning = b;	
	}
	
	public void startMove(Direction dir) throws AssertionError{
		try {
			if(!this.isMoving()) {
				this.setAcceleration(dir.dirToInt()*Mazub.xAcceleration, this.getAcceleration()[1]);
				this.setDirection(dir);
				this.setOldDirection(dir);
				if (dir == Direction.LEFT) {
					this.setVelocity(-this.getMinHorizontalVelocity(), this.getVelocity()[1]);
				}
				if (dir == Direction.RIGHT) {
					this.setVelocity(this.getMinHorizontalVelocity(), this.getVelocity()[1]);
				}
				/**
				this.oldOrientation = dir;
				*/
				setCurrentSprite(0);
				this.setMoving(true);
			}
			else {
				throw new AssertionError("Mazub is already moving");
			}
		}catch (AssertionError exc) {
			
			this.setDoubleRunning(true);
			this.setAcceleration(dir.dirToInt()*this.getMaxXacceleration(), this.getAcceleration()[1]);
			this.setDirection(dir);
			this.setOldDirection(dir);
			if (dir == Direction.LEFT) {
				this.setVelocity(-this.getMinHorizontalVelocity(), this.getVelocity()[1]);
			}
			if (dir == Direction.RIGHT) {
				this.setVelocity(this.getMinHorizontalVelocity(), this.getVelocity()[1]);
			}
			/**
			this.oldOrientation = dir;
			*/
			setCurrentSprite(0);
			this.setMoving(true);
			assert(false);
		}
	}

	/**
	 * @note This method is not required to be documented.
	 */
	private boolean isDoubleRunning() {
		return this.doubleRunning;
	}
	
	/**
	 * TODO
	 */
	public void endMove() {
		if (!this.isDoubleRunning()) {
		try {
			if (!this.isMoving()) {
				throw new ModelException("isn't moving");
			}
			this.setMoving(false);
			this.setDirection(Direction.FRONT);
			this.setVelocity(0, this.getVelocity()[1]);
			this.setAcceleration(0,this.getAcceleration()[1]);
			this.setCurrentSprite(0);
		}catch (ModelException exc) {
			if (!this.isMoving()) {
				throw new ModelException("isn't moving");
			}
			this.setMoving(false);
			this.setDirection(Direction.FRONT);
			this.setVelocity(0, this.getVelocity()[1]);
			this.setAcceleration(0,this.getAcceleration()[1]);
			this.setCurrentSprite(0);
		}
		}
		else {
			this.setDoubleRunning(false);
		}
	}
	

	/** 
	 * if Mazub is already jumping, throw modelexception
	 * else, set the vertical velocity to the maximum value, set the vertical acceleration to -10
	 */
	public void startJump() {
		if (this.isJumping()) {
			throw new ModelException("Mazub is already jumping");//Waarom een modelexception?
		}
		else {
			this.setVelocity(this.getVelocity()[0],this.getJumpSpeed());
			this.setAcceleration(this.getAcceleration()[0], this.getGravity());
			this.setJumping(true);
		}
		setCurrentSprite(0);
	}
	
	/**
	 * if Mazub isn't jumping, throw modelexception
	 * 
	 * else, if the vertical velocity is positive, set it to 0
	 */
	public void endJump() throws ModelException{
		
		if (!this.isJumping()) {
			throw new ModelException("Mazub isn't jumping");
		}
		else{
			if(this.getVelocity()[1]>0) {
				this.setVelocity(this.getVelocity()[0],0);
			}
			this.setJumping(false);
		}
		this.setCurrentSprite(0);
	}
	


	
	public void setRunningTime(double t) {
		this.runningtime = t;
	}
	
	public double getRunningTime() {
		return this.runningtime;
	}
	
	public void setTimeSinceMove(double t) {
		this.timeSinceMove = t;
	}
	
	public double getTimeSinceMove() {
		return this.timeSinceMove;
	}
	

	private void setSprite(Sprite sprite) {
		this.sp=sprite;		
	}
	
	public Direction getOldDirection() {
		return this.oldDirection;
	}
	
	public void setOldDirection(Direction newOld) {
		this.oldDirection = newOld;
	}
	
	/**
	 * @note	This method is not required to be documented
	 */
	public void setCurrentSprite(double time) {
		Sprite temp = this.getCurrentSprite();
		
		int counter1 = 0;
		int counter2 = 0;
		
		int len = this.getSprites().length;
		
		int shortList = len-8;
		int shorterList = shortList/2;
		
		this.setTimeSinceMove(this.getTimeSinceMove()+time);
		if(this.isEliminated()) {
			this.setSprite(list[0]);
		}
		
		else if (!this.isDucking() && this.getVelocity()[0] == 0 && this.getTimeSinceMove() >= 1) {
			this.setSprite(list[0]);
			this.resetRunningTime();
			 
		}
		else if (this.isDucking()  && this.getVelocity()[0] == 0 && this.getTimeSinceMove() >= 1) {
			this.setSprite(list[1]);
			this.resetRunningTime();
			 
		}
		else if (!this.isDucking() && this.getVelocity()[0] == 0 && this.getTimeSinceMove() < 1 && this.getOldDirection() == Direction.RIGHT) {
			this.setSprite(list[2]);
			this.resetRunningTime();
			 
		}
		else if (!this.isDucking() && this.getVelocity()[0] == 0 && this.getTimeSinceMove() < 1 && this.getOldDirection() == Direction.LEFT) {
			this.setSprite(list[3]);
			this.resetRunningTime();
			 
		}
		else if (!this.isDucking() && this.getVelocity()[0] > 0 && this.getVelocity()[1] > 0) {
			this.setSprite(list[4]);
			this.resetRunningTime();
			this.setTimeSinceMove(0);
		}
		else if (!this.isDucking() && this.getVelocity()[0] < 0 && this.getVelocity()[1] > 0) {
			this.setSprite(list[5]);
			this.resetRunningTime();
			this.setTimeSinceMove(0);
		}
		else if (this.isDucking()  && this.getTimeSinceMove() < 1 && this.getOldDirection() == Direction.RIGHT) {
			this.setSprite(list[6]);	
			this.resetRunningTime();;
			 
		}
		else if (this.isDucking() == true && this.getTimeSinceMove() < 1 && this.getOldDirection() == Direction.LEFT) {
			this.setSprite(list[7]);
			this.resetRunningTime();;
			 
		}
		else if (this.isDucking() == true && this.getVelocity()[0] > 0) {
			this.setSprite(list[6]);	
			this.resetRunningTime();
			this.setTimeSinceMove(0);
			 
		}
		else if (this.isDucking() == true && this.getVelocity()[0] < 0) {
			this.setSprite(list[7]);
			this.resetRunningTime();
			this.setTimeSinceMove(0);
		}
		
		else if(this.getVelocity()[0] > 0 && !this.isDucking() && this.getVelocity()[1] <= 0) {
			this.setRunningTime(getRunningTime() + time);
			this.setTimeSinceMove(0);
			double counting = this.getRunningTime()/0.075;
			counter1 = (int) counting;
			while(counter1 >= shorterList) {
				counter1 -= shorterList;
			}
			this.sp = list[8+counter1];
			//if (getx() >= getMaxx()-0.01) {
			//	this.sp = list[8+shorterList];
			//}
			//else {
				
			//}
		}
		else if(this.getVelocity()[0] <0 && !this.isDucking() && this.getVelocity()[1] <= 0) {
			this.setRunningTime(getRunningTime() + time);
			this.setTimeSinceMove(0);
			double counting = this.runningtime/0.075;
			counter2 = (int) counting;
			while(counter2 >= shorterList) {
				counter2 -= shorterList;
			}
			
			//if (getx()<=0) {
			//	this.sp = list[len-1];
			//}
			//else {
				this.sp = list[8+shorterList+counter2];
			//}
		}
		else if (!this.isDucking()&& this.getVelocity()[0] == 0) {
			this.setSprite(list[0]);
			this.resetRunningTime();
		}
		this.setCurrentSize();
		try {// doe dit om te testen of de sprite op de locatie past
			 // hij wordt niet geterminate, want dezelfde positie wordt gezet:
			 // is in de wereld --> dit blijft in de wereld
			this.setPosition(this.getPosition());
		}catch (ModelException exc) {
			this.setSprite(temp);
			this.setCurrentSize();
		}
		
	}


	private void resetRunningTime() {
		this.setRunningTime(0);
	}

	/**
	 * @note	This method is not required to be documented
	 */
	public Sprite getCurrentSprite() {
		return this.sp;
	}
	
	/**
	 * @note	This method is not required to be documented
	 */
	public Sprite[] getAllSprites() {
		return Arrays.copyOf(list, list.length);
	}
	
	@Override
	public Sprite[] getSprites() {
		return this.list;
	}

	@Override
	public double getXAcceleration() {
		if (this.isDucking()){
			return 0;
		}
		else {
			return super.getXAcceleration();
		}
	}
	
	/**
	 * @note	This method is not required to be documented
	 */
	@Override
	public void advanceMisc(double time) {
		this.updateDuck();
		if (this.getDirection() != Direction.FRONT) {
			this.setOldDirection(this.getDirection());
		}
		else {
			if (this.getTimeSinceMove() > 1) {
				this.setOldDirection(Direction.FRONT);
			}
		}
	}

	@Override
	public void setVelocity(double[] v) {
		if(v[0] >= this.getMaxVelocity() || v[0] <= -this.getMaxVelocity()) {
			if (v[0] > 0)
				v[0] = this.getMaxVelocity();
			else
				v[0] = -this.getMaxVelocity();
		}
		if (v[0] > -this.getMinHorizontalVelocity() && v[0] < this.getMinHorizontalVelocity()) {
			if (v[0] < 0)
				v[0] = -this.getMinHorizontalVelocity();
			if (v[0] > 0)
				v[0] = this.getMinHorizontalVelocity();
			if (v[0] == 0) {

				if (this.getDirection() == Direction.LEFT && this.isDucking)
					v[0] = -this.getMinHorizontalVelocity();
				if (this.getDirection() == Direction.RIGHT && this.isDucking)

					v[0] = this.getMinHorizontalVelocity();
			}
		}
		super.setVelocity(v);
	}
	
	/**
	 * @note	This method is not required to be documented
	 */
	@Override
	public void advanceTiles(double time) {
		if(this.hasWorld()) {
			if (this.advanceLava(time)) {}
			else {
				if(this.advanceGas(time)) {}
				else {
				this.advanceWater(time);
				}
			}
		}	
	}
	
	/**
	 * @note	This method is not required to be documented
	 */
	@Override
	public void initializeMovement() {
		if(!this.isMoving()) {
			this.setXAcceleration(0);
			this.setXVelocity(0);
		}
	}
	
}
