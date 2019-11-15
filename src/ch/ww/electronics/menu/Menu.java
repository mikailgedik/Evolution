package ch.ww.electronics.menu;

import ch.ww.electronics.game.Game;
import ch.ww.electronics.graphics.Screen;

public class Menu extends MenuComponent {
	private final Game game;
	protected final Menu parentMenu;
	private MenuComponent focusComponent;
	private Screen background;
	private static final Screen defaultBackground;

	static {
		defaultBackground = new Screen(100, 100);
		for (int i = 0; i < defaultBackground.getPixels().length; i++) {
			defaultBackground.getPixels()[i] = (int) Math.pow(i, Math.E - 1);
		}
	}

	public Menu(Game game, Menu parentMenu) {
		super(0, 0);
		this.game = game;
		this.parentMenu = parentMenu;
		this.focusComponent = this;
	}

	public void showParent() {
		game.setMenu(parentMenu);
	}

	public void close() {
		game.closeMenu();
	}

	public void onClose() {

	}

	@Override
	public final int getX() {
		return 0;
	}

	@Override
	public final int getY() {
		return 0;
	}

	@Override
	public final int getWidth() {
		return game.getScreen().getWidth();
	}

	@Override
	public final int getHeight() {
		return game.getScreen().getHeight();
	}

	@Override
	public final MenuComponent getRoot() {
		return this;
	}

	@Override
	protected void drawOnScreen(Screen screen) {		
		if(getGame().getLevel() == null || getBackground() == null) {
			Screen b = screen.copy().fill(0);
			for (int x = 0; x < getWidth(); x += defaultBackground.getWidth()) {
				for (int y = 0; y < getHeight(); y += defaultBackground.getHeight()) {
					b.drawScreen(x, y, defaultBackground);
				}
			}
			setBackground(b);
		}
		screen.drawScreen(0, 0, getBackground());
	}

	public Game getGame() {
		return game;
	}

	public void setFocusComponent(MenuComponent mc) {
		if (!mc.hasFocus() && mc.canHaveFocus()) {
			MenuComponent oldOne = this.focusComponent;
			this.focusComponent = mc;
			mc.focusGained();
			oldOne.focusLost();
		}
	}

	public MenuComponent getFocusComponent() {
		return focusComponent;
	}

	@Override
	public final void keyPressed(int keyCode) {
		if (getFocusComponent() != null && getFocusComponent() != this) {
			getFocusComponent().keyPressed(keyCode);
		}
	}

	@Override
	public final void keyReleased(int keyCode) {
		if (getFocusComponent() != null && getFocusComponent() != this) {
			getFocusComponent().keyReleased(keyCode);
		}
	}

	public Screen getBackground() {
		return background;
	}

	public void setBackground(Screen background) {
		this.background = background;
	}

	@Override
	public final boolean canHaveFocus() {
		return false;
	}

	/** This method is only invoked by the container of this menu */
	public void mouseClicked(final int x, final int y, int mouseButton) {
		for (MenuComponent mc : getComponents()) {
			if (x > mc.getX() && x < mc.getX() + mc.getWidth() && y > mc.getY() && y < mc.getY() + mc.getHeight()) {
				if (!mc.hasFocus()) {
					mc.requestFocus();
				}
				mc.mouseClicked(x - mc.getX(), y - mc.getY(), x, y, mouseButton);
				return;
			}
		}
	}

	/** This method is here to be overridden */
	@Override
	public void mouseClicked(int x, int y, int rawX, int rawY, int mouseButton) {
	}
}