package ch.ww.electronics.game.gameobject;

import org.json.JSONObject;

public abstract class GameObjectConstructor<E extends GameObject> {
	public abstract E createInstance(JSONObject jsonObject);
}