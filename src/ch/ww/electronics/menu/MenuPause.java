package ch.ww.electronics.menu;

import ch.ww.electronics.game.Game;
import ch.ww.electronics.graphics.Screen;
import ch.ww.electronics.menu.action.ActionInfo;

public class MenuPause extends Menu {
	private Button buttonExit, buttonBackToGame, buttonStatistics;
		
	public MenuPause(Game game, Menu parentMenu) {
		super(game, parentMenu);
		
		buttonExit = new TextedButton("EXIT");
		buttonBackToGame = new TextedButton("Back to game");
		buttonStatistics = new TextedButton("Statistics");
		setLocations();
		
		buttonExit.addActionExecuter((ActionInfo i) -> {
			getGame().exitGame();
		});
		buttonBackToGame.addActionExecuter((ActionInfo i) -> {
			close();
		});
		buttonStatistics.addActionExecuter((ActionInfo i) -> {
			Menu stats = new MenuStatistics(game, this);
			game.setMenu(stats);
		});
		
		addMenuComponent(buttonExit);
		addMenuComponent(buttonBackToGame);
		addMenuComponent(buttonStatistics);

		
		buttonBackToGame.requestFocus();
	}
	
	private void setLocations() {
		buttonBackToGame.setX((getWidth() - buttonBackToGame.getWidth())/2);
		buttonBackToGame.setY(200);
		
		buttonExit.setX((getWidth() - buttonExit.getWidth())/2);
		buttonExit.setY(300);

		buttonStatistics.setX((getWidth() - buttonStatistics.getWidth())/2);
		buttonStatistics.setY(400);
	}

	@Override
	protected void drawOnScreen(Screen screen) {
		super.drawOnScreen(screen);
	}
}