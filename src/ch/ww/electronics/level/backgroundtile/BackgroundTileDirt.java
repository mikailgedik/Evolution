package ch.ww.electronics.level.backgroundtile;

import ch.ww.electronics.game.level.Level;
import ch.ww.electronics.graphics.Screen;
import ch.ww.electronics.graphics.TextureManager;

public class BackgroundTileDirt extends BackgroundTile {
	private Screen screen;
	public static final String NAME = "backgroundTile.backgroundTileDirt";
	public static final BackgroundTileDirt INSTANCE = new BackgroundTileDirt();
	
	private BackgroundTileDirt() {
		this.screen = TextureManager.getInstance().getTexture(NAME);
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