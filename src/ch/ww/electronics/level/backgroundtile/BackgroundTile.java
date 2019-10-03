package ch.ww.electronics.level.backgroundtile;

import ch.ww.electronics.game.level.Level;
import ch.ww.electronics.graphics.Screen;

public abstract class BackgroundTile {
	public static final int SIZE = Level.FIELD_SIZE;

	private double temperature;
	
	private int x, y;
	
	public BackgroundTile(double temp, int x, int y) {
		this.temperature = temp;
		this.x = x;
		this.y = y;
	}
	
	public double getTemperature() {
		return temperature;
	}
	
	public void setTemperature(double temperature) {
		if(temperature == 0) {
			System.out.print("");
		}
		this.temperature = temperature;
	}
	
	public int getX() {
		return x;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public int getY() {
		return y;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public abstract Screen getScreenToRender();
}