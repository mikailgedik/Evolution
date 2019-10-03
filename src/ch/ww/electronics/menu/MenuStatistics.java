package ch.ww.electronics.menu;

import ch.ww.electronics.game.Game;
import ch.ww.electronics.game.Statistic;
import ch.ww.electronics.graphics.Screen;

public class MenuStatistics extends DefaultMenu {
	private Statistic stat;
	public MenuStatistics(Game game, Menu parentMenu) {
		super(game, parentMenu);
		this.stat = game.getStatistic();
	}
	
	@Override
	public void renderOnScreen(Screen screen) {
		super.renderOnScreen(screen);
		drawInfo(screen);
	}

	private void drawInfo(Screen drawOn) {
		Screen s = new Screen(drawOn.getWidth() - 100, drawOn.getHeight() - 100, 0xffffff);
		
		
		drawOn.drawScreen(50, 50, s);
	}
}
