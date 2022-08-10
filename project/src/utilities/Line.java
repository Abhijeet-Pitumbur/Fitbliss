package utilities;

import javax.swing.*;

public class Line {

	private final JComponent target;
	private final Animator animator;
	private double width, targetWidth, widthIncrement;

	public Line(JComponent target) {

		this.target = target;

		animator = new Animator(new Animation() {

			@Override
			public void onStart() {

			}

			@Override
			public void onAnimate(double percent) {
				width += widthIncrement;
				target.repaint();
			}

			@Override
			public void onEnd() {
				width = targetWidth;
				target.repaint();
			}

			@Override
			public void onStop() {
				width = targetWidth;
				target.repaint();
			}

		}).setDelay(0).setDuration(200);

	}

	public void update() {
		animator.stop();
		if (target.isFocusOwner()) {
			targetWidth = target.getWidth();
			widthIncrement = (double) target.getWidth() / 200;
		} else {
			widthIncrement = -(double) target.getWidth() / 200;
			targetWidth = 0;
		}
		animator.start();
	}

	public double getWidth() {
		return width;
	}

}