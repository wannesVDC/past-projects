package jumpingalien.model;

import java.util.Arrays;

import be.kuleuven.cs.som.annotate.Raw;
import be.kuleuven.cs.som.annotate.Basic;
import jumpingalien.util.ModelException;
import jumpingalien.util.Sprite;
/**
 * A class of sneezeworts as a special kind of plants. They are not able to jump, fall or duck, 
 * but they are capable of hovering on passable and impassable terrain. They only have 1 HP.
 * 
 * @Invar	the amount of hitpoints of an sneezewort is a positive intiger
 * 			|this.getHP()>=0
 * @Invar	The maximum amount of hitpoints of a sneezewort is 1 .
 * 			|this.getMaxHP() == 1
 * @Invar 	A sneezewort has a lifetime ranging between 0 and 10 seconds
 * 			|if this.isAlive()
 * 			|then 0 =< this.getTimeAlive() < this.getMaxTimeAlive() == 10
 * @Invar	the time a sneezewort stays in a world after he dies is between 0 and this.MaxTimeDeath.
 * 			|if this.isEliminated()
 * 			|then ( 0 <= this.getTimeDeath() <= this.getMaxTimeDeath() )
 * @Invar	A sneezewort goes up and down with a constant velocity.
 * 			|this.getMaxXacceleration() == 0 and
 * 			|this.getMaxVelocity() == 0.5
 * @Invar	A sneezewort does not move in the y direction.
 * 			|this.getGravity() == 0
 * @Invar	A sneezewort does not heal nor take damage from geological features. 
 * 			|this.getTileValues().getGasDamage() == 0		and
 * 			|this.getTileValues().getWaterDamage() == 0		and
 *  		|this.getTileValues().getLavaDamage() == 0		and
 *   		|this.getTileValues().getnonWaterDamage() == 0	and
 * 			|this.getTileValues().getGasTime() == 1			and
 * 			|this.getTileValues().getWaterTime() == 1		and
 * 			|this.getTileValues().getLavaTime() == 1		and
 * 			|this.getTileValues().getnonWaterTime() == 1
 * @Invar	A sneezewort can overlap with other organisms.
 * 			|this.CanOverLap() = true
 * @Invar	The possible directions a sneezewort can have is
 * 			either Direction.LEFT or Direction.RIGHT .
 * 			|this.getDirection() == Direction.LEFT  or  this.getDirection() == Direction.RIGHT
 * @Invar	TODO associations
 * 
 * @authors Wannes Vande Cauter, Nils Van Dessel	--	Fysica
 */
public class Sneezewort extends Plant implements Ihorizontal {
	
	private static int maxHP = 1;
	private static double maxTimeAlive = 10;
	private Sprite[] list;
	private Sprite currentSprite;
	private Direction direction = Direction.LEFT;
	private double timeInLoop = 0;
	
	/**
	 * Initialize this new sneezewort with an x coordinate of its lower left pixel, 
	 * an y coordinate of its lower left pixel and a list of sprites.
	 * 
	 * @param 	pixelLeftX
	 * 			The x-coordinate of the lower left pixel of the sneezewort.
	 * @param 	pixelBottomY
	 * 			The y-coordinate of the lower left pixel of the sneezewort.
	 * @param 	sprites
	 * 			A list of sprites representing the appearance of sneezewort.
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
	public Sneezewort(int pixelLeftX, int pixelBottomY, Sprite... sprites) {
		super(pixelLeftX, pixelBottomY, Sneezewort.maxHP, Sneezewort.maxTimeAlive);
		if (sprites != null) {
			Sprite[] sprit = Arrays.copyOf(sprites, sprites.length);
			int len = sprites.length;
			if (this.containsNull(sprit)) {
				throw new ModelException("sprite list invalid");
			}
			if (len != 2) {
				throw new ModelException("sprite list can't be odd");
			}
		}
		else {
			throw new ModelException("...");
		}
		
		this.list = Arrays.copyOf(sprites, sprites.length);
		this.setCurrentSprite(0);
		this.setCurrentSize();
		this.advanceVelocity(0);
	}


	@Override
	public boolean isDucking() {
		return false;
	}

	public double getTimeInLoop() {
		return this.timeInLoop;
	}
	
	public void setTimeInLoop(double time) {
		this.timeInLoop = time;
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
			this.setDirection(Direction.LEFT);
		}
		else {
			this.setDirection(Direction.RIGHT);
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
		this.setXVelocity(this.getDirection().dirToInt()*this.getMaxVelocity());
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
	@Basic
	@Override
	public Sprite getCurrentSprite() {
		return this.currentSprite;
	}

	/**
	 * @note This method is not required to be documented.
	 */
	@Override
	public void setCurrentSprite(double time) {
		this.currentSprite =  (this.getDirection() == Direction.LEFT) ? list[0] : list[1];
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
	 * @note This method is not required to be documented.
	 */
	public double[] getAdvancePositionAmount(double time) {
		return new double[] {this.getMeterPosX() + this.getXVelocity()*time,this.getMeterPosition()[1]};
	}

	

}
