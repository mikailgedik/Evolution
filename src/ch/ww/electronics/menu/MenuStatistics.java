package ch.ww.electronics.menu;

import java.util.HashMap;
import java.util.Map.Entry;

import ch.ww.electronics.game.Game;
import ch.ww.electronics.game.Statistic;
import ch.ww.electronics.graphics.FontCreator;
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
		double xFactor = 1.0 * s.getWidth() / stat.getLastTick();
		double yFactor = s.getHeight() / 1;
		int num = 0;
		int color[] = new int[] {};
		for(int i = 0; i < color.length; i++) {
			color[i] = (int) (0xffffff * i / color.length);
		}
		for(Entry<Long, HashMap<String, Double>> e :stat.getInfo().entrySet()) {
			for(Entry<String, Double> a: e.getValue().entrySet()) {
				int x = (int)(xFactor * e.getKey());
				int y = (int)(a.getValue() * yFactor);
				s.fillCircle(x-1, y-1, color[num], 1);
				s.setPixel(x, y, color[num]);
				num++;
			}
			
			num = 0;
		}
		
		FontCreator.drawFontOnScreen("Hi", 150, 0, drawOn, 0xffffff);
		
		drawOn.drawScreen(50, 50, s);
	}
}
