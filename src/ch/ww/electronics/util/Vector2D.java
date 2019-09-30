package ch.ww.electronics.util;

public class Vector2D implements Vector {
	private final double x, y;

	public Vector2D(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	@Override
	public Vector2D withVector(Vector v) {
		return new Vector2D(v.getX() + x, v.getY() + y);
	}

	@Override
	public Vector2D copy() {
		return new Vector2D(x, y);
	}

	@Override
	public String toString() {
		return getClass().getName() + "[x = " + getX() + ", y = " + getY() + "]";
	}
}