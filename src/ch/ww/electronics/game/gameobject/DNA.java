package ch.ww.electronics.game.gameobject;

public class DNA {
	private Animal animal;

	private double size;
	private double fur;
	private double maxSpeed;
	private double viewrange;
	private double maxEnergy;
	private double stunned;

	private static final double VARIATION = 0.1;

	public DNA(Animal game, double size, double fur, double maxSpeed, double viewrange, double viewangle, double maxEnergy, double stunned) {
		this.animal = game;

		this.size = size;
		this.fur = fur;
		this.maxSpeed = maxSpeed;
		this.viewrange = viewrange;
		this.maxEnergy = maxEnergy;
		this.stunned = stunned;
		make_valid();
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
		maxEnergy = prandom() * 100;
		stunned = animal.getRandom().nextDouble()*200;
		make_valid();
	}
	private double prandom(){
		return(animal.getRandom().nextDouble()*0.5+0.5);
	}

	public void variate(double radiation) {
		size += (animal.getRandom().nextDouble() * 2 - 1) * VARIATION * (1 + radiation);
		fur += (animal.getRandom().nextDouble() * 2 - 1) * VARIATION * (1 + radiation);
		maxSpeed += (animal.getRandom().nextDouble() * 2 - 1) * VARIATION * (1 + radiation);
		viewrange += (animal.getRandom().nextDouble() * 2 - 1) * VARIATION * (1 + radiation);
		stunned += (animal.getRandom().nextDouble() * 2 - 1) * VARIATION * (1 + radiation)*10;
		make_valid();
	}
	private void make_valid(){
		if(size<0.5)size=1;
		if(fur<0)fur=0; if(fur>1)fur=1;
		if(maxSpeed<0)maxSpeed=0;
		if(viewrange<0)viewrange=0;
		if(stunned<0)stunned=0;
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

	public double getStunned() {
		return stunned;
	}
	
	public void getStunned(double stunned) {
		this.stunned = stunned;
	}
}
