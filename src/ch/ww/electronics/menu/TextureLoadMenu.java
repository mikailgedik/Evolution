package ch.ww.electronics.menu;

import ch.ww.electronics.game.Game;
import ch.ww.electronics.loader.GameLoader;
import ch.ww.electronics.loader.Loadable;

public class TextureLoadMenu extends Menu {
	private final LoadingBar bar;
	private final GameLoader loader;
	private final LoadEnd end;
	private int endTimer = 0;

	public TextureLoadMenu(Game game, Menu parentMenu, LoadEnd end) {
		super(game, parentMenu);
		this.loader = getGame().getGameLoader();
		this.end = end;

		// TODO add here things to load
		int totalCount = loader.totalCount();

		bar = new LoadingBar(totalCount, (getWidth() - 20) / totalCount, 100);
		bar.setY(getHeight() / 2 - bar.getHeight() / 2);
		bar.setX(10);
		addMenuComponent(bar);
	}

	@Override
	public void tick() {
		if (loader.hasNext()) {
			Loadable<?> loaded = loader.getNext();
			System.out.println("Loading: " + loaded.getName());
			bar.setText(loaded.getName());
			loader.loadNext();
			bar.setValue(bar.getValue() + 1);
		} else {
			if (endTimer == 30) {
				close();
				end.act();
			} else {
				endTimer++;
			}
		}
	}

	public static interface LoadEnd {
		public void act();
	}
}