package ch.ww.electronics.game.gameobject;

import org.json.JSONObject;

import ch.ww.electronics.game.level.Level;
import ch.ww.electronics.graphics.Screen;
import ch.ww.electronics.level.backgroundtile.BackgroundTile;
import ch.ww.electronics.util.MutableVector2D;
import ch.ww.electronics.util.Vector2D;

public class Animal extends GameObject{
	private DNA desoxyribonukleinsaeure;
	private Vector2D motion;
	private double energy;
	private final Brain brain;
	
	private boolean isDead;
	
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
		desoxyribonukleinsaeure = new DNA(this, 0,0,0,0,0,0);
		desoxyribonukleinsaeure.randomize();
		this.brain = new Brain(this);
		motion = new MutableVector2D(0, 0);
		this.energy = desoxyribonukleinsaeure.getMaxEnergy();
	}

	@Override
	public void tick() {
		brain.think();
		
		adjustEnergy();
		setTexture(new Screen((int) (BackgroundTile.SIZE * getWidth()), (int) (BackgroundTile.SIZE * getHeight()),
				0xffffff).darkScreen(energy/desoxyribonukleinsaeure.getMaxEnergy()));
		
		if(!isDead) {
			setLocation(getX() + motion.getX(), getY() + motion.getY());
		}
	}
	
	private void adjustEnergy() {
		addEnergy(-motion.getLength() * desoxyribonukleinsaeure.getSize());
		addEnergy(-0.1); //Passive energy burning
		if(energy < 0) {
			isDead= true;
			energy = 0;
		}
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
		if(energy < 0) {
			energy = 0;
		}
		if(energy > desoxyribonukleinsaeure.getMaxEnergy()) {
			energy = desoxyribonukleinsaeure.getMaxEnergy();
		}
	}
	
	public void addEnergy(double ammount) {
		setEnergy(getEnergy() + ammount);
	}
	
	public Vector2D getMotion() {
		return motion;
	}
	
	public void setMotion(Vector2D v) {
		this.motion = v;
	}
	
	public void setDNA(DNA dna){
		this.desoxyribonukleinsaeure=dna;
	}
	public DNA getDNA(){
		return(desoxyribonukleinsaeure);
	}
	public Brain getBrain(){
		return(brain);
	}
	
	public boolean isDead() {
		return isDead;
	}
	
	public double getSize() {
		return desoxyribonukleinsaeure.getSize();
	}
}
