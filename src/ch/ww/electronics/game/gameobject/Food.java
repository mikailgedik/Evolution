package ch.ww.electronics.game.gameobject;
import ch.ww.electronics.game.level.Level;

public class Food extends Animal{
	public Food(Level level, double x, double y) {
		super(level, x, y);
		setStatus(Brain.Status.BE_FOOD);
		
		
	}
	@Override public void tick(){}
}
