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
	public static final String BABY_WHEN_ENERGY = "babieWhenEnergy";
	
	private HashMap<String, Double> values;
	

	
	private static final HashMap <String, Double[]> specifications;
	static {
		//0: minimum
		//1: maximum
		//2: variation
		specifications = new HashMap<>();
		specifications.put(SIZE, new Double[]{0.2d, 1d, 0.1d});
		specifications.put(FUR, new Double[]{0d,1d,0.05d});
		specifications.put(MAX_SPEED, new Double[]{0.05d,0.1d,0.01});
		specifications.put(VIEWRANGE, new Double[]{5d,10d, 0.5});
		specifications.put(MAX_ENERGY, new Double[]{100d,5000d, 10d});
		specifications.put(STUNNED_TIME, new Double[]{.05d, .1d, 0.1d});
		specifications.put(RUNNING_TIME, new Double[]{.05d, .1d, 0.1d});
		specifications.put(START_SEARCHING_FOOD, new Double[]{0.6d, 1d, 0.05d});
		specifications.put(BABY_WHEN_ENERGY, new Double[]{0d, 1d , 0.05d});
	}
	
	public DNA(Animal animal, double size, double fur, double maxSpeed, double viewrange,
			double viewangle, double maxEnergy, double stunnedTime, double runningTime,
			double startSearchingFood, double babyWhenEnergy) {
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
		values.put(BABY_WHEN_ENERGY, babyWhenEnergy);
		validate();
	}
	
	public DNA(Animal animal){
		this(animal, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		randomize();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public DNA clone() {
		DNA dna = new DNA(this.animal);
		dna.values = (HashMap<String, Double>) this.values.clone();
		validate();
		return dna;
	}
	
	public void randomize() {		
		values.forEach((String key, Double value) -> {
			values.put(key, (1 - 2 * animal.getLevel().getRandom().nextDouble())*(specifications.get(key)[1]-specifications.get(key)[0]) + specifications.get(key)[0]);
		});
		validate();
	}

	public void variate(double radiation) {
		for(Entry <String, Double>e: values.entrySet()) {
			double val = values.get(e.getKey());
			val += (radiation) * ((1 - 2 *animal.getLevel().getRandom().nextDouble()) * specifications.get(e.getKey())[2]);
			values.put(e.getKey(), val);
		}
		validate();
	}
	
	private void validate(){
		for(Entry <String, Double> e: values.entrySet()){
			if(e.getValue()<specifications.get(e.getKey())[0]) {
				values.put(e.getKey(), specifications.get(e.getKey())[0]);
			}
			if(e.getValue()>specifications.get(e.getKey())[1]) {
				values.put(e.getKey(), specifications.get(e.getKey())[1]);
			}
		}
	}
	
	public double get(String key){
		return values.get(key);
	}
	
//	public void set(String key, Double value){
//		if(values.put(key, value)==null) throw new RuntimeException("Not a valid key");
//	}
	
	public HashMap<String, Double> getValues() {
		return values;
	}
	
	public Animal getAnimal() {
		return animal;
	}
	
	public void setAnimal(Animal animal) {
		this.animal = animal;
	}
	
	public String getNiceText(){
		String text="";
		for(Entry<String, Double> e: values.entrySet()) {
			text += e.getKey() + ":" + e.getValue() + "\n";
		}
		return(text);
	}
	
}
