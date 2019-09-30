package ch.ww.electronics.util;

public class MutableVector2D implements Vector {

	private double x;
	private double y;

	public MutableVector2D(double x, double y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public MutableVector2D withVector(Vector v) {
		return new MutableVector2D(x + v.getX(), y + v.getY());
	}

	@Override
	public Vector copy() {
		return new MutableVector2D(x, y);
	}

	@Override
	public double getX() {
		return x;
	}

	@Override
	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public void setX(double x) {
		this.x = x;
	}

	public void set(double x, double y) {
		setX(x);
		setY(y);
	}

	public void add(Vector vector) {
		setX(vector.getX() + x);
		setY(vector.getY() + y);
	}

	public void round(int digits) {
		int z = (int) Math.pow(10, digits);
		setX(1.0 * Math.round(getX() * z) / z);
		setY(1.0 * Math.round(getY() * z) / z);
	}

	@Override
	public String toString() {
		return getClass().getName() + "[x = " + getX() + ", y = " + getY() + "]";
	}
}