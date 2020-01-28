package ch.ww.electronics.game.gameobject;

import ch.ww.electronics.util.MutableVector2D;
import ch.ww.electronics.util.Vector2D;

public class State {
	public enum Status {IDLE, CHASING, SEARCHING_FOOD, RUNNING, STUNNED, BE_FOOD};
	
	private Vector2D motion;
	private double energy;
	private Status status;
	private boolean isDead;
	private Animal target;
	
	public State(double energy, Status status) {
		this.motion = new MutableVector2D(0, 0);
		this.isDead = false;

		this.setEnergy(energy);
		this.status = status;
		this.target = null;
	}
	
	public void updateFrom(State other) {
		this.motion = other.motion;
		this.energy = other.energy;
		this.status = other.status;
		this.isDead = other.isDead;
		this.target = other.target;
	}

	public Vector2D getMotion() {
		return motion;
	}

	public void setMotion(Vector2D motion) {
		this.motion = motion;
	}

	public double getEnergy() {
		return energy;
	}

	public void setEnergy(double energy) {
		this.energy = energy;
		if(this.energy <= 0) {
			this.isDead = true;
			this.energy = 0;
		}
	}

	public boolean isDead() {
		return isDead;
	}

	public void setDead(boolean isDead) {
		this.isDead = isDead;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Animal getTarget() {
		return target;
	}

	public void setTarget(Animal target) {
		this.target = target;
	}

	public void addEnergy(double energy) {
		this.setEnergy(this.getEnergy() + energy);
	}
}
