package ch.ww.electronics.game.gameobject;

import ch.ww.electronics.util.MutableVector2D;
import ch.ww.electronics.util.Vector2D;

public class State {
	/**
	 * IDLE: nichts CHASING: Anderes monster verfolgen SEARCHING_FOOD: andere
	 * monster suchen RUNNING: vom etwasem abhauen (z. B. Kinder von Eltern)
	 * STUNNED: eltern nach der geburt (damit sie kinder nicht sofort fressen)
	 */
	public enum Status {
		/** Doing nothing, just wandering around. May create a child in this status*/
		IDLE,
		/** Chasing something else*/
		CHASING,
		/** It is hungry and searching for something to eat*/
		SEARCHING_FOOD, 
		/** Given to an animal after it was born so it flees its mother*/
		RUNNING,
		/** Given to an animal after it gave birth to its child so it doesn't eat it*/
		STUNNED,
		/** Given to instances of the class {@link Food}*/
		BE_FOOD};
	
	private Vector2D motion;
	private double energy;
	private Status status;
	private boolean isDead;
	/** The animal with this state*/
	private Animal animal;
	/** The animal that is being pursued by this animal*/
	private Animal target;
	
	private int age;
	
	public State(double energy, Status status, Animal animal) {
		this.motion = new MutableVector2D(0, 0);
		this.isDead = false;
		this.status = status;
		this.animal = animal;
		this.target = null;
		this.age = 0;
		this.setEnergy(energy);
	}
	
	public void updateFrom(State other) {
		this.motion = other.motion;
		this.energy = other.energy;
		this.status = other.status;
		this.isDead = other.isDead;
		this.target = other.target;
		this.animal = other.animal;
		this.age    = other.age;
	}
	
	public State getDelta(State other) {
		State ret = new State(energy, status, animal);
		ret.updateFrom(this);
		ret.energy -=other.energy;
		ret.motion = new Vector2D(this.motion.getX() - other.motion.getX(), this.motion.getY() - other.motion.getY());
		ret.status = other.status == this.status ? other.status : null;
		ret.isDead = other.isDead && this.isDead;
		ret.target = other.target == this.target ? other.target : null;
		ret.animal = other.animal == this.animal ? other.animal : null;
		ret.age -= other.age;
		return ret;
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
		} else if(this.energy > animal.getDNA().get(DNA.MAX_ENERGY)) {
			this.energy = animal.getDNA().get(DNA.MAX_ENERGY);
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

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}
	
	public void incrementAge() {
		setAge(getAge() + 1);
	}
}
