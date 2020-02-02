package ch.ww.electronics.game.gameobject;

import org.json.JSONObject;

import ch.ww.electronics.game.gameobject.State.Status;
import ch.ww.electronics.game.level.Level;
import ch.ww.electronics.graphics.Screen;
import ch.ww.electronics.level.backgroundtile.BackgroundTile;

public class Animal extends GameObject{
	//TODO Es gibt eine Size in Gameobject, aber die wird in der Grafik nicht benutzt. Auch die getsize gibt immer nur 1 zurück. 
	private DNA dna;
	private Brain brain;
	private State calculState, actualState;
	
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
		dna.randomize(); 
		
		init();
	}
	
	public Animal(Level level, double x, double y, DNA dna) {
		super(level, x, y);
		this.dna = dna;
		this.dna.setAnimal(this);
		
		init();
	}
	
	private void init() {
		this.actualState = new State(dna.get(DNA.MAX_ENERGY), Status.IDLE, this);
		this.calculState = new State(dna.get(DNA.MAX_ENERGY), Status.IDLE, this);
		this.brain = new Brain(this);
		adjustTexture();
	}
	
	@Override
	public void tick() {
		adjustTexture();
		
		this.actualState.updateFrom(calculState);
		this.setX(this.getX() + this.actualState.getMotion().getX());
		this.setY(this.getY() + this.actualState.getMotion().getY());
	}
	
	@Override
	public void preTick() {
		brain.think();
		
		passiveEnergyBurning();
	}
	private void passiveEnergyBurning() {
		this.calculState.addEnergy(- this.dna.get(DNA.MAX_ENERGY) / 1000);
	}
	
	public void adjustTexture() {
		int c = 0x0;
		switch(this.actualState.getStatus()) {
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
		case BE_FOOD:
			c = 0x00ff00;
			break;
		default:
			throw new RuntimeException();		
		}
		
		
		Screen s = new Screen((int) (BackgroundTile.SIZE * getDNA().get(DNA.SIZE)), (int) (BackgroundTile.SIZE * getDNA().get(DNA.SIZE)),
				-1);
		//assert s.getHeight() < BackgroundTile.SIZE;
		
		s.fillCircle(0, 0, c, s.getHeight()/2);
		setTexture(s.darkScreen(actualState.getEnergy()/dna.get(DNA.MAX_ENERGY)));
	}
	
	@Override
	public GameObjectConstructor<? extends GameObject> getGameObjectConstructor() {
		return CONSTRUCTOR;
	}

	@Override
	public String getName() {
		return NAME;
	}
	
	public void setDNA(DNA dna){
		this.dna=dna;
		//Brain has reference to old DNA
		this.brain = new Brain(this);
	}
	
	public DNA getDNA(){
		return(dna);
	}
	
	public Brain getBrain(){
		return(brain);
	}
	
	public double getSize() {
		return dna.get(DNA.SIZE);
	}
	
	public State getCalculatedState() {
		return calculState;
	}
	
	public State getActualState() {
		return actualState;
	}
	
	
	
	@Override
	public String toString() {
		return "[isDead:" + actualState.isDead() + "]";
	}
	
	public String getNiceText() {
		String text = "";
		text+="x: " + getX() + "\n";
		text+="y: " + getY() + "\n";
		text+="Status: " + getActualState().getStatus().toString() + "\n";
		text += "Energy: " + getActualState().getEnergy() + "\n";
		text += "isDead: " + getActualState().isDead() + "\n"; 
		text+=dna.getNiceText();
		return(text);
	}
}
