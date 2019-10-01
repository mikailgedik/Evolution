package ch.ww.electronics.game.gameobject;

public class Brain {
	public enum Status {IDLE, CHASING, EATING, SEARCHING_FOOD};
	
	private final Animal animal;
	private Status status;
	
	public Brain(Animal animal) {
		this.animal = animal;
		status = Status.IDLE;
	}

	public void think() {
		if(Status.IDLE) {
			
		}
	}
	
	public Animal getAnimal() {
		return animal;
	}
}