package ch.ww.electronics.menu;

import ch.ww.electronics.graphics.FontCreator;
import ch.ww.electronics.graphics.Screen;

public class LoadingBar extends MenuComponent {
	private int maxValue = 1;
	private int value = 0;
	private double distanceSize = 1.0;
	private Screen screen;
	private String text;

	public LoadingBar(int maxvalue, int distanceSize, int height) {
		super(maxvalue * distanceSize, height);
		this.maxValue = maxvalue;
		setDistanceSize(distanceSize);
		init();
	}

	private void init() {
		screen.fill(0);
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}

	@Override
	protected void drawOnScreen(Screen screen) {
		Screen copy = this.screen.copy();
		double w = value * distanceSize;
		if (value == maxValue) {
			w = maxValue * distanceSize;
		}
		copy.fillRect(0, 0, (int) w, getHeight(), 0xffffff);
		if(text != null) {
			Screen textScreen = FontCreator.createFont(this.text, 0xf0f0f0, 0);
			copy.drawScreen(copy.getWidth() / 2 - textScreen.getWidth() / 2, copy.getHeight() / -textScreen.getHeight() / 2,
					textScreen);
		}
		screen.drawScreen(getX(), getY(), copy);
	}

	public void setValue(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public void setDistanceSize(double distanceSize) {
		this.distanceSize = distanceSize;
		setBounds((int) (distanceSize * maxValue), getHeight());
		screen = new Screen(getWidth(), getHeight(), 0);

	}

	public double getDistanceSize() {
		return distanceSize;
	}

	public void setMaxValue(int maxValue) {
		this.maxValue = maxValue;
		this.distanceSize = getWidth() * 1.0 / maxValue;
	}

	public int getMaxValue() {
		return maxValue;
	}

	@Override
	public boolean canHaveFocus() {
		return false;
	}

	@Override
	public void keyPressed(int keyCode) {
	}

	@Override
	public void keyReleased(int keyCode) {
	}

	@Override
	public void mouseClicked(int x, int y, int rawX, int rawY, int mouseButton) {		
	}
}