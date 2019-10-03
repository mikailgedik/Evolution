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
		for(int i = 0; i < (int)(Math.sqrt((getLevelWidth() * getLevelHeight()))/10); i++) {
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
		int foodC = 0, animalC = 0;
		
		for(GameObject o: this.getObjectsInLevel()) {
			if(o instanceof Food) {
				foodC++;
			} else {
				animalC++;
			}
		}
		
		int max = (int)(Math.sqrt((getLevelWidth() * getLevelHeight())) * 30);
		
		if(foodC< max){
			int n = (int)(getRandom().nextInt(max - foodC) * getRandom().nextDouble());
			for(int i = 0; i < n; i++)
			new Food(this, getLevelWidth()*getRandom().nextDouble(), getLevelHeight()*getRandom().nextDouble());
		}
		
		if(animalC == 0) {
			System.out.println("Dead world, readd");
			onStart();
		}
	}
}