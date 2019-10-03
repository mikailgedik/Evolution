package ch.ww.electronics.level.backgroundtile;

import ch.ww.electronics.graphics.Screen;
import ch.ww.electronics.graphics.TextureManager;

public class BackgroundTileStone extends BackgroundTile {
	private Screen screen;
	public static final String NAME = "backgroundTile.backgroundTileStone";

	public BackgroundTileStone(int x, int y) {
		super(x, y);
		screen = TextureManager.getInstance().getTexture(NAME);
	}

	@Override
	public Screen getScreenToRender() {
		return screen;
	}
}