package ch.ww.electronics.game.gameobject;

public class Eye extends Sensor{
	private double winkel;
	
	public Eye(Animal animal,double winkel){
		super(animal);
		this.winkel=winkel;
	}
	@Override
	public void getdata() {
		return(null);
	}
	
}
