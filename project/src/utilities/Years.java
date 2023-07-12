package utilities;

import app.App;

import javax.swing.*;
import java.awt.*;

public class Years extends JPanel {

	private Event event;
	private int startYear;

	public Years() {
		initialise();
	}

	public int showYear(int year) {
		year = calculateYear(year);
		for (int i = 0; i < getComponentCount(); i++) {
			JButton button = (JButton) getComponent(i);
			button.setText(String.valueOf(year));
			year++;
		}
		return startYear;
	}

	private int calculateYear(int year) {
		year -= year % 10;
		startYear = year;
		return year;
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
		DateButton command13 = new DateButton();
		DateButton command14 = new DateButton();
		DateButton command15 = new DateButton();
		DateButton command16 = new DateButton();
		DateButton command17 = new DateButton();
		DateButton command18 = new DateButton();
		DateButton command19 = new DateButton();
		DateButton command20 = new DateButton();

		setBackground(Color.WHITE);
		setLayout(new GridLayout(5, 4));

		setYearText(command1, "2010");
		setYearText(command2, "2011");
		setYearText(command3, "2012");
		setYearText(command4, "2013");
		setYearText(command5, "2014");
		setYearText(command6, "2015");
		setYearText(command7, "2016");
		setYearText(command8, "2017");
		setYearText(command9, "2018");
		setYearText(command10, "2019");
		setYearText(command11, "2020");
		setYearText(command12, "2021");
		setYearText(command13, "2022");
		setYearText(command14, "2023");
		setYearText(command15, "2024");
		setYearText(command16, "2025");
		setYearText(command17, "2026");
		setYearText(command18, "2027");
		setYearText(command19, "2028");
		setYearText(command20, "2029");

	}

	private void setYearText(DateButton button, String text) {
		button.setBackground(Color.WHITE);
		button.setForeground(Color.BLACK);
		button.setText(text);
		button.setFont(App.getFont().deriveFont(17f));
		button.setName("year");
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

	public int forward(int year) {
		showYear(year + 20);
		return startYear;
	}

	public int backward(int year) {
		showYear(year - 20);
		return startYear;
	}

}