package ch.ww.electronics.game.gameobject;
import ch.ww.electronics.game.level.Level;
import ch.ww.electronics.graphics.Screen;
import ch.ww.electronics.level.backgroundtile.BackgroundTile;

public class Food extends Animal{
	public Food(Level level, double x, double y) {
		super(level, x, y);
		setStatus(Brain.Status.BE_FOOD);
		
		Screen s = new Screen((int) (BackgroundTile.SIZE * getDNA().get(DNA.SIZE)), (int) (BackgroundTile.SIZE * getDNA().get(DNA.SIZE)),
				-1);
		s.fillCircle(0, 0, 0x00ff00, s.getHeight()/2);
		setTexture(s.darkScreen(getEnergy()/getDNA().get(DNA.MAX_ENERGY)));
	}
	
	
	@Override
	public void tick() {
		
	}
}
