package material;

import app.App;
import utilities.FloatingLabel;
import utilities.Line;
import utilities.Theme;
import utilities.Utilities;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;

public class MatTextField extends JTextField {

	public static final int hintOpacityMask = 0x99000000;
	public static final int lineOpacityMask = 0x66000000;
	private final FloatingLabel hintLabel = new FloatingLabel(this);
	private final Line line = new Line(this);
	private final Color accentColor = Theme.TURQUOISE.color;

	public MatTextField() {

		super();
		setBorder(null);
		setFont(App.getFont().deriveFont(16f));
		hintLabel.setText("");
		setOpaque(false);
		setBackground(Color.WHITE);

		setCaret(new DefaultCaret() {
			@Override
			public synchronized void damage(Rectangle rectangle) {
				MatTextField.this.repaint();
			}
		});

		getCaret().setBlinkRate(500);

	}

	public String getLabel() {
		return hintLabel.getText();
	}

	public void setLabel(String label) {
		hintLabel.setText(label);
		repaint();
	}

	@Override
	public void setForeground(Color foreground) {
		super.setForeground(foreground);
		if (hintLabel != null) {
			hintLabel.updateForeground();
		}
	}

	@Override
	public void setText(String text) {
		super.setText(text);
		hintLabel.update();
		line.update();
	}

	@Override
	public void processFocusEvent(FocusEvent event) {
		super.processFocusEvent(event);
		hintLabel.update();
		line.update();
	}

	@Override
	public void processKeyEvent(KeyEvent event) {
		super.processKeyEvent(event);
		hintLabel.update();
		line.update();
	}

	@Override
	public void paintComponent(Graphics graphics) {

		Graphics2D graphics2d = (Graphics2D) graphics;
		graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		graphics2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		graphics2d.setColor(getBackground());
		graphics2d.fillRect(0, (getHeight() / 2) - 4, getWidth(), getHeight() / 2);
		graphics2d.translate(0, 9);
		super.paintComponent(graphics);
		graphics2d.translate(0, -9);

		if (!getLabel().isEmpty() && getText().isEmpty() && (getLabel().isEmpty() || isFocusOwner())) {
			graphics.setFont(App.getFont().deriveFont(16f));
			graphics2d.setColor(Utilities.applyAlphaMask(getForeground(), hintOpacityMask));
			FontMetrics metrics = graphics.getFontMetrics(graphics.getFont());
			graphics.drawString(getLabel(), 0, metrics.getAscent() + getHeight() / 2);
		}

		hintLabel.paint(graphics2d);

		graphics2d.setColor(Utilities.applyAlphaMask(getForeground(), lineOpacityMask));
		graphics2d.fillRect(0, getHeight() - 4, getWidth(), 1);
		graphics2d.setColor(accentColor);
		graphics2d.fillRect((int) ((getWidth() - line.getWidth()) / 2), getHeight() - 5, (int) line.getWidth(), 2);

	}

	@Override
	public void paintBorder(Graphics ignored) {

	}

}