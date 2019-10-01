package ch.ww.electronics.game.gameobject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;

import org.json.JSONObject;

import ch.ww.electronics.exception.IllegalLocationException;
import ch.ww.electronics.game.Game;
import ch.ww.electronics.game.level.Level;
import ch.ww.electronics.graphics.Screen;

public abstract class GameObject {
	private static final HashMap<String, GameObjectConstructor<? extends GameObject>> allTypes = new HashMap<>();

	public static void addTypeInDirectory(String name, GameObjectConstructor<? extends GameObject> goc) {
		allTypes.put(Objects.requireNonNull(name, "name == null"), Objects.requireNonNull(goc, "goc == null"));
	}

	public static HashMap<String, GameObjectConstructor<? extends GameObject>> getAllTypes() {
		return allTypes;
	}

	/**
	 * If equals <code>null</code>, then this {@link GameObject} has no world.
	 */
	private Level level;
	private double x, y;
	private Screen texture;
	private boolean canGetOutOfWorldBounds = false;

	/**
	 * Objects are automatically added into the level if the level is not
	 * <code>null</code>.
	 */
	public GameObject(Level level, double x, double y) {
		this.level = level;
		this.x = x;
		this.y = y;
		if (level != null) {
			level.addGameObject(this);
		}

		if (!allTypes.containsValue(getGameObjectConstructor())) {
			System.out.println("Type is not in allTypes! Fix bug!");
			addTypeInDirectory(getName(), getGameObjectConstructor());
		}

	}

	public void setCanGetOutOfWorldBounds(boolean canGetOutOfWorldBounds) {
		this.canGetOutOfWorldBounds = canGetOutOfWorldBounds;
	}

	public void setLocation(Level newLevel, double x, double y) {
		if (this.level != newLevel) {
			if (this.level != null) {
				this.level.removeGameObject(this);
			}
			if (newLevel != null) {
				newLevel.addGameObject(this);
			}
			this.level = newLevel;
		}
		setX(x);
		setY(y);
		repairCooridnates(canGetOutOfWorldBounds);
	}

	private void repairCooridnates(boolean canGetOutOfWorldBounds) {
		if (!canGetOutOfWorldBounds) {
			if (x < 0)
				x = 0;
			if (x + getWidth() >= level.getLevelWidth())
				x = level.getLevelWidth() - 1;
			if (y < 0)
				y = 0;
			if (y + getHeight() >= level.getLevelHeight())
				y = level.getLevelHeight() - 1;
		}
	}

	public boolean isTouching(GameObject o) {
		double oX = o.getX(), oY = o.getY();

		for (int i = 0; i < 2; i++) {
			if (this.x <= oX && this.y <= oY) {
				if (this.x + getWidth() >= oX && this.y + getHeight() >= oY) {
					return true;
				}
			}
			oX += o.getWidth();
			oY += o.getHeight();
		}

		return false;
	}

	public void setLocation(double x, double y) {
		setLocation(level, x, y);
	}

	public double getX() {
		checkIsInLevel();
		return x;
	}

	public void setX(double x) {
		checkIsInLevel();
		x = Math.round(x * 1000) / 1000.0;
		if(x<level.getLevelWidth()&x>0)
			this.x = x;
	}

	public double getY() {
		checkIsInLevel();
		return y;
	}

	public void setY(double y) {
		checkIsInLevel();
		y = Math.round(y * 1000) / 1000.0;
		this.y = y;
	}

	public Level getLevel() {
		checkIsInLevel();
		return level;
	}

	public Game getGame() {
		return getLevel().getGame();
	}

	public void setLevel(Level level) {
		this.level = level;
	}

	public Screen getTexture() {
		return texture;
	}

	public void setTexture(Screen texture) {
		Objects.requireNonNull(texture, "texture == null");
		this.texture = texture;
	}

	public double getWidth() {
		return 1;
	}

	public double getHeight() {
		return 1;
	}

	private void checkIsInLevel() throws IllegalLocationException {
		if (!isInLevel()) {
			throw new IllegalLocationException("not in a level", null, this);
		}
	}

	public boolean isInLevel() {
		return level != null;// Don't use getter for level; ->
								// StackOverflowError
	}

	public ArrayList<GameObject> getIntersectingGameObjects() {
		return getIntersectingGameObjects(GameObject.class);
	}

	@SuppressWarnings("unchecked")
	public <E> ArrayList<E> getIntersectingGameObjects(Class<E> clzz) {
		ArrayList<E> ret = new ArrayList<>();
		for (GameObject o : getLevel().getObjectsInLevel()) {
			if (o == this) {
				continue;
			}
			if (clzz.isInstance(o)) {
				if (this.isTouching(o)) {
					ret.add((E) o);
				}
			}
		}
		return ret;
	}

	public abstract void tick();

	public abstract GameObjectConstructor<? extends GameObject> getGameObjectConstructor();

	public abstract String getName();

	public Random getRandom() {
		return getLevel().getRandom();
	}

	public static GameObject createInstance(JSONObject jsonObject) {
		Objects.requireNonNull(jsonObject, "jsonObject == null");
		String name = jsonObject.getString("name");
		if (!allTypes.containsKey(name)) {
			throw new RuntimeException("allTypes doesn't contain the key \"" + name + "\"");
		}
		return allTypes.get(name).createInstance(jsonObject);
	}

	public double distanceTo(GameObject o) {
		double dx = o.getX() - this.getX(), dy = o.getY() - this.getY();
		return Math.sqrt(dx * dx + dy * dy);
	}
}