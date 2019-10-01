package ch.ww.electronics.game.gameobject;

public class DNA {
	private double size;
	private double fur;
	private double speed;
	private double viewrange;
	
	public DNA(double size,double fur,double speed, double viewrange){
		this.size=size;
		this.fur=fur;
		this.speed=speed;
		this.viewrange=viewrange;
	}
	
	public void randomize(){
		size=Math.random();
		fur=Math.random();
		speed=Math.random();
		viewrange=Math.random();
	}
	public void variate(double strahlung){
		
	}

	public double getSize() {
		return size;
	}

	public void setSize(double size) {
		this.size = size;
	}

	public double getFur() {
		return fur;
	}

	public void setFur(double fur) {
		this.fur = fur;
	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public double getViewrange() {
		return viewrange;
	}

	public void setViewrange(double viewrange) {
		this.viewrange = viewrange;
	}
}
