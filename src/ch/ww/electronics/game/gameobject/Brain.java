package ch.ww.electronics.game.gameobject;

public class Brain {
	public enum Status {IDLE, CHASING, EATING, SEARCHING_FOOD};
	
	private final Animal animal;
	private final Sensors sensors;
	
	private Status status;
	
	private Animal target;
	
	public Brain(Animal animal) {
		this.animal = animal;
		this.sensors = new Sensors(this.animal);
		this.target = null;
		status = Status.IDLE;
	}

	public void think() {
		switch(status) {
		case IDLE:
			
		case CHASING:
			
		case EATING:
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