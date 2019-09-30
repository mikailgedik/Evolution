package ch.ww.electronics.graphics;

import java.net.URL;
import java.util.Objects;

import ch.ww.electronics.game.Game;
import ch.ww.electronics.loader.Loadable;

public class Texture implements Loadable<Screen> {
	private final URL url;
	private final String name;
	private Screen screen;
	private final TextureGroup group;

	public Texture(URL url, String name, TextureGroup group) {
		Objects.requireNonNull(url, "url == null");
		Objects.requireNonNull(name, "name == null");
		this.group = group; // May null
		this.url = url;
		this.name = name;
	}

	public URL getUrl() {
		return url;
	}

	@Override
	public String getName() {
		return name;
	}

	public Screen getScreen() {
		return screen;
	}

	public void setScreen(Screen screen) {
		this.screen = screen;
	}

	public TextureGroup getGroup() {
		return group;
	}

	@Override
	public void load(Game game) {
		setScreen(Screen.fromURL(url));
	}

	@Override
	public Screen getLoaded() {
		return this.getScreen();
	}
}