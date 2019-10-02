package ch.ww.electronics.game.gameobject;

public class Fight {
	private Animal a1, a2;
	public Fight(Animal a1, Animal a2) {
		this.a1 = a1;
		this.a2 = a2;
	}
	
	public Animal getA1() {
		return a1;
	}
	
	public Animal getA2() {
		return a2;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Fight) {
			Fight f = (Fight) obj;
			if((this.a1 == f.a1 && this.a2 == f.a2) || (this.a1 == f.a2 && this.a2 == f.a1)) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
}
