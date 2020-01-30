package ch.ww.electronics.game.gameobject;

import java.util.ArrayList;
import java.util.Random;

import ch.ww.electronics.game.gameobject.State.Status;
import ch.ww.electronics.util.MutableVector2D;

public class Brain {
	private final Animal animal;
	/** Reference to the animal's random */
	private final Random r;
	/** Reference to the animals's sensor */
	private final Sensors sensors;
	/** Reference to state of the animal */
	private final State calculState;
	/** Reference to the dna of the animal */
	private final DNA dna;

	public Brain(Animal animal) {
		this.animal = animal;
		this.sensors = new Sensors(this.animal);
		r = animal.getRandom();
		calculState = animal.getCalculatedState();
		this.dna = animal.getDNA();
	}

	public void think() {
		switch (this.animal.getActualState().getStatus()) {
		case IDLE:
			idle(sensors.getEyeInput());
			break;
		case CHASING:
			chasing(sensors.getEyeInput());
			break;
		case SEARCHING_FOOD:
			searchingFood(sensors.getEyeInput());
			break;
		case RUNNING:
			run(sensors.getEyeInput());
			break;
		case STUNNED:
			stunned(sensors.getEyeInput());
			break;
		case BE_FOOD:
			assert false : "Food should not think";
		default:
			throw new RuntimeException("Step should not be reached");
		}

	}

	private void setMotionToRandomDirection(double speed) {
		MutableVector2D v = new MutableVector2D(r.nextDouble() * (r.nextBoolean() ? 1 : -1),
				r.nextDouble() * (r.nextBoolean() ? 1 : -1));
		double factor = speed / v.getLength();
		v = new MutableVector2D(v.getX() * factor, v.getY() * factor);
		calculState.setMotion(v);
	}

	private void idle(ArrayList<Animal> nearby) {
		if (this.dna.get(DNA.BABY_WHEN_ENERGY) * dna.get(DNA.MAX_ENERGY) < this.calculState.getEnergy()) {
			this.animal.getLevel().babyFrom(this.animal);
		} else if (this.dna.get(DNA.START_SEARCHING_FOOD) * dna.get(DNA.MAX_ENERGY) > this.calculState.getEnergy()) {
			calculState.setStatus(Status.SEARCHING_FOOD);
		} else if (r.nextDouble() < 0.05) {
			setMotionToRandomDirection(animal.getDNA().get(DNA.MAX_SPEED) * r.nextDouble());
		}
	}

	private void chasing(ArrayList<Animal> nearby) {
		if (!nearby.contains(this.calculState.getTarget())) {
			this.calculState.setTarget(null);
		} else {
			Animal t = this.calculState.getTarget();
			if (t.isTouching(this.animal)) {
				this.animal.getLevel().addFight(new Fight(this.animal, t));
				this.calculState.setTarget(null);
				this.calculState.setStatus(Status.IDLE);
			} else {
				MutableVector2D v = new MutableVector2D(t.getX() - this.animal.getX(), t.getY() - this.animal.getY());
				v.factor(dna.get(DNA.MAX_SPEED) / v.getLength());
				this.calculState.setMotion(v);
			}
		}
	}

	private void searchingFood(ArrayList<Animal> nearby) {
		if (!nearby.isEmpty()) {
			this.calculState.setTarget(nearby.get(0));
			this.calculState.setStatus(Status.CHASING);
		} else {
			this.calculState.setTarget(null);
			if (r.nextDouble() < 0.05) {
				setMotionToRandomDirection(animal.getDNA().get(DNA.MAX_SPEED) * r.nextDouble());
			}
		}
	}

	private void run(ArrayList<Animal> nearby) {
		if (r.nextDouble() < dna.get(DNA.RUNNING_TIME)) {
			this.calculState.setStatus(Status.IDLE);
			System.out.println("Return");
		} else if (r.nextDouble() < 0.05 || this.calculState.getMotion().getLength() < 0.9 * dna.get(DNA.MAX_SPEED)) {
			setMotionToRandomDirection(dna.get(DNA.MAX_SPEED));
		}
	}

	private void stunned(ArrayList<Animal> nearby) {
		if (r.nextDouble() < dna.get(DNA.STUNNED_TIME)) {
			this.calculState.setStatus(Status.IDLE);
			System.out.println("Return");
		}
	}

	public Animal getAnimal() {
		return animal;
	}
}