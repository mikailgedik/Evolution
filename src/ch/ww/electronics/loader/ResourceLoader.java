package ch.ww.electronics.loader;

public interface ResourceLoader<T> {
	public default void loadAll() {
		while (hasNext()) {
			loadNext();
		}
	}

	public boolean hasNext();

	public T getNext();

	public void loadNext();

	public int totalCount();

	public boolean hasLoaded();
	
	public boolean hasNotLoaded();
}