package utilities;

import app.App;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Dates extends JPanel {

	private final int day, month, year;
	private Event event;
	private int m, y, startDate, maxOfMonth;
	private int selectedDay = 0;

	public Dates() {
		initialise();
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
		Date date = new Date();
		String dateText = format.format(date);
		day = Integer.parseInt(dateText.split("-")[0]);
		month = Integer.parseInt(dateText.split("-")[1]);
		year = Integer.parseInt(dateText.split("-")[2]);
	}

	public void showDate(int month, int year, SelectedDate selectedDate) {

		m = month;
		y = year;
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month - 1, 1);
		int start = calendar.get(Calendar.DAY_OF_WEEK);
		maxOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		if (start == 1) {
			start += 7;
		}
		clear();
		start += 5;
		startDate = start;

		for (int i = 1; i <= maxOfMonth; i++) {
			DateButton button = (DateButton) getComponent(start);
			button.setSelectedColor(getForeground());
			button.setText(String.valueOf(i));
			if (i == day && month == this.month && year == this.year) {
				if (month == m && y == year && !button.getText().equals("") && Integer.parseInt(button.getText()) == day) {
					button.setBackground(Theme.LIGHT_GRAY.color);
					button.setForeground(Color.BLACK);
				} else {
					button.setBackground(Theme.LIGHT_BLUE.color);
					button.setForeground(Color.WHITE);
				}
			} else {
				button.setBackground(Color.WHITE);
			}
			if (i == selectedDate.getDay() && month == selectedDate.getMonth() && year == selectedDate.getYear()) {
				button.setBackground(getForeground());
				button.setForeground(Color.WHITE);
			}
			start++;
		}

	}

	private void clear() {
		for (int i = 7; i < getComponentCount(); i++) {
			((JButton) getComponent(i)).setText("");
		}
	}

	public void clearSelected() {
		for (int i = 7; i < getComponentCount(); i++) {
			JButton button = (JButton) getComponent(i);
			if (month == m && y == year && !button.getText().equals("") && Integer.parseInt(button.getText()) == day) {
				button.setBackground(Theme.LIGHT_GRAY.color);
			} else {
				button.setBackground(Color.WHITE);
			}
			button.setForeground(Color.BLACK);
		}
		selectedDay = 0;
	}

	private void addEvent() {
		for (int i = 7; i < getComponentCount(); i++) {
			((DateButton) getComponent(i)).setEvent(event);
		}
	}

	public void setSelected(int index) {
		selectedDay = index;
	}

	private void initialise() {

		DateButton commandMo = new DateButton();
		DateButton commandTu = new DateButton();
		DateButton commandWe = new DateButton();
		DateButton commandTh = new DateButton();
		DateButton commandFr = new DateButton();
		DateButton commandSa = new DateButton();
		DateButton commandSu = new DateButton();
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
		DateButton command21 = new DateButton();
		DateButton command22 = new DateButton();
		DateButton command23 = new DateButton();
		DateButton command24 = new DateButton();
		DateButton command25 = new DateButton();
		DateButton command26 = new DateButton();
		DateButton command27 = new DateButton();
		DateButton command28 = new DateButton();
		DateButton command29 = new DateButton();
		DateButton command30 = new DateButton();
		DateButton command31 = new DateButton();
		DateButton command32 = new DateButton();
		DateButton command33 = new DateButton();
		DateButton command34 = new DateButton();
		DateButton command35 = new DateButton();
		DateButton command36 = new DateButton();
		DateButton command37 = new DateButton();
		DateButton command38 = new DateButton();
		DateButton command39 = new DateButton();
		DateButton command40 = new DateButton();
		DateButton command41 = new DateButton();
		DateButton command42 = new DateButton();

		setBackground(Color.WHITE);
		setLayout(new GridLayout(7, 7));

		setDayLabel(commandMo, "Mo");
		setDayLabel(commandTu, "Tu");
		setDayLabel(commandWe, "We");
		setDayLabel(commandTh, "Th");
		setDayLabel(commandFr, "Fr");
		setDayLabel(commandSa, "Sa");
		setDayLabel(commandSu, "Su");

		setDayButton(command1);
		setDayButton(command2);

		setDayText(command3, "1");
		setDayText(command4, "2");
		setDayText(command5, "3");
		setDayText(command6, "4");
		setDayText(command7, "5");
		setDayText(command8, "6");
		setDayText(command9, "7");
		setDayText(command10, "8");
		setDayText(command11, "9");
		setDayText(command12, "10");
		setDayText(command13, "11");
		setDayText(command14, "12");
		setDayText(command15, "13");
		setDayText(command16, "14");
		setDayText(command17, "15");
		setDayText(command18, "16");
		setDayText(command19, "17");
		setDayText(command20, "18");
		setDayText(command21, "19");
		setDayText(command22, "20");
		setDayText(command23, "21");
		setDayText(command24, "22");
		setDayText(command25, "23");
		setDayText(command26, "24");
		setDayText(command27, "25");
		setDayText(command28, "26");
		setDayText(command29, "27");
		setDayText(command30, "28");
		setDayText(command31, "29");
		setDayText(command32, "30");
		setDayText(command33, "31");

		setDayButton(command34);
		setDayButton(command35);
		setDayButton(command36);
		setDayButton(command37);
		setDayButton(command38);
		setDayButton(command39);
		setDayButton(command40);
		setDayButton(command41);
		setDayButton(command42);

	}

	private void setDayLabel(DateButton button, String text) {
		button.setBorder(new EmptyBorder(1, 1, 5, 1));
		button.setForeground(Theme.BLUE.color);
		button.setText(text);
		button.setFont(App.getFont().deriveFont(16f));
		button.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		add(button);
	}

	private void setDayButton(DateButton button) {
		button.setBackground(Color.WHITE);
		button.setForeground(Color.BLACK);
		button.setName("day");
		button.setFont(App.getFont().deriveFont(17f));
		add(button);
	}

	private void setDayText(DateButton button, String text) {
		button.setBackground(Color.WHITE);
		button.setForeground(Color.BLACK);
		button.setText(text);
		button.setName("day");
		button.setFont(App.getFont().deriveFont(16f));
		add(button);
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
		addEvent();
	}

	public void forward() {
		if (selectedDay == maxOfMonth) {
			selectedDay = 0;
		}
		JButton button = (JButton) getComponent(startDate - 1 + selectedDay + 1);
		String text = button.getText();
		if (!text.equals("") && Integer.parseInt(text) <= maxOfMonth) {
			selectedDay++;
			event.execute(null, selectedDay);
			button.setBackground(Theme.BLUE.color);
		}
	}

	public void backward() {
		if (selectedDay <= 1) {
			selectedDay = maxOfMonth + 1;
		}
		JButton button = (JButton) getComponent(startDate - 1 + selectedDay - 1);
		String text = button.getText();
		if (!text.equals("") && button.getName() != null) {
			selectedDay--;
			event.execute(null, selectedDay);
			button.setBackground(Theme.BLUE.color);
		}
	}

	public void up() {
		JButton button = (JButton) getComponent(startDate - 1 + selectedDay - 7);
		String text = button.getText();
		if (!text.equals("") && button.getName() != null) {
			selectedDay -= 7;
			event.execute(null, selectedDay);
			button.setBackground(Theme.BLUE.color);
		}
	}

	public void down() {
		if (getComponents().length > startDate - 1 + selectedDay + 7) {
			JButton button = (JButton) getComponent(startDate - 1 + selectedDay + 7);
			String text = button.getText();
			if (!text.equals("") && button.getName() != null) {
				selectedDay += 7;
				event.execute(null, selectedDay);
				button.setBackground(Theme.BLUE.color);
			}
		}
	}

}