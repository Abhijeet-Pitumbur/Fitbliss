package utilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DateButton extends JButton {

	private Event event;
	private boolean paintBackground = true;
	private Color selectedColor;

	public DateButton() {

		setBorder(null);
		setContentAreaFilled(false);
		setFocusable(false);
		setBackground(Color.WHITE);
		setCursor(new Cursor(Cursor.HAND_CURSOR));

		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent event) {
				if (!getText().equals("") && getName() != null) {
					if (getName().equals("day") || getName().equals("year")) {
						DateButton.this.event.execute(event, Integer.parseInt(getText()));
					} else {
						DateButton.this.event.execute(event, Integer.parseInt(getName()));
					}
					setBackground(getSelectedColor());
					setForeground(Color.WHITE);
				}
			}
		});

	}

	public void setPaintBackground(boolean paintBackground) {
		this.paintBackground = paintBackground;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	@Override
	public void paint(Graphics graphics) {
		if (paintBackground) {
			int width = getWidth();
			int height = getHeight();
			int size = Math.min(width, height);
			int x = (width - size) / 2;
			int y = (height - size) / 2;
			Graphics2D graphics2D = (Graphics2D) graphics;
			graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			graphics2D.setColor(getBackground());
			graphics2D.fillOval(x, y, size, size);
		}
		super.paint(graphics);
	}

	public Color getSelectedColor() {
		return selectedColor;
	}

	public void setSelectedColor(Color selectedColor) {
		this.selectedColor = selectedColor;
	}

}