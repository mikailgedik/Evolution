package ch.ww.electronics.menu;

import ch.ww.electronics.game.Game;
import ch.ww.electronics.game.Statistic;

public class MenuStatistics extends Menu {
	private Statistic stat;
	public MenuStatistics(Game game, Menu parentMenu) {
		super(game, parentMenu);
		this.stat = game.getStatistic();
	}

}
