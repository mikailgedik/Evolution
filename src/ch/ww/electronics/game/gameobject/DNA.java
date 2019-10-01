package ch.ww.electronics.game.gameobject;

import ch.ww.electronics.game.Game;

public class DNA {
	private Game game;

	private double maxEnergy;
	private double maxSize;
	private double fur;
	private double maxSpeed;
	private double viewrange;

	private final double variation = 0.1;

	public DNA(Game game, double size, double fur, double speed, double viewrange) {
		this.game = game;

		this.maxSize = size;
		this.fur = fur;
		this.maxSpeed = speed;
		this.viewrange = viewrange;
	}

	public DNA(DNA dna) {
		this.game = dna.game;

		this.maxSize = dna.getMaxSize();
		this.fur = dna.getFur();
		this.maxSpeed = dna.getMaxSpeed();
		this.viewrange = dna.getViewrange();
	}

	public void randomize() {
		maxSize = (game.getRandom().nextDouble() * 2 - 1);
		fur = (game.getRandom().nextDouble() * 2 - 1);
		maxSpeed = (game.getRandom().nextDouble() * 2 - 1);
		viewrange = (game.getRandom().nextDouble() * 2 - 1);
	}

	public void variate(double strahlung) {
		maxSize += (game.getRandom().nextDouble() * 2 - 1) * variation * (1 + strahlung);
		fur += (game.getRandom().nextDouble() * 2 - 1) * variation * (1 + strahlung);
		maxSpeed += (game.getRandom().nextDouble() * 2 - 1) * variation * (1 + strahlung);
		viewrange += (game.getRandom().nextDouble() * 2 - 1) * variation * (1 + strahlung);
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
