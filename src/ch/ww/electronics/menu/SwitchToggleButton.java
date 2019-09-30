package ch.ww.electronics.menu;

import java.awt.event.MouseEvent;

import ch.ww.electronics.graphics.Screen;

public class SwitchToggleButton extends MenuComponent {
	private Screen screen;
	private Screen leverScreen;
	
	/** Time in ticks this thing needs to switch from on to off. */
	private int timeToChange = 5;
	
	private int backgroundColor = 0xC0C0C0; // Gray
	
	private int leverColorOn = 0x00ff00;// Green
	private int leverColorOff = 0xff0000;// Red
	
	private int location;
	private int movement;
	private boolean selected = false;
	
	public SwitchToggleButton(int width, int height) {
		super(width, height);
		newScreen();
	}
	
	public SwitchToggleButton(int width, int height, MenuComponent root) {
		super(width, height, root);
		newScreen();
	}
	
	@Override
	public void keyReleased(int keyCode) {
	}
	
	@Override
	protected void drawOnScreen(Screen screen) {
		screen.drawScreen(getX(), getY(), this.screen);
	}
	
	private void trigger() {
		if(movement == 0) {
			selected = (!selected);
			movement = (getWidth() / timeToChange);
			if(movement == 0) {
				movement = 1;
			}
			if (!selected) {
				movement = (-movement);
			}
			newScreen();
		}
	}
	
	private void newScreen() {
		screen = new Screen(getWidth(), getHeight()).fill(backgroundColor);
		leverScreen = new Screen(getWidth() / 10, getHeight());
		
		if (isSelected()) {
			leverScreen.fill(leverColorOn);
		} else {
			leverScreen.fill(leverColorOff);
		}
		
		screen.drawScreen(location, 0, leverScreen);
		if (hasFocus()) {
			drawBorder(screen);
		}
	}
	
	@Override
	public void tick() {
		if(movement != 0) {
			location += movement;
			if (location >= getWidth() - leverScreen.getWidth()) {
				movement = 0;
				location = getWidth() - leverScreen.getWidth();
			}
			if (location <= 0) {
				movement = 0;
				location = 0;
			}
			newScreen();
		}
	}
	
	public boolean isSelected() {
		return selected;
	}
	
	@Override
	public void keyPressed(int keyCode) {
	}
	
	@Override
	protected void focusGained() {
		newScreen();
	}
	
	@Override
	protected void focusLost() {
		newScreen();
	}
	
	@Override
	public boolean canHaveFocus() {
		return true;
	}

	@Override
	public void mouseClicked(int x, int y, int rawX, int rawY, int mouseButton) {
		if(mouseButton == MouseEvent.BUTTON1) {
			trigger();
		}
	}
}