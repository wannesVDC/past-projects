package jumpingalien.model;

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Immutable;
import be.kuleuven.cs.som.annotate.Value;

/**
 * An enumeration of geological features.
 *    In its current definition, the class distinguishes between
 *    air,water,magma,gas,solid ground and ice.
 * 
 * @authors Wannes Vande Cauter, Nils Van Dessel	--	Fysica
 */
@Value
public enum GeologicalFeature {
	
	AIR,WATER,MAGMA,GAS,SOLID,ICE;
	
	/**
	 * This number represents a valid translation from Geological Feature to number (according to the testfiles).
	 */
	private int number;
	static {
		AIR.number   = 0;
		SOLID.number = 1;
		WATER.number = 2;
		MAGMA.number = 3;
		ICE.number   = 4;
		GAS.number   = 5;
	}
	/**
	 * Returns the corresponding number for the Geological Feature.
	 */
	@Basic
	@Immutable
	public int getNumber() {
		return this.number;
	}
	
	
	/**
	 * Some organisms take damage from tiles, this integer is set below.
	 */
	private int damage;
	static {
		AIR.damage   = 0;
		WATER.damage = 2;
		MAGMA.damage = 50;
		GAS.damage   = 4;
		SOLID.damage = 0;
		ICE.damage   = 0;
	}
	/**
	 * Returns the damage associated with this Geological Feature.
	 */
	@Basic
	@Immutable
	public int getDamage() {
		return this.damage;
	}
	
	
	/**
	 * Some organisms heal from tiles, this integer is set below.
	 */
	private int heal;
	static {
		AIR.heal   = 0;
		WATER.heal = 0;
		MAGMA.heal = 0;
		GAS.heal   = 2;
		SOLID.heal = 0;
		ICE.heal   = 0;
	}
	/**
	 * Returns the amount of HP associated with this Geological Feature.
	 */
	@Basic
	@Immutable
	public int getHeal() {
		return this.heal;
	}

	
	/**
	 * All Geological Features except for SOLID and ICE are passable.
	 */
	private boolean passable;
	static {
		AIR.passable   = true;
		WATER.passable = true;
		MAGMA.passable = true;
		GAS.passable   = true;
		SOLID.passable = false;
		ICE.passable   = false;
	}
	/**
	 * Returns whether this Geological Feature is passable.
	 */
	@Basic
	@Immutable
	public boolean isPassable() {
		return this.passable;
	}
	
}
