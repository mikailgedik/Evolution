package ch.ww.electronics.game;

import java.util.HashMap;
import java.util.Map.Entry;

import ch.ww.electronics.game.gameobject.Animal;
import ch.ww.electronics.game.gameobject.DNA;
import ch.ww.electronics.game.gameobject.GameObject;
import ch.ww.electronics.game.level.Level;

public class Statistic {
	private HashMap<Long, HashMap<String, Double>> info;
	private long lastTick;
	public Statistic() {
		info = new HashMap<>();
	}
	
	public void addStatistic(Level l, long tick) {
		lastTick = tick > lastTick ? tick : lastTick;
		HashMap<String, Double> average = new HashMap<>();
		int count = 0;
		for(GameObject o: l.getObjectsInLevel()) {
			if(o instanceof Animal) {
				DNA dna = ((Animal)o).getDNA();
				for(Entry<String, Double> entry: dna.getValues().entrySet()) {
					average.put(entry.getKey(), entry.getValue() + average.getOrDefault(entry.getKey(), 0d));
				}
				count++;
			}
		}
		
		final int c = count;
		
		average.replaceAll((key, s) -> {return s/c;});
		
		info.put(tick, average);
	}
	
	public HashMap<Long, HashMap<String, Double>> getInfo() {
		return info;
	}
	
	public long getLastTick() {
		return lastTick;
	}
	
}
