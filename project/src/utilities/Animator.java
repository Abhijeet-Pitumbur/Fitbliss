package utilities;

import javax.swing.*;

public class Animator {

	private final Animation listener;
	private Timer timer;
	private int duration, timeTaken;
	private long startTime;

	public Animator(Animation listener) {

		this.listener = listener;

		timer = new Timer(1, event -> {
			timeTaken = (int) (System.currentTimeMillis() - startTime);
			this.listener.onAnimate((double) timeTaken / duration);
			if (timeTaken >= duration) {
				SwingUtilities.invokeLater(() -> {
					timer.stop();
					this.listener.onEnd();
				});
			}
		});

		timer.setCoalesce(true);

	}

	public Animator setDelay(int delay) {
		timer.setInitialDelay(delay);
		return this;
	}

	public Animator setDuration(int duration) {
		this.duration = duration;
		return this;
	}

	public void start() {
		timer.stop();
		listener.onStart();
		startTime = System.currentTimeMillis();
		timer.start();
	}

	public void stop() {
		if (timer != null && timer.isRunning()) {
			listener.onStop();
			timer.stop();
		}
	}

	public boolean isRunning() {
		return timer != null && timer.isRunning();
	}

}