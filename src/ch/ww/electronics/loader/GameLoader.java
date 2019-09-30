package ch.ww.electronics.loader;

import java.util.ArrayList;

import ch.ww.electronics.game.Game;
import ch.ww.electronics.graphics.TextureManager;
import ch.ww.electronics.level.backgroundtile.BackgroundTileDirt;
import ch.ww.electronics.level.backgroundtile.BackgroundTileStone;
import ch.ww.electronics.main.GameClass;

public class GameLoader implements ResourceLoader<Loadable<?>> {
	private final ArrayList<ResourceLoader<? extends Loadable<?>>> loaders;
	private int index;
	private final int count;
	private final Game game;
	private int amountLoaded = 0;

	public GameLoader(Game game) {
		this.game = game;
		loaders = new ArrayList<>();
		prepareLoad();

		int c = 0;
		for (ResourceLoader<?> rs : loaders) {
			c += rs.totalCount();
		}
		count = c;
	}

	private void prepareLoad() {
		// TODO add things to load here
		prepareTextures();
	}

	private void prepareTextures() {
		TextureManager tm = TextureManager.getInstance();
		
		// BackgroundTile
		tm.addTexture(GameClass.class.getResource("/res/textures/backgroundtile/BackgroundTileDirt.png"),
				BackgroundTileDirt.NAME);
		tm.addTexture(GameClass.class.getResource("/res/textures/backgroundtile/BackgroundTileStone.png"),
				BackgroundTileStone.NAME);

		loaders.add(tm.getLoader());

	}

	public Game getGame() {
		return game;
	}

	@Override
	public boolean hasNext() {
		return !hasLoaded();
	}

	@Override
	public Loadable<?> getNext() {
		if (!this.loaders.get(index).hasNext()) {
			index++;
		}
		if (hasLoaded()) {
			throw new RuntimeException("Already loaded!");
		}
		return this.loaders.get(index).getNext();
	}

	@Override
	public void loadNext() {
		if (!this.loaders.get(index).hasNext()) {
			index++;
		}
		if (hasLoaded()) {
			throw new RuntimeException("Already loaded!");
		}
		this.loaders.get(index).loadNext();
		this.amountLoaded++;
	}

	@Override
	public boolean hasLoaded() {
		return amountLoaded == totalCount();
	}

	@Override
	public boolean hasNotLoaded() {
		return amountLoaded == 0;
	}

	@Override
	public int totalCount() {
		return this.count;
	}
}
