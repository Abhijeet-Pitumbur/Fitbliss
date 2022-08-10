package utilities;

import javax.swing.*;
import java.awt.*;

public class Slider extends JPanel {

	private boolean sliding = false;

	public Slider() {
		initialise();
	}

	public boolean slideRight(Component component) {
		boolean act = false;
		if (!sliding) {
			act = true;
			new Thread(() -> {
				sliding = true;
				Component oldComponent = null;
				if (getComponentCount() > 0) {
					oldComponent = getComponent(0);
				}
				add(component);
				component.setLocation(-getWidth(), 0);
				int x = 0;
				int width = (int) getSize().getWidth() / 70;
				for (int i = -getWidth(); i <= 0; i += width) {
					component.setLocation(i, 0);
					if (oldComponent != null) {
						oldComponent.setLocation(x, 0);
						x += width;
					}
					sleep();
				}
				component.setLocation(0, 0);
				while (getComponentCount() != 1) {
					remove(getComponentCount() - 2);
				}
				repaint();
				revalidate();
				sliding = false;
			}).start();
		}
		return act;
	}

	public boolean slideLeft(Component component) {
		boolean act = false;
		if (!sliding) {
			act = true;
			new Thread(() -> {
				sliding = true;
				Component oldComponent = null;
				if (getComponentCount() > 0) {
					oldComponent = getComponent(0);
				}
				add(component);
				component.setLocation(getWidth(), 0);
				int x = 0;
				int width = (int) getSize().getWidth() / 70;
				for (int i = getWidth(); i >= 0; i -= width) {
					component.setLocation(i, 0);
					if (oldComponent != null) {
						oldComponent.setLocation(x, 0);
						x -= width;
					}
					sleep();
				}
				component.setLocation(0, 0);
				while (getComponentCount() != 1) {
					remove(getComponentCount() - 2);
				}
				repaint();
				revalidate();
				sliding = false;
			}).start();
		}
		return act;
	}

	public boolean slideDown(Component component) {
		boolean act = false;
		if (!sliding) {
			act = true;
			new Thread(() -> {
				sliding = true;
				Component oldComponent = null;
				if (getComponentCount() > 0) {
					oldComponent = getComponent(0);
				}
				add(component);
				component.setLocation(0, -getHeight());
				int x = 0;
				int right = (int) getSize().getHeight() / 70;
				for (int i = getHeight(); i >= 0; i -= right) {
					component.setLocation(0, i);
					if (oldComponent != null) {
						oldComponent.setLocation(0, x);
						x -= right;
					}
					sleep();
				}
				component.setLocation(0, 0);
				while (getComponentCount() != 1) {
					remove(getComponentCount() - 2);
				}
				repaint();
				revalidate();
				sliding = false;
			}).start();
		}
		return act;
	}

	public void slideNon(Component component) {
		this.removeAll();
		this.add(component);
		repaint();
		revalidate();
	}

	private void sleep() {
		try {
			Thread.sleep(2);
		} catch (Exception ignored) {

		}
	}

	private void initialise() {
		setBackground(Color.WHITE);
		setLayout(new BorderLayout());
	}

}