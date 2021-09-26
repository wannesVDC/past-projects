package jumpingalien.model;

import be.kuleuven.cs.som.annotate.*;
import jumpingalien.util.ModelException;
import jumpingalien.util.Sprite;

import java.util.Arrays;
import java.util.Set;
/**
 * A class of organisms who can belong to a world.
 * 
 * @Invar	The amount of hitpoints of an organism is a positive integer.
 * 			|this.getHP() >= 0
 * @Invar	The amount of hitpoints of an organism is smaller than (or equal to) it maximum amount of HP.
 * 			|this.getHP() <= this.getMaxHP()
 * @Invar	the time an organism is alive is a positive double.
 * 			|this.getTimeAlive() >= 0
 * @Invar	the time an organism stays in a world after he dies is between 0 and this.MaxTimeDeath.
 * 			|if this.isEliminated()
 * 			|then ( 0 <= this.getTimeDeath() <= this.getMaxTimeDeath() )
 * 
 * @authors  Wannes Vande Cauter, Nils Van Dessel	--	Fysica
 */
public abstract class Organism {
	private ObjectState currentObjectState = ObjectState.ALIVE;
	private int HP;
	private World world = null;
	private int[] pixelPosition = new int[2];
	private double maxVelocity;
	private double xAcceleration;
	private double yAcceleration;
	private double[] meterPosition = new double[2];
	private double[] velocity = new double[2];		//  m/s
	private double[] acceleration = new double[2];	// m/s**2
	private double timeAlive = 0;
	private double timeDeath = 0;
	protected double timeInteraction = -1;
	private final double maxTimeDeath = 0.6;
	
	protected double timeOnGas = -1;
	protected double timeOnLava = -1;
	protected double timeOnWater = -1;
	private double timeSinceInteraction = -1;
	
	protected TileInfo tileValues;
	private boolean[] collidesSolidAt = new boolean[4];	
	
	//subclass-specific variables
	private int maxHP;	
	private double maxTimeAlive;
	private boolean canOverlap;
	private int[] currentSize = {0,0};
	private static double interactionTimeOut = 0.6;
	
	/**
	 * Initialize this new organism based on the given parameters of the subclass that calls the constructor
	 * 
	 * @param 	maxHP
	 * 			The maximum amount of HP the organism can have.
	 * @param 	maxVelocity
	 * 			The maximum absolute value amount of the velocity the organism can have
	 * 			by default this is meant for the horizontal direction
	 * 			but for skullcab this is meant for the vertical direction.
	 * @param 	xAcceleration
	 * 			The maximum absolute value of the amount of the acceleration of the organism 
	 * 			in the horizontal direction.
	 * @param 	yAcceleration 
	 * 			The maximum absolute value of the amount of the acceleration of the organism 
	 * 			in the horizontal direction.
	 * @param 	maxTimeAlive
	 * 			The maximum amount of time the organism can be alive.
	 * 			If the value is a negative amount, the organism will not die of old age.
	 * @param 	canOverlap
	 * 			This boolean shows whether the organism allows overlap with other organisms.
	 * @param 	tileValues
	 * 			This tileInfo object represents how the organism interacts with water, magma, gas and nonWater.
	 * @post	The maximum amount of hitpoints of this organism is 100 or 1.
	 * 			|new.getMaxHP() == 1 or this.getMaxHP() ==100
	 * @post	The maximum velocity is set to maxVelocity.
	 * 			|new.maxVelocity() = Math.abs(maxVelocity)
	 * @post	The maximum xAcceleration is set to xAcceleration.
	 * 			|this.getMaxXAcceleration() == xAcceleration
	 * @post	The gravity is set to yAcceleration.
	 * 			|this.getGravity() == yAcceleration
	 * @post	The maximum time alive is set to maxTimeAlive.
	 * 			|this.getMaxTimeAlive() == maxTimeAlive
	 * @post	The organism knows whether he can overlap.
	 * 			|this.canOverlap() == canOverlap
	 * @post	The organism knows it's tileValues.
	 * 			|this.getTileValues() == tileValues
	 * 
	 */
	protected Organism(int maxHP,double maxVelocity,double xAcceleration,double yAcceleration,double maxTimeAlive,boolean canOverlap,TileInfo tileValues) {
		this.maxHP = Math.abs(maxHP);
		this.changeHP(100);
		this.maxVelocity = Math.abs(maxVelocity);
		this.xAcceleration = Math.abs(xAcceleration);
		this.yAcceleration = yAcceleration;
		this.maxTimeAlive = maxTimeAlive;
		this.canOverlap = canOverlap;
		this.tileValues = tileValues;
	}
	
	/**
	 * Returns the current pixel position of the lower left pixel represented by an integer array of length 2.
	 */
	@Basic
	public int[] getPosition() {
		return this.pixelPosition;
	}
	
	/**
	 * Returns the current X-coordinate of the lower left pixel of the organism represented by an integer.
	 */
	public int getPosX() {
		return this.getPosition()[0];
	}
	
	/**
	 * Returns the current Y-coordinate of the lower left pixel of the organism represented by an integer.
	 */
	public int getPosY() {
		return this.getPosition()[1];
	}
	
	/**
	 * Returns the position in meters of the lower left pixel of the organism represented by a double array of length 2.
	 */
	@Basic
	public double[] getMeterPosition() {
		return this.meterPosition;
	}
	
	/**
	 * Sets the position in meters for 2 given doubles.
	 * 
	 * @param 	x
	 * 			The x amount of a new position in meters.
	 * @param 	y
	 * 			The y amount of a new position in meters.
	 * @effect	Another setMeterPosition is called
	 * 			|this.setMeterPosition(x,y,false)
	 */
	public void setMeterPosition(double x, double y) throws ModelException {
		setMeterPosition(x, y, false);
	}
	
	/**
	 * Sets the position in meters for 2 given doubles and a boolean.
	 * 
	 * @param 	x
	 * 			The x amount of a new position in meters.
	 * @param 	y
	 * 			The y amount of a new position in meters.
	 * @param	advance
	 * 			A boolean which shows whether an advance-position called this function.
	 * @post	This organism is terminated if the lower left pixel is to the left of x=0.
	 * 			|if x<0
	 * 			|then new.isTerminated() == true.
	 * @post	This organism is terminated if the lower left pixel is lower than y=0.
	 * 			|if y<0
	 * 			|then new.isTerminated() == true.
	 * @effect	Another setPosition is called.
	 * 			|this.setPosition(x,y,advance)
	 */
	public void setMeterPosition(double x, double y, boolean advance) throws ModelException {
		double[] list = {x,y};
		if (x<0||y<0) {
			this.terminate();
		}
		this.meterPosition = list;
		this.setPosition(list, advance);
		
	}
	/**
	 * The position in meters is set to the given double array.
	 * 
	 * @param 	list
	 * 			This double list of length 2 represents a new position in meters.
	 * @param 	advance
	 * 			A boolean which shows whether an advance-position called this function.
	 * @effect	Another setMeterPosition is called.
	 * 			|this.setMeterPosition(list[0],list[1])
	 */
	public void setMeterPosition(double[] list, boolean advance) throws ModelException{
		this.setMeterPosition(list[0], list[1], advance);
	}
	
	/**
	 * The position in meters is set to the given double array.
	 * 
	 * @param 	list
	 * 			This double list of length 2 represents a new position in meters.
	 * 			A boolean which shows whether an advance-position called this function.
	 * @effect	Another setMeterPosition is called.
	 * 			|this.setMeterPosition(list,false)
	 */
	public void setMeterPosition(double[] list) throws ModelException{
		this.setMeterPosition(list,false);
	}
	
	/**
	 * Returns a boolean array which shows the side where this organism hits a solid (solid ground or ice).
	 */
	@Basic
	public boolean[] getCollidesSolid() {
		return this.collidesSolidAt;
	}
	
	/**
	 * Returns whether this organism collides at his perimeter.
	 * 
	 * @return	Returns this.getCollidesSolid() contains a true
	 * 			|if this.getCollidesSolid.contains(true)
	 * 			|then result == true
	 * @note	I know that the above syntax does not work for arrays.
	 */
	public boolean getCollidesSolidBool() {
		for(boolean i : this.getCollidesSolid()) {
			if (i) {
				return true;}
		}
		return false;
	}
	
	/**
	 * Returns the maximum acceleration of this organism in the downward direction
	 */
	public double getGravity() {
		return this.yAcceleration;
	}
	
	/**
	 * Returns the maximum acceleration of this organism in the horizontal direction
	 */
	public double getMaxXacceleration() {
		return this.xAcceleration;
	}
	
	/**
	 * Sets the array that shows where this organism hit solid ground or ice.
	 * 
	 * @param 	x
	 * 			A hypothetical x-coordinate for the pixel position of this organism.
	 * @param 	y
	 * 			A hypothetical y-coordinate for the pixel position of this organism.
	 * @post	If this organism has a world, the array will be set to this.getWorld().colidesSolid(this,x,y).
	 * 			|if this.hasWorld()
	 * 			|then new.getCollidesSolid() == this.getWorld().colidesSolid(this,x,y)
	 * @post	If this organism does not have a world, the array will be set to an array filled with false.
	 * 			|if ! this.hasWorld()
	 * 			|then new.getCollidesSolid() == new boolean[] {false,false,false,false};
	 */
	public void setCollides(int x, int y) {
		boolean[] result;
		if (this.hasWorld()) {
			result = this.getWorld().colidesSolid(this, x, y);
		}
		else {
			result = new boolean[] {false,false,false,false};
		}
		this.collidesSolidAt = result;
	}
	
	/**
	 * Sets the position in pixels to the given x and y.
	 * 
	 * @param 	x
	 * 			The new x-coordinate of this organism.
	 * @param 	y
	 * 			The new y-coordinate of this organism.
	 * @param 	advance
	 * 			Whether this function is called from somewhere in advanceTime().
	 * 
	 * @post	|if !this.hasWorld()
	 * 			|then new.getPosition() == [x,y]
	 * 
	 * @post	|if this.hasWorld() and this.getWorld().isValidPosition(x,y) and !this.getWorld.overlapsSolid(this.x,y)
	 * 			|then new.getPosition() == [x,y]
	 * 
	 * @effect	|if (this.hasWorld() and this.getWorld().isValidPosition(x,y)
	 * 			|then this.terminate()
	 * 
	 * @throws 	ModelException
	 * 			|if (this.hasWorld() and this.getWorld().isValidPosition(x,y) and !advance and this.getWorld().overlapsSolid(this,x,y))
	 * 			|then throw new ModelException
	 */
	public void setPosition (int x, int y, boolean advance) throws ModelException{
		if (this.hasWorld()) {
			if (this.getWorld().isValidPosition(x, y)) {
				if (!(this instanceof Plant)) {
					if (!advance) {
						this.setCollides(x, y);
						if (this.getWorld().overlapsSolid(this,x,y)) {
							throw new ModelException("invalid position");
						}
						else {
							this.pixelPosition[0] = x;
							this.pixelPosition[1] = y;
						}
					}
					else {
						this.pixelPosition[0] = x;
						this.pixelPosition[1] = y;
					}
				}
				else {
					this.pixelPosition[0] = x;
					this.pixelPosition[1] = y;
				}
			}
			else {
				this.terminate();
			}
		}
		else {
			this.pixelPosition[0] = x;
			this.pixelPosition[1] = y;
		}
	}
	
	/**
	 * Sets the position of this organism to the given position.
	 * 
	 * @param 	pos
	 * 			An integer array of length 2 which represents position coordinates to set.
	 * @effect	Calls another setPosition.
	 * 			|this.setPosition(pos[0], pos[1], advance)
	 */
	public void setPosition(int[] pos, boolean advance) throws ModelException{
		this.setPosition(pos[0], pos[1], advance);
	}
	
	/**
	 * Sets the position of this organism to the given position.
	 * 
	 * @param 	Pos
	 * 			An integer array of length 2 which represents position coordinates to set.
	 * @effect 	Another setposition gets called with the given position translated to meters.
	 * 			|this.setPosition(this.meterToPixel(Pos), advance);
	 */
	public void setPosition(int[] pos) {
		this.setPosition(pos, false);
	}
	
	/**
	 * Sets the position of this organism to the given position.
	 * 
	 * @param 	Pos
	 * 			The position coordinates to set in meters
	 * @effect 	Another setposition gets called with the given position translated to meters.
	 * 			|this.setPosition(this.meterToPixel(Pos), advance);
	 */
	public void setPosition(double[] Pos, boolean advance) throws ModelException{
		int[] pos = this.meterToPixel(Pos);
		this.setPosition(pos, advance);
	}
	public void setPosition(double[] Pos) {
		this.setPosition(Pos, false);
	}
	
	/**
	 * Translates an array of the position from meters to pixels.
	 * 
	 * @param 	list
	 * 			The list of meters to convert to pixels
	 * @return	The list, converted to pixels
	 * 			|result == [(int) list[0]*100 , (int) list[1]*100]
	 */
	public int[] meterToPixel(double[] list) {
		double[] temp = new double[2];
		temp[0] = list[0]*100;
		temp[1] = list[1]*100;
		
		int temp0 = (int) temp[0];
		int temp1 = (int) temp[1];
		
		double tolerance = 0.99;
		if (temp[0] - temp0 > tolerance) {
			temp0 += 1;
		}
		if (temp[1] - temp1 > tolerance) {
			temp1 += 1;
		}
		return new int[] {temp0,temp1};
	}
	
	/**
	 * Translates an array of the position from pixels to meters.
	 * 
	 * @param 	list
	 * 			The list of pixels to convert to meters
	 * @return	The list, converted to meters
	 * 			|result == [(double) list[0] /100 , (double) list[1] / 100
	 */
	public double[] pixelToMeter(int[] list) {
		double x = (double) list[0];
		x /= 100;
		double y = (double) list[1];
		y /= 100;
		double[] dList = {x,y};
		return dList;
	}
	
	/**
	 * Returns whether this organism wants to overlap.
	 */
	@Basic
	@Immutable
	public boolean canOverLap() {
		return this.canOverlap;
	}
	
	/**
	 * Returns whether 2 organisms are allowed to overlap.
	 * 
	 * @param 	org
	 * 			The organism over which this organism wants to go.
	 * @return 	2 organisms can overlap if they at least 1 of them doesn't mind overlapping.
	 * 			|result == (this.canOverLap() || org.canOverLap())
	 */
	public boolean canPass(Organism org) {
		return (this.canOverLap() || org.canOverLap());
		}
	
	/**
	 * @note	This method is not required to be documented.
	 */
	public boolean overlap(Organism org,int x2,int y2,boolean forInteraction) {
		int sizeX1 = this.getCurrentSize()[0];
		int sizeX2 = org.getCurrentSize()[0];
		int sizeXTotal = sizeX1 + sizeX2;
		int XM1 = this.getPosition()[0] + sizeX1/2;
		int XM2 = x2 + sizeX2/2;
		int dX = Math.abs(XM1 - XM2);
		int sizeY1 = this.getCurrentSize()[1];
		int sizeY2 = org.getCurrentSize()[1];
		int sizeYTotal = sizeY1 + sizeY2;
		int YM1 = this.getPosition()[1] + sizeY1/2;
		int YM2 = y2 + sizeY2/2;
		int dY = Math.abs(YM1 - YM2);
		if (forInteraction) {
			return (dX<=sizeXTotal/2 && dY<=sizeYTotal/2);
		}
		else {
			return (dX<sizeXTotal/2 && dY<sizeYTotal/2);
		}
	}
	
	/**
	 * Returns whether the organism can spawn in a world without overlapping impassable organisms.
	 * 
	 * @param 	x2
	 * 			The x-coordinate to the location this is trying to spawn in.
	 * @param 	y2
	 * 			The y-coordinate to the location this is trying to spawn in.
	 * @return	This can spawn when all of this iterations in the loop over the organisms give ret the value true
	 * 			|result == for all objects in the objectList of world: if(this != object){if(!this.canPass(org){ !object.overlap(this, x2, y2, false)}}
	 */			
	public boolean canSpawn(int x2, int y2) {
		boolean ret = true;
		if (this.hasWorld()) {
			for (Object obj : this.getWorld().getOrganism()) {
				Organism org = (Organism) obj;
				if (this != org) {
					if (!this.canPass(org) && ret) {
						ret = !org.overlap(this, x2, y2, false);
					}
				}
			}
		}
		return ret;
	}


	/**
	 * Returns the velocity of this organism as an array of 2 doubles.
	 */
	@Basic
	public double[] getVelocity() {
		return this.velocity;
	}
	
	/**
	 * Returns the maximum velocity of this organism in a certain direction.
	 */
	@Basic
	public double getMaxVelocity() {
		return maxVelocity;
	}
	
	/**
	 * Usualy this methods allows for a change of sign of the speed in the horizontal direction.
	 * @note	I don't think this method should be documented because it is only used in advanceTime.
	 */
	protected abstract void switchDirection() ;
	
	
	/**
	 * Sets the velocity of this organism to the correction of the given value.
	 * 
	 * @param 	v
	 * 			the new speed of the organism
	 * @post	The velocity is set to [Math.signum(v[0]) *Math.min(this.maxVelocity, Math.abs(v[0])),v[1]]
	 * 			|this.getVelocity() == [Math.signum(v[0]) *Math.min(this.maxVelocity, Math.abs(v[0])),v[1]]
	 */
	public void setVelocity(double[] v) {
		double VX = v[0];
		VX = Math.signum(VX) *Math.min(this.maxVelocity, Math.abs(VX));
		v[0] = VX;
		this.velocity = v;
	}
	
	/**
	 * Tries to set the velocity of this organism to the given x and y.
	 * 
	 * @param 	x
	 * 			the new speed of the organism in the x-direction
	 * @param	y
	 * 			the new speed of the organism in the y-direction
	 * @effect	Another setVelocity gets called.
	 * 			|this.setVelocity([x,y])
	 */
	public void setVelocity(double x, double y) {
		double[] l = {x,y};
		this.setVelocity(l);
	}
	
	/**
	 * Returns the current acceleration of this organism.
	 */
	@Basic
	public double[] getAcceleration() {
		return this.acceleration;
	}
	
	/**
	 * Sets the acceleration of this organism to the given double array.
	 * 
	 * @param 	a
	 * 			The new acceleration of this organism.
	 * @post	The acceleration of this organism is set to a.
	 * 			|new.getAcceleration == a
	 */
	public void setAcceleration(double[] a) {
		this.acceleration = a;
	}
	
	/**
	 * Sets the acceleration of this organism to the given x and y in their respective directions.
	 * 
	 * @param 	x
	 * 			The new x value of the acceleration.
	 * @param 	y
	 * 			The new y-value of the acceleration.
	 * @effect	Another setAcceleration sets this values.
	 * 			|this.setAcceleration(new double[] {x,y} );
	 */
	public void setAcceleration(double x, double y) {
		this.setAcceleration(new double[] {x,y} );
	}
	
	/**
	 * Returns the maximum amount of hitpoints this organism can have.
	 */
	@Basic
	@Immutable
	public int getMaxHP() {
		return this.maxHP;
	}
	
	/**
	 * Returns the current amount of hitpoint of this organism.
	 */
	@Basic
	public int getHP() {
		return this.HP;
	}
	
	/**
	 * Sets the hitpoints of this organism to the given hp.
	 * 
	 * @param 	hp
	 * 			The new amount of hitpoints of this organism.
	 * @post	The new amount of hitpoints of this organism is set to the given hp.
	 * 			|new.getHP() == hp
	 */
	private void setHP(int hp) {
		this.HP = hp;
	}
	
	/**
	 * Changes the hitpoints of this organism to the correction of a given hp.
	 * 
	 * @param 	hp
	 *			The new amount of hitpoints.
	 * @post	The hitpoints stay between 0 and this.getMaxHP()
	 * 			|if (0 < hp < this.getMaxHP()) 
	 * 			|then new.getHP() == hp
	 * @post 	If the given hp < 0 , then the hitpoints gets set to 0n.
	 * 			|if (hp < 0) then new.getHP() == 0
	 * @post	If the given hp > this.getMaxHP() , then the hitpoints gets set to this.getMaxHP().
	 * 			|if (hp > this.getMaxHP()) then new.getHP() == this.getMaxHP()
	 */
	protected void changeHP(int hp) {
		this.setHP(Math.min(this.getMaxHP(),Math.max(hp,0)));
	}
	
	/**
	 * The hitpoints of this organism gets increased/decreased by the given delta.
	 * 
	 * @param 	delta
	 * 			The amount that will be added to the hitpoints.
	 * @effect	delta gets added to the current hitpoints of this organism.
	 * 			|this.changeHP(this.getHP()+delta)
	 * @effect	If the new amount of hitpoints equals 0, then this organism gets eliminated.
	 * 			|if (this.getHP() == 0) 
	 * 			|then this.eliminate();
	 */
	protected void changeHPByAmount(int delta) {
		this.changeHP(this.getHP()+delta);
		if (this.getHP() <= 0) {
			this.eliminate();
		}
	}
	
	/**
	 * The organism gains a given amount of hitpoints.
	 * 
	 * @param 	gain
	 *			An amount that has to be added to the old amount of the hitpoints.
	 * @effect	gain only adds to the old amount.
	 * 			|this.changeHPByAmount(Math.max(0, gain))
	 */
	public void gainHP(int gain) {
		this.changeHPByAmount(Math.max(0, gain));
	}
	
	/**
	 * The organism loses a given amount of hitpoints.
	 * 
	 * @param 	loss
	 *			An amount that has to be subtracted to the old amount of the hitpoints.
	 * @effect	loss only removes from the old amount.
	 * 			|this.changeHPByAmount(-Math.max(0, loss))
	 */
	public void loseHP(int loss) {
		this.changeHPByAmount(-Math.max(0, loss));
	}
	
	/**
	 * Returns the current ObjectState of the organism.
	 */
	private ObjectState getState() {
		return this.currentObjectState;
	}

	/**
	 * This organism interacts with another organism.
	 */
	private void interaction(Organism organism) {
		if(organism instanceof Mazub) {
			this.interact(organism);
			organism.interact(this);
		}
		else {
			organism.interact(this);
			this.interact(organism);
		}
	}
	
	/**
	 * This organism interacts on another organism.
	 * 
	 * @param 	organism
	 * 			The organism that is effected by this interaction.
	 */
	abstract void interact(Organism organism);
	
	/**
	 * This organism  gets interacted on by a mazub.
	 * 
	 * @param 	mazub
	 * 			The mazub that effects this organism.
	 * @note 	The interactfunctions used to only have ...Alive as parameter, 
	 * 			but this got changed for mazub and shark.
	 */
	abstract void interactMazub(Mazub mazub);
	
	/**
	 * This organism  gets interacted on by a shark.
	 * 
	 * @param	sharkAlive
	 * 			Whether the shark that is effecting this organism is alive.
	 * @note 	The interactfunctions used to only have ...Alive as parameter, 
	 * 			but this got changed for mazub and shark.
	 */
	abstract void interactShark(boolean sharkAlive);
	
	/**
	 * This organism gets interacted on by a slime
	 * 
	 * @param 	slime
	 * 			The slime that is effecting this organism.
	 * @note 	The interactfunctions used to only have ...Alive as parameter, 
	 * 			but this got changed for mazub and shark.
	 */
	abstract void interactSlime(Slime slime);
	
	/**
	 * This organism  gets interacted on by a plant.
	 * 
	 * @param	plantAlive
	 * 			Whether the plant that is effecting this organism is alive.
	 * @note 	The interactfunctions used to only have ...Alive as parameter, 
	 * 			but this got changed for mazub and shark.
	 */
	abstract void interactPlant(boolean plantAlive);
	
	/**
	 * @note	This method is not required to be documented.
	 */
	private double getTimeSinceInteraction() {
		return this.timeSinceInteraction;
	}
	
	/**
	 * @note	This method is not required to be documented.
	 */
	private void setTimeSinceInteraction(double time) {
		this.timeSinceInteraction = time;
	}
	
	/**
	 * @note	This method is not required to be documented.
	 */
	private void stopTimeSinceInteraction() {
		this.setTimeSinceInteraction(-1);
	}
	
	/**
	 * @note	This method is not required to be documented.
	 */
	private void startTimeSinceInteraction() {
		this.setTimeSinceInteraction(0);
	}

	/**
	 * @note	This method is not required to be documented.
	 */
	public void advanceTimeSinceInteraction(double time) {
		if (this.getTimeSinceInteraction() >= 0) {
			this.setTimeSinceInteraction(this.getTimeSinceInteraction() + time);
		}
		if (this.getTimeSinceInteraction() > Organism.getInteractionTimeOut()) {
			this.stopTimeSinceInteraction();
		}
	}
	
	/**
	 * @note	This method is not required to be documented.
	 */
	private boolean canInteract(Organism org) {
		if (this instanceof Slime && org instanceof Slime) {
			return true;
		}
		if (!(org instanceof Plant || this instanceof Plant)) {
			boolean thisTime = (Organism.getInteractionTimeOut() <= this.getTimeSinceInteraction() || this.getTimeSinceInteraction() < 0);
			boolean orgTime = (Organism.getInteractionTimeOut() <= org.getTimeSinceInteraction() || org.getTimeSinceInteraction() < 0);
			return (thisTime && orgTime);
		}
		else if(this instanceof Plant) {
			return (Organism.getInteractionTimeOut() <= this.getTimeSinceInteraction() || this.getTimeSinceInteraction() < 0);
		}
		else {// org instanceof Plant
			return (Organism.getInteractionTimeOut() <= org.getTimeSinceInteraction() || org.getTimeSinceInteraction() < 0);
		}
	}
	
	/**
	 * Returns the time out value for the interaction.
	 */
	private static double getInteractionTimeOut() {
		return Organism.interactionTimeOut;
	}

	/**
	 * The state of the object gets set to the given state
	 * 
	 * @param 	newState
	 * 			The new state of the object.
	 * @post	The new state of the object is set to newState.
	 * 			|new.getState == newState
	 */
	protected void setState(ObjectState newState) {
		this.currentObjectState = newState;
		
	}	
	
	/**
	 * @note	This method is not required to be documented.
	 */
	public void updateDuck() {/* Nothing */}
	
	/**
	 * Returns the current size of the organism.
	 */
	@Basic
	public int[] getCurrentSize() {
		return this.currentSize;
	}
	
	/**
	 * The current size is set based of the sprite.
	 * 
	 * @post	|this.getSizeX() == this.getCurrentSprite().getWidth()
	 * @post	|this.getSizeY() == this.getCurrentSprite().getHeight()
	 */
	public void setCurrentSize() {
		this.currentSize = new int[] {this.getCurrentSprite().getWidth(),this.getCurrentSprite().getHeight()};
	}
	
	/**
	 * Returns the width of an organism.
	 * 
	 * @return	The width of an organism is this.getCurrentSize()[0] .
	 * 			|result == this.getCurrentSize()[0]
	 */
	public int getSizeX() {
		return this.getCurrentSize()[0];
	}
	
	/**
	 * Returns the width of an organism.
	 * 
	 * @return	The height of an organism is this.getCurrentSize()[1] .
	 * 			|result == this.getCurrentSize()[1]
	 */
	public int getSizeY() {
		return this.getCurrentSize()[1];
	}

	/**
	 * An organism is expired when his time alive is greater than his maximum time alive (and his max time alive is set to a positive integer).
	 * 
	 * @return 	Returns false when the maximum time alive is negative.
	 * 			|if (this.getMaxTimeAlive() < 0)
	 * 			|then result == false
	 * @return 	Returns true when the maximum time alive is positive and the time alive is greater than the maximum time alive.
	 * 			|if (this.getMaxTimeAlive() > 0 and this.getTimeAlive()>this.getMaxTimeAlive())
	 *			|then result == true
	 */
	public boolean isExpired() {
		if (this.getMaxTimeAlive() > 0) {
			return ( (this.getTimeAlive()>this.getMaxTimeAlive()) && (this.getTimeAlive()>0) );
		}
		else {
			return false;
		}
	}
	
	/**
	* Returns the maximum amount of time this organism can be alive.
	*/
	@Basic
	@Immutable
	public double getMaxTimeAlive() {
		return this.maxTimeAlive;
	}
	
	/**
	 * Returns the time this organism is alive.
	 */
	@Basic
	public double getTimeAlive() {
		return this.timeAlive;
	}
	
	/**
	 * Set the time this organism is alive.
	 * 
	 * @param	time
	 * 			The new time value.
	 * @Pre		The given time should be greater than 0.
	 * 			|time>0
	 * @post 	The time alive is set to time.
	 * 			|this.getTimeAlive() == time
	 */
	public void setTimeAlive(double time) {
		assert(time>0);
		this.timeAlive = time;
	}
	
	/**
	 * An organism's death is expired when his death time is greater than his maximum death time (and his max death time is set to a positive integer).
	 * 
	 * @return 	Returns false when the maximum death time is negative.
	 * 			|if (this.getMaxTimeDeath() < 0)
	 * 			|then result == false
	 * @return 	Returns true when the maximum death time is positive and the time alive is greater than the maximum death time.
	 * 			|if (this.getMaxTimeDeath() > 0 and this.getTimeDeath()>this.getMaxTimeDeath())
	 */
	public boolean isExpiredDeath() {
		return ( (this.getTimeDeath()>this.getMaxTimeDeath()) && (this.getTimeDeath()>0) );
		
	}
	
	/**
	 * Returns the maximum time an organism can be alive.
	 */
	@Basic
	@Immutable 
	public double getMaxTimeDeath() {
		return this.maxTimeDeath;
	}
	
	/**
	 * Returns the time the has passed since the death of this organism.
	 */
	@Basic
	public double getTimeDeath() {
		return this.timeDeath;
	}
	
	/**
	 * Set the time this organism has been dead.
	 * 
	 * @param	time
	 * 			The new time value.
	 * @post 	The death time is set to time.
	 * 			|this.getTimeDeath() == time
	 */
	public void setTimeDeath(double time) {
		this.timeDeath = time;
	}
	
	/**
	 * Returns the possible sprites of the organism.
	 */
	public abstract Sprite[] getSprites();
	

	/**
	 * Returns whether the world is valid, meaning that this organism is in it.
	 * 
	 * @param 	world
	 * 			The world that needs verification.
	 * @return 	whether the world contains the organism.
	 * 			|result == world.getOrganism().contains(this)
	 */
	private boolean isValidWorld(World world) {
		return ( world.getOrganism().contains(this) );
	}
	
	/**
	 * This organism goes in a world.
	 * 
	 * @param 	world
	 * 			The world which wants to add an organism.
	 * @effect	In order for the world to be set, the world should be valid 
	 * 			and the organism can't already have a world.
	 * 			|if this.world() and not this.hasWorld()
	 * 			|then this.setWorld(world)
	 */
	@Raw
	public void addWorld(World world) {
		if ( this.isValidWorld(world) && !this.hasWorld() ) {
			this.setWorld(world);
		}
	}
	
	/**
	 * Set the world of the organism.
	 * 
	 * @param 	world
	 * 			the world containing the organism
	 * @post	the world of the organism is set
	 * 			|this.getWorld() == world
	 */
	@Raw
	private void setWorld(World world) {
		this.world=world;
	}
	
	
	/**
	 * Returns the world of this organism
	 */
	@Basic
	public World getWorld() {
		return this.world;
	}

	/**
	 * This organism asks the world to be removed.
	 * 
     * @effect	This organism asks the world to be removed (if it has a world).
     * 			|if this.hasWorld()
     * 			|then this.getWorld().removeObject(this)
     * @effect	This organism sets his world to null (if it has a world).
     * 			|if this.hasWorld()
     * 			|then this.setWorld(null)
	 */
	public void removeFromWorld() {
		if (this.hasWorld()) {
			this.getWorld().removeObject(this);
			this.setWorld(null);
		}
	}
	
	
	/**
	 * Returns whether the world is different from null.
	 * 
	 * @return	Returns false if this.getWorld() == null.
	 * 			|result == (this.getWorld() == null)
	 */
	public boolean hasWorld() {
		return (this.getWorld() != null);
	}
	
	/**
	 * @note	This method is not required to be documented.
	 */
	public void advanceForSolids(double[] tempPos, double[] testPos) {
		if (this.getCollidesSolidBool()) {
			double[] a = this.getAcceleration();
			if ((this.getCollidesSolid()[0] || this.getCollidesSolid()[2]) && (this.getCollidesSolid()[1] || this.getCollidesSolid()[3])) {
				this.setVelocity(0,0);
				this.setAcceleration(0, 0);
				//if (this instanceof Mazub) {
				//	((Mazub) this).endMove();
				//}
			}
			else if (this.getCollidesSolid()[0] || this.getCollidesSolid()[2]) {
				this.setMeterPosition(testPos[0], tempPos[1], true);
				this.setVelocity(this.getVelocity()[0], 0);
				this.setAcceleration(a[0], 0);
			}
			else if (this.getCollidesSolid()[1] || this.getCollidesSolid()[3]){
				this.setMeterPosition(tempPos[0], testPos[1], true);
				this.setVelocity(0,this.getVelocity()[1]);
				this.setAcceleration(0, a[1]);
				//if (this instanceof Mazub) {
				//	((Mazub) this).endMove();
				//}
			}
		}
		else {
			this.setMeterPosition(testPos,true);
		}
	}
	
	/**
	 * @note	This method is not required to be documented.
	 */
	public void cantPass(Organism org,double[] testPos, int[] testPosPixel, double[] tempPos, int[] tempPosPixel) {
		if(org.overlap(this, testPosPixel[0], testPosPixel[1],false)) {
			this.setMeterPosition(tempPos);
			this.setVelocity(0, 0);
			this.setAcceleration(0, 0);
		}
		if(org.overlap(this, tempPosPixel[0], testPosPixel[1],false)) {
			this.setMeterPosition(testPos[0], tempPos[1]);
			this.setVelocity(this.getVelocity()[0], 0);
			this.setAcceleration(this.getAcceleration()[0], 0);
		}
		
		if (org.overlap(this, testPosPixel[0], tempPosPixel[1],false)) {
			this.setMeterPosition(tempPos[0],testPos[1]);
			this.setVelocity(0,this.getVelocity()[1]);
			this.setAcceleration(0,this.getAcceleration()[1]);
		}
		if (this.getYVelocity() > 0) {
			if (org.overlap(this, tempPosPixel[0], testPosPixel[1]+1, false)) {
				this.setVelocity(this.getVelocity()[0], 0);
			}
		}
	}
	
	/**
	 * @note	This method is not required to be documented.
	 */
	public void advanceForOrganism(double [] tempPos, int[] tempPosPixel, double time) {
		double[] testPos = this.getMeterPosition();
		int[] testPosPixel = this.meterToPixel(testPos);
		if (this.hasWorld()) {
			Set<Object> objListTemp = Set.copyOf(world.getOrganism());
			for (Object obj : objListTemp) {
				Organism org = (Organism) obj;
				if (org != this) {					
					//some of them aren't allowed to ever overlap
					//but still need to interact
					//so I put a 1pixel margin of error on it
					
					if(org.overlap(this, testPosPixel[0], testPosPixel[1],true)) {
						if (this.canInteract(org)) {
							this.interaction(org);
							this.startTimeSinceInteraction();
							org.startTimeSinceInteraction();
						}
					}
					
					if (!this.canPass(org)) {
						this.cantPass(org,testPos,testPosPixel,tempPos,tempPosPixel);
					}
				}
			}
		}
	}
	
	/**
	 * Returns how long this has been consecutively touching lava.
	 *
	 * @note	This method is not required to be documented.
	 */
	public double getTimeOnLava() {
		return this.timeOnLava;
	}
	
	/**
	 * Set the time this organisms has been consecutively touching lava.
	 * 
	 * @param	time
	 * 			The new value of the time.
	 * @post	The time this organisms has been consecutively touching lava has been set to time.
	 * 			|new.getTimeOnLava() == time
	 * @note	This method is not required to be documented.
	 */
	public void setTimeOnLava(double time) {
		this.timeOnLava = time;
	}
	
	/**
	 * Returns how long this has been consecutively touching gas.
	 *
	 * @note	This method is not required to be documented.
	 */
	public double getTimeOnGas() {
		return this.timeOnGas;
	}
	
	/**
	 * Set the time this organisms has been consecutively touching gas.
	 * 
	 * @param	time
	 * 			The new value of the time.
	 * @post	The time this organisms has been consecutively touching gas has been set to time.
	 * 			|new.getTimeOnLava() == time
	 * @note	This method is not required to be documented.
	 */
	public void setTimeOnGas(double time) {
		this.timeOnGas = time;
	}
	
	/**
	 * Returns how long this has been consecutively touching water.
	 *
	 * @note	This method is not required to be documented.
	 */
	public double getTimeOnWater() {
		return this.timeOnWater;
	}
	
	/**
	 * Set the time this organisms has been consecutively touching water.
	 * 
	 * @param	time
	 * 			The new value of the time.
	 * @post	The time this organisms has been consecutively touching water has been set to time.
	 * 			|new.getTimeOnLava() == time
	 * @note	This method is not required to be documented.
	 */
	public void setTimeOnWater(double time) {
		this.timeOnWater = time;
	}
	
	/**
	 * @note	This method is not required to be documented.
	 */
	public boolean advanceLava(double time) {
		boolean ret = false;
		int[] checkPos = this.getPosition();
		if (this.getWorld().overlapsLava(this,checkPos[0],checkPos[1])) {
			this.setTimeOnWater(-1);
			this.setTimeOnGas(-1);
			ret = true;
			if (this.getTimeOnLava() == -1) {
				this.setTimeOnLava(0);
			}
			if (this.getTimeOnLava() != -1) {
				this.setTimeOnLava(this.getTimeOnLava() + time);
			}
			if (this.getTimeOnLava() >= 0) {
				/**
				this.loseHP(this.getMultiplier()*this.getWorld().getLavaDamage());
				*/
				this.changeHPByAmount(this.getTileValues().getLavaDelta());
				this.setTimeOnLava(this.getTimeOnLava() - this.getTileValues().getLavaTime());
			}
		}
		else {
			this.setTimeOnLava(-1);
		}
		return ret;
	}
	
	/**
	 * @note	This method is not required to be documented.
	 */
	public boolean advanceGas(double time) {
		boolean ret = false;
		int[] checkPos = this.getPosition();
		if (this.hasWorld()) {
			if (this.getWorld().overlapsGas(this,checkPos[0],checkPos[1])) {
				this.setTimeOnWater(-1);
				ret = true;
				if (this.getTimeOnGas() == -1) {
					this.setTimeOnGas(0);
				}
				if (this.getTimeOnGas() != -1) {
					this.setTimeOnGas(this.getTimeOnGas() + time);
				}
				if ((this.getTimeOnGas() >= 0 && !(this instanceof Slime)) || (this instanceof Slime && this.getTimeOnGas() >= this.getTileValues().getGasTime())) {
					/**
					this.loseHP(this.getMultiplier()*this.getWorld().getLavaDamage());
					 */
					this.changeHPByAmount(this.getTileValues().getGasDelta());
					this.setTimeOnGas(this.getTimeOnGas() - this.getTileValues().getGasTime());
				}
			}
		}
		else {
			this.setTimeOnGas(-1);
		}
		return ret;
	}
	
	/**
	 * @note	This method is not required to be documented.
	 */
	public void advanceWater(double time) {
		int[] checkPos = this.getPosition();
		if (this.hasWorld()) {		
			if(this.getWorld().overlapsWater(this,checkPos[0],checkPos[1])) {
				if(this.getTimeOnWater() == -1) {
					this.setTimeOnWater(0);
				}
				if (this.getTimeOnWater() != -1) {
					this.setTimeOnWater(this.getTimeOnWater() + time);
				}
				if (this.getTimeOnWater() >= this.getTileValues().getWaterTime()) {
					/**
					this.loseHP(this.getMultiplier()*this.getWorld().getWaterDamage());
				 	*/
					this.changeHPByAmount(this.getTileValues().getWaterDelta());
					this.setTimeOnWater(this.getTimeOnWater() - this.getTileValues().getWaterTime());
				}
			}
			else {
				this.setTimeOnWater(-1);
			}
		}
	}
	
	/**
	 * @note	This method is not required to be documented.
	 */
	public abstract void advanceTiles(double time);
	
	/**
	 * @note	This method is not required to be documented.
	 */
	public double alterTypeTime(double dt, double timing) {
		//leave empty, Overrides
		return dt;
	}
	
	/**
	 * @note	This method is not required to be documented.
	 */
	public double[] alterTime(double dt, double timing, double time) {
		dt = this.alterTypeTime(dt, timing);
		
		if (timing + dt <= time) {
			timing += dt;
		}
		else {
			dt = time - timing;
			timing = time;
		}
		return new double[] {dt,timing};
	}
	
	/**
	 * @note	This method is not required to be documented.
	 */
	private void advanceKill (double time) {
		if (this.isExpired() && !this.isEliminated()) {
			this.eliminate();	
		}
		if (this.getTimeDeath() >= 0 && this.isEliminated()) {
			this.setTimeDeath(this.getTimeDeath() + time);
		}
		if(this.isExpiredDeath()) {
			this.removeFromWorld();
		}
	}
	
	/**
	 * @note	This method is not required to be documented.
	 */
	public void advanceMisc(double time) {
		// Override in de nodige classes
		// laat leeg
	}
	/**
	 * @note	This method is not required to be documented.
	 */
	public abstract void advanceLoop(double time);
	
	/**
	 * @note	This method is not required to be documented.
	 */
	public void initializeMovement() {
		//Override when needed, leave empty
	}
	
	/**
	 * @note	This method is not required to be documented.
	 */
	public void advanceTime(double time) {
		if (this.isAlive()) {
			if (this.getTimeAlive() == 0) {
				this.initializeMovement();
			}
			this.advanceLoop(time);
		}
		//OTHER
		
		this.setTimeAlive(this.getTimeAlive() + time); //leave in this order
		this.setCurrentSprite(time);
		
		
			//KILL?
		this.advanceKill(time);
		
		if (this instanceof Mazub && this.hasWorld()) {
			this.getWorld().updateGameState();
		}
		}
	
	/**
	 * @note	This method is not required to be documented.
	 */
	public abstract Sprite getCurrentSprite();
	

	/**
	 * Eliminate this organism.
	 * 
	 * @effect	The objectState is set to ObjectState.ELIMINATED
	 * 			|this.setState(ObjectState.ELIMINATED)
	 */
	public void eliminate() {
		this.setState(ObjectState.ELIMINATED);
		if(this.hasWorld()) {
			this.getWorld().updateGameState();
		}
	}
	
	/**
	 * Terminate this organism.
	 * 
	 * @effect	The objectState is set to ObjectState.ELIMINATED
	 * 			|this.setState(ObjectState.TERMINATED)
	 */
	public void terminate() {
		this.setState(ObjectState.TERMINATED);
		if(this.hasWorld()) {
			this.getWorld().updateGameState();
		}
		this.removeFromWorld();
	}

	/**
	 * Returns whether this organism is alive.
	 * 
	 * @return	|result == (this.getState()==ObjectState.ALIVE)
	 */
	public boolean isAlive() {
		return (this.getState()==ObjectState.ALIVE);
	}
	
	
	/**
	 * Returns whether this organism is dead.
	 * 
	 * @return	|result == (this.getState()==ObjectState.ELIMINATED)
	 */
	public boolean isEliminated() {
		return (this.getState()==ObjectState.ELIMINATED);
	}
	
	/**
	 * Returns whether this organism is terminated.
	 * 
	 * @return	|result == (this.getState()==ObjectState.TERMINATED)
	 */
	public boolean isTerminated() {
		return (this.getState()==ObjectState.TERMINATED);
	}
	
	/**
	 * Returns whether this organism has a velocity and acceleration.
	 * 
	 * @return	Whether an organism has a current velocity of zero and acceleration of zero.
	 * 			|result == (getVelocity()==double[]{0,0} and getAcceleration()==double[]{0,0})
	 */
	public boolean isStationary() {
		double[] test = {0,0};
		return (this.getVelocity() == test && this.getAcceleration() == test);
	}
	
	/**
	 * @note	This method is not required to be documented.
	 */
	public abstract void setCurrentSprite(double time);
	
	/**
	 * Returns the current direction of this organism.
	 */
	public abstract Direction getDirection();
	
	/**
	 * Set the direction of the organism.
	 * 
	 * @param 	dir
	 * 			The new direction of the organsism.
	 * @throws 	IllegalArgumentException
	 * 			An IllegalArgumentException gets thrown if the given direcion is invalid.
	 */
	public abstract void setDirection(Direction dir) throws IllegalArgumentException; //TODO
	
	/**
	 * Change the y value of the position of this organism.
	 * 
	 * @param 	y
	 * 			The new y value of the postion of this organism.
	 * @param 	advance
	 * 			Whether this function has been called from an advanceMethod.
	 * @effect	|this.setPosition(this.getPosition()[0], y, advance)
	 */
	public void setPosY(int y, boolean advance) {
		this.setPosition(this.getPosition()[0], y, advance);
	}
	
	/**
	 * Change the y value of the position of this organism in meters.
	 * 
	 * @param 	y
	 * 			The new y value of the postion of this organism in meters.
	 * @param 	advance
	 * 			Whether this function has been called from an advanceMethod.
	 * @effect	this.setMeterPosition(this.getMeterPosition()[0], y,advance)
	 */
	public void setMeterPosY(double y, boolean advance) {
		this.setMeterPosition(this.getMeterPosition()[0], y,advance);
	}
	
	/**
	 * Returns the y velocity of this organism.
	 * 
	 * @return	|result == this.getVelocity()[1]
	 */
	public double getYVelocity() {
		return this.getVelocity()[1];
	}
	
	/**
	 * Change the y value of the acceleration of this organism.
	 * 
	 * @param 	y
	 * 			The new y value of the acceleration of this organism.
	 * @param 	advance
	 * 			Whether this function has been called from an advanceMethod.
	 * @effect	this.setAcceleration(this.getAcceleration()[0], y)
	 */
	public void setYAcceleration(double y) {
		this.setAcceleration(this.getAcceleration()[0], y);
	}
	
	/**
	 * Returns the values of the different tiles.
	 */
	@Basic
	@Immutable
	public TileInfo getTileValues() {
		return this.tileValues;
	}
	
	/**
	 * @note	This method is not required to be documented.
	 */
	public boolean containsNull(Sprite[] test) {
		return (Arrays.stream(test)).anyMatch(element->element==null);
	}
	

	
}
