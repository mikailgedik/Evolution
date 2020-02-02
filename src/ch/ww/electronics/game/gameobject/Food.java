package ch.ww.electronics.game.gameobject;
import ch.ww.electronics.game.gameobject.State.Status;
import ch.ww.electronics.game.level.Level;

public class Food extends Animal {
	public Food(Level level, double x, double y) {
		super(level, x, y, new DNA(null, .5, 0, 0, 0, 0, 10000, 0, 0, 0, 0));
		getActualState().setStatus(Status.BE_FOOD);
		getCalculatedState().setStatus(Status.BE_FOOD);

		this.adjustTexture();
	}
	
	@Override
	public void preTick() {
		//Don't do anything
	}
}
