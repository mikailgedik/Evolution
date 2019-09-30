package ch.ww.electronics.menu;

import ch.ww.electronics.game.Game;
import ch.ww.electronics.menu.action.ActionInfo;

public class DefaultMenu extends Menu {
	private Button backButton;

	public DefaultMenu(Game game, Menu parentMenu) {
		super(game, parentMenu);
		backButton = Button.createBackButton();
		backButton.setX(0);
		backButton.setY(0);
		backButton.addActionExecuter((ActionInfo info) -> {
			if (DefaultMenu.this.parentMenu != null) {
				showParent();
			} else {
				DefaultMenu.this.close();
			}
		});
		addMenuComponent(backButton);
		backButton.requestFocus();
	}

}