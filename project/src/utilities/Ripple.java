package utilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Ripple {

	private final JComponent target;
	private final RippleAnimation ripple = new RippleAnimation();

	private Ripple(final JComponent component) {
		this.target = component;
	}

	public static Ripple applyTo(final JComponent target) {

		final Ripple rippleEffect = new Ripple(target);

		target.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent event) {
				rippleEffect.addRipple(event.getPoint(), target.getWidth());
			}
		});

		return rippleEffect;

	}

	public void paint(Graphics graphics) {
		if (ripple.isRippling()) {
			Graphics2D graphics2d = (Graphics2D) graphics;
			float rippleOpacity = (float) ripple.getRippleOpacity();
			Point rippleCenter = ripple.getRippleCenter();
			int rippleRadius = (int) ripple.getRippleRadius();
			Color foreground = graphics2d.getColor();
			graphics2d.setColor(new Color(foreground.getRed() / 255f, foreground.getGreen() / 255f, foreground.getBlue() / 255f, rippleOpacity));
			graphics2d.fillOval(rippleCenter.x - rippleRadius, rippleCenter.y - rippleRadius, 2 * rippleRadius, 2 * rippleRadius);
		}
	}

	private void addRipple(Point point, int maxRadius) {
		ripple.setRipple(point, maxRadius);
		ripple.start();
	}

	private class RippleAnimation {

		private final Animator animator;
		private Point rippleCenter;
		private int maxRadius;
		private double rippleRadius, targetRippleRadius, rippleRadiusIncrement, rippleOpacity;

		private RippleAnimation() {

			animator = new Animator(new Animation() {

				@Override
				public void onStart() {
					rippleRadius = 0;
					targetRippleRadius = maxRadius;
					rippleRadiusIncrement = targetRippleRadius - rippleRadius;
					rippleOpacity = 0.5;
				}

				@Override
				public void onAnimate(double percent) {
					rippleRadius = rippleRadiusIncrement * percent * percent;
					rippleOpacity = 0.5 * Math.sin(3 * percent * percent);
					target.repaint();
				}

				@Override
				public void onEnd() {
					rippleRadius = 0;
					target.repaint();
				}

				@Override
				public void onStop() {
					rippleRadius = 0;
					target.repaint();
				}

			}).setDelay(0).setDuration(1000);

		}

		public void start() {
			animator.start();
		}

		public void setRipple(Point rippleCenter, int maxRadius) {
			this.rippleCenter = rippleCenter;
			this.maxRadius = maxRadius;
		}

		public double getRippleOpacity() {
			return rippleOpacity;
		}

		public Point getRippleCenter() {
			return rippleCenter;
		}

		public double getRippleRadius() {
			return rippleRadius;
		}

		public boolean isRippling() {
			return animator.isRunning();
		}

	}

}