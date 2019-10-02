package ch.ww.electronics.game;

import java.awt.event.KeyEvent;
import java.util.Random;

import ch.ww.electronics.game.level.Level;
import ch.ww.electronics.graphics.FontCreator;
import ch.ww.electronics.graphics.Screen;
import ch.ww.electronics.listener.EventExecuter;
import ch.ww.electronics.listener.GameListener;
import ch.ww.electronics.loader.GameLoader;
import ch.ww.electronics.main.GameCanvas;
import ch.ww.electronics.menu.Menu;
import ch.ww.electronics.menu.MenuPause;

public class Game implements EventExecuter {
	private final Screen screen, errorScreen;
	private long totalLevelTicks = 0, totalMenuTicks = 0, totalInventoryTicks = 0;
	private final Random random;
	private GameCanvas gameCanvas;
	private GameLoader loader;

	/*
	 * Priority of rendering/ticking: <br> Menu<br>OldInventory<br>Level
	 */
	private Menu menu;
	private Level level;

	public Game(Screen screen, GameCanvas gameCanvas) {
		this.screen = screen;
		this.gameCanvas = gameCanvas;
		errorScreen = new Screen(screen.getWidth(), screen.getHeight(), 0x0);
		menu = null;
		random = new Random();
		loader = new GameLoader(this);

		gameCanvas.getListener().addExecuter(this);
	}

	public GameLoader getGameLoader() {
		return loader;
	}

	public Screen getScreen() {
		return screen;
	}

	public int getWidth() {
		return screen.getWidth();
	}

	public int getHeight() {
		return screen.getHeight();
	}
		
	public void tick() {
		getGameListener().flushEvents();
		if (getMenu() != null) {
			getMenu().doNativeTick();
			totalMenuTicks++;
		} else if (getLevel() != null) {
			if (totalLevelTicks == 0) {
				getLevel().onStart();
			}
			getLevel().tick();
			totalLevelTicks++;
		} else {
			System.out.println("Nothing to tick!");
		}
	}

	public Menu getMenu() {
		return menu;
	}

	public long getTotalLevelTicks() {
		return totalLevelTicks;
	}

	public long getTotalMenuTicks() {
		return totalMenuTicks;
	}

	public void render() {
		if (getMenu() != null) {
			if (getLevel() != null) {
				screen.drawScreen(0, 0, getLevel().getScreenToRender(false));
			}
			getMenu().renderOnScreen(screen);
		} else if (getLevel() != null) {
			screen.drawScreen(0, 0, getLevel().getScreenToRender(true));
		} else {
			FontCreator.drawFontOnScreen("NO MENU\nNO LEVEL\nERROR", screen.getWidth() / 2, screen.getHeight() / 2,
					getErrorScreen(), 0xffffff);
			throw new RuntimeException("menu == null && level == null");
		}
	}

	public Level getLevel() {
		return level;
	}

	public void setMenu(Menu newMenu) {
		if (this.getMenu() != newMenu) {
			if (this.getMenu() != null) {
				this.getMenu().onClose();
			}
			this.menu = newMenu;
		}
	}

	public void closeMenu() {
		setMenu(null);
	}

	@Override
	public void keyPressed(int code) {
		if (getMenu() != null) {
			getMenu().keyPressed(code);
		} else if (getLevel() != null) {
			getLevel().keyPressed(code);
		} else {
			System.out.println("Key " + (char) code + " (" + code + ")was pressed, but everything is null");
		}
	}

	@Override
	public void keyReleased(int code) {
		if (getMenu() != null) {
			getMenu().keyReleased(code);
		} else if (getLevel() != null) {
			if (code == KeyEvent.VK_ESCAPE) {
				if (getMenu() == null) {
					setMenu(new MenuPause(this, null));
				}
			} else {
				getLevel().keyReleased(code);
			}
		}
	}

	public void closeInventory() {
		closeMenu();
	}

	public long getTotalTicks() {
		return getTotalMenuTicks() + getTotalLevelTicks() + getTotalInventoryTicks();
	}

	public long getTotalInventoryTicks() {
		return totalInventoryTicks;
	}

	public void setLevel(Level level) {
		this.level = level;
	}

	public void exitGame() {
		// TODO Save the game
		System.out.println("Closing...");
		System.exit(0);
	}

	public Screen getErrorScreen() {
		return errorScreen;
	}

	public boolean isKeyDown(int code) {
		return getGameListener().isKeyDown(code);
	}

	public GameListener getGameListener() {
		return gameCanvas.getListener();
	}

	@Override
	public void mouseClicked(int x, int y, int mouseButton) {
		if (getMenu() != null) {
			getMenu().mouseClicked(x, y, mouseButton);
		} else if (getLevel() != null) {
			getLevel().mouseClicked(x, y, mouseButton);
		}
	}

	public Random getRandom() {
		return random;
	}
}