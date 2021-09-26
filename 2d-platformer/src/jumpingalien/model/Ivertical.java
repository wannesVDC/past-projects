package jumpingalien.model;

/**
 * @note	The methods in this interface are not required to be documented
 */
public interface Ivertical {
	
public abstract int[] getPosition();
	
	public default int getPosY() {
		return this.getPosition()[1];
	}
	
	public abstract double[] getMeterPosition();
	
	public default double getMeterPosY() {
		return this.getMeterPosition()[1];
	}
	
	public default void setMeterPosY(double y, boolean advance) {
		this.setMeterPosition(this.getMeterPosX(), y, advance);
	}
	
	public abstract void setPosition(int x, int y, boolean advance);
	
	public default void setPosY(int y, boolean advance) {
		this.setPosition(this.getPosX(), y, advance);
	}
	
	public abstract double[] getVelocity();
	
	public default double getYVelocity() {
		return this.getVelocity()[1];
	}
	
	public abstract void setVelocity(double x, double y);
	
	public default void setYVelocity(double y) {
		this.setVelocity(0, y);
	}
	
	public abstract void setAcceleration(double x, double y);
	
	public default void setYAcceleration(double y) {
		this.setAcceleration(0, y);
	}
	
	public abstract double[] getAcceleration();
	
	public default double getYAcceleration() {
		return this.getAcceleration()[1];
	}
	
	public default double getAdvanceYPositionAmount(double time) {
		double r = this.getMeterPosY();
		double v = this.getYVelocity();
		double a = this.getYAcceleration();
		
		double newR = r + v*time + a*time*time/2 ;
		return newR;
	}
	
	public default void advanceVelocity(double time) {
		double[] v = this.getVelocity();
		double[] a = this.getAcceleration();
		
		double [] newV = new double[] {0, v[1] + a[1]*time};
		this.setVelocity(newV);
	}
	
	public default void setVelocity(double[] newV) {
		this.setVelocity(newV[0],newV[1]);
	}

	public abstract double getGravity();
	
	public default void advanceAcceleration(double time) {
		int pos = this.getPosY();
		int x = this.getPosX();
		double a = this.getYAcceleration();
		if(this.hasWorld()) {
			if (((World) this.getWorld()).analyseHLine(pos-2, x, x+this.getSizeX() ,GeologicalFeature.SOLID)) {
				a = 0;
			}
			else {
				a = this.getGravity();
			}
		}
		else {
			a = this.getGravity();
		}
		
		this.setYAcceleration(a);
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
	
	
}
