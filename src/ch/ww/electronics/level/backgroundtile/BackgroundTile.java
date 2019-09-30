package ch.ww.electronics.level.backgroundtile;

import ch.ww.electronics.game.level.Level;
import ch.ww.electronics.graphics.Screen;

public abstract class BackgroundTile {
	public static final int SIZE = Level.FIELD_SIZE;

	public static final BackgroundTileDirt BACKGROUND_DIRT = BackgroundTileDirt.INSTANCE;
	public static final BackgroundTileStone BACKGROUND_STONE = BackgroundTileStone.INSTANCE;

	public static final int DEFAULT_HEALTH = 64;

	protected abstract Screen getScreenToRender(BackgroundTileMetaData backgroundTileMetaData);

	protected Screen getProcessedScreen(Screen s, BackgroundTileMetaData b) {
		if (b.getHealth() == b.getMaxHealth()) {
			return s;
		} else {
			s = s.copy();
			double factor = 1.0 * b.getHealth() / b.getMaxHealth();
			s.darkScreen(factor);
			return s;
		}
	}

	public int getMaxHealth() {
		return DEFAULT_HEALTH;
	}

	public abstract void tick(BackgroundTileMetaData backgroundTileMetaData);

	public abstract BackgroundTileMetaData createBackgroundTileMetaData(Level level, int x, int y);
}