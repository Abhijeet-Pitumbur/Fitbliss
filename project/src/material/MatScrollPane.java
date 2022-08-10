package material;

import utilities.Theme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;

public class MatScrollPane extends JScrollPane {

	public MatScrollPane(Component component) {

		super(component);
		setBackground(Color.WHITE);
		setBorder(new EmptyBorder(50, 50, 75, 50));
		getVerticalScrollBar().setUnitIncrement(15);
		getVerticalScrollBar().setBlockIncrement(150);
		setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		getVerticalScrollBar().setUI(new BasicScrollBarUI() {

			@Override
			public void paintTrack(Graphics graphics, JComponent component, Rectangle rectangle) {
				graphics.setColor(Color.WHITE);
				graphics.fillRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
			}

			@Override
			public JButton createDecreaseButton(int orientation) {
				JButton button = new JButton();
				button.setPreferredSize(new Dimension(0, 0));
				return button;
			}

			@Override
			public JButton createIncreaseButton(int orientation) {
				JButton button = new JButton();
				button.setPreferredSize(new Dimension(0, 0));
				return button;
			}

			@Override
			public Dimension getMinimumThumbSize() {
				return new Dimension(5, 50);
			}

			@Override
			public void paintThumb(Graphics graphics, JComponent component, Rectangle rectangle) {
				if (!rectangle.isEmpty() && this.scrollbar.isEnabled()) {
					((Graphics2D) graphics).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
					graphics.setColor(Theme.GRAY.color);
					graphics.fillRoundRect(rectangle.x, rectangle.y, 8, rectangle.height, rectangle.width, 10);
				}
			}

			@Override
			public void layoutContainer(Container scrollbarContainer) {
				super.layoutContainer(scrollbarContainer);
				incrButton.setBounds(0, 0, 0, 0);
				decrButton.setBounds(0, 0, 0, 0);
			}

		});

	}

}