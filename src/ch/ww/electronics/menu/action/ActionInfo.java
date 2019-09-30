package ch.ww.electronics.menu.action;

import ch.ww.electronics.menu.MenuComponent;

public class ActionInfo {
	private final MenuComponent source;
	private final long when;
	private final Object data;

	public ActionInfo(MenuComponent source, long when) {
		this(source, when, null);
	}

	public ActionInfo(MenuComponent source, long when, Object data) {
		this.source = source;
		this.when = when;
		this.data = data;
	}

	public long getWhen() {
		return when;
	}

	public MenuComponent getSource() {
		return source;
	}

	public Object getData() {
		return data;
	}
}