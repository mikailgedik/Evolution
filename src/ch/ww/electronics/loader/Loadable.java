package ch.ww.electronics.loader;

import ch.ww.electronics.game.Game;

public interface Loadable<T> {
	public String getName();
	
	public void load(Game game);
	
	public T getLoaded();
}