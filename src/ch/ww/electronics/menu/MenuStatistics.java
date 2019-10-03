package ch.ww.electronics.menu;

import java.util.HashMap;
import java.util.Map.Entry;

import ch.ww.electronics.game.Game;
import ch.ww.electronics.game.Statistic;
import ch.ww.electronics.graphics.FontCreator;
import ch.ww.electronics.graphics.Screen;

public class MenuStatistics extends DefaultMenu {
	private Statistic stat;
	
	private ListComponent<String> list;
	
	public MenuStatistics(Game game, Menu parentMenu) {
		super(game, parentMenu);
		this.stat = game.getStatistic();
		list = new ListComponent<String>(100, 100, this);
	}
	
	@Override
	public void renderOnScreen(Screen screen) {
		super.renderOnScreen(screen);
		drawInfo(screen);
	}

	private void drawInfo(Screen drawOn) {
		if(stat.getInfo().size() == 0) {
			return;
		}
		Screen s = new Screen(drawOn.getWidth() - 100, drawOn.getHeight() - 250, 0xffffff);
		double xFactor = 1.0 * s.getWidth() / stat.getLastTick();
		double yFactor = s.getHeight() / 1;
		int num = 0;
		int color[] = new int[] {	0x0000ff,0x00ff00,0xff0000,
									0xff00ff,0x00ffff,0xffff00,
									0xff7fff,0x7fffff,0xffff7f,};
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
		
		if(list.getComponents().size() == 0) {
			for(Entry<String, Double> e: stat.getInfo().get(0L).entrySet()) {
				list.addContent(e.getKey(), e.getKey());
			}
			
			list.setBounds(drawOn.getWidth() -100, 150);
			list.setX(drawOn.getWidth() / 2 - list.getWidth() / 2);
			list.setY(50 + s.getHeight() + 20);
		}
		
		
		drawOn.drawScreen(50, 50, s);
	}
}
