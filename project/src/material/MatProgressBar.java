package material;

import utilities.Theme;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class MatProgressBar extends JComponent {

	private int percentage;

	public MatProgressBar() {
		percentage = 0;
		setPreferredSize(new Dimension(300, 30));
	}

	public void setPercentage(int percentage) {
		this.percentage = percentage;
	}

	@Override
	public void paint(Graphics graphics) {

		Graphics2D graphics2D = (Graphics2D) graphics;
		graphics2D.setPaint(Theme.LIGHT_GRAY.color);
		graphics2D.fill(new RoundRectangle2D.Double(0, 0, getWidth(), 30, 30, 30));

		Color color;
		if (percentage < 35) {
			color = Theme.TURQUOISE.color;
		} else if (percentage < 65) {
			color = Theme.LIGHT_BLUE.color;
		} else if (percentage < 100) {
			color = Theme.BLUE.color;
		} else {
			color = Theme.DARK_BLUE.color;
			percentage = 100;
		}

		graphics2D.setPaint(color);
		graphics2D.fill(new RoundRectangle2D.Double(0, 0, (percentage * getWidth()) / 100.0, 30, 30, 30));

	}

}