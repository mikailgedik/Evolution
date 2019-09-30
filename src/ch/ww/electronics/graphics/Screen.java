package ch.ww.electronics.graphics;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Function;

import javax.imageio.ImageIO;

public class Screen {
	private final int[] pixels;
	private final int width, height;

	public Screen(int width, int height) {
		this(width, height, -1);
	}

	public Screen(int width, int height, int color) {
		assert width > 0 : "width <= 0";
		assert height > 0 : "height <= 0";
		this.pixels = new int[width * height];
		this.width = width;
		this.height = height;
		Arrays.fill(getPixels(), color);
	}

	public Screen(Screen s) {
		this.pixels = s.getPixels().clone();
		this.width = s.getWidth();
		this.height = s.getHeight();
	}

	public int[] getPixels() {
		return pixels;
	}

	public Screen drawScreen(int xPoint, int yPoint, Screen screen) {
		Objects.requireNonNull(screen, "screen == null");
		return drawScreen(xPoint, yPoint, 0, 0, screen.getWidth(), screen.getHeight(), screen);
	}

	public Screen drawScreen(int xPoint, int yPoint, int xStart, int yStart, int w, int h, Screen screen) {
		Objects.requireNonNull(screen, "screen == null");
		for (int y = 0; y < h; y++) {
			int yPix = y + yPoint;
			if (yPix < 0 || yPix >= height)
				continue;

			for (int x = 0; x < w; x++) {
				int xPix = x + xPoint;
				if (xPix < 0 || xPix >= width)
					continue;

				int src = screen.getPixel((x + xStart), (y + yStart));
				if (src != -1) {
					getPixels()[xPix + yPix * width] = src;
				}
			}
		}
		return this;
	}

	public Screen subScreen(int xPoint, int yPoint, int width, int height) {
		Screen screen = new Screen(width, height);
		for (int y = 0; y < height; y++) {
			int yOnThis = y + yPoint;
			if (yOnThis >= this.height) {
				return screen;
			}
			for (int x = 0; x < width; x++) {
				int xOnThis = x + xPoint;
				if (xOnThis >= this.width)
					continue;
				screen.getPixels()[x + y * width] = getPixels()[xOnThis + yOnThis * this.width];
			}
		}
		return screen;
	}

	public Screen copy() {
		Screen screen = new Screen(width, height);
		System.arraycopy(getPixels(), 0, screen.getPixels(), 0, getPixels().length);
		return screen;
	}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}

	public Screen fill(int color) {
		Arrays.fill(getPixels(), color);
		return this;
	}

	public BufferedImage toBufferedImage() {
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		for (int x = 0; x < image.getWidth(); x++) {
			for (int y = 0; y < image.getHeight(); y++) {
				image.setRGB(x, y, getPixels()[y * width + x]);
			}
		}
		return image;
	}

	public Screen fillRect(int xStart, int yStart, int width, int height, int color) {
		for (int y = yStart; y < yStart + height; y++) {
			if (y >= this.height) {
				return this;
			}
			if (y < 0) {
				continue;
			}
			for (int x = xStart; x < xStart + width; x++) {
				if (x < 0 || x >= this.width) {
					continue;
				}
				int dest = this.width * y + x;
				setPixel(dest, color);
			}
		}
		return this;
	}

	public Screen drawRect(int xStart, int yStart, int width, int height, int color) {
		for (int x = xStart; x <= xStart + width; x++) {
			setPixel(x, yStart, color);
			setPixel(x, yStart + width, color);
		}
		for (int y = yStart; y <= yStart + height; y++) {
			setPixel(xStart, y, color);
			setPixel(xStart + width, y, color);
		}
		return this;
	}

	public Screen getScaledScreen(int width, int height) {
		return new ScreenScaler(this, width, height).scaledInstance();
	}

	public static Screen toScreen(BufferedImage image) {
		Screen screen = new Screen(image.getWidth(), image.getHeight());
		for (int x = 0; x < image.getWidth(); x++) {
			for (int y = 0; y < image.getHeight(); y++) {
				int rgb, alpha;
				rgb = image.getRGB(x, y);
				alpha = ((rgb >> 24) & 0xFF);
				if (alpha == 0) {
					rgb = -1;
				} else {
					// Remove alpha
					rgb = rgb & (0xFFFFFF | -1);
				}

				screen.getPixels()[y * screen.getWidth() + x] = rgb;
			}
		}
		return screen;
	}

	public static Screen fromURL(URL url) {
		Objects.requireNonNull(url, "url == null");
		try {
			return toScreen(ImageIO.read(url));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public Screen forRect(int xStart, int yStart, int width, int height, Function<Integer, Integer> f) {
		for (int y = yStart; y < yStart + height; y++) {
			if (y >= this.height) {
				return this;
			}
			if (y < 0) {
				continue;
			}
			for (int x = xStart; x < xStart + width; x++) {
				if (x < 0 || x >= this.width) {
					continue;
				}
				int dest = this.width * y + x;
				setPixel(dest, f.apply(dest));
			}
		}
		return this;
	}

	public boolean setPixel(int x, int y, int color) {
		if (x >= getWidth() || x < 0 || y >= getHeight() || y < 0) {
			return false;
		} else {
			return setPixel(x + y * width, color);
		}
	}

	public boolean setPixel(int loc, int color) {
		if (loc >= getPixels().length || loc < 0) {
			return false;
		}
		if (color < -1) {
			color = -1;
		}
		pixels[loc] = color;
		return true;
	}

	public int getPixel(int x, int y) {
		if (x > -1 && y > -1 && x < getWidth() && y < getHeight()) {
			return getPixels()[y * getWidth() + x];
		} else {
			return -2;
		}
	}

	public void drawLine(int xStart, int yStart, int xEnd, int yEnd, int color, int brushSize) {

		int x, y, t, dx, dy, incx, incy, pdx, pdy, ddx, ddy, deltaslowdirection, deltafastdirection, err;

		/* Entfernung in beiden Dimensionen berechnen */
		dx = xEnd - xStart;
		dy = yEnd - yStart;

		/* Vorzeichen des Inkrements bestimmen */
		incx = (dx > 0) ? 1 : (dx < 0) ? -1 : 0;
		incy = (dy > 0) ? 1 : (dy < 0) ? -1 : 0;
		if (dx < 0)
			dx = -dx;
		if (dy < 0)
			dy = -dy;

		/* feststellen, welche Entfernung größer ist */
		if (dx > dy) {
			/* x ist schnelle Richtung */
			pdx = incx;
			pdy = 0; /* pd. ist Parallelschritt */
			ddx = incx;
			ddy = incy; /* dd. ist Diagonalschritt */
			deltaslowdirection = dy;
			deltafastdirection = dx;
		} else {
			/* y ist schnelle Richtung */
			pdx = 0;
			pdy = incy; /* pd. ist Parallelschritt */
			ddx = incx;
			ddy = incy; /* dd. ist Diagonalschritt */
			deltaslowdirection = dx;
			deltafastdirection = dy;
		}

		/* Initialisierungen vor Schleifenbeginn */
		x = xStart;
		y = yStart;
		err = deltafastdirection / 2;
		setPixel(x, y, color);

		/* Pixel berechnen */
		for (t = 0; t < deltafastdirection; ++t) {
			err -= deltaslowdirection;
			if (err < 0) {
				err += deltafastdirection;
				x += ddx;
				y += ddy;
			} else {
				x += pdx;
				y += pdy;
			}

			fillCircle(x - brushSize, y - brushSize, color, brushSize);
		}

	}

	public Screen drawCircle(int xStart, int yStart, int color, int r) {
		int d = -r;
		int x = r;
		int y = 0;

		xStart += r;
		yStart += r;

		while (y <= x) {
			setPixel(x + xStart, y + yStart, color);
			setPixel(-x + xStart, y + yStart, color);
			setPixel(x + xStart, -y + yStart, color);
			setPixel(-x + xStart, -y + yStart, color);

			setPixel(y + xStart, x + yStart, color);
			setPixel(-y + xStart, x + yStart, color);
			setPixel(y + xStart, -x + yStart, color);
			setPixel(-y + xStart, -x + yStart, color);

			d = d + 2 * y + 1;
			y = y + 1;

			if (d > 0) {
				d = d - 2 * x + 2;
				x = x - 1;
			}
		}
		return this;
	}

	public Screen fillRectTo(int xStart, int yStart, int xEnd, int yEnd, int color) {
		int width = xEnd - xStart;
		if (width < 0) {
			width = -width;
			xStart = xEnd;
		}

		return fillRect(xStart, yStart, width, yEnd - yStart, color);
	}

	public Screen fillCircle(int xStart, int yStart, int color, int r) {
		int d = -r;
		int x = r;
		int y = 0;

		xStart += r;
		yStart += r;

		while (y <= x) {
			drawLine(x + xStart, y + yStart, -x + xStart, y + yStart, color);
			drawLine(x + xStart, -y + yStart, -x + xStart, -y + yStart, color);
			drawLine(y + xStart, x + yStart, -y + xStart, x + yStart, color);
			drawLine(y + xStart, -x + yStart, -y + xStart, -x + yStart, color);

			d = d + 2 * y + 1;
			y = y + 1;

			if (d > 0) {
				d = d - 2 * x + 2;
				x = x - 1;
			}
		}
		return this;
	}

	/**
	 * Taken from <a href=
	 * http://www.mttcs.org/Skripte/Pra/Material/vorlesung9.pdf">http://www.mttcs.org/Skripte/Pra/Material/vorlesung9.pdf</a>
	 * (Bresenham-Algorithmus)
	 */
	public void drawLine(int xstart, int ystart, int xend, int yend, int color) {
		int x, y, t, dx, dy, incx, incy, pdx, pdy, ddx, ddy, deltaslowdirection, deltafastdirection, err;

		/* Entfernung in beiden Dimensionen berechnen */
		dx = xend - xstart;
		dy = yend - ystart;

		/* Vorzeichen des Inkrements bestimmen */
		incx = (dx > 0) ? 1 : (dx < 0) ? -1 : 0;
		incy = (dy > 0) ? 1 : (dy < 0) ? -1 : 0;
		if (dx < 0)
			dx = -dx;
		if (dy < 0)
			dy = -dy;

		/* feststellen, welche Entfernung größer ist */
		if (dx > dy) {
			/* x ist schnelle Richtung */
			pdx = incx;
			pdy = 0; /* pd. ist Parallelschritt */
			ddx = incx;
			ddy = incy; /* dd. ist Diagonalschritt */
			deltaslowdirection = dy;
			deltafastdirection = dx;
		} else {
			/* y ist schnelle Richtung */
			pdx = 0;
			pdy = incy; /* pd. ist Parallelschritt */
			ddx = incx;
			ddy = incy; /* dd. ist Diagonalschritt */
			deltaslowdirection = dx;
			deltafastdirection = dy;
		}

		/* Initialisierungen vor Schleifenbeginn */
		x = xstart;
		y = ystart;
		err = deltafastdirection / 2;
		setPixel(x, y, color);

		/* Pixel berechnen */
		for (t = 0; t < deltafastdirection; ++t) {
			err -= deltaslowdirection;
			if (err < 0) {
				err += deltafastdirection;
				x += ddx;
				y += ddy;
			} else {
				x += pdx;
				y += pdy;
			}
			setPixel(x, y, color);
		}
	}

	/** Multiplies the pixel's red green blue values by the factor */
	public Screen darkScreen(double factor) {
		if (factor == 1) {
			return this;
		}
		int rgb;
		int[] c = new int[3];
		for (int i = 0; i < getPixels().length; i++) {
			rgb = getPixels()[i];
			if (rgb == -1) {
				continue;
			}

			c[0] = (rgb >> 16) & 0xFF;
			c[1] = (rgb >> 8) & 0xFF;
			c[2] = (rgb >> 0) & 0xFF;

			for (int x = 0; x < c.length; x++) {
				c[x] = (int) (c[x] * factor);
				if (c[x] > 0xFF) {
					c[x] = 0xFF;
				}
			}

			rgb = ((c[0] & 0xFF) << 16) | ((c[1] & 0xFF) << 8) | ((c[2] & 0xFF) << 0);
			getPixels()[i] = rgb;
		}
		return this;
	}

	/** Changes the pixel's values by the given amount */
	public Screen changePixelsByNumber(int num) {
		if (num == 0) {
			return this;
		}
		int rgb;
		int[] c = new int[3];
		for (int i = 0; i < getPixels().length; i++) {
			rgb = getPixels()[i];
			if (rgb == -1) {
				continue;
			}

			c[0] = (rgb >> 16) & 0xFF;
			c[1] = (rgb >> 8) & 0xFF;
			c[2] = (rgb >> 0) & 0xFF;

			for (int x = 0; x < c.length; x++) {
				c[x] = c[x] + num;
				if (c[x] > 0xFF) {
					c[x] = 0xFF;
				} else if (c[x] < 0) {
					c[x] = 0;
				}
			}

			rgb = ((c[0] & 0xFF) << 16) | ((c[1] & 0xFF) << 8) | ((c[2] & 0xFF) << 0);
			setPixel(i, rgb);
		}
		return this;
	}

	public Screen drawOval(int xStart, int yStart, int width, int height, int color) {
		if (width == height) {
			return drawCircle(xStart, yStart, color, width / 2);
		}
		width /= 2;
		height /= 2;
		xStart += width;
		yStart += height;

		int yDest;
		for (int x = 0; x < width; x++) {
			yDest = y(x, width, height);
			setPixel(xStart + x, yStart + yDest, color);
			setPixel(xStart + x, yStart - yDest, color);

			setPixel(xStart - x, yStart + yDest, color);
			setPixel(xStart - x, yStart - yDest, color);
		}
		int xDest;
		for (int y = 0; y < height; y++) {
			xDest = x(y, width, height);
			setPixel(xStart + xDest, yStart + y, color);
			setPixel(xStart + xDest, yStart - y, color);

			setPixel(xStart - xDest, yStart + y, color);
			setPixel(xStart - xDest, yStart - y, color);
		}

		return this;
	}

	private int y(int x, int a, int b) {
		return (int) Math.sqrt(b * b - (b * b * x * x) / (a * a));
	}

	private int x(int y, int a, int b) {
		return (int) Math.sqrt((a * a * (b * b - y * y)) / (b * b));
	}

	public Screen fillOval(int xStart, int yStart, int width, int height, int color) {
		if (width == height) {
			return fillCircle(xStart, yStart, color, width / 2);
		}

		width /= 2;
		height /= 2;
		xStart += width;
		yStart += height;

		int yDest;
		for (int x = 0; x < width; x++) {
			yDest = y(x, width, height);
			drawLine(xStart + x, yStart + yDest, xStart + x, yStart - yDest, color);
			drawLine(xStart - x, yStart + yDest, xStart - x, yStart - yDest, color);
		}

		return this;
	}

	public int getX(int loc) {
		return loc % getWidth();
	}

	public int getY(int loc) {
		return loc / getWidth();
	}

	public static int getRed(int p) {
		return (p >> 16) & 0xFF;
	}

	public static int getGreen(int p) {
		return (p >> 8) & 0xFF;
	}

	public static int getBlue(int p) {
		return (p >> 0) & 0xFF;
	}

	public static int getColor(int r, int g, int b) {
		// assert !(r > 255 || r < 0);
		// assert !(g > 255 || g < 0);
		// assert !(b > 255 || b < 0);
		if (r > 255) {
			r = 255;
		} else if (r < 0) {
			r = 0;
		}
		if (g > 255) {
			g = 255;
		} else if (g < 0) {
			g = 0;
		}
		if (b > 255) {
			b = 255;
		} else if (b < 0) {
			b = 0;
		}

		return ((r & 0xFF) << 16) | ((g & 0xFF) << 8) | ((b & 0xFF) << 0);
	}
}