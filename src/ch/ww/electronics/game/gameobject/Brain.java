package ch.ww.electronics.game.gameobject;

import java.util.ArrayList;
import java.util.Random;

import ch.ww.electronics.util.MutableVector2D;
import ch.ww.electronics.util.Vector2D;

public class Brain {
	/**IDLE: nichts
	 * CHASING: Anderes monster verfolgen
	 * SEARCHING_FOOD: andere monster suchen
	 * RUNNING: vom etwasem abhauen (z. B. Kinder von Eltern)
	 * STUNNED: eltern nach der geburt (damit sie kinder nicht sofort fressen)
	 * */
	public enum Status {IDLE, CHASING, SEARCHING_FOOD, RUNNING, STUNNED, BE_FOOD};
	
	private final Animal animal;
	private final Random r;
	private final DNA dna;
	
	private final Sensors sensors;
	private double facingangle;
	
	private Status status;
	
	private Animal target;
	
	public Brain(Animal animal) {
		this.animal = animal;
		this.sensors = new Sensors(this.animal);
		r = animal.getRandom();
		dna = animal.getDNA();
		this.target = null;
		status = Status.IDLE;
	}

	public void think() {
		ArrayList<Animal> nearby = sensors.getEyeInput();
		if(status != Status.RUNNING && status != Status.STUNNED) {
			if(animal.getEnergy()/dna.getMaxEnergy() < 0.9 && status != Status.CHASING) {
				status = Status.SEARCHING_FOOD;
			}
		}
		
		switch(status) {
		case IDLE:
			idle(nearby);
			break;
		case CHASING:
			chasing(nearby);
			break;
		case SEARCHING_FOOD:
			searchingFood(nearby);
			break;
		case RUNNING:
			run(nearby);
			break;
		case STUNNED:
			stunned(nearby);
			break;
		default:
			throw new RuntimeException("Should not reach this step");
		}
		
		
	}
	
	private void setMotionToRandomDirection(double speed) {
		MutableVector2D v = new MutableVector2D(r.nextDouble() * (r.nextBoolean() ? 1 : -1), r.nextDouble() * (r.nextBoolean() ? 1 : -1));
		double factor = speed / v.getLength();
		v = new MutableVector2D(v.getX() * factor, v.getY() * factor);
		animal.setMotion(v);
	}
	
	private void idle(ArrayList<Animal> nearby) {
		if(animal.getMotion().getLength() == 0 && animal.getLevel().getRandom().nextDouble() < 0.1) {
			setMotionToRandomDirection(r.nextDouble() *dna.getMaxSpeed());
		} else if(animal.getLevel().getRandom().nextDouble() < 0.1) {
			animal.setMotion(new Vector2D(0,0));
		}
		
		if(getAnimal().getEnergy()/getAnimal().getDNA().getMaxEnergy()> 0.8/* && r.nextDouble() < 0.01 */){
			System.out.println("NEW BABY");
			Animal baby = new Animal(animal.getLevel(), animal.getX(), animal.getY());
			baby.getDNA().variate(0);
			baby.setEnergy(baby.getEnergy()/2);
			getAnimal().setEnergy(getAnimal().getEnergy()/3);
			
			getAnimal().setStatus(Status.STUNNED);
			baby.setStatus(Status.RUNNING);
		}
	}
	
	private void chasing(ArrayList<Animal> nearby) {
		if(this.animal.isTouching(target)) {
			animal.getLevel().addFight(new Fight(this.animal, target));
			status = Status.IDLE;
		} else if(!nearby.contains(target)) {
			//Target out of sight
			target = null;
			status = Status.IDLE;
		} else {
			MutableVector2D v = new MutableVector2D(target.getX() - this.animal.getX(), target.getY() - this.animal.getY());
			double factor = dna.getMaxSpeed() / v.getLength();
			v = new MutableVector2D(v.getX() * factor, v.getY() * factor);
			animal.setMotion(v);
		}
	}
	
	private void searchingFood(ArrayList<Animal> nearby) {
		if(nearby.size() > 0) {
			target = nearby.get(animal.getRandom().nextInt(nearby.size()));
			status = Status.CHASING;
		} else {
			if(r.nextDouble() < 0.01) {
				setMotionToRandomDirection(r.nextDouble() *dna.getMaxSpeed());
			}
			target = null;
		}
	}
	
	private void run(ArrayList<Animal> nearby) {
		if(this.animal.getMotion().getLength() < this.animal.getDNA().getMaxSpeed() * 0.9) {
			setMotionToRandomDirection(dna.getMaxSpeed());
		}
		
		//Je mehr Gegner es sieht, desto eher rennt es weiter
		if(animal.getRandom().nextDouble() < Math.pow(0.005, nearby.size())) {
			status = Status.IDLE;
		}
	}
	
	private void stunned(ArrayList<Animal> nearby) {
		if(this.animal.getMotion().getLength() != 0) {
			this.animal.setMotion(new Vector2D(0, 0));
		}

		if(animal.getRandom().nextDouble() < 0.001) {
			status = Status.IDLE;
		}
	}
	
	public Animal getAnimal() {
		return animal;
	}
	
	public Status getStatus() {
		return status;
	}
	
	public double getFacingAngle(){
		return (facingangle);
	}

	public void setStatus(Status status) {
		this.status = status;
	}
}