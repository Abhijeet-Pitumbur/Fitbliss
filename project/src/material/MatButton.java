package material;

import utilities.Elevation;
import utilities.Ripple;
import utilities.Shadow;
import utilities.Utilities;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

public class MatButton extends JButton {

	private final Ripple ripple;
	private final Elevation elevation;
	private ButtonType type = ButtonType.DEFAULT;
	private boolean isMousePressed = false;
	private boolean isMouseOver = false;
	private Color rippleColor = Color.WHITE;
	private Cursor cursor = super.getCursor();
	private int borderRadius = 2;

	public MatButton() {

		ripple = Ripple.applyTo(this);
		elevation = Elevation.applyTo(this, 0);
		setOpaque(false);
		setCursor(new Cursor(Cursor.HAND_CURSOR));

		addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent event) {
				isMousePressed = true;
				elevation.setLevel(getElevation());
			}

			@Override
			public void mouseReleased(MouseEvent event) {
				isMousePressed = false;
				elevation.setLevel(getElevation());
			}

			@Override
			public void mouseEntered(MouseEvent event) {
				isMouseOver = true;
				elevation.setLevel(getElevation());
			}

			@Override
			public void mouseExited(MouseEvent event) {
				isMouseOver = false;
				elevation.setLevel(getElevation());
			}

		});

		setUI(new BasicButtonUI() {
			@Override
			public boolean contains(JComponent component, int x, int y) {
				return x > Shadow.offsetLeft && y > Shadow.offsetTop && x < getWidth() - Shadow.offsetRight && y < getHeight() - Shadow.offsetBottom;
			}
		});

	}

	public ButtonType getType() {
		return type;
	}

	public void setType(ButtonType type) {
		this.type = type;
	}

	@Override
	public void setBackground(Color background) {
		super.setBackground(background);
		setForeground(Utilities.isDark(background) ? Color.WHITE : Color.BLACK);
		setRippleColor(Utilities.isDark(background) ? Color.WHITE : Utilities.darken(Utilities.darken(background)));
	}

	public void setRippleColor(Color rippleColor) {
		this.rippleColor = rippleColor;
	}

	public void setBorderRadius(int borderRadius) {
		this.borderRadius = borderRadius;
		elevation.setBorderRadius(borderRadius);
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		super.setCursor(enabled ? cursor : Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}

	@Override
	public void setCursor(Cursor cursor) {
		super.setCursor(cursor);
		this.cursor = cursor;
	}

	@Override
	public void processFocusEvent(FocusEvent event) {
		super.processFocusEvent(event);
	}

	@Override
	public void processMouseEvent(MouseEvent event) {
		super.processMouseEvent(event);
	}

	private int getElevation() {
		if (isMousePressed) {
			return 2;
		} else if (type == ButtonType.RAISED || isMouseOver) {
			return 1;
		} else {
			return 0;
		}
	}

	@Override
	public void paintComponent(Graphics graphics) {

		Graphics2D graphics2d = (Graphics2D) graphics;
		graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		graphics2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		if ((type != ButtonType.FLAT) && isEnabled()) {
			elevation.paint(graphics);
			graphics2d.translate(Shadow.offsetLeft, Shadow.offsetTop);
		}

		int offsetTb, offsetLr;

		if (type == ButtonType.FLAT) {
			offsetTb = 0;
			offsetLr = 0;
		} else {
			offsetTb = Shadow.offsetTop + Shadow.offsetBottom;
			offsetLr = Shadow.offsetLeft + Shadow.offsetRight;
		}

		if (isEnabled()) {
			graphics2d.setColor(getBackground());
			graphics2d.fill(new RoundRectangle2D.Double(0, 0, getWidth() - offsetLr, getHeight() - offsetTb, borderRadius, borderRadius));
			graphics2d.setColor(new Color(rippleColor.getRed() / 255f, rippleColor.getBlue() / 255f, rippleColor.getBlue() / 255f, 0.12f));
			if (type == ButtonType.FLAT) {
				elevation.paint(graphics);
			}
		} else {
			Color background = getBackground();
			graphics2d.setColor(new Color(background.getRed() / 255f, background.getGreen() / 255f, background.getBlue() / 255f, 0.6f));
			graphics2d.fill(new RoundRectangle2D.Double(0, 0, getWidth() - offsetLr, getHeight() - offsetTb, borderRadius * 2, borderRadius * 2));
		}

		FontMetrics metrics = graphics.getFontMetrics(getFont());
		int x = (getWidth() - offsetLr - metrics.stringWidth(getText())) / 2;
		int y = (getHeight() - offsetTb - metrics.getHeight()) / 2 + metrics.getAscent();
		graphics2d.setFont(getFont());

		if (isEnabled()) {
			graphics2d.setColor(getForeground());
		} else {
			Color foreground = getForeground();
			graphics2d.setColor(new Color(foreground.getRed() / 255f, foreground.getGreen() / 255f, foreground.getBlue() / 255f, 0.6f));
		}

		graphics2d.drawString(getText(), x, y);

		if (isEnabled()) {
			try {
				graphics2d.setClip(new RoundRectangle2D.Double(0, 0, getWidth() - offsetLr, getHeight() - offsetTb, Math.max(borderRadius * 2 - 4, 0), Math.max(borderRadius * 2 - 4, 0)));
				graphics2d.setColor(rippleColor);
				ripple.paint(graphics2d);
			} catch (Exception ignored) {

			}
		}

	}

	@Override
	public void paintBorder(Graphics ignored) {

	}

	public enum ButtonType {
		DEFAULT,
		RAISED,
		FLAT
	}

}