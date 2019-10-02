package ch.ww.electronics.level.backgroundtile;

import ch.ww.electronics.graphics.Screen;
import ch.ww.electronics.graphics.TextureManager;

public class BackgroundTileStone extends BackgroundTile {
	public static final String NAME = "backgroundTile.backgroundTileStone";
	private Screen screen;

	public BackgroundTileStone(int x, int y) {
		super(0, x, y);
		screen = TextureManager.getInstance().getTexture(NAME);
	}

	@Override
	public Screen getScreenToRender() {
		return screen;
	}
}