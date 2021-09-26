package jumpingalien.model;

import java.util.Arrays;

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Immutable;
import be.kuleuven.cs.som.annotate.Raw;
import jumpingalien.util.ModelException;
import jumpingalien.util.Sprite;
/**
 *A class of sharks as a special kind of jumpers. Appart from being able to jump they can also float on water.
 * Sharks like to eat plants, but only alive plants. Sharks are immune for normal geological features.
 * 
 * @Invar	The amount of hitpoints of an shark is a positive integer
 * 			|thisHP()>=0
 * @Invar 	The maximum amount of hitpoints of a shark is 500 .
 * 			|this.getMaxHP() == 500
 * 			|this.getHP() =< this.getMaxHP()
 * @Invar 	A shark does not die of old age, its maximum time it can be alive is -1.
 * 			|this.getMaxTimeAlive() == -1
 * @Invar	The time a shark is alive is a positive double.
 * 			|this.getTimeAlive() >= 0
 * @Invar	The time an shark stays in a world after he dies is between 0 and maxTimeDeath.
 * 			|( 0 <= this.getTimeDeath() <= this.getMaxTimeDeath() )
 * @Invar	The maximum absolute value of the acceleration in the horizontal direction of
 * 			a shark is 1.5 .
 * 			|this.getMaxXAcceleration() == 1.5
 * @Invar	The vertical free fall acceleration of a shark is -10 .
 * 			|this.getGravity() == -10
 * @Invar	A shark jumps with a speed of 2
 * 			|This.jumpspeed == 2
 * @Invar	Shark's damage for touching gas is 0 .
 * 			|this.getTileValues().getGasDelta() == 0
 * @Invar	Shark's damage for touching water is 0 .
 * 			|this.getTileValues().getWaterDelta() == 0
 * @Invar	Shark's damage for touching lava is 0 .
 * 			|this.getTileValues().getLavaDelta() == 0
 * @Invar	Shark's damage for not touching water is 6 .
 * 			|this.getTileValues().getNonWaterDelta() == 6
 * @Invar	Shark takes damage from tiles every 0.2 seconds.
 * 			|this.getTileValues().getGasTime() == 0.2 		and
 * 			|this.getTileValues().getWaterTime() == 0.2 	and
 * 			|this.getTileValues().getLavaTime() == 0.2 		and
 * 			|this.getTileValues().getNonWaterTime() == 0.2
 * @Invar	Shark's damage for touching a mazub that is alive is 50 .
 * 			|Shark.getMazubDamage() == 50
 * @Invar	Shark gains 20 HP for touching a slime that is alive.
 * 			|Shark.getSlimeDamage() == 20
 * @Invar	A shark does not want to overlap with other organisms.
 * 			|this.CanOverLap() = false
 * @Invar	The possible directions a shark can have is Direction.LEFT or Direction.RIGHT .
 * 			|this.getDirection() == Direction.LEFT  or  this.getDirection() == Direction.RIGHT
 * @Invar	The velocity of a shark in the vertical direction (going up) can never
 * 			be greater the its jumpspeed which is 2 .
 * 			|this.getYVelocity() <= this.getJumpSpeed() == 2
 * @Invar	TODO associations
 * 
 * @author	Wannes Vande Cauter, Nils Van Dessel	--	Fysica
 */
public class Shark extends Jumper{
	
	private Sprite[] list;
	private static double xAcceleration = 1.5;
	private static double yAcceleration = -10;
	private static double maxVelocity = 1000;
	
	private static int gasDelta = 0;
	private static int waterDelta = 0;
	private static int lavaDelta = 0;
	private static int nonWaterDelta = -6;
	private static int[] tileDeltas = new int[] {gasDelta,waterDelta,lavaDelta,nonWaterDelta};
	private static double[] tileTimes = new double[] {0.2,0.2,0.2,0.2};
	private static TileInfo tileValues = new TileInfo(tileDeltas, tileTimes);
	
	private Sprite currentSprite;
	
	//Shark specific variables
	public static final int mazubDamage=50;
	public static final int slimeHeal=10;
	public static final double jumpSpeed = 2;
	
	/**
	 * Initialize this new shark with an x coordinate of its lower left pixel, 
	 * an y coordinate of its lower left pixel and a list of sprites.
	 * 
	 * @param 	pixelLeftX
	 * 			The x-coordinate of the lower left pixel of the shark.
	 * @param 	pixelBottomY
	 * 			The y-coordinate of the lower left pixel of the shark.
	 * @param 	sprites
	 * 			A list of sprites representing the appearance of shark.
	 * @post	TODO
	 * @throws	ModelException
	 * 			TODO
	 * @throws
	 * @throws
	 * @throws
	 */
	@Raw
	public Shark(int x, int y,Sprite... sprites) {
		super(maxVelocity,xAcceleration,yAcceleration,jumpSpeed,tileValues);
		if (sprites != null) {
			
			Sprite[] sprit = Arrays.copyOf(sprites, sprites.length);
			int len = sprites.length;
			if (len != 3) {
				throw new ModelException("sprite list can't be odd");
			}
			
			if (this.containsNull(sprit)) {
				throw new ModelException("sprite list invalid");
			}
			
			this.list = Arrays.copyOf(sprites, sprites.length);
			this.setCurrentSprite(0);
			
			}
			else {
				throw new ModelException("sprite list can't be null");
			}
			this.setCurrentSize();
			double meterX = (double) x;
			meterX /= 100;
			double meterY = (double) y;
			meterY /= 100;
			
			this.setCurrentSize();
			this.setMeterPosition(meterX,meterY);
			this.setDirection(Direction.LEFT);
			this.setSprite(this.getSprites()[0]);
	}
	
	/**
	 * Returns the damage amount a mazub would do to a shark in case of interaction.
	 */
	@Basic
	@Immutable
	public static int getMazubDamage() {
		return Shark.mazubDamage;
	}
	
	/**
	 * Returns the damage amount a slime would do to a shark in case of interaction.
	 */
	@Basic
	@Immutable
	public static int getSlimeHeal() {
		return Shark.slimeHeal;
	}
	
	/**
	 * An interaction from shark to the organism happens.
	 * 
	 * @param	organism
	 * 			The organism on which shark interacts.
	 * @effect	The organism gets interacted on by this shark.
	 * 			|organism.interactShark(this.isAlive())
	 * @note	The organism does not retaliate in this method.
	 */
	@Override
	protected void interact(Organism organism) {
		organism.interactShark(this.isAlive());
	}
	
	
	
	/**
	 * This Shark feels the interaction of a mazub.
	 * 
	 * @param	mazub
	 * 			The mazub who does damage to this shark (if mazub is alive).
	 * @post	If the mazub is alive, he will do Shark.getMazubDamage() damage to the shark.
	 * 			|if (mazub.isAlive())
	 * 			|then this.loseHP(Shark.getMazubDamage())
	 */
	@Override
	protected void interactMazub(Mazub mazub) {
		if (mazub.isAlive()) {
			this.loseHP(Shark.getMazubDamage());
		}
	}
	
	/**
	 * This shark feels the interaction of another shark, which does nothing in the current version of the game.
	 * 
	 * @param 	sharkAlive
	 * 			Whether other shark is alive.
	 * @note 	Absolutely Nothing.
	 */
	@Override
	protected void interactShark(boolean sharkAlive) {/* Nothing */}
	
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
		if (slime.isAlive()) {
			this.gainHP(Shark.getSlimeHeal());
		}
	}
	
	/**
	 * Nothing
	 */
	@Override
	protected void interactPlant(boolean plantAlive) {/* Nothing */}

	/**
	 * The velocity of the shark gets set.
	 * 
	 * @param 	v
	 * 			The new velocity of the organism.
	 * @effect	The superclass uses v as a new velocity.
	 * 			|super.setVelocity(v)
	 * @effect	The direction gets updated.
	 * 			|this.updateDirection()
	 */
	@Override
	public void setVelocity(double[] v) {
		super.setVelocity(v);
	}

	/**
	 * @note This method is not required to be documented.
	 */
	@Override
	protected void updateDirection(double time) {
		double timing = this.getTimeAlive() + time;
		while (timing >= 3) {
			timing -= 3;
		}
		if (timing < 0.5) {
			if(this.getDirection() != Direction.LEFT) {
				this.setDirection(Direction.LEFT);
				this.setXAcceleration(-Shark.xAcceleration);
				if (this.getWorld().overlapsWater(this,this.getPosX(),this.getPosY())) {
					this.setYVelocity(Shark.jumpSpeed);
					this.setYAcceleration(Shark.yAcceleration);
				}
				int pos = this.getPosY();
				int x = this.getPosX();
				if(this.hasWorld()) {
					if ((this.getWorld()).analyseHLine(pos-2, x, x+this.getSizeX() ,GeologicalFeature.SOLID) || (this.getWorld()).analyseHLine(pos-2, x, x+this.getSizeX() ,GeologicalFeature.ICE)) {
						this.setYVelocity(Shark.jumpSpeed);
						this.setYAcceleration(Shark.yAcceleration);
					}
				}
			}
		}
		else if ((timing >= 0.5 && timing < 1.5) || (timing >= 2 && timing < 3)) {
			this.setDirection(Direction.FRONT);
			this.setXVelocity(0);
			this.setXAcceleration(0);
		}
		else if (timing >= 1.5 && timing < 2) {
			if (this.getDirection() != Direction.RIGHT) {
				this.setDirection(Direction.RIGHT);
				this.setXAcceleration(-Shark.xAcceleration);
				
				if (this.getWorld().overlapsWater(this,this.getPosX(),this.getPosY())) {
					this.setYVelocity(Shark.jumpSpeed);
					this.setYAcceleration(Shark.yAcceleration);
				}
				int pos = this.getPosY();
				int x = this.getPosX();
				if(this.hasWorld()) {
					if ((this.getWorld()).analyseHLine(pos-2, x, x+this.getSizeX() ,GeologicalFeature.SOLID) || (this.getWorld()).analyseHLine(pos-2, x, x+this.getSizeX() ,GeologicalFeature.ICE)) {
						this.setYVelocity(Shark.jumpSpeed);
						this.setYAcceleration(Shark.yAcceleration);
					}
				}

			}
		}
		
	}
	
	/**
	 * @note This method is not required to be documented.
	 */
	@Override
	public Sprite[] getSprites() {
		return this.list;
	}
	
	/**
	 * @note This method is not required to be documented.
	 */
	@Override
	public Sprite getCurrentSprite() {
		return this.currentSprite;
	}
	
	/**
	 * @note This method is not required to be documented.
	 */
	@Override
	public void setCurrentSprite(double time) {
		if (this.getDirection() == Direction.LEFT) {
			this.currentSprite = list[1];
		}
		else if(this.getDirection() == Direction.FRONT) {
			this.currentSprite = list[0];
		}
		else if (this.getDirection() == Direction.RIGHT) {
			this.currentSprite = list[2];
		}
	}
	
	/**
	 * @note This method is not required to be documented.
	 */
	@Override
	public void advanceAcceleration(double time) {		
		int pos = this.getPosY();
		int x = this.getPosX();
		double a = this.getYAcceleration();
		if(this.hasWorld()) {
			if ((this.getWorld()).analyseHLine(pos-2, x, x+this.getSizeX() ,GeologicalFeature.SOLID) || (this.getWorld()).analyseHLine(pos-2, x, x+this.getSizeX() ,GeologicalFeature.ICE)) {
				a = 0;
			}
			else {
				a = this.getGravity();
			}
			if (this.getYVelocity() > 0) {
				a = this.getGravity();
			}
			if (this.getWorld().analyseHLine(pos+this.getCurrentSize()[1], x, x+this.getSizeX(), GeologicalFeature.WATER)) {
				a = 0;
				if (this.getYVelocity() < 0) {
					this.setYVelocity(0);
				}
			}
		}
		else {
			a = this.getGravity();
		}
		this.setAcceleration(this.getDirection().dirToInt()*this.getMaxXacceleration(), a);
}
	
	/**
	 * @note This method is not required to be documented.
	 */
	@Override
	public void advanceVelocity(double time) {
		double vx = this.getXVelocity();
		double ax = this.getXAcceleration();
		
		vx = vx + ax*time;
		
		double vy = this.getYVelocity();
		double ay = this.getYAcceleration();
		
		vy = vy + ay*time;
		
		this.setVelocity(new double[] {vx,vy});		
	}

	/**
	 * @note This method is not required to be documented.
	 */
	@Override
	public void advanceTiles(double time) {
		this.advanceWater(time);
	}
	
	/**
	 * @note This method is not required to be documented.
	 */
	@Override
	public void advanceWater(double time) {
		if (this.hasWorld()) {
			int[] checkPos = this.getPosition();
			if(!this.getWorld().overlapsWater(this,checkPos[0],checkPos[1])) {
				if(this.getTimeOnWater() == -1) {
					this.setTimeOnWater(0);
				}
				if (this.getTimeOnWater() != -1) {
					this.setTimeOnWater(this.getTimeOnWater() + time);
				}
				if (this.getTimeOnWater() >= this.getTileValues().getNonWaterTime()) {
					/**
					this.loseHP(this.getMultiplier()*this.getWorld().getWaterDamage());
				 	*/
					this.changeHPByAmount(this.getTileValues().getNonWaterDelta());
					this.setTimeOnWater(this.getTimeOnWater() - this.getTileValues().getWaterTime());
				}
			}
			else {
				this.setTimeOnWater(-1);
			}
		}
	}
	
	/**
	 * @note This method is not required to be documented.
	 */
	@Override
	public void initializeMovement() {
		this.setDirection(Direction.FRONT);
		this.updateDirection(0);
		this.setCurrentSprite(0);
	}
	
	/**
	 * @note This method is not required to be documented.
	 */
	public void setSprite(Sprite sprite) {
		this.currentSprite = sprite;
	}
	
	/**
	 * @note This method is not required to be documented.
	 */
	@Override
	public double alterTypeTime(double dt, double timing) {
			double timeCheck = this.getTimeAlive()+timing;
			while (timeCheck > 3)
				timeCheck -= 3;
			if (timeCheck < 0.5 && timeCheck+dt >= 0.5) {
				dt = 0.5 - timeCheck;
			}
			if (timeCheck >= 0.5 && timeCheck < 1.5 && timeCheck+dt >= 1.5) {
				dt = 1.5 - timeCheck;
			}
			if (timeCheck >= 1.5 && timeCheck < 2 && timeCheck+dt >= 2) {
				dt = 2 - timeCheck;
			}
			if (timeCheck >= 2 && timeCheck < 3 && timeCheck+dt >= 3) {
				dt = 3 - timeCheck;
			}
		return dt;
	}
	
}
