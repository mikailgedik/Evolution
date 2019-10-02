package ch.ww.electronics.game.gameobject;

public class DNA {
	private Animal animal;

	private double size;
	private double fur;
	private double maxSpeed;
	private double viewrange;
	private double viewangle;
	private double maxEnergy;

	private static final double VARIATION = 0.1;

	public DNA(Animal game, double size, double fur, double maxSpeed, double viewrange, double viewangle, double maxEnergy) {
		this.animal = game;

		this.size = size;
		this.fur = fur;
		this.maxSpeed = maxSpeed;
		this.viewrange = viewrange;
		this.viewangle = viewangle;
		this.maxEnergy = maxEnergy;
	}

	public void randomize() {
		size = (animal.getRandom().nextDouble());
		fur = (animal.getRandom().nextDouble());
		maxSpeed = (animal.getRandom().nextDouble());
		viewrange = (animal.getRandom().nextDouble()) * 5;
		viewrange = 10;
		viewangle = 2*Math.PI;
		this.maxEnergy = (animal.getRandom().nextDouble()) * 100;
		this.maxEnergy = 3000;
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
}
