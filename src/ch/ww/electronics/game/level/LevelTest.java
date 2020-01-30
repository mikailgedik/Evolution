package ch.ww.electronics.game.level;

import ch.ww.electronics.game.Game;
import ch.ww.electronics.game.gameobject.Animal;
import ch.ww.electronics.game.gameobject.Food;
import ch.ww.electronics.game.gameobject.GameObject;

public class LevelTest extends Level {

	public LevelTest(Game game, int levelWidth, int levelHeight) {
		super(game, levelWidth, levelHeight);
	}

	@Override
	public void onStart() {
		new Animal(this, getRandom().nextDouble() * getLevelWidth(),  getRandom().nextDouble() * getLevelHeight());
	}

	@Override
	public void keyPressed(int code) {
	}

	@Override
	public void keyReleased(int code) {
	}

	@Override
	public void reset() {
	}
	
	@Override
	public void tick() {
		super.tick();
		int food = 0;
		int animals = 0;
		
		for(GameObject o: this.getObjectsInLevel()) {
			if(o instanceof Food) {
				food++;
			} else if (o instanceof Animal) {
				animals++;
			}
		}
		if(animals == 0) {
			System.out.println("Dead");
		}
		
		if(food < 300) {
			if(getRandom().nextDouble() < 0.5) {
				new Food(this, getRandom().nextDouble() * this.getLevelHeight(), getRandom().nextDouble() * this.getLevelWidth());
			}
		}
	}
}