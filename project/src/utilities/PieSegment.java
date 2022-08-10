package utilities;

import java.awt.*;

public record PieSegment(String label, double value, Color color) {

	public String getLabel() {
		return label;
	}

	public double getValue() {
		return value;
	}

	public Color getColor() {
		return color;
	}

}