package utilities;

import app.App;

import javax.swing.*;
import java.awt.*;

public class FloatingLabel {

	public static final int hintOpacityMask = 0x99000000;
	private final JTextField target;
	private final Animator animator;
	private final Color accentColor = Theme.TURQUOISE.color;
	private Color color;
	private String text;
	private double fontIncrement, targetFontSize, fontSize;

	public FloatingLabel(JTextField target) {

		this.target = target;
		targetFontSize = fontSize = 16.0;
		color = Utilities.applyAlphaMask(target.getForeground(), hintOpacityMask);

		animator = new Animator(new Animation() {

			@Override
			public void onStart() {

			}

			@Override
			public void onAnimate(double percent) {
				fontSize += fontIncrement;
				target.repaint();
			}

			@Override
			public void onEnd() {
				fontSize = targetFontSize;
				target.repaint();
			}

			@Override
			public void onStop() {
				fontSize = targetFontSize;
				target.repaint();
			}

		}).setDelay(0).setDuration(100);

	}

	public void update() {

		animator.stop();
		targetFontSize = target.isFocusOwner() ? 12.0 : 16.0;

		if (fontSize != targetFontSize) {
			fontIncrement = (targetFontSize - fontSize) / 100;
			animator.start();
		}

		if (target.isFocusOwner()) {
			color = accentColor;
		} else {
			color = Utilities.applyAlphaMask(target.getForeground(), hintOpacityMask);
		}

	}

	public void updateForeground() {
		color = (Utilities.applyAlphaMask(target.getForeground(), hintOpacityMask));
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void paint(Graphics2D graphics2D) {
		graphics2D.setFont(App.getFont().deriveFont((float) fontSize));
		graphics2D.setColor(color);
		FontMetrics metrics = graphics2D.getFontMetrics(graphics2D.getFont());
		graphics2D.drawString(getText(), 0, metrics.getAscent());
	}

}