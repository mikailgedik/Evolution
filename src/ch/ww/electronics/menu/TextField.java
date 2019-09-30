package ch.ww.electronics.menu;

import java.awt.event.KeyEvent;

import ch.ww.electronics.graphics.FontCreator;
import ch.ww.electronics.graphics.Screen;

public class TextField extends MenuComponent {
	private StringBuffer text;
	private Screen screen;
	private int defaultFontColor = 0x00ff00;
	private int focusedFontColor = 0xff00ff;
	private int defaultBackgroundColor = 0x000000;
	private int focusedBackgroundColor = 0xffffff;

	private int minCharShown;
	private EnterListener listener;

	public TextField(int minCharShown) {
		this("");
		this.minCharShown = minCharShown;
	}

	public TextField(String text) {
		super(text.length() * FontCreator.LETTER_WIDTH, FontCreator.LETTER_HEIGHT);
		init(text);
	}

	public void setListener(EnterListener listener) {
		this.listener = listener;
	}

	public EnterListener getListener() {
		return listener;
	}

	public TextField(MenuComponent root) {
		this("", root);
	}

	public TextField(String text, MenuComponent root) {
		super(text.length() * FontCreator.LETTER_WIDTH, FontCreator.LETTER_HEIGHT, root);
		init(text);
	}

	private void init(String text) {
		this.text = new StringBuffer(text);
		minCharShown = text.length();
		screen = new Screen(getWidth(), getHeight()).fill(defaultBackgroundColor);
		newScreen();
	}

	@Override
	public void keyPressed(int keyCode) {
		if (keyCode == KeyEvent.VK_ENTER) {
			if (listener != null) {
				listener.enterPressed(this);
			}
		}
		if (keyCode == KeyEvent.VK_DELETE || keyCode == KeyEvent.VK_BACK_SPACE) {
			if (text.length() > 0) {
				text.deleteCharAt(text.length() - 1);
			}
		} else {
			text.append((char) keyCode);
		}
		newScreen();
	}

	private void newScreen() {
		int col, back;
		if (hasFocus()) {
			col = this.focusedFontColor;
			back = this.focusedBackgroundColor;
		} else {
			col = this.defaultFontColor;
			back = this.defaultBackgroundColor;
		}
		screen = new Screen(
				(this.minCharShown > text.length() ? this.minCharShown : text.length()) * FontCreator.LETTER_WIDTH,
				FontCreator.LETTER_HEIGHT * text.toString().split("\n").length).fill(back);
		FontCreator.drawFontOnScreen(text.toString(), 0, 0, screen, col);

		setBounds(screen.getWidth(), screen.getHeight());
	}

	@Override
	public void keyReleased(int keyCode) {

	}

	@Override
	protected void drawOnScreen(Screen screen) {
		screen.drawScreen(getX(), getY(), this.screen);
	}

	@Override
	public boolean canHaveFocus() {
		return true;
	}

	@Override
	protected void focusLost() {
		newScreen();
	}

	protected void focusGained() {
		newScreen();
	}

	@Override
	public void mouseClicked(int x, int y, int rawX, int rawY, int mouseButton) {
	}

	@FunctionalInterface
	public static interface EnterListener {
		public void enterPressed(TextField field);
	}
}