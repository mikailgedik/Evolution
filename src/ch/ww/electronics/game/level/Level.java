package ch.ww.electronics.game.level;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

import ch.ww.electronics.game.Game;
import ch.ww.electronics.game.gameobject.Animal;
import ch.ww.electronics.game.gameobject.DNA;
import ch.ww.electronics.game.gameobject.Fight;
import ch.ww.electronics.game.gameobject.Food;
import ch.ww.electronics.game.gameobject.GameObject;
import ch.ww.electronics.game.gameobject.State;
import ch.ww.electronics.game.gameobject.State.Status;
import ch.ww.electronics.graphics.FontCreator;
import ch.ww.electronics.graphics.Screen;
import ch.ww.electronics.level.backgroundtile.BackgroundTile;
import ch.ww.electronics.level.backgroundtile.BackgroundTileDirt;
import ch.ww.electronics.listener.GameListener;

public abstract class Level {
	public static final int FIELD_SIZE = 32;
	private static final double ZOOM_SPEED = 1.01;
	
	private final Game game;
	private final int levelWidth, levelHeight;
	private final Screen endScreen;
	private Screen renderScreen;
	
	private int renderWidth, renderHeight;
	
	private double zoom;
	
	private final ArrayList<GameObject> objects;
	private final ArrayList<Fight> fights;
	private final BackgroundTile[] backgroundTile;
	private final LevelCreator levelCreator;
	private double viewX, viewY;
	
	private int killcounter=0;
	
	private GameObject selected;
		
	public Level(Game game, int levelWidth, int levelHeight) {
		this.game = game;
		this.endScreen = new Screen(game.getWidth(), game.getHeight());

		this.renderWidth = this.endScreen.getWidth();
		this.renderHeight = this.endScreen.getHeight();
		this.renderScreen = new Screen(renderWidth, renderHeight);
		
		this.levelWidth = levelWidth;
		this.levelHeight = levelHeight;

		objects = new ArrayList<>();
		fights = new ArrayList<>();
		this.backgroundTile = new BackgroundTile[levelWidth * levelHeight];

		this.levelCreator = new DefaultLevelCreator(game.getRandom());
		this.levelCreator.createLevel(this);
		
		viewX = getLevelWidth() / 2;
		viewY = getLevelHeight() / 2; 
		
		this.zoom = 1;
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

	public int getEndScreenHeight() {
		return endScreen.getHeight();
	}

	public int getEndScreenWidth() {
		return endScreen.getWidth();
	}
	
	public int getRenderScreenWidth() {
		return renderScreen.getWidth();
	}
	
	public int getRenderScreenHeight() {
		return renderScreen.getHeight();
	}
	
	public void setRenderBounds(int w, int h) {
		this.renderWidth = w;
		this.renderHeight = h;
		
		this.renderScreen = new Screen(w, h);
	}
	
	public int getRenderWidth() {
		return renderWidth;
	}
	
	public int getRenderHeight() {
		return renderHeight;
	}

	
	public synchronized Screen getScreenToRender(boolean renderHUD) {
		boolean renderHeat = getGameListener().isKeyDown(KeyEvent.VK_H);
		
		endScreen.fill(0);

		Screen screen = this.renderScreen;
				
		double xInPixels = (-viewX * FIELD_SIZE) + (getEndScreenWidth() / 2) - (FIELD_SIZE / 2);
		double yInPixels = (-viewY * FIELD_SIZE) + (getEndScreenHeight() / 2) - (FIELD_SIZE / 2);
		
		xInPixels = (-viewX * FIELD_SIZE) + (getRenderScreenWidth() / 2) - (FIELD_SIZE / 2);
		yInPixels = (-viewY * FIELD_SIZE) + (getRenderScreenHeight() / 2) - (FIELD_SIZE / 2);

		double xOnScreen, yOnScreen;

		Screen tScreen;
		for (BackgroundTile t : backgroundTile) {
			if (t != null) {
				xOnScreen = t.getX() * FIELD_SIZE + xInPixels;
				yOnScreen = t.getY() * FIELD_SIZE + yInPixels;
				
				if(xOnScreen < -FIELD_SIZE || yOnScreen < -FIELD_SIZE || xOnScreen >= screen.getWidth() || yOnScreen >= screen.getHeight()) {
					continue;
				}
				
				tScreen = t.getScreenToRender();
				
				if(renderHeat) {
					tScreen = tScreen.copy();
					tScreen.fill(0xffffff);
					tScreen.darkScreen(t.getTemperature());
				}
				
				screen.drawScreen((int) xOnScreen, (int) yOnScreen, tScreen);
			}
		}

		for (GameObject o : objects) {
			xOnScreen = o.getX() * FIELD_SIZE + xInPixels;
			yOnScreen = o.getY() * FIELD_SIZE + yInPixels;
			
			xOnScreen -= o.getTexture().getWidth() / 2;
			yOnScreen -= o.getTexture().getHeight() / 2;
			
			if(xOnScreen < -FIELD_SIZE || yOnScreen < -FIELD_SIZE || xOnScreen >= screen.getWidth() || yOnScreen >= screen.getHeight()) {
				continue;
			}
			
			screen.drawScreen((int) xOnScreen, (int) yOnScreen, o.getTexture());
		}
		
		this.endScreen.drawScreen(0, 0, renderScreen.getScaledScreen(this.getEndScreenWidth(), this.getEndScreenHeight()));

		if (renderHUD) {
			renderHUD(this.endScreen);
		}
				
		return this.endScreen;
	}
	
	public synchronized void renderOnScreen(Screen endScreen, boolean renderHUD) {
		boolean renderHeat = getGameListener().isKeyDown(KeyEvent.VK_H);
		renderScreen = new Screen((int)(zoom * endScreen.getWidth()),(int)(zoom * endScreen.getHeight()));
		renderScreen.fill(0);
		
		double drawX, drawY;
		
		//These values center the image
		double xOffset = -FIELD_SIZE * viewX + renderScreen.getWidth()  / 2 - FIELD_SIZE / 2;
		double yOffset = -FIELD_SIZE * viewY + renderScreen.getHeight() / 2 - FIELD_SIZE / 2;
		
		for (BackgroundTile t : backgroundTile) {
			assert t != null;
			drawX = t.getX() * FIELD_SIZE + xOffset;
			drawY = t.getY() * FIELD_SIZE + yOffset;
			
			Screen tScreen = t.getScreenToRender();
			if(renderHeat) {
				tScreen = tScreen.copy();
				tScreen.fill(0xffffff);
				tScreen.darkScreen(t.getTemperature());
			}
			
			renderScreen.drawScreen((int)drawX, (int)drawY, tScreen);
		}
		
		double xOnScreen, yOnScreen;
		
		for (GameObject o : objects) {
			xOnScreen = o.getX() * FIELD_SIZE + xOffset;
			yOnScreen = o.getY() * FIELD_SIZE + yOffset;
			
			xOnScreen -= o.getTexture().getWidth() / 2;
			yOnScreen -= o.getTexture().getHeight() / 2;
			
			if(xOnScreen < -FIELD_SIZE || yOnScreen < -FIELD_SIZE || xOnScreen >= renderScreen.getWidth() || yOnScreen >= renderScreen.getHeight()) {
				continue;
			}
			
			renderScreen.drawScreen((int) xOnScreen, (int) yOnScreen, o.getTexture());
		}
		
		endScreen.drawScreen(0, 0, renderScreen.getScaledScreen(endScreen.getWidth(), endScreen.getHeight()));
		if(renderHUD) {
			renderHUD(endScreen);
		}
	}
	
	private void renderHUD(Screen screen) {
		int[] s = new int[6];
		for(GameObject o: objects) {
			if(o instanceof Animal) {
				switch(((Animal)o).getActualState().getStatus()) {
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
		if(getGameListener().isKeyDown(KeyEvent.VK_M)){
			if(selected!=null){
				if(!(selected instanceof Food)){
					text= ((Animal) selected).getNiceText();
				}else{
					text="Es ist kein Tier ausgewählt";
				}
			}else{
				text = "Kein Objekt ausgewählt"; 
			}
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
		if(getGameListener().isKeyDown(KeyEvent.VK_O)) {
			zoom *= ZOOM_SPEED;
		}
		if(getGameListener().isKeyDown(KeyEvent.VK_P)) {
			zoom *= (1.0/ ZOOM_SPEED);
		}
		
		for (Object o : objects.toArray()) {
			//TODO multithreading
			((GameObject) o).preTick();
		}
		
		fights.forEach((f) -> {
			this.fight(f.getA1(), f.getA2());
		});
		fights.clear();
		
		for (Object o : objects.toArray()) {
			//TODO multithreading
			((GameObject) o).tick();
		}
		
		
		objects.removeIf((t) -> {
			if(t instanceof Animal) {
			return ((Animal)t).getActualState().isDead();
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
		if (mouseButton == MouseEvent.BUTTON1){
			int[] co = getBackgroundTilesAt(xScreen, yScreen);
			int x = co[0], y = co[1];
			
			double minabstand=-1;
			for(GameObject o:objects) {
				//sqrt weggelassen, weil keine Rolle
				double abstand=Math.pow(x - o.getX(),2)+Math.pow(y - o.getY(),2);
				if(o instanceof Animal && (minabstand==-1 || abstand<minabstand)){
					minabstand=abstand;
					selected = o;
				}
			}
			
		}
	}

	public int[] getBackgroundTilesAt(int xScreen, int yScreen) {
		xScreen *= zoom;
		yScreen *= zoom;
		int ret[] = {0,0};
		xScreen -= renderScreen.getWidth() / 2 - FIELD_SIZE /2 - viewX * FIELD_SIZE;
		yScreen -= renderScreen.getHeight() / 2 - FIELD_SIZE /2 - viewY * FIELD_SIZE;

		ret[0] = xScreen / FIELD_SIZE;
		ret[1] = yScreen / FIELD_SIZE;
		
		return ret;
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
		//TODO
		double diff;
		/*
		diff = a1.getSize() * a1.getEnergy() - a2.getSize() * a2.getEnergy();
		if(a1.getStatus() == Brain.Status.BE_FOOD) {
			diff = -1;
		} else if(a2.getStatus() == Brain.Status.BE_FOOD) {
			diff = 1;
		} else {
			killcounter++;
		}
		*/
		diff = 0;
		if(a2.getActualState().getStatus() == State.Status.BE_FOOD) {
			diff = 1;
		}
		if(diff == 0) {
			diff = getRandom().nextBoolean() ? 1 : -1;
		}
		if(diff > 0) {
			a1.getCalculatedState().addEnergy(a2.getCalculatedState().getEnergy());
			a2.getCalculatedState().setEnergy(0);
		} else if(diff < 0) {
			a2.getCalculatedState().addEnergy(a1.getCalculatedState().getEnergy());
			a1.getCalculatedState().setEnergy(0);
		}
		
	}

	public void babyFrom(Animal parent) {
		Animal baby = new Animal(this, parent.getX(), parent.getY());
		DNA dna = parent.getDNA().clone();
		dna.variate(1);
		baby.setDNA(dna);
		
		double totenergy = parent.getCalculatedState().getEnergy();
		baby.getCalculatedState().setEnergy(0.5 * baby.getDNA().get(DNA.MAX_ENERGY));
		totenergy -= 0.5 * baby.getDNA().get(DNA.MAX_ENERGY);
		parent.getCalculatedState().setEnergy(totenergy);
		
		baby.getCalculatedState().setStatus(Status.RUNNING);
		parent.getCalculatedState().setStatus(Status.STUNNED);
	}
}