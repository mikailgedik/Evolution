package ch.ww.electronics.level.backgroundtile;

import ch.ww.electronics.game.level.Level;
import ch.ww.electronics.graphics.Screen;
import ch.ww.electronics.graphics.TextureManager;

public class BackgroundTileStone extends BackgroundTile {
	public static final String NAME = "backgroundTile.backgroundTileStone";
	public static final BackgroundTileStone INSTANCE = new BackgroundTileStone();
	private Screen screen;

	private BackgroundTileStone() {
		screen = TextureManager.getInstance().getTexture(NAME);
	}

	@Override
	public Screen getScreenToRender(BackgroundTileMetaData b) {
		return getProcessedScreen(screen, b);
	}

	@Override
	public void tick(BackgroundTileMetaData bgtmd) {
	}

	@Override
	public BackgroundTileMetaData createBackgroundTileMetaData(Level level, int x, int y) {
		return new BackgroundTileMetaData(INSTANCE, level, x, y);
	}
}