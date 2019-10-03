package ch.ww.electronics.menu;

import java.awt.FileDialog;
import java.awt.Frame;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;

import ch.ww.electronics.game.Game;
import ch.ww.electronics.game.Statistic;
import ch.ww.electronics.graphics.FontCreator;
import ch.ww.electronics.graphics.Screen;
import ch.ww.electronics.menu.action.ActionInfo;

public class MenuStatistics extends DefaultMenu {
	private Statistic stat;
	private TextedButton outputToFile;
	
	private ListComponent<String> list;
	
	public MenuStatistics(Game game, Menu parentMenu) {
		super(game, parentMenu);
		this.stat = game.getStatistic();
		list = new ListComponent<String>(100, 100, this);
		outputToFile = new TextedButton("Output to file");
		
		outputToFile.addActionExecuter((ActionInfo s) -> {
			FileDialog d = new FileDialog((Frame) getGame().getGameCanvas().getParent().getParent().getParent().getParent());
			d.setDirectory("C:\\Users\\Mikail Gedik\\Desktop");
			d.setTitle("Saving data to...");
			d.setMode(FileDialog.SAVE);
			
			d.setVisible(true);
			if(d.getFiles().length != 0) {
				try {
					writeStatsToFile(d.getFiles()[0]);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
			
			d.setVisible(false);
		});
		
		this.addMenuComponent(outputToFile);
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
		int color[] = new int[] {	0xBFBFBF, 0x00ff00,0xff0000,
									0xff00ff, 0x00ffff,0xffff00,
									0x0		, 0xff0000, 0x7f7f7f};
		
		for(Entry<Long, HashMap<String, Double>> e :stat.getInfo().entrySet()) {
			for(Entry<String, Double> a: e.getValue().entrySet()) {
				int x = (int)(xFactor * e.getKey());
				int y = s.getHeight() - (int)(a.getValue() * yFactor);
				s.fillCircle(x-1, y-1, color[num], 1);
				s.setPixel(x, y, color[num]);
				num++;
			}
			num = 0;
		}
		
		num = 0;
		for(Entry<String, Double> e: stat.getInfo().get(0L).entrySet()) {
			Screen f = FontCreator.createFont(e.getKey(), color[num], -1);
			f = f.getScaledScreen(f.getWidth()/2, f.getHeight()/2);
			s.drawScreen(s.getWidth( )- f.getWidth(), num * f.getHeight(), f);
			num++;
		}
		
		if(list.getCount() == 0) {
			for(Entry<String, Double> e: stat.getInfo().get(0L).entrySet()) {
				list.addContent(e.getKey(), e.getKey());
			}
			
			list.setBounds(drawOn.getWidth() -100, 150);
			list.setX(drawOn.getWidth() / 2 - list.getWidth() / 2);
			list.setY(50 + s.getHeight() + 20);
			
			outputToFile.setX(drawOn.getWidth()/2 - outputToFile.getWidth() / 2);
			outputToFile.setY(drawOn.getHeight() - outputToFile.getHeight());
		}
		
		drawOn.drawScreen(50, 50, s);
	}

	public void writeStatsToFile(File f) throws IOException {
		BufferedWriter w = new BufferedWriter(new FileWriter(f));
		
		w.write("tick;");
		for(Entry<String, Double> e: stat.getInfo().get(0L).entrySet()) {
			w.write(e.getKey() + ";");
		}
		
		w.newLine();
		
		for(Entry<Long, HashMap<String, Double>> map: stat.getInfo().entrySet()) {
			w.write(Long.toString(map.getKey()) + ";");
			
			for(Entry<String, Double> e: map.getValue().entrySet()) {
				w.write(Math.round(e.getValue() * 100)/100.0 + ";");

			}
			w.newLine();
		}
		
		w.close();
		System.out.println("DONE");
	}
}
