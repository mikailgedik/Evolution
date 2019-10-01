package ch.ww.electronics.game.gameobject;

import org.json.JSONObject;

import ch.ww.electronics.game.level.Level;

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
	}

	@Override
	public void tick() {
		// TODO Auto-generated method stub
		
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
