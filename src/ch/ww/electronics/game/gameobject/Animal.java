package ch.ww.electronics.game.gameobject;

import org.json.JSONObject;

import ch.ww.electronics.game.level.Level;
import ch.ww.electronics.graphics.Screen;
import ch.ww.electronics.level.backgroundtile.BackgroundTile;

public class Animal extends GameObject{
	
	
	public static final GameObjectConstructor<Animal> CONSTRUCTOR = new GameObjectConstructor<Animal>() {
		@Override
		public Animal createInstance(JSONObject jsonObject) {
			return new Animal(null, jsonObject.getDouble("x"), jsonObject.getDouble("y"));
		}
	};
	
	public static final String NAME = "evolution.gameobject.animal";

	public Animal(Level level, double x, double y) {
		super(level, x, y);
		
		setTexture(new Screen((int) (BackgroundTile.SIZE * getWidth()), (int) (BackgroundTile.SIZE * getHeight()),
				0xffffff));
	}

	@Override
	public void tick() {
		setX(getX()+0.1);
	}
	
	@Override
	public GameObjectConstructor<? extends GameObject> getGameObjectConstructor() {
		return CONSTRUCTOR;
	}

	@Override
	public String getName() {
		return NAME;
	}
	
	
	
}
