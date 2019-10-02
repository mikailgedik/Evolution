package ch.ww.electronics.level.backgroundtile;

import ch.ww.electronics.graphics.Screen;
import ch.ww.electronics.graphics.TextureManager;

public class BackgroundTileDirt extends BackgroundTile {
	private Screen screen;
	public static final String NAME = "backgroundTile.backgroundTileDirt";
	
	public BackgroundTileDirt(int x, int y) {
		super(0, x, y);
		this.screen = TextureManager.getInstance().getTexture(NAME);
	}
	
	@Override
	public Screen getScreenToRender() {
		return screen;
	}
}