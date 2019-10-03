package ch.ww.electronics.game.gameobject;
import ch.ww.electronics.game.level.Level;

public class Food extends Animal{
	public Food(Level level, double x, double y) {
		super(level, x, y);
	}
	@Override public void tick(){}
}
