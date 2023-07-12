package utilities;

import java.awt.*;

public class Utilities {

	public static boolean isDark(Color color) {
		return (color.getRed() * 0.2125 + color.getGreen() * 0.7154 + color.getBlue() * 0.0721) < (0.535 * 255);
	}

	public static Color applyAlphaMask(Color color, int bitMask) {
		return new Color(color.getRGB() & 0x00FFFFFF | (bitMask & 0xFF000000), true);
	}

	public static Color darken(Color color) {
		int r = clamp(color.getRed() - 30);
		int g = clamp(color.getGreen() - 30);
		int b = clamp(color.getBlue() - 30);
		return new Color(r, g, b, color.getAlpha());
	}

	public static Color brighten(Color color) {
		int r = clamp(color.getRed() + 30);
		int g = clamp(color.getGreen() + 30);
		int b = clamp(color.getBlue() + 30);
		return new Color(r, g, b, color.getAlpha());
	}

	public static Color brighten(Color color, int level) {
		int r = clamp(color.getRed() + level);
		int g = clamp(color.getGreen() + level);
		int b = clamp(color.getBlue() + level);
		return new Color(r, g, b, color.getAlpha());
	}

	private static int clamp(int i) {
		return Math.min(255, Math.max(0, i));
	}

}