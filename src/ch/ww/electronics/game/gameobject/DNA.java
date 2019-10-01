package ch.ww.electronics.game.gameobject;

import ch.ww.electronics.game.Game;

public class DNA {
	private Game game;
	
	private double size;
	private double fur;
	private double speed;
	private double viewrange;
	
	private final double variation=0.1;
	
	public DNA(Game game, double size,double fur,double speed, double viewrange){
		this.game=game;
		
		this.size=size;
		this.fur=fur;
		this.speed=speed;
		this.viewrange=viewrange;
	}
	
	public void randomize(){
		size=(Math.random()*2-1);
		fur=(Math.random()*2-1);
		speed=(Math.random()*2-1);
		viewrange=(Math.random()*2-1);
	}
	
	public void variate(double strahlung){
		size+=(Math.random()*2-1)*variation*(1+strahlung);
		fur+=(Math.random()*2-1)*variation*(1+strahlung);
		speed+=(Math.random()*2-1)*variation*(1+strahlung);
		viewrange+=(Math.random()*2-1)*variation*(1+strahlung);
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
