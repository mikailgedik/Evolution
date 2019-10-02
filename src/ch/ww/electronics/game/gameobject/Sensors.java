package ch.ww.electronics.game.gameobject;

import java.util.ArrayList;

public class Sensors{
	private Animal animal;
	public Sensors(Animal animal){
		this.animal=animal;
	}
	public ArrayList<Animal> getEyeInput(){
		ArrayList<Animal> animals= new ArrayList<>();
		animal.getGame().getLevel().getObjectsInLevel().forEach((GameObject go) -> {
			if(go instanceof Animal) {
				Animal a = (Animal) go;
				if(this.animal!=a){
					if(this.animal.distanceTo(a) <= animal.getDNA().getViewrange()) {
						
						//animals.add(a);
					
						//(a.getY()-animal.getY())/(a.getX()-animal.getX()) ist die Steigung
						//atan(steigung) ist der Winkel
						double winkel=Math.atan((a.getY()-animal.getY())/(a.getX()-animal.getX()));
						winkel=(winkel+3*Math.PI/2)%(2*Math.PI);
						
						System.out.println(winkel);
						
						if(winkel<(animal.getDNA().getViewangle()+animal.getDNA().getViewangle())%(2*Math.PI) & winkel>animal.getBrain().getFacingAngle()){
							animals.add(a);
							System.out.println("chasing");
						}
					}
				}
			}
		});
		return(animals);
	}
	
	
}
