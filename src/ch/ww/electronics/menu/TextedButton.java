package ch.ww.electronics.menu;

import ch.ww.electronics.graphics.FontCreator;
import ch.ww.electronics.graphics.Screen;

public class TextedButton extends Button {
	private String text;
	private Screen screen;

	public TextedButton(String text) {
		super(createScreenForText(text));
	}

	public void setText(String text) {
		this.screen = createScreenForText(text);
		this.text = text;

		setBounds(screen.getWidth(), screen.getHeight());
	}

	public String getText() {
		return text;
	}

	private static Screen createScreenForText(String text) {
		Screen screen = FontCreator.createFont(text, FONT_COLOR, 0);
		return new Screen(screen.getWidth() + TextedButton.SELECTED_BORDER_WIDTH,
				screen.getHeight() + TextedButton.SELECTED_BORDER_WIDTH).drawScreen(
						TextedButton.SELECTED_BORDER_WIDTH / 2, TextedButton.SELECTED_BORDER_WIDTH / 2, screen);
	}
}