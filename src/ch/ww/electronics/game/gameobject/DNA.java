package ch.ww.electronics.game.gameobject;

public class DNA {
	private Animal animal;

	private double maxEnergy;
	private double maxSize;
	private double fur;
	private double maxSpeed;
	private double viewrange;

	private final double variation = 0.1;

	public DNA(Animal game, double size, double fur, double speed, double viewrange) {
		this.animal = game;

		this.maxSize = size;
		this.fur = fur;
		this.maxSpeed = speed;
		this.viewrange = viewrange;
	}

	public DNA(DNA dna) {
		this.animal = dna.animal;

		this.maxSize = dna.getMaxSize();
		this.fur = dna.getFur();
		this.maxSpeed = dna.getMaxSpeed();
		this.viewrange = dna.getViewrange();
	}

	public void randomize() {
		maxSize = (animal.getRandom().nextDouble() * 2 - 1);
		fur = (animal.getRandom().nextDouble() * 2 - 1);
		maxSpeed = (animal.getRandom().nextDouble() * 2 - 1);
		viewrange = (animal.getRandom().nextDouble() * 2 - 1);
	}

	public void variate(double radiation) {
		maxSize += (animal.getRandom().nextDouble() * 2 - 1) * variation * (1 + radiation);
		fur += (animal.getRandom().nextDouble() * 2 - 1) * variation * (1 + radiation);
		maxSpeed += (animal.getRandom().nextDouble() * 2 - 1) * variation * (1 + radiation);
		viewrange += (animal.getRandom().nextDouble() * 2 - 1) * variation * (1 + radiation);
	}

	public double getMaxSize() {
		return maxSize;
	}

	public void setMaxSize(double maxSize) {
		this.maxSize = maxSize;
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
}
