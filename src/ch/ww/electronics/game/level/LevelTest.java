package ch.ww.electronics.game.level;

import ch.ww.electronics.game.Game;
import ch.ww.electronics.game.gameobject.Animal;

public class LevelTest extends Level {

	public LevelTest(Game game, int levelWidth, int levelHeight) {
		super(game, levelWidth, levelHeight);
	}

	@Override
	public void onStart() {
		
		/*
		for(int i = 0; i < 100; i++) {
			new Animal(this, getRandom().nextInt(getLevelWidth()),  getRandom().nextInt(getLevelHeight()));
		}
		*/
		new Animal(this, getLevelWidth()/2, getLevelHeight()/2);
//		new Animal(this, getLevelWidth()/2, getLevelHeight()/2+4);
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
}