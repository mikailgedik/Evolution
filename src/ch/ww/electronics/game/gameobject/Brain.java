package ch.ww.electronics.game.gameobject;

import java.util.Random;

import ch.ww.electronics.util.Vector2D;

public class Brain {
	public enum Status {IDLE, CHASING, EATING, SEARCHING_FOOD};
	
	private final Animal animal;
	private final Random r;
	private final DNA dna;
	
	private final Sensors sensors;
	
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
		switch(status) {
		case IDLE:
			if(animal.getMotion().getLength() == 0 && animal.getLevel().getRandom().nextDouble() < 0.1) {
				animal.setMotion(new Vector2D());
			
			} else if(animal.getLevel().getRandom().nextDouble() < 0.1) {
				
			}
			break;
		case CHASING:
			break;
		case EATING:
			break;
		case SEARCHING_FOOD:
			break;
		default:
			throw new RuntimeException("Should not reach this step");
		}
	}
	
	public Animal getAnimal() {
		return animal;
	}
}