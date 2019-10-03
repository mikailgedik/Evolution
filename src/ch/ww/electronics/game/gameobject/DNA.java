package ch.ww.electronics.game.gameobject;

import java.util.HashMap;
import java.util.Map;

public class DNA {
	private Animal animal;

	public static final String SIZE = "size";
	public static final String FUR = "fur";
	public static final String MAX_SPEED = "maxSpeed";
	public static final String VIEWRANGE = "viewrange";
	public static final String MAX_ENERGY = "maxEnergy";
	public static final String STUNNED = "stunned";
//	
//	private double size;
//	private double fur;
//	private double maxSpeed;
//	private double viewrange;
//	private double maxEnergy;
//	private double stunned;

	private Map<String, Double> values;
	
	private static final Map <String, Double[]> specifications;
	static {
		//0: minimum
		//1: maximum
		//2: variation
		specifications = new HashMap<>();
		specifications.put(SIZE, new Double[]{0d, 1d, 0.1d});
		specifications.put(FUR, new Double[]{0d,1d,0.1d});
		specifications.put(MAX_SPEED, new Double[]{0d,0.1d,0.01});
		specifications.put(VIEWRANGE, new Double[]{0d,10d, 0.5});
		specifications.put(MAX_ENERGY, new Double[]{0d,2000d, 10d});
		specifications.put(STUNNED, new Double[]{0d,1000d, 100d});
	}
	
	private static final double VARIATION = 0.1;

	public DNA(Animal game, double size, double fur, double maxSpeed, double viewrange, double viewangle, double maxEnergy, double stunned) {
		this.animal = game;
		
		values = new HashMap<>();
		values.put(SIZE, size);
		values.put(FUR, fur);
		values.put(MAX_SPEED, maxSpeed);
		values.put(VIEWRANGE, viewangle);
		values.put(MAX_ENERGY, maxEnergy);
		values.put(STUNNED, stunned);
		
//		this.size = size;
//		this.fur = fur;
//		this.maxSpeed = maxSpeed;
//		this.viewrange = viewrange;
//		this.maxEnergy = maxEnergy;
//		this.stunned = stunned;
		
		validate();
	}
	public DNA(Animal animal){
		this.animal=animal;
		randomize();
	}

	public void randomize() {
//		size = (prandom());
//		fur = animal.getRandom().nextDouble();
//		maxSpeed = prandom() * 0.1 + 0.2;
//		viewrange = prandom() * 5;
//		maxEnergy = prandom() * 100;
//		stunned = animal.getRandom().nextDouble()*200;
		
		values.forEach((String key, Double value) -> {
			value=animal.getLevel().getRandom().nextDouble()*(specifications.get(key)[1]-specifications.get(key)[0])+specifications.get(key)[0];
		});
		
		validate();
	}
//	private double prandom(){
//		return(animal.getRandom().nextDouble()*0.5+0.5);
//	}

	public void variate(double radiation) {
//		size += (animal.getRandom().nextDouble() * 2 - 1) * VARIATION * (1 + radiation);
//		fur += (animal.getRandom().nextDouble() * 2 - 1) * VARIATION * (1 + radiation);
//		maxSpeed += (animal.getRandom().nextDouble() * 2 - 1) * VARIATION * (1 + radiation);
//		viewrange += (animal.getRandom().nextDouble() * 2 - 1) * VARIATION * (1 + radiation);
//		stunned += (animal.getRandom().nextDouble() * 2 - 1) * VARIATION * (1 + radiation)*10;
		values.forEach((String key, Double value) -> {
			value+=animal.getLevel().getRandom().nextDouble()*specifications.get(key)[2];
		});
		validate();
	}
	private void validate(){
		
//		if(size<0.5)size=1;
//		if(fur<0)fur=0; if(fur>1)fur=1;
//		if(maxSpeed<0)maxSpeed=0;
//		if(viewrange<0)viewrange=0;
//		if(stunned<0)stunned=0;
		values.forEach((String key, Double value)->{
			if(value<specifications.get(key)[0]){
				value=specifications.get(key)[0];
				return;
			}
			if(value>specifications.get(key)[1]){
				value=specifications.get(key)[1];
				return;
			}
		});
	}
	
	public double get(String key){
		return values.get(key);
	}
	public void set(String key, Double value){
		if(values.put(key, value)==null) throw new RuntimeException("Not a valid key");
	}
	
//	public double getSize() {
//		return size;
//	}
//
//	public void setSize(double size) {
//		this.size = size;
//	}
//
//	public double getFur() {
//		return fur;
//	}
//
//	public void setFur(double fur) {
//		this.fur = fur;
//	}
//
//	public double getMaxSpeed() {
//		return maxSpeed;
//	}
//
//	public void setMaxSpeed(double speed) {
//		this.maxSpeed = speed;
//	}
//
//	public double getViewrange() {
//		return viewrange;
//	}
//
//	public void setViewrange(double viewrange) {
//		this.viewrange = viewrange;
//	}
//
//	public double getMaxEnergy() {
//		return maxEnergy;
//	}
//
//	public void setMaxEnergy(double maxEnergy) {
//		this.maxEnergy = maxEnergy;
//	}
//
//	public double getStunned() {
//		return stunned;
//	}
//	
//	public void getStunned(double stunned) {
//		this.stunned = stunned;
//	}
}
