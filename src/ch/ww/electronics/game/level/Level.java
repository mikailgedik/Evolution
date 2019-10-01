package ch.ww.electronics.game.level;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

import ch.ww.electronics.game.Game;
import ch.ww.electronics.game.gameobject.GameObject;
import ch.ww.electronics.graphics.Screen;
import ch.ww.electronics.level.backgroundtile.BackgroundTile;
import ch.ww.electronics.level.backgroundtile.BackgroundTileMetaData;
import ch.ww.electronics.listener.GameListener;

public abstract class Level {
	public static final int FIELD_SIZE = 32;

	private final Game game;
	private final int levelWidth, levelHeight;
	private final Screen screen;
	private final ArrayList<GameObject> objects;
	private final BackgroundTileMetaData[] backgroundTile;
	private final LevelCreator levelCreator;
	private double viewX, viewY;

	public Level(Game game, int levelWidth, int levelHeight) {
		this.game = game;
		this.screen = new Screen(game.getWidth(), game.getHeight());

		this.levelWidth = levelWidth;
		this.levelHeight = levelHeight;

		objects = new ArrayList<>();

		this.backgroundTile = new BackgroundTileMetaData[levelWidth * levelHeight];

		this.levelCreator = new DefaultLevelCreator(game.getRandom());
		this.levelCreator.createLevel(this);
		
		viewX = getLevelWidth() / 2;
		viewY = getLevelHeight() / 2;
	}

	public BackgroundTileMetaData[] getBackgroundTiles() {
		return backgroundTile;
	}

	public BackgroundTileMetaData getBackgroundTile(int x, int y) {
		if (x > -1 && x < getLevelWidth() && y > -1 && y < getLevelHeight()) {
			int loc = x + y * getLevelWidth();
			if (backgroundTile[loc] == null) {
				backgroundTile[loc] = BackgroundTile.BACKGROUND_DIRT.createBackgroundTileMetaData(this, x, y);
			}
			return backgroundTile[loc];
		} else {
			return null;
		}
	}

	public BackgroundTile getBackgroundTileType(int x, int y) {
		if (!coordinatesInLevel(x, y)) {
			return null;
		} else {
			return getBackgroundTile(x, y).getType();
		}
	}

	public abstract void onStart();

	public synchronized void removeGameObject(GameObject gameObject) {
		objects.remove(gameObject);
	}

	public synchronized void addGameObject(GameObject gameObject) {
		Objects.requireNonNull(gameObject, "gameObject == null");
		if (!objects.contains(gameObject)) {
			objects.add(gameObject);
		}
	}

	public GameObject getObjectAtSpot(double xPoint, double yPoint) {
		double x, y;
		for (GameObject o : objects) {
			x = o.getX();
			y = o.getY();
			if (x >= xPoint && x < xPoint + 1 && y >= yPoint && y < yPoint + 1) {
				return o;
			}
		}
		return null;
	}

	public ArrayList<GameObject> getObjectsAtSpot(double xPoint, double yPoint) {
		ArrayList<GameObject> ret = new ArrayList<>();
		double x, y;
		for (GameObject o : objects) {
			x = o.getX();
			y = o.getY();
			if (x >= xPoint && x < xPoint + 1 && y >= yPoint && y < yPoint + 1) {
				ret.add(o);
			}
		}
		return ret;
	}

	public int getLevelHeight() {
		return levelHeight;
	}

	public int getLevelWidth() {
		return levelWidth;
	}

	public int getScreenHeight() {
		return screen.getHeight();
	}

	public int getScreenWidth() {
		return screen.getWidth();
	}

	public synchronized Screen getScreenToRender(boolean renderHUD) {
		screen.fill(0);
		double xInPixels = (-viewX * FIELD_SIZE) + (getScreenWidth() / 2) - (FIELD_SIZE / 2);
		double yInPixels = (-viewY * FIELD_SIZE) + (getScreenHeight() / 2) - (FIELD_SIZE / 2);

		GameListener g = getGameListener();
		int x, y;
		x = (g.getMouseX() - FIELD_SIZE / 2) / FIELD_SIZE;
		y = (g.getMouseY() - FIELD_SIZE / 2) / FIELD_SIZE;

		
		double xOnScreen, yOnScreen;

		Screen tScreen;
		for (BackgroundTileMetaData t : backgroundTile) {
			if (t != null) {
				xOnScreen = t.getX() * FIELD_SIZE + xInPixels;
				yOnScreen = t.getY() * FIELD_SIZE + yInPixels;

				tScreen = t.getScreenToRender();

				screen.drawScreen((int) xOnScreen, (int) yOnScreen, tScreen);
			}
		}

		for (GameObject o : objects) {
			xOnScreen = o.getX() * FIELD_SIZE + xInPixels;
			yOnScreen = o.getY() * FIELD_SIZE + yInPixels;
			
			screen.drawScreen((int) xOnScreen, (int) yOnScreen, o.getTexture());
		}
		
		if (renderHUD) {
			renderHUD(screen);
		}
		
		return screen;
	}

	private void renderHUD(Screen screen) {
		//TODO
	}


	public GameListener getGameListener() {
		return getGame().getGameListener();
	}

	public void tick() {
		
		if(getGameListener().isKeyDown(KeyEvent.VK_A)) {
			viewX -= 0.1;
		}
		if(getGameListener().isKeyDown(KeyEvent.VK_D)) {
			viewX += 0.1;
		}
		if(getGameListener().isKeyDown(KeyEvent.VK_W)) {
			viewY -= 0.1;
		}
		if(getGameListener().isKeyDown(KeyEvent.VK_S)) {
			viewY += 0.1;
		}
		for (Object o : objects.toArray()) {
			((GameObject) o).tick();
		}
		BackgroundTileMetaData t;
		for (int i = 0; i < backgroundTile.length; i++) {
			t = backgroundTile[i];
			if (t.getHealth() <= 0) {
				backgroundTile[i] = BackgroundTile.BACKGROUND_DIRT.createBackgroundTileMetaData(this, t.getX(),
						t.getY());
			}
			t.tick();

		}
	}

	public ArrayList<GameObject> getObjectsInLevel() {
		return new ArrayList<>(objects);
	}

	public Game getGame() {
		return game;
	}

	public abstract void keyPressed(int code);

	public abstract void keyReleased(int code);

	public abstract void reset();

	public void mouseClicked(int xScreen, int yScreen, int mouseButton) {
		if (mouseButton == MouseEvent.BUTTON1) {
			int[] co = getBackgroundTilesAt(xScreen, yScreen);
			int x = co[0], y = co[1];
			System.out.println("MouseButtonClick at" + x + " " + y);
		}
	}

	public int[] getBackgroundTilesAt(int xScreen, int yScreen) {
		int xInPixels = (int) (-viewX * FIELD_SIZE) + (getScreenWidth() / 2) - (FIELD_SIZE / 2);
		int yInPixels = (int) (-viewY * FIELD_SIZE) + (getScreenHeight() / 2) - (FIELD_SIZE / 2);

		int x = (xScreen - xInPixels) / FIELD_SIZE;
		int y = (yScreen - yInPixels) / FIELD_SIZE;
		return new int[] { x, y };
	}

	public void setBackgroundTile(BackgroundTileMetaData bgt) {
		if (bgt.getLevel() != this) {
			throw new RuntimeException("Error");
		}
		if (!coordinatesInLevel(bgt.getX(), bgt.getY())) {
			throw new RuntimeException("Coordinates not in bounds");
		}
		backgroundTile[bgt.getY() * getLevelWidth() + bgt.getX()] = bgt;
	}

	public boolean coordinatesInLevel(int x, int y) {
		return x > -1 && x < getLevelWidth() && y > -1 && y < getLevelHeight();
	}

	public void setBackgroundTile(int x, int y, BackgroundTile type) {
		Objects.requireNonNull(type, "type == null");
		setBackgroundTile(type.createBackgroundTileMetaData(this, x, y));
	}

	public Random getRandom() {
		return getGame().getRandom();
	}
}