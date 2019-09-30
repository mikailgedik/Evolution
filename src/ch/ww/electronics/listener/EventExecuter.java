package ch.ww.electronics.listener;

public interface EventExecuter {
	public void keyPressed(int code);
	public void keyReleased(int code);
	public void mouseClicked(int x, int y, int mouseButton);
}