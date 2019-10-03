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
		for(int i = 0; i < 100; i++) {
			new Animal(this, getRandom().nextInt(getLevelWidth()),  getRandom().nextInt(getLevelHeight()));
		}
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
		int count = 0;
		
		for(GameObject o: this.getObjectsInLevel()) {
			if(o instanceof Food) {
				count++;
			}
		}
		
		if(getRandom().nextInt(getLevelWidth() * getLevelHeight()) > count * count) {
			new Food(this, getLevelWidth()*getRandom().nextDouble(), getLevelHeight()*getRandom().nextDouble());
		}
	}
}