package ch.ww.electronics.loader;

import java.util.ArrayList;

import ch.ww.electronics.graphics.Texture;
import ch.ww.electronics.graphics.TextureGroup;
import ch.ww.electronics.graphics.TextureManager;

public class TextureLoader implements ResourceLoader<Texture> {
	private int index = 0;
	private boolean loaded = false; // Do not delete!
	private final ArrayList<Texture> textures;
	private final TextureManager manager;

	public TextureLoader(TextureManager manager) {
		textures = new ArrayList<>();
		this.manager = manager;
		updateTextures();
	}

	public void updateTextures() {
		textures.clear();
		textures.addAll(manager.getAllTextures());
		for (TextureGroup group : manager.getAllGroups()) {
			for (Texture i : group.getTextures()) {
				textures.add(i);
			}
		}
	}

	@Override
	public boolean hasLoaded() {
		return loaded;
	}

	@Override
	public boolean hasNotLoaded() {
		return index == 0;
	}

	@Override
	public boolean hasNext() {
		return !loaded;
	}

	@Override
	public void loadNext() {
		if (index == 0) {
			updateTextures();
		}
		if (hasLoaded()) {
			throw new RuntimeException("Already loaded!");
		}
		textures.get(index).load(null);
		index++;
		if (index == textures.size()) {
			loaded = true;
		}
	}

	public int index() {
		return index;
	}

	@Override
	public Texture getNext() {
		return textures.get(index);
	}

	@Override
	public int totalCount() {
		return textures.size();
	}
}