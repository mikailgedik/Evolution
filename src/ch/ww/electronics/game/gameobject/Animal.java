package ch.ww.electronics.game.gameobject;

import org.json.JSONObject;

import ch.ww.electronics.game.level.Level;
import ch.ww.electronics.graphics.Screen;
import ch.ww.electronics.level.backgroundtile.BackgroundTile;
import ch.ww.electronics.util.MutableVector2D;
import ch.ww.electronics.util.Vector;

public class Animal extends GameObject{
	private DNA dna;
	private Vector motion;
	private double energy;
	private final Brain brain;
	
	
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
		dna = new DNA(getGame(), 0,0,0,0);
		this.brain = new Brain(this);
		motion = new MutableVector2D(0, 0);
	}

	@Override
	public void tick() {
		brain.think();
		
		setLocation(getX() + motion.getX(), getY() + motion.getY());
	}
	
	@Override
	public GameObjectConstructor<? extends GameObject> getGameObjectConstructor() {
		return CONSTRUCTOR;
	}

	@Override
	public String getName() {
		return NAME;
	}
	
	public double getEnergy() {
		return energy;
	}
	
	public void setEnergy(double energy) {
		this.energy = energy;
	}
	
	public void addEnergy(double ammount) {
		setEnergy(getEnergy() + ammount);
	}
}
