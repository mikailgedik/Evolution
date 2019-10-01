package ch.ww.electronics.util;

public class MutableVector2D extends Vector2D {

	public MutableVector2D(double x, double y) {
		super(x, y);
	}

	@Override
	public MutableVector2D withVector(Vector v) {
		return new MutableVector2D(x + ((Vector2D) v).getX(), y + ((Vector2D) v).getY());
	}

	@Override
	public MutableVector2D copy() {
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
		setX(((Vector2D) vector).getX() + x);
		setY(((Vector2D) vector).getY() + y);
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