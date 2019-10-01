package ch.ww.electronics.game.gameobject;

public abstract class Sensor{
	protected Animal animal;
	public Sensor(Animal animal){
		this.animal=animal;
	}
	public abstract void getdata();
}
