package ch.ww.electronics.graphics;

public class ManipulatedScreen extends Screen {
	private ScreenFunction f;

	public ManipulatedScreen(int width, int height, ScreenFunction f) {
		super(width, height);
		this.f = f;
	}

	public ManipulatedScreen(int width, int height, int color, ScreenFunction f) {
		super(width, height, color);
		this.f = f;
	}

	public ManipulatedScreen(Screen s, ScreenFunction f) {
		super(s);
		this.f = f;
	}

	@Override
	public int getPixel(int x, int y) {
		if (f != null) {
			return f.apply(x, y, super.getPixel(x, y), this);
		} else {
			return super.getPixel(x, y);
		}
	}

	public void setFunction(ScreenFunction f) {
		this.f = f;
	}

	public ScreenFunction getFunction() {
		return f;
	}

	@Override
	public ManipulatedScreen copy() {
		return new ManipulatedScreen(this, this.f);
	}

	@FunctionalInterface
	public static interface ScreenFunction {
		public int apply(int x, int y, int color, Screen screen);
	}
}