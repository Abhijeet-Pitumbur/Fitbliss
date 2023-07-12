package utilities;

import app.App;

import javax.swing.*;
import java.awt.*;

public class Months extends JPanel {

	private Event event;

	public Months() {
		initialise();
	}

	private void addEvent() {
		for (int i = 0; i < getComponentCount(); i++) {
			((DateButton) getComponent(i)).setEvent(event);
		}
	}

	private void initialise() {

		DateButton command1 = new DateButton();
		DateButton command2 = new DateButton();
		DateButton command3 = new DateButton();
		DateButton command4 = new DateButton();
		DateButton command5 = new DateButton();
		DateButton command6 = new DateButton();
		DateButton command7 = new DateButton();
		DateButton command8 = new DateButton();
		DateButton command9 = new DateButton();
		DateButton command10 = new DateButton();
		DateButton command11 = new DateButton();
		DateButton command12 = new DateButton();

		setBackground(Color.WHITE);
		setLayout(new GridLayout(4, 4));

		setMonthText(command1, "January", "1");
		setMonthText(command2, "February", "2");
		setMonthText(command3, "March", "3");
		setMonthText(command4, "April", "4");
		setMonthText(command5, "May", "5");
		setMonthText(command6, "June", "6");
		setMonthText(command7, "July", "7");
		setMonthText(command8, "August", "8");
		setMonthText(command9, "September", "9");
		setMonthText(command10, "October", "10");
		setMonthText(command11, "November", "11");
		setMonthText(command12, "December", "12");

	}

	private void setMonthText(DateButton button, String name, String text) {
		button.setBackground(Color.WHITE);
		button.setForeground(Color.BLACK);
		button.setText(name);
		button.setFont(App.getFont().deriveFont(16f));
		button.setName(text);
		button.setOpaque(true);
		add(button);
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
		addEvent();
	}

}