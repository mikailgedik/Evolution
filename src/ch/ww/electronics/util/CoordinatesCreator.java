package ch.ww.electronics.util;

import java.util.ArrayList;

public class CoordinatesCreator {
	private CoordinatesCreator() {
	}

	public static ArrayList<Integer[]> drawCircle(int xCenter, int yCenter, int color, int radius) {
		ArrayList<Integer[]> ret = new ArrayList<>();
		int d = -radius;
		int x = radius;
		int y = 0;

		while (y <= x) {
			ret.add(new Integer[] { x + xCenter, y + yCenter, color });
			ret.add(new Integer[] { -x + xCenter, y + yCenter, color });
			ret.add(new Integer[] { x + xCenter, -y + yCenter, color });
			ret.add(new Integer[] { -x + xCenter, -y + yCenter, color });

			ret.add(new Integer[] { y + xCenter, x + yCenter, color });
			ret.add(new Integer[] { -y + xCenter, x + yCenter, color });
			ret.add(new Integer[] { y + xCenter, -x + yCenter, color });
			ret.add(new Integer[] { -y + xCenter, -x + yCenter, color });

			d = d + 2 * y + 1;
			y = y + 1;

			if (d > 0) {
				d = d - 2 * x + 2;
				x = x - 1;
			}
		}
		return ret;
	}

	/**
	 * ret[0] <br>
	 * ret[1] = y <br>
	 * ret[2] = distance to mid^2
	 */
	public static ArrayList<Integer[]> createFilledCircle(int xStart, int yStart, int radius) {
		ArrayList<Integer[]> ret = new ArrayList<>();
		int xCor, yCor, max = radius * radius, c;
		for (int x = -radius; x < radius; x++) {
			xCor = x + xStart;
			if (xCor < 0) {
				continue;
			}
			yLoop: for (int y = -radius; y < radius; y++) {
				yCor = y + yStart;
				if (yCor < 0) {
					continue yLoop;
				}
				c = x * x + y * y;
				if (c < max) {
					ret.add(new Integer[] { xCor, yCor, c });
				}
			}
		}
		return ret;
	}

	/**
	 * Taken from <a href=
	 * http://www.mttcs.org/Skripte/Pra/Material/vorlesung9.pdf">http://www.mttcs.org/Skripte/Pra/Material/vorlesung9.pdf</a>
	 * (Bresenham-Algorithmus)
	 */
	public static ArrayList<Integer[]> createLine(int xstart, int ystart, int xend, int yend) {
		ArrayList<Integer[]> ret = new ArrayList<>();
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
		ret.add(new Integer[] { x, y });

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
			ret.add(new Integer[] { x, y });
		}
		return ret;
	}
}