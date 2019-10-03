package ch.ww.electronics.game.gameobject;

import java.util.HashMap;

public class DNA {
	private Animal animal;

	public static final String SIZE = "size";
	public static final String FUR = "fur";
	public static final String MAX_SPEED = "maxSpeed";
	public static final String VIEWRANGE = "viewrange";
	public static final String MAX_ENERGY = "maxEnergy";
	public static final String STUNNED = "stunned";
	
	private HashMap<String, Double> values;
	
	private static final HashMap <String, Double[]> specifications;
	static {
		//0: minimum
		//1: maximum
		//2: variation
		specifications = new HashMap<>();
		specifications.put(SIZE, new Double[]{0.5d, 1d, 0.1d});
		specifications.put(FUR, new Double[]{0d,1d,0.1d});
		specifications.put(MAX_SPEED, new Double[]{0.01d,0.1d,0.01});
		specifications.put(VIEWRANGE, new Double[]{2d,10d, 0.5});
		specifications.put(MAX_ENERGY, new Double[]{0.1d,20000d, 10d});
		specifications.put(STUNNED, new Double[]{0d,1000d, 100d});
	}
	
	public DNA(Animal animal, double size, double fur, double maxSpeed, double viewrange, double viewangle, double maxEnergy, double stunned) {
		this.animal = animal;
		
		values = new HashMap<String, Double>();
		values.put(SIZE, size);
		values.put(FUR, fur);
		values.put(MAX_SPEED, maxSpeed);
		values.put(VIEWRANGE, viewangle);
		values.put(MAX_ENERGY, maxEnergy);
		values.put(STUNNED, stunned);
		
		validate();
	}
	
	public DNA(Animal animal){
		this(animal, 0, 0, 0, 0, 0, 0, 0);
		randomize();
	}
	
	@Override
	protected DNA clone() {
		DNA dna = new DNA(this.animal);
		dna.values = (HashMap<String, Double>) this.values.clone();
		return dna;
	}
	
	public void randomize() {		
		values.forEach((String key, Double value) -> {
			values.put(key, animal.getLevel().getRandom().nextDouble()*(specifications.get(key)[1]-specifications.get(key)[0])+specifications.get(key)[0]);
		});
		
		
		
		validate();
	}

	public void variate(double radiation) {
		values.forEach((String key, Double value) -> {
			values.put(key, values.get(key) + (radiation+1)*(animal.getLevel().getRandom().nextDouble()*specifications.get(key)[2]));
		});
		validate();
	}
	private void validate(){
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
}
