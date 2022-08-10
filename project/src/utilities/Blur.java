package utilities;

import java.awt.image.BufferedImage;

public class Blur {

	public static void blur(BufferedImage image, final double radius, int num) {

		final int width = image.getWidth();
		final int height = image.getHeight();
		final int[] pixels = new int[width * height];
		image.getRGB(0, 0, width, height, pixels, 0, width);

		int[][] channels = new int[4][];
		int[] red = new int[width * height];

		for (int channel = 0; channel <= 3; channel++) {
			int[] blurRed = new int[width * height];
			for (int i = 0; i < width * height; i++) {
				red[i] = (pixels[i] >> (channel * 8)) & 0xFF;
			}
			gaussBlur(red, blurRed, width, height, radius, num);
			channels[channel] = blurRed;
		}

		for (int i = 0; i < width * height; i++) {
			pixels[i] = channels[0][i] | channels[1][i] << 8 | channels[2][i] << 16 | channels[3][i] << 24;
		}

		image.setRGB(0, 0, width, height, pixels, 0, width);

	}

	private static int[] boxesForGauss(double sigma, int num) {

		double wIdeal = Math.sqrt((12 * sigma * sigma / num) + 1);
		int wl = (int) Math.floor(wIdeal);

		if (wl % 2 == 0) {
			wl--;
		}

		int wu = wl + 2;
		double mIdeal = (12 * sigma * sigma - num * wl * wl - 4 * num * wl - 3 * num) / (-4 * wl - 4);
		int m = (int) Math.round(mIdeal);
		int[] sizes = new int[num];

		for (int i = 0; i < num; i++) {
			sizes[i] = i < m ? wl : wu;
		}

		return sizes;

	}

	private static void gaussBlur(int[] scl, int[] tcl, int width, int height, double radius, int num) {
		int[] boxes = boxesForGauss(radius, num);
		boxBlur(scl, tcl, width, height, (boxes[0] - 1) / 2.0);
		boxBlur(tcl, scl, width, height, (boxes[1] - 1) / 2.0);
		boxBlur(scl, tcl, width, height, (boxes[2] - 1) / 2.0);
	}

	private static void boxBlur(int[] scl, int[] tcl, int width, int height, double radius) {
		System.arraycopy(scl, 0, tcl, 0, scl.length);
		boxBlurH4(tcl, scl, width, height, radius);
		boxBlurT4(scl, tcl, width, height, radius);
	}

	private static void boxBlurH4(int[] scl, int[] tcl, int width, int height, double radius) {

		double index = 1 / (radius + radius + 1);

		for (int i = 0; i < height; i++) {

			int ti = i * width;
			int li = ti;
			int ri = (int) (ti + radius);
			int fv = scl[ti];
			int lv = scl[ti + width - 1];
			double val = (radius + 1) * fv;

			for (int j = 0; j < radius; j++) {
				val += scl[ti + j];
			}

			for (int j = 0; j <= radius; j++) {
				val += scl[ri++] - fv;
				tcl[ti++] = (int) Math.round(val * index);
			}

			for (int j = (int) (radius + 1); j < width - radius; j++) {
				val += scl[ri++] - scl[li++];
				tcl[ti++] = (int) Math.round(val * index);
			}

			for (int j = (int) (width - radius); j < width; j++) {
				val += lv - scl[li++];
				tcl[ti++] = (int) Math.round(val * index);
			}

		}

	}

	private static void boxBlurT4(int[] scl, int[] tcl, int width, int height, double radius) {

		double index = 1 / (radius + radius + 1);

		for (int i = 0; i < width; i++) {

			int ti = i;
			int li = ti;
			int ri = (int) (ti + radius * width);
			int fv = scl[ti];
			int lv = scl[ti + width * (height - 1)];
			double val = (radius + 1) * fv;

			for (int j = 0; j < radius; j++) {
				val += scl[ti + j * width];
			}

			for (int j = 0; j <= radius; j++) {
				val += scl[ri] - fv;
				tcl[ti] = (int) Math.round(val * index);
				ri += width;
				ti += width;
			}

			for (int j = (int) (radius + 1); j < height - radius; j++) {
				val += scl[ri] - scl[li];
				tcl[ti] = (int) Math.round(val * index);
				li += width;
				ri += width;
				ti += width;
			}

			for (int j = (int) (height - radius); j < height; j++) {
				val += lv - scl[li];
				tcl[ti] = (int) Math.round(val * index);
				li += width;
				ti += width;
			}

		}

	}

}