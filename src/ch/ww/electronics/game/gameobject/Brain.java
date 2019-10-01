package ch.ww.electronics.game.gameobject;

public class Brain {
	public enum Status {IDLE, CHASING, EATING, SEARCHING_FOOD};
	
	private final Animal animal;
	private Status status;
	
	public Brain(Animal animal) {
		this.animal = animal;
	}

	public void think() {
		
	}
	
	public Animal getAnimal() {
		return animal;
	}
}