package jumpingalien.model;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Immutable;
import be.kuleuven.cs.som.annotate.Raw;
import jumpingalien.util.ModelException;
import jumpingalien.util.Sprite;

/**
 * A class of slimes as a special kind of beast. Slime blobs are pretty dumb predatory land beasts aiming to devour Mazub.
 * They possess all properties of Mazub except for the abilities to duck, to jump and to consume Plants. (Copied from assignment 3)
 * 
 * @Invar	The amount of hitpoints of an slime is a positive integer
 * 			|thisHP()>=0
 * @Invar 	The maximum amount of hitpoints of a slime is 500 .
 * 			|this.getMaxHP() == 500
 * 			|this.getHP() =< this.getMaxHP()
 * @Invar 	A slime does not die of old age, its maximum time it can be alive is set to -1.
 * 			|this.getMaxTimeAlive() == -1
 * @Invar	The time a slime is alive is a positive double.
 * 			|this.getTimeAlive() >= 0
 * @Invar	The time an beast stays in a world after he dies is between 0 and maxTimeDeath.
 * 			|( 0 <= this.getTimeDeath() <= this.getMaxTimeDeath() )
 * @Invar	The maximum absolute value of the acceleration in the horizontal direction of
 * 			a slime is 0.7 .
 * 			|this.getMaxXAcceleration() == 0.7
 * @Invar	The vertical free fall acceleration of a slime is 0 .
 * 			|this.getGravity() == 0
 * @Invar	A slime gains GeologicalFeature.GAS.getHeal() for touching gas.
 * 			|this.getTileValues().getGasDelta() == GeologicalFeature.GAS.getDamage()
 * @Invar	Slime's damage for touching water equals -2*GeologicalFeature.WATER.getDamage().
 * 			|this.getTileValues().getWaterDelta() == -GeologicalFeature.WATER.getDamage()
 * @Invar	A slime instantly dies when touching lava.
 * 			|this.getTileValues().getLavaDelta() == -Integer.MAX_VALUE
 * @Invar	Slime's damage for not touching water equals 0 .
 * 			|this.getTileValues().getNonWaterDelta() == 0
 * @Invar	Slime takes heals from gas every 0.3 seconds and takes damage from water every 0.4 seconds.
 * 			|this.getTileValues().getGasTime() == 0.3 	and
 * 			|this.getTileValues().getWaterTime() == 0.4 and
 * 			|this.getTileValues().getLavaTime() == 1 	and
 * 			|this.getTileValues().getNonWaterTime() == 1
 * @Invar	Slime's damage for touching a mazub that is alive is 50 .
 * 			|Slime.getMazubDamage() == 50
 * @Invar	A slime does not want to overlap with other organisms.
 * 			|this.CanOverLap() = false
 * @Invar	The possible directions a slime can have is Direction.LEFT or Direction.RIGHT .
 * 			|this.getDirection() == Direction.LEFT  or  this.getDirection() == Direction.RIGHT
 * @Invar	TODO associations.
 * 
 * @authors Wannes Vande Cauter, Nils Van Dessel	--	Fysica
 */
public class Slime extends Beast implements Identifiable{
	
	private Sprite[] list;
	//private static int PassableMultiplier = 1;
	private static double xAcceleration = 0.7;
	private static double yAcceleration = 0;
	private static double maxVelocity = 2.5;
	
	private static int gasDelta = GeologicalFeature.GAS.getHeal();
	private static int waterDelta = -2*GeologicalFeature.WATER.getDamage();
	private static int lavaDelta = -Integer.MAX_VALUE;
	private static int nonWaterDelta = 0;
	private static int[] tileDeltas = new int[] {gasDelta,waterDelta,lavaDelta,nonWaterDelta};
	private static double[] tileTimes = new double[] {0.3,0.4,1,1};
	private static TileInfo tileValues = new TileInfo(tileDeltas, tileTimes);
	
	protected int width;
	private int normalHeight = 0;
	private int duckHeight = 200;
	
	//Slime specific variables
	private long id;
	private static List<Long> idList= new ArrayList<Long>();
	private School<Slime> school = null;
	public static final int mazubDamage = 30;
	private Direction dir = Direction.FRONT;
	
	/**
	 * Initialize this new slime with an x coordinate of its lower left pixel, 
	 * an y coordinate of its lower left pixel and a list of sprites.
	 * 
	 * @param 	pixelLeftX
	 * 			The x-coordinate of the lower left pixel of the slime.
	 * @param 	pixelBottomY
	 * 			The y-coordinate of the lower left pixel of the slime.
	 * @param 	sprites
	 * 			A list of sprites representing the appearance of slime.
	 * @post	TODO
	 * @throws	
	 * @throws
	 * @throws
	 * @throws
	 */
	@Raw
	public Slime(long id,int x,int y,School<Slime> school, Sprite... sprites) {
		super(maxVelocity,xAcceleration,yAcceleration,tileValues);
		if (!(idList.contains(id))) {
			this.id=id;
			idList.add(id);
		}
		

		if (sprites != null) {
			
			Sprite[] sprit = Arrays.copyOf(sprites, sprites.length);
			int len = sprites.length;
			if (len != 2) {
				throw new ModelException("sprite list needs length 2");
			}
			if (this.containsNull(sprit)) {
				throw new ModelException("sprite list invalid");
			}
			
			this.list = Arrays.copyOf(sprites, sprites.length);
			//this.setTimeSinceMove(0);
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
		double meterX = (double) x;
		meterX /= 100;
		double meterY = (double) y;
		meterY /= 100;
		this.setMeterPosition(meterX,meterY);
		
		this.setCurrentSize();
		if (school != null) {
			this.setSchool(school);
			school.addStudent(this);
		}
		this.setDirection(Direction.RIGHT);
		this.setXAcceleration(Slime.xAcceleration);
		this.changeHP(100);
	}
	
	/**
	 * Returns the static idList
	 */
	@Basic
	public static List<Long> getIdList(){
		return Slime.idList;
	}
	
	/**
	 * The list of ids gets cleared
	 * 
	 * @post 	The idList is empty.
	 * 			|Slime.getNBOfIds == 0
	 */
	public static void clearIds() {
		(Slime.getIdList()).clear();
	}
	
	/**
	 * Returns the number of stored ids.
	 */
	public static int getNBOfIds() {
		return (Slime.getIdList()).size();
	}
	
	/**
	 * Returns the school of this slime.
	 */
	@Basic
	public School<Slime> getSchool() {
		return this.school;
	}
	
	/**
	 * The school of the slime gets set.
	 * 
	 * @param	newSchool
	 * 			The new slime school of which the slime becomes a student.
	 * @post	The school of the slime gets set to newSchool.
	 * 			|new.getSchool() == newSchool
	 */
	public void setSchool(School<Slime> newSchool) {
		this.school=newSchool;
	}
	
	/**
	 * The slime switches school.
	 * 
	 * @param	newSchool
	 * 			The new school of which the slime becomes a student.
	 * @effect	The slime gets removed from his old school (if he had one).
	 * 			|If this.hasSchool()
	 * 			|then (this.getSchool()).removeStudent(this)
	 * @effect	The other students from the old school each give 1 hitpoint to the slime.
	 * 			|let oldSchool = this.getschool()
	 * 			|(after removal)
	 * 			|this.changeHPByAmount(-oldSchool.getNBOfStudents());
	 *			|for Organism org in oldSchool.getStudents()
	 *			|	org.gainHP(1)
	 * @post	The new school of the slime is newSchool.
	 * 			|new.getSchool == newSchool
	 * @throws	ModelException
	 * 			A new ModelException gets thrown if the not this.hasSchool() .
	 * 			|if not this.hasSchool()
	 * 			|then throw new ModelException
	 */
	public void switchSchool(School<Slime> newSchool) throws ModelException{
		if (this.hasSchool()) {
			School<Slime> oldSchool = this.getSchool();
			oldSchool.removeStudent(this);
			this.loseHPBySchool(-oldSchool.getNBOfStudents());
			for (Organism org : oldSchool.getStudents()) {
				org.gainHP(1);
			}
		}
		else {
			throw new ModelException("isn't in school");
		}
		for (Organism org : newSchool.getStudents()) {
			if (org instanceof Slime) {				
				((Slime) org).loseHPBySchool(-1);
			}
		}
		this.changeHPByAmount(newSchool.getNBOfStudents());
		newSchool.addStudent(this);
		this.setSchool(newSchool);
	}
	
	/**
	 * Returns whether the slime has a school.
	 * 
	 * @return	result == (this.getShool() != null)
	 */
	private boolean hasSchool() {
		return this.getSchool() != null;
	}
	
	/**
	 * The slime leaves the school.
	 * 
	 * @post	The slime does not have a school anymore.
	 * 			|new.getSchool() == null
	 */
	public void leaveSchool() {
		this.setSchool(null);
	}
	
	/**
	 * Returns the id of this slime
	 */
	public long getId() {
		return this.id;
	}
	
	/**
	 * This slime gets damaged by the school.
	 * 
	 * @param 	change
	 * 			The amount that lowers the HP.
	 * @effect 	The HP gets lowered.
	 * 			|super.loseHP(change)
	 */
	public void loseHPBySchool(int change) {
		super.changeHPByAmount(-Math.abs(change));
	}
	
	@Override
	protected void changeHPByAmount(int delta) {
		this.changeHP(this.getHP()+delta);
		if (this.getHP() <= 0) {
			this.eliminate();
		}
		if (delta < 0) {
			if (this.hasSchool()) {
				for (Organism org : this.getSchool().getStudents()) {
					if (org != this) {
						((Slime) org).loseHPBySchool(1);
					}
				}
			}
		}
	}
	
	/**
	 * This slime gets healed by the school.
	 * 
	 * @param 	change
	 * 			The amount that increases the HP.
	 * @effect 	The HP gets increased.
	 * 			|super.gainHP(change)
	 */
	public void gainHPBySchool(int change) {
		this.changeHPByAmount(change);
	}
	
	/**
	 * A slime loses hitpoints and it causes the other slimes to also get damaged.
	 * 
	 * @param 	change
	 * 			The amount that gets subtracted from the hitpoints of this slime.
	 * @effect	The slime loses change hitpoints.
	 * 			|super.loseHP(change)
	 * @effect	Every slime in the school except for this one loses 1 HP.
	 * 			|for slime	in this.getSchool()
	 * 			|	if (slime != this)
	 * 			|	then slime.loseHPBySchool(1)
	 */
	@Override
	public void loseHP(int change) {
		super.loseHP(change);
	}
	
	/**
	 * An interaction from slime to the organism happens.
	 * 
	 * @param	organism
	 * 			The organism on which slime interacts.
	 * @effect	The organism gets interacted on by this slime.
	 * 			|organism.interactSlime(this)
	 * @note	The organism does not retaliate in this method.
	 */
	@Override
	protected void interact(Organism organism) {
		organism.interactSlime(this);
	}
	
	/**
	 * This slime gets interacted on by a mazub.
	 * 
	 * @param	mazub
	 * 			The mazub that attacks this slime.
	 * @effect	Slimes die for having contact with a living shark.
	 * 			|if (mazub.isAlive())
	 * 			|this.loseHP(Slime.mazubDamage)
	 */
	@Override
	protected void interactMazub(Mazub mazub) {
		if (mazub.isAlive()) {
			this.loseHP(Slime.mazubDamage); }
	}
	
	/**
	 * This slime gets interacted on by a shark.
	 * 
	 * @param	sharkAlive
	 * 			This boolean shows whether the shark that is touching mazub is alive.
	 * @effect	Slimes die for having contact with a living shark.
	 * 			|if (sharkAlive)
	 * 			|then this.eliminate()
	 */
	@Override
	protected void interactShark(boolean sharkAlive) {
		if (sharkAlive) {
			this.eliminate(); }
	}
	
	/**
	 * This slime gets interacted on by another slime.
	 * 
	 * @param 	slime
	 * 			The slime that is touching this slime.
	 * @effect	This slime changes direction.
	 * 			|this.swhitchDirection()
	 * @effect	This slime will change to the school of the other slime if that school is bigger.
	 * 			|if (this.getSchool().getNBOfStudents()<slime.getSchool().getNBOfStudents())
	 * 			|then this.swhitchSchool(slime.getSchool());
	 */
	@Override
	protected void interactSlime(Slime slime) {
		this.switchDirection();
		if (this.getSchool() != null && slime.getSchool() != null) {
			int n1 = this.getSchool().getNBOfStudents();
			int n2 = slime.getSchool().getNBOfStudents();
			if (n1<n2) {
				this.switchSchool(slime.getSchool());
			}
		}
	}
	
	/**
	 * This slime gets hit by a plant, but this does nothing in this version of the game.
	 * Nothing
	 */
	@Override
	protected void interactPlant(boolean plantAlive) {/* Nothing */}
	
	/**
	 * Returns the list of sprites associated with slimes.
	 */
	@Override
	@Basic
	@Immutable
	public Sprite[] getSprites() {
		return this.list;
	}
	
	/**
	 * @note	This method is not required to be documented.
	 */
	@Override
	public Sprite getCurrentSprite() {
		if (this.getDirection() == Direction.LEFT)
			return this.getSprites()[1];
		else {
			return this.getSprites()[0];
		}
	}

	/** 
	 * Returns the current direction of this slime.
	 */
	@Override
	@Basic
	public Direction getDirection() {
		return this.dir;
	}
	
	/**
	 * This method will set the direction of the slime to the given direction if it is legal.
	 * 
	 * @param	dir
	 * 			The new direction of this slime.
	 * @post	The new direction of the slime will be set to dir if this.validDirection(dir) .
	 *			|if this.validDirection(dir)
	 *			|then new.getDirection() == dir
	 * @throws	IllegalArgumentException
	 * 			An IllegalArgumentException is thrown if not this.validDirection(Dir) .
	 * 			|if !this.validDirection(dir)
	 * 			|then throw new IllegalArgumentException()
	 */
	@Override
	public void setDirection(Direction dir) throws IllegalArgumentException{
		if (!this.validDirection(dir)) {
			throw new IllegalArgumentException();
		}
		this.dir = dir;
	}
	
	/**
	 * Returns whether the given direction is legal
	 * 
	 * @param 	dir
	 * 			The direction of which the legality should be checked. 
	 * @return 	The direction is not allowed to be Direction.DOWN or Direction.UP
	 * 			|if (dir==Direction.DOWN or dir==Direction.UP)
	 * 			|then result == false
	 */			
	public boolean validDirection(Direction dir) {
		return (!(dir==Direction.DOWN || dir==Direction.UP));
	}
	
	/**
	 * TODO
	 */
	@Override
	public void setVelocity(double[] v) {
		if(v[0] >= this.getMaxVelocity() || v[0] <= -this.getMaxVelocity()) {
			if (v[0] > 0) {
				v[0] = this.getMaxVelocity();
			}
			else {
				v[0] = -this.getMaxVelocity();
			}
		}
		
		if (v[0] < 0) {
			this.setDirection(Direction.LEFT);
		}
		else if (v[0] == 0) {
			this.setDirection(Direction.FRONT);
		}
		else if (v[0] > 0) {
			this.setDirection(Direction.RIGHT);
		}
		
		super.setVelocity(v);;
	}
	
	/**
	 * @note	This method is not required to be documented.
	 */
	@Override
	public void setCurrentSprite(double time) {}
	
	/**
	 * The slime gets terminated.
	 * 
	 * @effect	The current object state of the slime is set to ObjectState.TERMINATED .
	 * 			|this.setState(ObjectState.TERMINATED)
	 * @effect	The slime gets removed from its world.
	 * 			|this.removeFromWorld()
	 * @effect	The slime graduates from the school.
	 * 			|this.graduate.
	 */
	@Override
	public void terminate() {
		this.setState(ObjectState.TERMINATED);
		this.removeFromWorld();
		this.graduate();
	}
	
	/**
	 * The slime graduates from the school.
	 * 
	 * @effect	The slime asks the school to leave (if it has a school)
	 * 			|if this.hasSchool()
	 * 			|then this.getSchool().removeStudent(this)
	 * @effect	The slime school gets set to null by leaving the school (if it had a school).
	 * 			|if this.hasSchool()
	 * 			|then this.leaveSchool()
	 */
	@Raw
	private void graduate() {
		if (this.hasSchool()) {
			this.getSchool().removeStudent(this);
			this.leaveSchool();
		}
	}
	
	/**
	 * @note	This method is not required to be documented.
	 */
	@Override
	public void advanceTiles(double time) {
		this.advanceGas(time);
		this.advanceWater(time);
		this.advanceLava(time);
	}
	
	/** TODO
	 * @note	This method is not required to be documented.
	 */
	@Override
	public boolean advanceLava(double time) {
		int[] checkPos = this.getPosition();
		if (this.hasWorld()) {
			if (this.getWorld().overlapsLava(this,checkPos[0],checkPos[1])) {
				this.eliminate();
			}
		}
		return false;
	}
	
	
}
