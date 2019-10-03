package ch.ww.electronics.game.level;

import java.util.ArrayList;
import java.util.Random;

import ch.ww.electronics.level.backgroundtile.BackgroundTile;
import ch.ww.electronics.level.backgroundtile.BackgroundTileDirt;
import ch.ww.electronics.level.backgroundtile.BackgroundTileStone;
import ch.ww.electronics.util.CoordinatesCreator;

public class DefaultLevelCreator implements LevelCreator {
	private final Random random;
	private Level level;

	private int stoneBouldersCount;

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

		stoneBouldersCount = (level.getLevelWidth() + level.getLevelHeight()) / 10;
		System.out.println("Created " + createStoneBoulders() + " stoneboulders");
		
		//createHeatMap();
	}

	private int createStoneBoulders() {
		int radius = 6, max = radius * radius;
		int count = this.stoneBouldersCount;

		int[] center;
		for (int circle = 0; circle < count; circle++) {
			center = new int[] { getRandom().nextInt(level.getLevelWidth()),
					getRandom().nextInt(level.getLevelHeight()) };

			for (Integer[] loc : CoordinatesCreator.createFilledCircle(center[0], center[1], radius)) {
				if ((getRandom().nextInt(max) > loc[2])) {
					setBackgroundTile(new BackgroundTileStone(loc[0], loc[1]));
				}
			}
		}

		return count;
	}

	private void createHeatMap() {
		int gridsize = 20;
		int[][] coordinates = new int[(level.getLevelWidth() / gridsize) * (level.getLevelHeight() / gridsize)][2];

		for(int y = 0; y < level.getLevelHeight(); y += gridsize) {
			for(int x = 0; x < level.getLevelWidth()/gridsize; x += gridsize) {
				level.getBackgroundTile(x, y).setTemperature(getRandom().nextDouble());
				coordinates[(x/gridsize) + (y/gridsize* (level.getLevelWidth()/gridsize))][0] = x;
				coordinates[(x/gridsize) + (y/gridsize* level.getLevelWidth()/gridsize)][1] = y;
			}
		}
		
		for(int[] center: coordinates) {
			final double temp =  getBackgroundTile(center[0], center[1]).getTemperature();
			double t = temp;
			ArrayList<Integer[]> circle = CoordinatesCreator.createFilledCircle(center[0], center[1], (int)(gridsize));
			for(Integer[] c: circle) {
				t = temp + (getRandom().nextInt(2) * 2 -1)  * getRandom().nextDouble() * Math.exp(c[2]/(gridsize* gridsize));
				getBackgroundTile(c[0], c[1]).setTemperature(t);
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