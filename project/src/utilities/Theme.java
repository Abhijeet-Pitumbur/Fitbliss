package utilities;

import java.awt.*;

public enum Theme {

	DARK_BLUE("#113F67"),
	BLUE("#366CA6"),
	LIGHT_BLUE("#5592DB"),
	TURQUOISE("#61B8CA"),
	RED("#D9534F"),
	GRAY("#9E9E9E"),
	LIGHT_GRAY("#E8E8E8");

	public Color color;

	Theme(String hex) {
		color = Color.decode(hex);
	}

}