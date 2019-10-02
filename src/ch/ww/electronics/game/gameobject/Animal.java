package ch.ww.electronics.game.gameobject;

import org.json.JSONObject;

import ch.ww.electronics.game.gameobject.Brain.Status;
import ch.ww.electronics.game.level.Level;
import ch.ww.electronics.graphics.Screen;
import ch.ww.electronics.level.backgroundtile.BackgroundTile;
import ch.ww.electronics.util.MutableVector2D;
import ch.ww.electronics.util.Vector2D;

public class Animal extends GameObject{
	//TODO Es gibt eine Size in Gameobject, aber die wird in der Grafik nicht benutzt. Auch die getsize gibt immer nur 1 zurück. 
	private DNA dna;
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
		dna = new DNA(this);
		setTexture(new Screen((int) (BackgroundTile.SIZE * dna.getSize()), (int) (BackgroundTile.SIZE * dna.getSize()),
				0xffffff));
		this.brain = new Brain(this);
		motion = new MutableVector2D(0, 0);
		this.energy = dna.getMaxEnergy();
	}

	@Override
	public void tick() {
		adjustTexture();
		
		
		if(!isDead) {
			brain.think();
			adjustEnergy();
			setLocation(getX() + motion.getX(), getY() + motion.getY());
		}
	}
	
	private void adjustEnergy() {
		addEnergy(-motion.getLength() * dna.getSize());
		addEnergy(-0.01); //Passive energy burning
	}
	
	private void adjustTexture() {
		int c = 0x0;
		switch(this.getBrain().getStatus()) {
		case CHASING:
			c = 0x0000ff;
			break;
		case IDLE:
			c = 0xffffff;
			break;
		case RUNNING:
			c = 0xff0000;
			break;
		case SEARCHING_FOOD:
			c = 0x00ff00;
			break;
		case STUNNED:
			c = 0x551A8B;
			break;
		default:
			break;
		
		}
		Screen s = new Screen((int) (BackgroundTile.SIZE * getWidth()), (int) (BackgroundTile.SIZE * getHeight()),
				c);
		setTexture(s.darkScreen(getEnergy()/dna.getMaxEnergy()));
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
		if(energy <= 0) {
			energy = 0;
			isDead= true;
		}
		if(energy > dna.getMaxEnergy()) {
			energy = dna.getMaxEnergy();
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
		this.dna=dna;
	}
	
	public DNA getDNA(){
		return(dna);
	}
	
	public Brain getBrain(){
		return(brain);
	}
	
	public boolean isDead() {
		return isDead;
	}
	
	public double getSize() {
		return dna.getSize();
	}
	
	@Override
	public String toString() {
		return "[isDead:" + isDead() + "]";
	}

	public Status getStatus() {
		return brain.getStatus();
	}
	
	public void setStatus(Status s) {
		brain.setStatus(s);
	}
}
