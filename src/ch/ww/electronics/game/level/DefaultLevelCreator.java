package ch.ww.electronics.game.level;

import java.util.Random;

import ch.ww.electronics.level.backgroundtile.BackgroundTile;
import ch.ww.electronics.level.backgroundtile.BackgroundTileMetaData;
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
				level.setBackgroundTile(BackgroundTile.BACKGROUND_DIRT.createBackgroundTileMetaData(level, x, y));
			}
		}

		stoneBouldersCount = (level.getLevelWidth() + level.getLevelHeight()) / 10;
		System.out.println("Created " + createStoneBoulders() + " stoneboulders");
	}

	private int createStoneBoulders() {
		int radius = 6, max = radius * radius;
		int count = this.stoneBouldersCount;

		int[] center;
		for (int circle = 0; circle < count; circle++) {
			center = new int[] { getRandom().nextInt(level.getLevelWidth()),
					getRandom().nextInt(level.getLevelHeight()) };

			for (Integer[] loc : CoordinatesCreator.createFilledCircle(center[0], center[1], radius)) {
				if (loc[0] == center[0] && loc[1] == center[1]) {
					//TODO
					setBackgroundTile(loc[0], loc[1], BackgroundTile.BACKGROUND_STONE);
				} else if ((getRandom().nextInt(max) > loc[2])) {
					setBackgroundTile(loc[0], loc[1], BackgroundTile.BACKGROUND_STONE);
				}
			}
		}

		return count;
	}

	public void setBackgroundTile(int x, int y, BackgroundTile type) {
		setBackgroundTile(type.createBackgroundTileMetaData(level, x, y));
	}

	public void setBackgroundTile(BackgroundTileMetaData d) {
		Level l = d.getLevel();
		if (d.getX() < 0 || d.getX() >= l.getLevelWidth() || d.getY() < 0 || d.getY() >= l.getLevelHeight()) {
			return;
		} else {
			l.setBackgroundTile(d);
		}
	}
}