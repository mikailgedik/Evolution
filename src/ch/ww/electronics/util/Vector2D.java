package ch.ww.electronics.util;

public class Vector2D implements Vector {
	protected double x, y;

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
	
	public double getLength() {
		return (Math.sqrt(x*x + y*y));
	}
	
	@Override
	public Vector2D withVector(Vector v) {
		return new Vector2D(((Vector2D) v).getX() + x, ((Vector2D) v).getY() + y);
	}

	@Override
	public Vector2D copy() {
		return new Vector2D(x, y);
	}

	@Override
	public String toString() {
		return getClass().getName() + "[x = " + getX() + ", y = " + getY() + "]";
	}
	
	public static double dotProduct(Vector2D a, Vector2D b) {
		return a.getX() * b.getX() + a.getY() * b.getY();
	}
	
	public static Vector2D add(Vector2D a, Vector2D b) {
		return new Vector2D(a.getX() + b.getX(), a.getY() + b.getY());
	}
}