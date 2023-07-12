package utilities;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

public class Shadow {

	public static final int offsetTop = 2;
	public static final int offsetLeft = 2;
	public static final int offsetBottom = 2;
	public static final int offsetRight = 2;
	private int pWd, pHt;
	private double pLv, pRd;
	private BufferedImage shadowBackground;

	public Shadow() {

	}

	public static BufferedImage renderShadow(int width, int height, double level, double borderRadius) {

		if (level <= 0) {
			level = 0;
		}

		if (level >= 2) {
			level = 2;
		}

		float opacity = (float) ((2 / (1 + Math.exp(-2 * level))) - 1);
		float radius = (float) ((4 / (1 + Math.exp(-2 * level))) - 2);
		BufferedImage shadowBlurImage = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D graphics2D = (Graphics2D) shadowBlurImage.getGraphics();

		graphics2D.setColor(new Color(0f, 0f, 0f, opacity));
		graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		graphics2D.fill(new RoundRectangle2D.Double(offsetLeft, offsetTop, width - offsetLeft - offsetRight, height - offsetTop - offsetBottom, (float) borderRadius, (float) borderRadius));
		graphics2D.dispose();
		Blur.blur(shadowBlurImage, radius, 3);

		return shadowBlurImage;

	}

	public BufferedImage render(int width, int height, double radius, double level) {
		if (pWd != width || pHt != height || pRd != radius || pLv != level) {
			shadowBackground = Shadow.renderShadow(width, height, level, radius);
			pWd = width;
			pHt = height;
			pRd = radius;
			pLv = level;
		}
		return shadowBackground;
	}

}