package jumpingalien.model;

/**
 * @note	The methods in this interface are not required to be documented
 */
public interface Ihorizontal {
	
	public abstract int[] getPosition();
	
	public default int getPosY() {
		return this.getPosition()[1];
	}
	
	public abstract double[] getMeterPosition();
	
	public abstract void setPosition(int x, int y, boolean advance);
	
	public abstract double[] getVelocity();
	
	public abstract void setVelocity(double x, double y);
	
	public abstract void setAcceleration(double x, double y);
	
	public abstract double[] getAcceleration();
		
	public default void advanceVelocity(double time) {
		double[] v = this.getVelocity();
		double[] a = this.getAcceleration();
		
		double [] newV = new double[] {v[0] + a[0]*time, 0};
		this.setVelocity(newV);
	}
	
	public default void setVelocity(double[] newV) {
		this.setVelocity(newV[0],newV[1]);
	}

	
	public default void advanceAcceleration(double time) {		
		double a = 0;
		if (this.getDirection() == Direction.LEFT && !this.isDucking()) {
			a = -this.getMaxXacceleration();
		}
		else if (this.getDirection() == Direction.RIGHT && !this.isDucking()) {
			a = this.getMaxXacceleration();
		}
		
		this.setXAcceleration(a);
	}
	
	public abstract boolean isDucking();

	public abstract Direction getDirection();

	public abstract int getSizeX();

	public abstract Object getWorld();

	public abstract boolean hasWorld();

	public default void setAcceleration(double[] ans) {
		this.setAcceleration(ans[0], ans[1]);
	}

	//public abstract void advanceYAcceleration(double time);
	
	public default int getPosX() {
		return this.getPosition()[0];
	}
	
	public default double getMeterPosX() {
		return this.getMeterPosition()[0];
	}
	
	public abstract void setMeterPosition(double x,double y, boolean advance);
	
	public default void setMeterPosX(double x, boolean advance) {
		this.setMeterPosition(x,this.getPosY(),advance);
	}
	
	public default void setPosX(int y, boolean advance) {
		this.setPosition(this.getPosX(), y, advance);
	}
	
	public default double getXVelocity() {
		return this.getVelocity()[0];
	}
	
	public default void setXVelocity(double x) {
		this.setVelocity(x, 0);
	}
	
	public default void setXAcceleration(double x) {
		this.setAcceleration(x, 0);
	}
	
	public default double getXAcceleration() {
		return this.getAcceleration()[0];
	}
	
	public default double getAdvanceXPositionAmount(double time) {
		double r = this.getMeterPosX();
		double v = this.getXVelocity();
		double a = this.getXAcceleration();
		int temp = this.getDirection().dirToInt();
		
		double newR = r + v*time + temp*a*time*time/2 ;
		return newR;
	}
	public abstract double getMaxXacceleration();
}
