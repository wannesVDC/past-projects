package jumpingalien.model;

import java.util.Arrays;

import be.kuleuven.cs.som.annotate.Raw;
import be.kuleuven.cs.som.annotate.Basic;
import jumpingalien.util.ModelException;
import jumpingalien.util.Sprite;

/**
 * A class of skullcabs as a special kind of plants. They are not able to jump, fall or duck, 
 * but they are capable of hovering on passable and impassable terrain. They only have 3 HP.
 * He always wanted to be a taxi driver.
 * 
 * @Invar	the amount of hitpoints of an skullcab is a positive intiger
 * 			|this.getHP()>=0
 * @Invar	The maximum amount of hitpoints of a skullcab is 3 .
 * 			|this.getMaxHP() == 3 and
 * 			|this.getHP() <= this.getMaxHP()
 * @Invar 	A skullcab has a lifetime ranging between 0 and 12 seconds
 * 			|if this.isAlive()
 * 			|then 0 =< this.getTimeAlive() < this.getMaxTimeAlive() == 12
 * @Invar	the time a skullcab stays in a world after he dies is between 0 and this.MaxTimeDeath.
 * 			|if this.isEliminated()
 * 			|then ( 0 <= this.getTimeDeath() <= this.getMaxTimeDeath() )
 * @Invar	A skullcab does not move in the x direction.
 * 			|this.getMaxXacceleration() == 0 
 * @Invar	A skullcab goes up and down with a constant velocity.
 * 			|this.getGravity() == 0 and
 * 			|this.getMaxVelocity() == 0.5
 * @Invar	A skullcab does not heal nor take damage from geological features. 
 * 			|this.getTileValues().getGasDamage() == 0		and
 * 			|this.getTileValues().getWaterDamage() == 0		and
 *  		|this.getTileValues().getLavaDamage() == 0		and
 *   		|this.getTileValues().getnonWaterDamage() == 0	and
 * 			|this.getTileValues().getGasTime() == 1			and
 * 			|this.getTileValues().getWaterTime() == 1		and
 * 			|this.getTileValues().getLavaTime() == 1		and
 * 			|this.getTileValues().getnonWaterTime() == 1
 * @Invar	A skullcab can overlap with other organisms.
 * 			|this.CanOverLap() = true
 * @Invar	The possible directions a skullcab can have is
 * 			either Direction.DOWN or Direction.UP .
 * 			|this.getDirection() == Direction.DOWN  or  this.getDirection() == Direction.UP
 * @Invar	A skullcab takes 1 damage after touching an alive mazub for 0.6 seconds who does not have the maximum amount of HP he is able to have.
 * 			|skullcab.getMazubDamage() == 1
 * @Invar	TODO associations
 * 
 * @authors Wannes Vande Cauter, Nils Van Dessel	--	Fysica
 */
public class Skullcab extends Plant implements Ivertical {

	private static int maxHP = 3;
	private static double maxTimeAlive = 12;
	
	private double timeInLoop = 0;
	private Sprite[] list;
	private Sprite currentSprite;
	private Direction direction = Direction.UP;
	//private double timeTouchingMazub = -1;
	
	private static final int mazubDamage = 1;
	
	/**
	 * Initialize this new skullcab with an x coordinate of its lower left pixel, 
	 * an y coordinate of its lower left pixel and a list of sprites.
	 * 
	 * @param 	pixelLeftX
	 * 			The x-coordinate of the lower left pixel of the skullcab.
	 * @param 	pixelBottomY
	 * 			The y-coordinate of the lower left pixel of the skullcab.
	 * @param 	sprites
	 * 			A list of sprites representing the appearance of skullcab.
	 * @post	The maximum amount of hitpoints is set.
	 * 			|new.getMaxHP() == 1
	 * @post	The maximum time alive is set.
	 * 			|new.getMaxTimeAlive() == 10
	 * @post	The spritelist is set.
	 * @post	The current sprite is set.
	 * @post	The direction is set to Direction.LEFT
	 * 			|new.getDirection() == Direction.LEFT
	 * @throws	ModelException
	 * 			TODO
	 * @throws	ModelException
	 * 
	 * @throws	ModelException
	 * 
	 * @throws 	ModelException
	 * 
	 */
	@Raw
	public Skullcab(int pixelLeftX, int pixelBottomY, Sprite... sprites) {
		super(pixelLeftX, pixelBottomY,Skullcab.maxHP,Skullcab.maxTimeAlive);
		// TODO Auto-generated constructor stub
		Sprite[] sprit = Arrays.copyOf(sprites, sprites.length);
		int len = sprites.length;
		if (len != 2) {
			throw new ModelException("sprite list can't be odd");
		}
		if (this.containsNull(sprit)) {
			throw new ModelException("sprite list invalid");
		}
		
		this.list = Arrays.copyOf(sprites, sprites.length);
		this.advanceVelocity(0);
		this.setCurrentSprite(0);
		this.setCurrentSize();
	}

	@Basic
	public double getTimeInLoop() {
		return this.timeInLoop;
	}
	@Basic
	public void setTimeInLoop(double time) {
		this.timeInLoop = time;
	}
	
	@Basic
	public static int getMazubDamage() {
		return Skullcab.mazubDamage;
	}
	
	/**
	 * @note This method is not required to be documented.
	 */
	public void advanceDirection(double time) {
		// time alive hasn't been updated yet
		// I need to find a way to keep counting up in advance loop
		// so: timeInLoop
		this.setTimeInLoop(this.getTimeInLoop()+time);
		double timing = this.getTimeAlive() + this.getTimeInLoop();
		while (timing >=1) {
			timing -= 1;
		}
		if (timing < 0.5) {
			this.setDirection(Direction.UP);
		}
		else {
			this.setDirection(Direction.DOWN);
		}
	}
	
	/**
	 * @note This method is not required to be documented.
	 */
	@Override
	public void advanceMisc(double time) {
		this.setTimeInLoop(0);
	}
	
	/**
	 * @note This method is not required to be documented.
	 */
	@Override
	public void advanceVelocity(double time) {
		this.advanceDirection(time);
		this.setYVelocity(this.getDirection().dirToInt()*this.getMaxVelocity());
	}
	
	/**
	 * @note This method is not required to be documented.
	 */
	public double[] getAdvancePositionAmount(double time) {
		return new double[] {this.getMeterPosX(),this.getMeterPosY() + this.getYVelocity()*time};
	}

	/**
	 * Returns whether this is ducking.
	 * 
	 * @return 	false
	 * 			This cannot duck, so it is never ducking.
	 */
	@Override
	public boolean isDucking() {
		return false;
	}

	/**
	 * @note This method is not required to be documented.
	 */
	@Override
	protected void switchDirection() {
		this.setDirection(this.getDirection().getOppositeDirection());
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
		this.currentSprite = (this.getDirection() == Direction.UP)? list[0]:list[1];
	}

	@Basic
	@Override
	public Direction getDirection() {
		return this.direction;
	}


	@Override
	public void setDirection(Direction dir) {
		this.direction = dir;
	}
	
	/**
	 * Makes sure this loses 1 HP because of the mazub, if the mazub can eat it and dies if HP becomes 0
	 * 
	 * @param 	mazub
	 * 			The mazub to interact with.
	 * 
	 * @effect	this loses 1HP and may be terminated
	 * 			|this.loseHP(1)
	 * 			|if (this.getHP() == 0)
	 * 			|this.terminate();
	 */
	@Override
	protected void interactMazub(Mazub mazub) {
		if (this.getTimeAlive() < this.getMaxTimeAlive()) {
			if (mazub.getHP() < mazub.getMaxHP()) {
				this.loseHP(Skullcab.getMazubDamage());
				if (this.getHP() == 0) {
					this.terminate();
				}
			}
		}
		else {
			this.terminate();
		}
	}
}
