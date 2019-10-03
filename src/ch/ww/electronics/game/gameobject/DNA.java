package ch.ww.electronics.game.gameobject;

import java.util.HashMap;
import java.util.Map.Entry;

public class DNA {
	private Animal animal;

	public static final String SIZE = "size";
	public static final String FUR = "fur";
	public static final String MAX_SPEED = "maxSpeed";
	public static final String VIEWRANGE = "viewrange";
	public static final String MAX_ENERGY = "maxEnergy";
	public static final String STUNNED_TIME = "stunnedTime";
	public static final String RUNNING_TIME ="runningTime";
	public static final String START_SEARCHING_FOOD = "startSearchingFood";
	public static final String BABY_WHEN_ENERGIE = "babieWhenEnergie";
	
	private HashMap<String, Double> values;
	
	private static final HashMap <String, Double[]> specifications;
	static {
		//0: minimum
		//1: maximum
		//2: variation
		specifications = new HashMap<>();
		specifications.put(SIZE, new Double[]{0.05d, 0.2d, 0.1d});
		specifications.put(FUR, new Double[]{0d,1d,0.1d});
		specifications.put(MAX_SPEED, new Double[]{0.05d,0.1d,0.01});
		specifications.put(VIEWRANGE, new Double[]{5d,10d, 0.5});
		specifications.put(MAX_ENERGY, new Double[]{0.1d,5000d, 10d});
		specifications.put(STUNNED_TIME, new Double[]{0d, 1d, 0.1d});
		specifications.put(RUNNING_TIME, new Double[]{0d, 1d, 0.1d});
		specifications.put(START_SEARCHING_FOOD, new Double[]{0.6d, 1d, 0.05d});
		specifications.put(BABY_WHEN_ENERGIE, new Double[]{0d, 1d , 0.05d});
	}
	
	public void setgood(){
		/*
		values.put(SIZE, 0.8);
		values.put(FUR, 0.5);
		values.put(MAX_SPEED, 0.08);
		values.put(VIEWRANGE, 10d);
		values.put(MAX_ENERGY,20000d);
		values.put(STUNNED_TIME,0.05);
		values.put(RUNNING_TIME, 0.01d);
		values.put(START_SEARCHING_FOOD,1d);
		values.put(BABY_WHEN_ENERGIE, 0.5);
		*/
	}
	
	public DNA(Animal animal, double size, double fur, double maxSpeed, double viewrange, double viewangle, double maxEnergy, double stunnedTime, double runningTime, double startSearchingFood, double idleProbability, double babyWhenEnergie) {
		this.animal = animal;
		
		values = new HashMap<String, Double>();
		values.put(SIZE, size);
		values.put(FUR, fur);
		values.put(MAX_SPEED, maxSpeed);
		values.put(VIEWRANGE, viewangle);
		values.put(MAX_ENERGY, maxEnergy);
		values.put(STUNNED_TIME, stunnedTime);
		values.put(RUNNING_TIME, runningTime);
		values.put(START_SEARCHING_FOOD, startSearchingFood);
		values.put(BABY_WHEN_ENERGIE, babyWhenEnergie);
		validate();
	}
	
	public DNA(Animal animal){
		this(animal, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
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
		//setgood();
	}

	public void variate(double radiation) {
		values.forEach((String key, Double value) -> {
			values.put(key, values.get(key) + (radiation+1)*(animal.getLevel().getRandom().nextDouble()*specifications.get(key)[2]));
		});
		validate();
	}
	private void validate(){
		for(Entry <String, Double> e: values.entrySet()){
			if(e.getValue()<specifications.get(e.getKey())[0]) {
				values.put(e.getKey(), specifications.get(e.getKey())[0]);
				return;
			}
			if(e.getValue()>specifications.get(e.getKey())[1]) {
				values.put(e.getKey(), specifications.get(e.getKey())[1]);
				return;
			}
		}
	}
	
	public double get(String key){
		return values.get(key);
	}
	public void set(String key, Double value){
		if(values.put(key, value)==null) throw new RuntimeException("Not a valid key");
	}
	
	public HashMap<String, Double> getValues() {
		return values;
	}
	public String getNiceText(){
		String text="";
		for(Entry<String, Double> e: values.entrySet()) {
			text += e.getKey() + ":" + e.getValue() + "\n";
		}
		return(text);
	}
	
}
