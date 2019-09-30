package ch.ww.electronics.graphics;

import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;

import ch.ww.electronics.loader.TextureLoader;

public class TextureManager {
	private static final TextureManager instance = new TextureManager();

	private final ArrayList<Texture> textures;
	private final ArrayList<TextureGroup> groups;

	private final TextureLoader loader;

	private TextureManager() {
		textures = new ArrayList<>();
		groups = new ArrayList<>();
		loader = new TextureLoader(this);
		fillList();
	}

	private final void fillList() {
		textures.add(new Texture(TextureManager.class.getResource("/res/textures/font/DefaultFont.png"), "font", null));
		textures.get(0).load(null);// Force to load the font
	}

	public void addTexture(URL url, String name) {
		Objects.requireNonNull(url, "url == null");
		Objects.requireNonNull(name, "url == name");
		for (Texture i : textures) {
			if (i.getName().equals(name)) {
				throw new RuntimeException("name " + name + " does already exists");
			}
		}
		Texture t = new Texture(url, name, null);
		if (getLoader().hasLoaded()) {
			// Force to load texture now without listener!
			System.out.println("Will load texture '" + name + "' now");
			t.load(null);
		}
		textures.add(t);
		this.loader.updateTextures();
	}

	public void addGroup(URL url, String groupName) {
		for (TextureGroup i : this.groups) {
			if (i.getGroupName().equals(i.getGroupName())) {
				throw new RuntimeException("group " + groupName + " does already exists");
			}
		}
		TextureGroup i = new TextureGroup(groupName, url);
		if (getLoader().hasLoaded()) {
			System.out.println("Will load group '" + groupName + "' now");
			for (Texture single : i.getTextures()) {
				single.load(null);
			}
		}
		groups.add(i);
		this.loader.updateTextures();
	}

	public boolean removeTexture(String name) {
		boolean b = textures.removeIf((Texture i) -> {
			return i.getName().equals(name);
		});
		if(b) {
			this.loader.updateTextures();
		}
		return b;
	}

	public TextureLoader getLoader() {
		return loader;
	}

	public static TextureManager getInstance() {
		return instance;
	}

	public Screen[] getTextures(String group) {
		for (TextureGroup textureGroup : this.groups) {
			if (!textureGroup.getGroupName().equals(group)) {
				continue;
			}
			Screen[] screen = new Screen[textureGroup.getTextures().length];
			for (int i = 0; i < screen.length; i++) {
				screen[i] = textureGroup.getTextures()[i].getScreen();
			}
			return screen;
		}
		throw new RuntimeException("group '" + group + "' doesn't exist");
	}

	public Screen getTexture(String name) {
		for (int i = 0; i < this.textures.size(); i++) {
			if (textures.get(i).getName().equals(name)) {
				if (textures.get(i).getScreen() != null) {
					return textures.get(i).getScreen();
				} else {
					throw new RuntimeException("Not loaded yet");
				}
			}
		}
		throw new RuntimeException("name '" + name + "' doesn't exist");
	}

	public ArrayList<Texture> getAllTextures() {
		return this.textures;
	}

	public ArrayList<TextureGroup> getAllGroups() {
		return groups;
	}
}