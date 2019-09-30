package ch.ww.electronics.menu;

import java.util.ArrayList;
import java.util.Objects;

import ch.ww.electronics.graphics.Screen;

public abstract class MenuComponent {
	public static final int SELECTED_BORDER_WIDTH = 5;
	public static final int SELECTED_COLOR = 0xff0000;
	public static final int FONT_COLOR = 0xFF00FF;
	
	private MenuComponent root;
	private int x, y, width, height;
	private final ArrayList<MenuComponent> components;
	private long ticksShown = 0;

	public MenuComponent(int width, int height) {
		this.width = width;
		this.height = height;
		this.components = new ArrayList<>();
	}

	public MenuComponent(int width, int height, MenuComponent root) {
		this(width, height);
		root.addMenuComponent(this);
	}

	public boolean hasFocus() {
		if (getMenu() != null) {
			return getMenu().getFocusComponent() == this;
		} else
			return false;
	}

	public void requestFocus() {
		if (getMenu() != null) {
			getMenu().setFocusComponent(this);
		}
	}

	public MenuComponent getRoot() {
		return root;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void addMenuComponent(MenuComponent mc) {
		if (mc instanceof Menu) {
			throw new RuntimeException("Cannot add a menu to something");
		}
		if (mc == this) {
			throw new RuntimeException("Cannot add itself to itself");
		}
		Objects.requireNonNull(mc, "mc == null");
		if (mc.getRoot() != null) {
			mc.getRoot().removeMenuComponent(mc);
		}
		if (!components.contains(mc)) {
			components.add(mc);
			mc.setRoot(this);
		}
	}

	public void removeMenuComponent(MenuComponent mc) {
		if (components.contains(mc)) {
			components.remove(mc);
			mc.setRoot(null);
			if (getMenu() != null) {
				getMenu().setFocusComponent(this);
			}
		}
	}

	private void setRoot(MenuComponent root) {
		this.root = root;
	}

	/** Only invoked by {@link #setBounds(int, int)} */
	private void setWidth(int w) {
		this.width = w;
	}

	/** Only invoked by {@link #setBounds(int, int)} */
	private void setHeight(int h) {
		this.height = h;
	}

	/** If not overridden, it throws a {@link RuntimeException}. */
	public final void setBounds(int width, int height) {
		onScale(width, height);
		setWidth(width);
		setHeight(height);
	}

	protected void onScale(int newWidth, int newHeight) {

	}

	public void tick() {

	}

	public long getTicksShown() {
		return ticksShown;
	}

	/** Do not call */
	public final void doNativeTick() {
		tick();
		for (MenuComponent mc : components) {
			mc.doNativeTick();
		}
		ticksShown++;
	}

	protected void drawChildrenOnScreen(Screen screen) {
		for (MenuComponent mc : components) {
			mc.renderOnScreen(screen);
		}
	}

	public void renderOnScreen(Screen screen) {
		drawOnScreen(screen);
		drawChildrenOnScreen(screen);
	}

	public abstract void keyPressed(int keyCode);

	public abstract void keyReleased(int keyCode);

	/**
	 * @param x
	 *            the x on this component
	 * @param y
	 *            the y on this component
	 * @param rawX
	 *            the x on the screen of the whole game
	 * @param rawY
	 *            the y on the screen of the whole game
	 * @param mouseButton
	 *            the mousebutton which was pressed
	 */
	public abstract void mouseClicked(int x, int y, int rawX, int rawY, int mouseButton);

	protected abstract void drawOnScreen(Screen screen);

	public abstract boolean canHaveFocus();

	protected void focusLost() {

	}

	protected void focusGained() {

	}

	public Menu getMenu() {
		if (root == null && !(this instanceof Menu)) {
			return null;
		}
		MenuComponent root = this;
		while (root != null) {
			if (root instanceof Menu)
				return (Menu) root;
			root = root.root;
		}
		throw new RuntimeException("No menu as a root!");
	}

	public ArrayList<MenuComponent> getComponents() {
		return new ArrayList<>(components);
	}

	protected void lostFocus() {

	}

	public static void drawBorder(Screen screen, int borderWidth, int borderColor) {
		Screen rect1 = new Screen(screen.getWidth(), borderWidth, borderColor);
		Screen rect2 = new Screen(borderWidth, screen.getHeight(), borderColor);
		screen.drawScreen(0, 0, rect1);
		screen.drawScreen(0, 0, rect2);
		screen.drawScreen(0, screen.getHeight() - borderWidth, rect1);
		screen.drawScreen(screen.getWidth() - borderWidth, 0, rect2);
	}

	public static void drawBorder(Screen screen) {
		drawBorder(screen, SELECTED_BORDER_WIDTH, SELECTED_COLOR);
	}
}