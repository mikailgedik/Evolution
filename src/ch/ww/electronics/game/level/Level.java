package ch.ww.electronics.game.level;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

import ch.ww.electronics.game.Game;
import ch.ww.electronics.game.gameobject.Animal;
import ch.ww.electronics.game.gameobject.Brain;
import ch.ww.electronics.game.gameobject.Fight;
import ch.ww.electronics.game.gameobject.GameObject;
import ch.ww.electronics.graphics.FontCreator;
import ch.ww.electronics.graphics.Screen;
import ch.ww.electronics.level.backgroundtile.BackgroundTile;
import ch.ww.electronics.level.backgroundtile.BackgroundTileDirt;
import ch.ww.electronics.listener.GameListener;

public abstract class Level {
	public static final int FIELD_SIZE = 32;

	private final Game game;
	private final int levelWidth, levelHeight;
	private final Screen screen;
	private final ArrayList<GameObject> objects;
	private final ArrayList<Fight> fights;
	private final BackgroundTile[] backgroundTile;
	private final LevelCreator levelCreator;
	private double viewX, viewY;
	
	private int killcounter=0;
	private int foodeaten=0;
	
	private GameObject selected;

	public Level(Game game, int levelWidth, int levelHeight) {
		this.game = game;
		this.screen = new Screen(game.getWidth(), game.getHeight());

		this.levelWidth = levelWidth;
		this.levelHeight = levelHeight;

		objects = new ArrayList<>();
		fights = new ArrayList<>();
		this.backgroundTile = new BackgroundTile[levelWidth * levelHeight];

		this.levelCreator = new DefaultLevelCreator(game.getRandom());
		this.levelCreator.createLevel(this);
		
		viewX = getLevelWidth() / 2;
		viewY = getLevelHeight() / 2;
	}

	public BackgroundTile[] getBackgroundTiles() {
		return backgroundTile;
	}

	public BackgroundTile getBackgroundTile(int x, int y) {
		if (x > -1 && x < getLevelWidth() && y > -1 && y < getLevelHeight()) {
			int loc = x + y * getLevelWidth();
			if (backgroundTile[loc] == null) {
				backgroundTile[loc] = new BackgroundTileDirt(x, y);
			}
			return backgroundTile[loc];
		} else {
			return null;
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
			gameObject.setLevel(this);
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
		
		double xOnScreen, yOnScreen;

		Screen tScreen;
		for (BackgroundTile t : backgroundTile) {
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
			
			xOnScreen -= o.getTexture().getWidth() / 2;
			yOnScreen -= o.getTexture().getHeight() / 2;
			
			screen.drawScreen((int) xOnScreen, (int) yOnScreen, o.getTexture());
		}
		
		if (renderHUD) {
			renderHUD(screen);
		}
		
		return screen;
	}

	private void renderHUD(Screen screen) {
		int[] s = new int[6];
		for(GameObject o: objects) {
			if(o instanceof Animal) {
				switch(((Animal)o).getStatus()) {
				case IDLE:
					s[0]++;
					break;
				case CHASING:
					s[1]++;
					break;
				case SEARCHING_FOOD:
					s[2]++;
					break;
				case RUNNING:
					s[3]++;
					break;
				case STUNNED:
					s[4]++;
					break;
				case BE_FOOD:
					s[5]++;
				default:
					break;
				}
			}
		}
		
		String text = 
				"TOTAL" + objects.size() + "\n" + "IDLE:" + s[0] + "\n" + 
				"CHASING:" + s[1] + "\n" 		+ "SEARCHING_FOOD:" + s[2] + "\n" +
				"RUNNING:" + s[3] + "\n" 		+ "STUNNED:" + s[4] + "\n" +
				"FOOD_SOURCE:" + s[5] + "\n" ;
		if(getGameListener().isKeyDown(KeyEvent.VK_L)){
			text = 
					"Animals" + (objects.size()-s[5]) + "\n" + "IDLE:" + s[0] + "\n" + 
					"FOOD_SOURCE:" + s[5] + "\n" +
							
					"CHASING:" + s[1] + "\n" +
					"SEARCHING_FOOD:" + s[2] + "\n" +
					"RUNNING:" + s[3] + "\n" +
					"STUNNED:" + s[4] + "\n" +
					"KILLS:" + killcounter + "\n" ;
		}
		if(selected!=null){
			
		}else{
			text = "Kein Objekt ausgewählt"; 
		}
		
		FontCreator.drawFontOnScreen(text, 0, 0, screen, 0x00ff00);
	}

	public GameListener getGameListener() {
		return getGame().getGameListener();
	}

	public void tick() {
		if(getGameListener().isKeyDown(KeyEvent.VK_A)) {
			viewX -= 0.3;
		}
		if(getGameListener().isKeyDown(KeyEvent.VK_D)) {
			viewX += 0.3;
		}
		if(getGameListener().isKeyDown(KeyEvent.VK_W)) {
			viewY -= 0.3;
		}
		if(getGameListener().isKeyDown(KeyEvent.VK_S)) {
			viewY += 0.3;
		}
		for (Object o : objects.toArray()) {
			((GameObject) o).tick();
		}
		
		fights.forEach((f) -> {
			System.out.println("Fight!");
			this.fight(f.getA1(), f.getA2());
		});
		
		fights.clear();
		
		objects.removeIf((t) -> {
			if(t instanceof Animal) {
			return ((Animal)t).isDead();
			} else {
				return false;
			}
		});
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
		if (mouseButton == MouseEvent.BUTTON1){
			double xInPixels = (-viewX * FIELD_SIZE) + (getScreenWidth() / 2) - (FIELD_SIZE / 2);
			double yInPixels = (int) (-viewY * FIELD_SIZE) + (getScreenHeight() / 2) - (FIELD_SIZE / 2);
			double x = (xScreen - xInPixels) / FIELD_SIZE;
			double y = (yScreen - yInPixels) / FIELD_SIZE;
			
			double minabstand=-1;
			for(GameObject o:objects){
				//sqrt weggelassen, weil keine ROlle
				double abstand=Math.pow(x - o.getX(),2)+Math.pow(y - o.getY(),2);
				if(minabstand==-1|abstand<minabstand){
					minabstand=abstand;
					selected=o;
				}
			}
		}
	}

	public int[] getBackgroundTilesAt(int xScreen, int yScreen) {
		int xInPixels = (int) (-viewX * FIELD_SIZE) + (getScreenWidth() / 2) - (FIELD_SIZE / 2);
		int yInPixels = (int) (-viewY * FIELD_SIZE) + (getScreenHeight() / 2) - (FIELD_SIZE / 2);

		int x = (xScreen - xInPixels) / FIELD_SIZE;
		int y = (yScreen - yInPixels) / FIELD_SIZE;
		return new int[] { x, y };
	}

	public void setBackgroundTile(BackgroundTile bgt) {
		
		if (!coordinatesInLevel(bgt.getX(), bgt.getY())) {
			throw new RuntimeException("Coordinates not in bounds");
		}
		backgroundTile[bgt.getY() * getLevelWidth() + bgt.getX()] = bgt;
	}

	public boolean coordinatesInLevel(int x, int y) {
		return x > -1 && x < getLevelWidth() && y > -1 && y < getLevelHeight();
	}

	public void setBackgroundTile(int x, int y, BackgroundTile t) {
		Objects.requireNonNull(t, "t == null");
		t.setX(x);
		t.setY(y);
		setBackgroundTile(t);
	}

	public Random getRandom() {
		return getGame().getRandom();
	}
	
	public void addFight(Fight f) {
		for(Fight a: fights) {
			if(a.equals(f)) {
				return;
			}
		}
		fights.add(f);
	}
	
	private void fight(Animal a1, Animal a2) {
		double diff = a1.getSize() * a1.getEnergy() - a2.getSize() * a2.getEnergy();
		if(a1.getStatus() == Brain.Status.BE_FOOD) {
			diff = -1;
			foodeaten++;
			System.out.println("foodeaten:"+foodeaten);
		} else if(a2.getStatus() == Brain.Status.BE_FOOD) {
			diff = 1;
			foodeaten++;
			System.out.println("foodeaten:"+foodeaten);
		} else {
			killcounter++;
			System.out.println("killcounter:"+killcounter);
		}
		if(diff == 0) {
			diff = getRandom().nextBoolean() ? 1 : -1;
		}
		if(diff > 0) {
			a1.addEnergy(a2.getEnergy());
			a2.setEnergy(0);
		} else if(diff < 0) {
			a2.addEnergy(a1.getEnergy());
			a1.setEnergy(0);
		}
	}
}