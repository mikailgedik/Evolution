package ch.ww.electronics.game.gameobject;

public class DNA {
	private Animal animal;

	private double size;
	private double fur;
	private double maxSpeed;
	private double viewrange;
	private double viewangle;
	private double maxEnergy;
	private double stunned;

	private static final double VARIATION = 0.1;

	public DNA(Animal game, double size, double fur, double maxSpeed, double viewrange, double viewangle, double maxEnergy, double stunned) {
		this.animal = game;

		this.size = size;
		this.fur = fur;
		this.maxSpeed = maxSpeed;
		this.viewrange = viewrange;
		this.viewangle = viewangle;
		this.maxEnergy = maxEnergy;
		this.stunned = stunned;
	}
	public DNA(Animal animal){
		this.animal=animal;
		randomize();
	}

	public void randomize() {
		size = (prandom());
		fur = animal.getRandom().nextDouble();
		maxSpeed = prandom() * 0.1 + 0.2;
		viewrange = prandom() * 5;
		viewangle = prandom()*Math.PI;
		viewangle = Math.PI*3;
		maxEnergy = prandom() * 100;
		stunned = animal.getRandom().nextDouble()*200;
	}
	private double prandom(){
		return(animal.getRandom().nextDouble()*0.5+0.5);
	}

	public void variate(double radiation) {
		size += (animal.getRandom().nextDouble() * 2 - 1) * VARIATION * (1 + radiation);
		fur += (animal.getRandom().nextDouble() * 2 - 1) * VARIATION * (1 + radiation);
		maxSpeed += (animal.getRandom().nextDouble() * 2 - 1) * VARIATION * (1 + radiation);
		viewrange += (animal.getRandom().nextDouble() * 2 - 1) * VARIATION * (1 + radiation);
	}

	public double getSize() {
		return size;
	}

	public void setSize(double size) {
		this.size = size;
	}

	public double getFur() {
		return fur;
	}

	public void setFur(double fur) {
		this.fur = fur;
	}

	public double getMaxSpeed() {
		return maxSpeed;
	}

	public void setMaxSpeed(double speed) {
		this.maxSpeed = speed;
	}

	public double getViewrange() {
		return viewrange;
	}

	public void setViewrange(double viewrange) {
		this.viewrange = viewrange;
	}

	public double getMaxEnergy() {
		return maxEnergy;
	}

	public void setMaxEnergy(double maxEnergy) {
		this.maxEnergy = maxEnergy;
	}
	
	public double getViewangle() {
		return viewangle;
	}
	
	public void setViewangle(double viewangle) {
		this.viewangle = viewangle;
	}
	public double getStunned() {
		return stunned;
	}
	
	public void getStunned(double stunned) {
		this.stunned = stunned;
	}
}
