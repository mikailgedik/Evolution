package ch.ww.electronics.menu;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import ch.ww.electronics.graphics.Screen;
import ch.ww.electronics.menu.action.ActionExecuter;
import ch.ww.electronics.menu.action.ActionInfo;

public class Button extends MenuComponent {
	private Screen screen;
	private ArrayList<ActionExecuter> actionExecuters;
	
	public Button(Screen screen) {
		super(screen.getWidth(), screen.getHeight());
		this.actionExecuters = new ArrayList<>();
		this.screen = screen.copy();
	}
	
	@Override
	public void tick() {
	}
	
	@Override
	protected void drawOnScreen(Screen paraScreen) {
		Screen screen = this.screen.copy();
		if (hasFocus()) {
			drawBorder(screen);
		}
		paraScreen.drawScreen(getX(), getY(), screen);
	}
	
	@Override
	protected void onScale(int newWidth, int newHeight) {
		screen = screen.getScaledScreen(newWidth, newHeight);
	}
	
	public void addActionExecuter(ActionExecuter executer) {
		if (!this.actionExecuters.contains(executer)) {
			this.actionExecuters.add(executer);
		}
	}
	
	public void removeActionExecuter(ActionExecuter executer) {
		this.actionExecuters.remove(executer);
	}
	
	private void doAction() {
		ActionInfo info = new ActionInfo(this, getMenu().getGame().getTotalTicks());
		for (ActionExecuter exe : this.actionExecuters) {
			exe.execute(info);
		}
	}
	
	@Override
	public void keyPressed(int code) {
		
	}
	
	@Override
	public void keyReleased(int code) {
		if (code == KeyEvent.VK_SPACE || code == KeyEvent.VK_ENTER) {
			doAction();
		}
	}
		
	protected void setScreen(Screen screen) {
		this.screen = screen;
	}
	
	public static Button createBackButton() {
		return new TextedButton("BACK");
	}
	
	@Override
	public boolean canHaveFocus() {
		return true;
	}

	@Override
	public void mouseClicked(int x, int y, int rawX, int rawY, int mouseButton) {
		if(mouseButton == MouseEvent.BUTTON1) {
			doAction();
		}
	}
}