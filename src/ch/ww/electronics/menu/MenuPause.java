package ch.ww.electronics.menu;

import ch.ww.electronics.game.Game;
import ch.ww.electronics.graphics.Screen;
import ch.ww.electronics.menu.action.ActionInfo;

public class MenuPause extends Menu {
	private Button buttonExit, buttonBackToGame;
		
	public MenuPause(Game game, Menu parentMenu) {
		super(game, parentMenu);
		buttonExit = new TextedButton("EXIT");
		buttonBackToGame = new TextedButton("Back to game");
		setLocations();
		buttonExit.addActionExecuter((ActionInfo i) -> {
			getGame().exitGame();
		});
		buttonBackToGame.addActionExecuter((ActionInfo i) -> {
			close();
		});
		
		addMenuComponent(buttonExit);
		addMenuComponent(buttonBackToGame);
		
		buttonBackToGame.requestFocus();
	}
	
	private void setLocations() {
		buttonBackToGame.setX((getWidth() - buttonExit.getWidth())/2);
		buttonBackToGame.setY(200);
		
		buttonExit.setX((getWidth() - buttonExit.getWidth())/2);
		buttonExit.setY(300);

	}

	@Override
	protected void drawOnScreen(Screen screen) {
		super.drawOnScreen(screen);
	}
}