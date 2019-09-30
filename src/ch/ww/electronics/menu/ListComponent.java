package ch.ww.electronics.menu;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

import ch.ww.electronics.graphics.FontCreator;
import ch.ww.electronics.graphics.ManipulatedScreen;
import ch.ww.electronics.graphics.Screen;
import ch.ww.electronics.menu.action.ActionExecuter;
import ch.ww.electronics.menu.action.ActionInfo;

public class ListComponent<T> extends MenuComponent {
	public static final int SELECTED_COLOR = 0xffff00, UNSELECTED_COLOR = 0x0f0f0f;
	public static final int SELECTION_SINGLE = 0x1, SELECTION_NONE = 0x2;

	private ArrayList<ListEntry> content;
	private int selected;

	private int upperOffset;

	private int fontHeight = FontCreator.LETTER_HEIGHT;
	private int offsetChange = fontHeight / 4;

	private int selectionMode;

	public ListComponent(int width, int height, MenuComponent root) {
		this(width, height, root, SELECTION_SINGLE);
		content = new ArrayList<>();
		selected = -1;
	}

	public ListComponent(int width, int height, MenuComponent root, int selectionType) {
		super(width, height, root);
		content = new ArrayList<>();
		selected = -1;
		this.selectionMode = selectionType;
	}

	public void addContent(String s, T value) {
		this.addContent(s, value, null, null);
	}

	public void addContent(String s, T value, ActionExecuter ae, ActionExecuter earlyAE) {
		content.add(new ListEntry(s, value, ae, earlyAE));
	}

	public void clear() {
		content.clear();
	}

	public int getCount() {
		return content.size();
	}

	public T getSelected() {
		if (selected != -1) {
			return content.get(selected).getValue();
		} else {
			return null;
		}
	}

	public int getSelectedIndex() {
		return selected;
	}

	public ListEntry getSelectedEntry() {
		if (selected != -1) {
			return content.get(selected);
		} else {
			return null;
		}
	}

	@Override
	public void keyPressed(int keyCode) {
		if (keyCode == KeyEvent.VK_UP) {
			upperOffset -= offsetChange;
		} else if (keyCode == KeyEvent.VK_DOWN) {
			upperOffset += offsetChange;
		}
		if (upperOffset > this.fontHeight * this.content.size() - this.getHeight()) {
			upperOffset = this.fontHeight * this.content.size() - this.getHeight();
		}
		if (upperOffset < 0) {
			upperOffset = 0;
		} else {
			upperOffset %= this.content.size() * fontHeight;
		}
	}

	@Override
	public void keyReleased(int keyCode) {
	}

	@Override
	protected void drawOnScreen(Screen para) {
		para.fillRect(getX(), getY(), getWidth(), getHeight(), this.hasFocus() ? SELECTED_COLOR : UNSELECTED_COLOR);

		Screen component = new Screen(getWidth(), getHeight());

		int y;
		for (int i = 0; i < content.size(); i++) {
			ListEntry le = content.get(i);
			y = i * fontHeight - upperOffset;
			if (y + fontHeight < 1 || y > getHeight()) {
				continue;
			}
			Screen s;
			if (i == selected) {
				s = le.getSelectedScreenToRender(getWidth(), fontHeight);
			} else {
				s = le.getScreenToRender(getWidth(), fontHeight);
			}
			component.drawScreen(0, y, s);
		}

		para.drawScreen(getX(), getY(), component);
	}

	@Override
	public boolean canHaveFocus() {
		return true;
	}

	@Override
	public void mouseClicked(int x, int y, int rawX, int rawY, int mouseButton) {
		if (selectionMode != SELECTION_NONE) {
			int oldSelected = selected;

			y -= upperOffset;
			y /= fontHeight;
			selected = y;

			if (selected < 0) {
				selected = 0;
			} else if (selected >= this.content.size()) {
				selected = -1;
			}

			if (selected != -1) {
				ActionExecuter action;
				ListEntry entry = content.get(selected);
				if (oldSelected == selected) {
					entry.hasBeenSelectedNormal();
					action = entry.getActionExecuter();
				} else {
					entry.hasBeenSelectedEarly();
					action = entry.getEarlyActionExecuter();
				}
				if (action != null) {
					action.execute(new ActionInfo(this, this.getMenu().getGame().getTotalTicks()));
				}
			}
		}
	}

	public int getFontHeight() {
		return this.fontHeight;
	}

	@Override
	public void tick() {
		for (ListEntry e : this.content) {
			e.tick();
		}
	}

	public class ListEntry {
		private static final double MAX_VAR = 1, MIN_VAR = 0.001, CHANGE = 0.1;
		private final String text;
		private ManipulatedScreen screen, selectedScreen;
		private T value;
		private ActionExecuter actionExectuer, earlyActionExecuter;
		private SelectedFunction function;

		public ListEntry(String text, T value) {
			this(text, value, null, null);
		}

		public ListEntry(String text, T value, ActionExecuter e, ActionExecuter earlyAE) {
			this.text = text;
			this.value = value;
			this.actionExectuer = e;
			this.earlyActionExecuter = earlyAE;
			function = new SelectedFunction(1.0, SelectedFunction.INACTIVE);
			createNewScreen(getWidth(), fontHeight);
		}

		public String getTextToDisplay() {
			return text;
		}

		public Screen getScreenToRender(int w, int h) {
			if (w != screen.getWidth() || h != screen.getHeight()) {
				createNewScreen(w, h);
			}
			return screen;
		}

		public Screen getSelectedScreenToRender(int w, int h) {
			if (w != selectedScreen.getWidth() || h != selectedScreen.getHeight()) {
				createNewScreen(w, h);
			}
			return selectedScreen;
		}

		private void createNewScreen(int w, int h) {
			Screen font = FontCreator.createFont(text, FONT_COLOR, -1);
			font = font.getScaledScreen(getWidth() < font.getWidth() ? getWidth() : font.getWidth(), fontHeight);

			screen = new ManipulatedScreen(w, h, -1, this.function);
			selectedScreen = screen.copy();
			selectedScreen.fill(0x7f7f7f);

			screen.drawScreen(0, 0, font);
			selectedScreen.drawScreen(0, 0, font);
		}

		public T getValue() {
			return value;
		}

		public ActionExecuter getActionExecuter() {
			return actionExectuer;
		}

		public ActionExecuter getEarlyActionExecuter() {
			return earlyActionExecuter;
		}

		private void tick() {
			if (getSelectedEntry() == this) {
				int status = function.getStatus();
				if ((status & (SelectedFunction.EARLY | SelectedFunction.NORMAL)) != 0) {
					if (function.getVar() < MIN_VAR) {
						function.setStatus(SelectedFunction.INACTIVE);
					}
					function.setVar(function.getVar() - CHANGE);
				}
			} else {
				function.setStatus(SelectedFunction.INACTIVE);
			}
		}

		private void hasBeenSelectedEarly() {
			function.setStatus(SelectedFunction.EARLY);
			function.setVar(MAX_VAR);
		}

		private void hasBeenSelectedNormal() {
			function.setStatus(SelectedFunction.NORMAL);
			function.setVar(MAX_VAR);
		}
	}

	public static class SelectedFunction implements ManipulatedScreen.ScreenFunction {
		public static final int EARLY = 0x1, NORMAL = 0x2, INACTIVE = 0x0;

		/** Relative, from 0 to 1 */
		private double var;

		private int status;

		public SelectedFunction(double radius, int status) {
			this.var = radius;
			this.status = status;
		}

		@Override
		public int apply(int x, int y, int color, Screen screen) {
			switch (status) {
			case INACTIVE:
				return color;
			case EARLY:
				return earlyFunction(x, y, color, screen);
			case NORMAL:
				return normalFunction(x, y, color, screen);
			default:
				throw new RuntimeException("Unacceptable status: " + status);
			}
		}

		private int earlyFunction(int x, int y, int color, Screen screen) {
			if (color != FONT_COLOR) {
				int a = (int) ((screen.getWidth() / 2) * var);
				int b = (int) ((screen.getHeight() / 2) * var);
				if (x * 2 > screen.getWidth()) {
					x = screen.getWidth() - x;
				}
				if (y * 2 > screen.getHeight()) {
					y = screen.getHeight() - y;
				}
				if (x * x < x(y, a, b) && y * y < y(x, a, b)) {
					return 0x0000ff;
				} else {
					return color;
				}
			} else {
				return color;
			}
		}

		private int normalFunction(int x, int y, int color, Screen screen) {
			if (color != FONT_COLOR) {
				return Screen.getColor((int) (Screen.getRed(color) * (1 - var)),
						(int) (Screen.getGreen(color) * (1 - var)), (int) (Screen.getBlue(color) * (1 - var)));
			} else {
				return color;
			}
		}

		private int y(int x, int a, int b) {
			if (a == 0) {
				return 0;
			}
			return b * b - (b * b * x * x) / (a * a);
		}

		private int x(int y, int a, int b) {
			if (b == 0) {
				return 0;
			}
			return (a * a * (b * b - y * y)) / (b * b);
		}

		public void setVar(double var) {
			this.var = var;
		}

		public double getVar() {
			return var;
		}

		public int getStatus() {
			return status;
		}

		public void setStatus(int status) {
			this.status = status;
		}
	}
}