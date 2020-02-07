package ch.ww.electronics.game.level;

import java.util.ArrayList;
import java.util.Random;

import ch.ww.electronics.level.backgroundtile.BackgroundTile;
import ch.ww.electronics.level.backgroundtile.BackgroundTileDirt;
import ch.ww.electronics.util.CoordinatesCreator;

public class DefaultLevelCreator implements LevelCreator {
	private final Random random;
	private Level level;

	public DefaultLevelCreator(long seed) {
		random = new Random(seed);
	}

	public DefaultLevelCreator() {
		random = new Random();
	}

	public DefaultLevelCreator(Random random) {
		this.random = random;
	}

	public Random getRandom() {
		return random;
	}

	public Level getLevel() {
		return level;
	}

	@Override
	public void createLevel(Level level) {
		this.level = level;
		System.out.println("Creating background...");
		for (int x = 0; x < level.getLevelWidth(); x++) {
			for (int y = 0; y < level.getLevelHeight(); y++) {
				level.setBackgroundTile(new BackgroundTileDirt(x, y));
			}
		}
		createHeatMap();
	}

	private void createHeatMap() {
		final int CONCENTRAITION_POINTS = 2;
		final int factor = 10;
		int amount = level.getLevelWidth() * level.getLevelHeight() / (CONCENTRAITION_POINTS * factor);
		int[][] heatpoint = new int[amount][2];
		double[] temperature = new double[amount];
		/*
		for(int i = 0; i < amount; i++) {
			heatpoint[i] = new int[] {(int) (random.nextDouble() * level.getLevelWidth()), (int) (random.nextDouble() * level.getLevelHeight())};
			temperature[i] = random.nextDouble();
		}
		*/
		heatpoint[0] = new int[] {0,0};
		heatpoint[1] = new int[] {199,199};
		
		temperature[0] = 0;
		temperature[1] = 1;
		
		for(int i = 0; i < CONCENTRAITION_POINTS; i++) {
			double t = random.nextDouble() * 0.9 + 0.1;
			int[] pos = new int[] {(int) (random.nextDouble() * level.getLevelWidth()), (int) (random.nextDouble() * level.getLevelHeight())};
			
			for(int y = i * CONCENTRAITION_POINTS * factor; y < (i + 1) * CONCENTRAITION_POINTS * factor; y++) {
				heatpoint[i] = new int[] {(int) (pos[0] + random.nextDouble() * level.getLevelWidth() / CONCENTRAITION_POINTS - (level.getLevelWidth() / CONCENTRAITION_POINTS / 2)),
						(int) (pos[1] + random.nextDouble() * level.getLevelHeight() / CONCENTRAITION_POINTS - (level.getLevelWidth() / CONCENTRAITION_POINTS / 2))};
				temperature[i] = t + (1 - 2 * random.nextDouble()) * 0.1;
			}
		}
		
		for(int y = 0; y < level.getLevelHeight(); y++) {
			for(int x = 0; x < level.getLevelWidth(); x++) {
				double dist = 0;
				double[] first = new double[2];
				double[] second = new double[2];
				
				dist = Math.sqrt((heatpoint[0][0] - x) * (heatpoint[0][0] - x) + (heatpoint[0][1] - y) * (heatpoint[0][1] - y));
				first[0] = dist;
				first[1] = temperature[0];
				
				dist = Math.sqrt((heatpoint[1][0] - x) * (heatpoint[1][0] - x) + (heatpoint[1][1] - y) * (heatpoint[1][1] - y));
				second[0] = dist;
				second[1] = temperature[1];
				
				for(int i = 0; i < amount; i++) {
					dist = Math.sqrt((heatpoint[i][0] - x) * (heatpoint[i][0] - x) + (heatpoint[i][1] - y) * (heatpoint[i][1] - y));
					if(dist < second[0]) {
						second[0] = dist;
						second[1] = temperature[i];
						if(dist < first[0]) {
							double[] temp = new double[] {first[0], first[1]};
							first = second;
							second = temp;
						}
					}
				}
				double temp = (first[0] * first[1]) / (first[0] + second[0]) + (second[0] * second[1]) / (first[0] + second[0]);
				getBackgroundTile(x, y).setTemperature(temp);
			}
		}
	}
	
	public BackgroundTile getBackgroundTile(int x, int y) {
		return level.getBackgroundTile(x, y);
	}
	
	public void setBackgroundTile(BackgroundTile d) {
		if (d.getX() < 0 || d.getX() >= level.getLevelWidth() || d.getY() < 0 || d.getY() >= level.getLevelHeight()) {
			return;
		} else {
			level.setBackgroundTile(d);
		}
	}
}