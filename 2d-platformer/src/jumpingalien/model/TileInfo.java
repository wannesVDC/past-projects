package jumpingalien.model;

import java.util.Arrays;

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Immutable;
import be.kuleuven.cs.som.annotate.Value;

@Value
/**
 * A class of informations to store data for damage values and associated time periods.
 * 
 * @Invar	The minimum length of a list given as parameter is 4 in this version of the game
 * 			|this.getMinimumLength == 4
 * @author	Wannes Vande Cauter, Nils Van Dessel	--	Fysica
 */
public class TileInfo {
	
	final private int gasDelta;
	final private int waterDelta;
	final private int lavaDelta;
	final private int nonWaterDelta;
	
	final private double gasTime;
	final private double waterTime;
	final private double lavaTime;
	final private double nonWaterTime;
	
	private final static int minimumLength = 4;
	
	/**
	 * Initialize this new tileInfo with a list for HP deltas for given tiles and associated periods.
	 * 
	 * @param 	deltas
	 * 			An integer list (currently of lenght 4) containing heal/damage done by a certain tile
	 * 			(gas, water, lava and nonwater).
	 * @param 	times
	 * 			A double list (currently of lenght 4) containing the time periods for the damage done by a certain tile
	 * 			(gas, water, lava and nonwater).
	 * @Pre		The length of list deltas cannot be smaller than the minimum length.
	 * 			|deltas.lenght >= this.getMinimumLenght()
	 * @Pre		The length of list times cannot be smaller than the minimum length.
	 * 			|times.lenght >= this.getMinimumLenght()
	 * @Pre		The given times should be greater than 0.
	 * 			|for element in times
	 * 			|	0 < element
	 * @post	The damagedelta of gas is set to the first element of deltas.
	 * 			|this.getGasDelta() == deltas[0]
	 * @post	The damagedelta of water is set to the second element of deltas.
	 * 			|this.getWaterDelta() == deltas[1]
	 * @post	The damagedelta of lava is set to the third element of deltas.
	 * 			|this.getLavaDelta() == deltas[2]
	 * @post	The damagedelta of nonWater is set to the fourth element of deltas.
	 * 			|this.getNonWaterDelta() == deltas[4]
	 * @post	The damagedelta of gas is set to the first element of times.
	 * 			|this.getGasDelta() == deltas[0]
	 * @post	The damagedelta of water is set to the second element of times.
	 * 			|this.getWaterDelta() == deltas[0]
	 * @post	The damagedelta of lava is set to the third element of times.
	 * 			|this.getLavaDelta() == deltas[0]
	 * @post	The damagedelta of nonWater is set to the fourth element of times.
	 * 			|this.getNonWaterDelta() == deltas[0]
	 */
	public TileInfo(int[]deltas,double[] times) {
		assert(deltas.length >= TileInfo.getMinimumLength());
		assert(times.length  >= TileInfo.getMinimumLength());
		assert( (Arrays.stream(times).allMatch(element->0<element)) );
		
		this.gasDelta=deltas[0];
		this.waterDelta=deltas[1];
		this.lavaDelta=deltas[2];
		this.nonWaterDelta=deltas[3];
		
		this.gasTime=times[0];
		this.waterTime=times[1];
		this.lavaTime=times[2];
		this.nonWaterTime=times[3];
	}
	
	/**
	 * Returns the minimum length of the lists given as parameters.
	 */
	@Basic
	@Immutable
	public static int getMinimumLength() {
		return TileInfo.minimumLength;
	}
	
	/**
	 * Returns the amount of HP that should be added for having contact with gas.
	 */
	@Basic
	@Immutable
	public int getGasDelta() {
		return this.gasDelta;
	}
	
	/**
	 * Returns the amount of HP that should be added for having contact with water.
	 */
	@Basic
	@Immutable
	public int getWaterDelta() {
		return this.waterDelta;
	}
	
	/**
	 * Returns the amount of HP that should be added for having contact with lava.
	 */
	@Basic
	@Immutable
	public int getLavaDelta() {
		return this.lavaDelta;
	}
	
	/**
	 * Returns the amount of HP that should be added for not having contact with water.
	 */
	@Basic
	@Immutable
	public int getNonWaterDelta() {
		return this.nonWaterDelta;
	}
	
	/**
	 * Returns a time period for when to add the gas damage.
	 */
	@Basic
	@Immutable
	public double getGasTime() {
		return this.gasTime;
	}
	
	/**
	 * Returns a time period for when to add the water damage.
	 */
	@Basic
	@Immutable
	public double getWaterTime() {
		return this.waterTime;
	}
	
	/**
	 * Returns a time period for when to add the lava damage.
	 */
	@Basic
	@Immutable
	public double getLavaTime() {
		return this.lavaTime;
	}
	
	/**
	 * Returns a time period for when to add the nonWater damage.
	 */
	@Basic
	@Immutable
	public double getNonWaterTime() {
		return this.nonWaterTime;
	}	
	
}
