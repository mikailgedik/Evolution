package ch.ww.electronics.util;

public interface Vector {
	public Vector withVector(Vector v);
	public Vector copy();
	public double getX();
	public double getY();
}