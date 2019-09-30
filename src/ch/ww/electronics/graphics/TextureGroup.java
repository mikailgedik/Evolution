package ch.ww.electronics.graphics;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class TextureGroup {
	private final String groupName;
	private final Texture[] textures;
	
	public TextureGroup(String groupName, URL jsonFile) {
		Objects.requireNonNull(groupName, "groupName == null");
		Objects.requireNonNull(jsonFile, "jsonFile == null");
		try {
			this.groupName = groupName;
			JSONObject initialObject = new JSONObject(new JSONTokener(jsonFile.openStream()));
			JSONArray images = initialObject.getJSONArray("images");
			textures = new Texture[images.length()];
			load(images);
		} catch (JSONException e) {
			throw new RuntimeException("invalid json-file", e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public TextureGroup(String groupName, JSONObject object) {
		Objects.requireNonNull(groupName, "groupName == null");
		Objects.requireNonNull(object, "object == null");
		try {
			this.groupName = groupName;
			JSONArray images = object.getJSONArray("images");
			textures = new Texture[images.length()];
			load(images);
		} catch (JSONException e) {
			throw new RuntimeException("invalid json-file", e);
		}
	}
	
	private final void load(JSONArray images) {
		JSONObject currentImage;
		for (int i = 0; i < textures.length; i++) {
			currentImage = images.getJSONObject(i);
			textures[i] = new Texture(TextureManager.class.getResource(currentImage.getString("url")),
					groupName + "." + i, this);
		}
	}
	
	public String getGroupName() {
		return groupName;
	}
	
	public Texture[] getTextures() {
		return textures;
	}
}