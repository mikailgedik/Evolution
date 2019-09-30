package ch.ww.electronics.level.backgroundtile;

import java.util.LinkedHashMap;
import java.util.Objects;

import ch.ww.electronics.game.level.Level;
import ch.ww.electronics.graphics.Screen;

public class BackgroundTileMetaData {
	private final BackgroundTile type;
	private final int x, y;
	private final Level level;
	private int health;
	private final LinkedHashMap<String, Object> metaData;

	public BackgroundTileMetaData(BackgroundTile type, Level level, int x, int y) {
		this(type, level, x, y, BackgroundTile.DEFAULT_HEALTH);
	}

	public BackgroundTileMetaData(BackgroundTile type, Level level, int x, int y, int health) {
		Objects.requireNonNull(type, "type == null");
		assert health != 0;
		this.metaData = new LinkedHashMap<>();
		this.type = type;
		this.level = level;
		this.x = x;
		this.y = y;
		this.health = health;
	}

	public Object getInfo(String name) {
		switch (name) {
		case "type":
			return getType();
		case "x":
			return getX();
		case "y":
			return getY();
		case "level":
			return getLevel();
		case "health":
			return getHealth();
		default:
			if (!metaData.containsKey(name)) {
				throw new RuntimeException("No key with name " + name);
			}
			return metaData.get(name);
		}
	}

	public boolean hasInfo(String name) {
		return metaData.containsKey(name);
	}

	public BackgroundTileMetaData putInfo(String name, Object value) {
		switch (name) {
		case "type":
			throw new RuntimeException("Cannot set type");
		case "x":
			throw new RuntimeException("Cannot set x");
		case "y":
			throw new RuntimeException("Cannot set y");
		case "level":
			throw new RuntimeException("Cannot set level");
		case "health":
			setHealth((int) value);
			break;
		default:
			metaData.put(name, value);
		}
		return this;
	}

	public BackgroundTile getType() {
		return type;
	}

	public Screen getScreenToRender() {
		return type.getScreenToRender(this);
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public Level getLevel() {
		return level;
	}

	public int getHealth() {
		return health;
	}

	public int setHealth(int health) {
		if (getType() == BackgroundTile.BACKGROUND_DIRT) {
			return 0;
		}
		int ret = this.health - health;
		this.health = health;
		if (ret < 0) {
			this.health = 0;
			return -ret;
		}
		return 0;
	}

	public void tick() {
		getType().tick(this);
	}

	public int getMaxHealth() {
		return getType().getMaxHealth();
	}
}