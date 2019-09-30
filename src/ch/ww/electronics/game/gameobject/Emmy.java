package ch.ww.electronics.game.gameobject;

import org.json.JSONObject;

import ch.ww.electronics.game.level.Level;
import ch.ww.electronics.graphics.Screen;
import ch.ww.electronics.graphics.TextureManager;
import ch.ww.electronics.level.backgroundtile.BackgroundTile;
import ch.ww.electronics.level.backgroundtile.BackgroundTileStone;
import ch.ww.electronics.util.MutableVector2D;

/** {@link Emmy} is the first enemy programmed */
public class Emmy extends GameObject {
	public static final String NAME = "electronics.gameobject.emmy";
	public static final GameObjectConstructor<Emmy> CONSTRUCTOR = new GameObjectConstructor<Emmy>() {
		@Override
		public Emmy createInstance(JSONObject jsonObject) {
			return new Emmy(null, jsonObject.getDouble("x"), jsonObject.getDouble("y"));
		}
	};

	public final static double MAX_SPEED = 0.05;

	private MutableVector2D motion;

	public Emmy(Level level, double x, double y) {
		super(level, x, y);
		motion = new MutableVector2D(0, 0);
		setTexture(new Screen((int) (BackgroundTile.SIZE * getWidth()), (int) (BackgroundTile.SIZE * getHeight()),
				0xffffff));
		setTexture(TextureManager.getInstance().getTexture(/*ItemStonePickaxe.NAME*/ BackgroundTileStone.NAME).getScaledScreen(BackgroundTile.SIZE,
				BackgroundTile.SIZE));

	}

	@Override
	public void tick() {
		//ai.execute();
	}

	public void setMotion(double x, double y) {
		double factor = 1.0;
		if (Math.abs(x) > Math.abs(y)) {
			if (x > MAX_SPEED || x < -MAX_SPEED) {
				factor = MAX_SPEED / x;
			}
		} else {
			if (y > MAX_SPEED || y < -MAX_SPEED) {
				factor = MAX_SPEED / y;
			}
		}

		factor = Math.abs(factor);

		motion.set(x * factor, y * factor);
		motion.round(4);
	}

	public void move() {
		setLocation(getX() + motion.getX(), getY() + motion.getY());
	}

	public MutableVector2D getMotion() {
		return motion;
	}

	@Override
	public GameObjectConstructor<? extends GameObject> getGameObjectConstructor() {
		return CONSTRUCTOR;
	}

	@Override
	public String getName() {
		return NAME;
	}
}