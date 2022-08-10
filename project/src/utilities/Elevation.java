package utilities;

import material.MatButton;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class Elevation {

	public final JComponent target;
	public final Shadow shadow;
	private final Animator animator;
	public double level;
	public int borderRadius;
	private double startLevel, targetLevel;

	private Elevation(final JComponent component, int elevationLevel) {

		target = component;
		level = elevationLevel;
		targetLevel = elevationLevel;
		shadow = new Shadow();

		animator = new Animator(new Animation() {

			@Override
			public void onStart() {

			}

			@Override
			public void onAnimate(double percent) {
				level = startLevel + ((targetLevel - startLevel) * percent);
				target.repaint();
			}

			@Override
			public void onEnd() {
				level = targetLevel;
				target.repaint();
			}

			@Override
			public void onStop() {
				level = targetLevel;
				target.repaint();
			}

		}).setDelay(0).setDuration(500);

	}

	public static Elevation applyTo(JComponent target, int level) {
		return new Elevation(target, level);
	}

	public void setLevel(int elevationLevel) {
		if (target.isShowing()) {
			if (elevationLevel != level) {
				animator.stop();
				startLevel = level;
				targetLevel = elevationLevel;
				animator.start();
			}
		} else {
			this.level = elevationLevel;
		}
	}

	public void setBorderRadius(int borderRadius) {
		this.borderRadius = borderRadius;
	}

	public void paint(Graphics graphics) {
		Graphics2D graphics2d = (Graphics2D) graphics;
		graphics2d.setBackground(target.getParent().getBackground());
		graphics2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
		if (target instanceof MatButton && (((MatButton) target).getType() == MatButton.ButtonType.FLAT)) {
			graphics2d.setColor(Utilities.brighten(target.getBackground(), (int) (((66 / (1 + Math.exp(-2 * level))) - 33))));
			graphics2d.fill(new RoundRectangle2D.Double(0, 0, target.getWidth(), target.getHeight(), borderRadius, borderRadius));
		} else {
			graphics.drawImage(shadow.render(target.getWidth(), target.getHeight(), borderRadius, level), 0, 0, null);
		}
	}

}