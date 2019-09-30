package ch.ww.electronics.menu;

import ch.ww.electronics.graphics.Screen;

public class ScrollableComponent extends MenuComponent {

	public ScrollableComponent(int width, int height) {
		super(width, height);
	}

	public ScrollableComponent(int width, int height, MenuComponent root) {
		super(width, height, root);
	}

	@Override
	public void keyPressed(int keyCode) {
		// TODO Auto-generated method stub
	}

	@Override
	public void keyReleased(int keyCode) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseClicked(int x, int y, int rawX, int rawY, int mouseButton) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void drawOnScreen(Screen screen) {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean canHaveFocus() {
		// TODO Auto-generated method stub
		return false;
	}

}
