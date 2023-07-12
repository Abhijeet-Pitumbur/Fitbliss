package material;

import app.App;
import utilities.Event;
import utilities.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class MatDatePicker extends JPanel {

	private final String[] monthsArray = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
	private JTextField textField;
	private int month = 1;
	private int year = 2022;
	private int day = 1;
	private int status = 1;
	private int startYear;
	private SelectedDate selectedDate = new SelectedDate();
	private List<EventDatePicker> events;
	private DateButton commandMonth, commandYear;
	private JPanel header;
	private JPopupMenu popup;
	private Slider slide;

	public MatDatePicker() {
		initialise();
		execute();
	}

	public void setTextField(JTextField textField) {

		this.textField = textField;
		this.textField.setEditable(false);
		this.textField.setCursor(new Cursor(Cursor.HAND_CURSOR));

		this.textField.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent event) {
				if (MatDatePicker.this.textField.isEnabled()) {
					showPopup();
				}
			}
		});

		setText(false, 0);

	}

	public void addEventDatePicker(EventDatePicker event) {
		events.add(event);
	}

	private void execute() {
		setForeground(Theme.LIGHT_BLUE.color);
		setBackground(Color.WHITE);
		events = new ArrayList<>();
		popup.add(this);
		today();
	}

	private void setText(boolean runEvent, int act) {
		if (textField != null) {
			try {
				textField.setText(day + " " + monthsArray[month - 1] + " " + year);
			} catch (Exception ignored) {

			}
		}
		if (runEvent) {
			runEvent(act);
		}
	}

	private void runEvent(int act) {
		SelectedAction action = () -> act;
		for (EventDatePicker event : events) {
			event.dateSelected(action, selectedDate);
		}
	}

	private Event getEventDay(Dates dates) {
		return (MouseEvent event, int num) -> {
			dates.clearSelected();
			dates.setSelected(num);
			day = num;
			selectedDate.setDay(day);
			selectedDate.setMonth(month);
			selectedDate.setYear(year);
			setText(true, 1);
			if (event != null && event.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(event)) {
				popup.setVisible(false);
			}
		};
	}

	private Event getEventMonth() {
		return (MouseEvent event, int num) -> {
			month = num;
			selectedDate.setDay(day);
			selectedDate.setMonth(month);
			selectedDate.setYear(year);
			setText(true, 2);
			Dates dates = new Dates();
			dates.setForeground(getForeground());
			dates.setEvent(getEventDay(dates));
			dates.showDate(month, year, selectedDate);
			if (slide.slideDown(dates)) {
				commandMonth.setText(monthsArray[month - 1]);
				commandYear.setText(String.valueOf(year));
				status = 1;
			}
		};
	}

	private Event getEventYear() {
		return (MouseEvent event, int num) -> {
			year = num;
			selectedDate.setDay(day);
			selectedDate.setMonth(month);
			selectedDate.setYear(year);
			setText(true, 3);
			Months months = new Months();
			months.setEvent(getEventMonth());
			if (slide.slideDown(months)) {
				commandMonth.setText(monthsArray[month - 1]);
				commandYear.setText(String.valueOf(year));
				status = 2;
			}
		};
	}

	private void today() {
		Dates dates = new Dates();
		dates.setForeground(getForeground());
		dates.setEvent(getEventDay(dates));
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
		Date date = new Date();
		String today = format.format(date);
		day = Integer.parseInt(today.split("-")[0]);
		month = Integer.parseInt(today.split("-")[1]);
		year = Integer.parseInt(today.split("-")[2]);
		selectedDate.setDay(day);
		selectedDate.setMonth(month);
		selectedDate.setYear(year);
		dates.showDate(month, year, selectedDate);
		slide.slideNon(dates);
		commandMonth.setText(monthsArray[month - 1]);
		commandYear.setText(String.valueOf(year));
		setText(false, 0);
	}

	private void setDateForward() {
		Dates dates = new Dates();
		dates.setForeground(getForeground());
		dates.setEvent(getEventDay(dates));
		dates.showDate(month, year, selectedDate);
		if (slide.slideLeft(dates)) {
			commandMonth.setText(monthsArray[month - 1]);
			commandYear.setText(String.valueOf(year));
		}
	}

	private void setDateBackward() {
		Dates dates = new Dates();
		dates.setForeground(getForeground());
		dates.setEvent(getEventDay(dates));
		dates.showDate(month, year, selectedDate);
		if (slide.slideRight(dates)) {
			commandMonth.setText(monthsArray[month - 1]);
			commandYear.setText(String.valueOf(year));
		}
	}

	private void setYearForward() {
		Years years = new Years();
		years.setEvent(getEventYear());
		startYear = years.forward(startYear);
		slide.slideLeft(years);
	}

	private void setYearBackward() {
		if (startYear >= 1000) {
			Years years = new Years();
			years.setEvent(getEventYear());
			startYear = years.backward(startYear);
			slide.slideRight(years);
		}
	}

	public void showPopup() {
		popup.show(textField, 0, textField.getHeight() + 15);
	}

	public void hidePopup() {
		popup.setVisible(false);
	}

	private void initialise() {

		popup = new JPopupMenu() {
			@Override
			public void paintComponent(Graphics graphics) {
				popup.setBorder(null);
				graphics.setColor(Color.WHITE);
				graphics.fillRect(0, 0, getWidth(), getHeight());
				graphics.fillRect(1, 1, getWidth() - 2, getHeight() - 2);
			}
		};

		header = new JPanel();
		DateButton commandForward = new DateButton();
		JLayeredPane layeredPane = new JLayeredPane();
		commandMonth = new DateButton();
		JLabel label = new JLabel();
		commandYear = new DateButton();
		DateButton commandBackward = new DateButton();
		slide = new Slider();

		setBackground(Color.WHITE);

		header.setBackground(Theme.LIGHT_BLUE.color);
		header.setMaximumSize(new Dimension(325, 55));

		commandForward.setCursor(new Cursor(Cursor.HAND_CURSOR));
		commandForward.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/forward.png"))));
		commandForward.setFocusable(true);
		commandForward.setPaintBackground(false);
		commandForward.addActionListener(event -> commandForward());

		FlowLayout flowLayout = new FlowLayout(FlowLayout.CENTER, 5, 0);
		flowLayout.setAlignOnBaseline(true);
		layeredPane.setLayout(flowLayout);

		commandMonth.setCursor(new Cursor(Cursor.HAND_CURSOR));
		commandMonth.setForeground(Color.WHITE);
		commandMonth.setText("January");
		commandMonth.setFocusPainted(false);
		commandMonth.setFont(App.getFont().deriveFont(17f));
		commandMonth.setPaintBackground(false);
		commandMonth.addActionListener(event -> commandMonth());
		layeredPane.add(commandMonth);

		label.setForeground(Color.WHITE);
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setText(" · ");
		label.setFont(App.getFont().deriveFont(17f));
		layeredPane.add(label);

		commandYear.setCursor(new Cursor(Cursor.HAND_CURSOR));
		commandYear.setForeground(Color.WHITE);
		commandYear.setText("2022");
		commandYear.setFocusPainted(false);
		commandYear.setFont(App.getFont().deriveFont(17f));
		commandYear.setPaintBackground(false);
		commandYear.addActionListener(event -> commandYear());
		layeredPane.add(commandYear);

		commandBackward.setCursor(new Cursor(Cursor.HAND_CURSOR));
		commandBackward.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/backward.png"))));
		commandBackward.setFocusable(true);
		commandBackward.setPaintBackground(false);
		commandBackward.addActionListener(event -> commandBackward());

		commandBackward.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent event) {
				commandBackwardKeyPressed(event);
			}
		});

		GroupLayout headerLayout = new GroupLayout(header);

		header.setLayout(headerLayout);
		headerLayout.setHorizontalGroup(
				headerLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addGroup(GroupLayout.Alignment.TRAILING, headerLayout.createSequentialGroup()
								.addContainerGap()
								.addComponent(commandBackward, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(layeredPane, GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE)
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(commandForward, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addContainerGap())
		);

		headerLayout.setVerticalGroup(
				headerLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addGroup(GroupLayout.Alignment.TRAILING, headerLayout.createSequentialGroup()
								.addContainerGap()
								.addGroup(headerLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
										.addComponent(commandBackward, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(layeredPane, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addComponent(commandForward, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
								.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);

		slide.setLayout(new BoxLayout(slide, BoxLayout.X_AXIS));

		GroupLayout layout = new GroupLayout(this);
		this.setLayout(layout);

		layout.setHorizontalGroup(
				layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addGroup(layout.createSequentialGroup()
								.addGap(0, 0, 0)
								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
										.addComponent(slide, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(header, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
								.addGap(0, 0, 0))
		);

		layout.setVerticalGroup(
				layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addGroup(layout.createSequentialGroup()
								.addGap(0, 0, Short.MAX_VALUE)
								.addComponent(header, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addGap(15, 15, 15)
								.addComponent(slide, GroupLayout.PREFERRED_SIZE, 250, GroupLayout.PREFERRED_SIZE)
								.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);

	}

	private void commandBackward() {
		if (status == 1) {
			if (month == 1) {
				month = 12;
				year--;
			} else {
				month--;
			}
			setDateBackward();
		} else if (status == 3) {
			setYearBackward();
		} else {
			if (year >= 1000) {
				year--;
				Months months = new Months();
				months.setEvent(getEventMonth());
				slide.slideRight(months);
				commandYear.setText(String.valueOf(year));
			}
		}
	}

	private void commandForward() {
		if (status == 1) {
			if (month == 12) {
				month = 1;
				year++;
			} else {
				month++;
			}
			setDateForward();
		} else if (status == 3) {
			setYearForward();
		} else {
			year++;
			Months months = new Months();
			months.setEvent(getEventMonth());
			slide.slideLeft(months);
			commandYear.setText(String.valueOf(year));
		}
	}

	private void commandMonth() {
		if (status != 2) {
			status = 2;
			Months months = new Months();
			months.setEvent(getEventMonth());
			slide.slideDown(months);
		} else {
			Dates dates = new Dates();
			dates.setForeground(getForeground());
			dates.setEvent(getEventDay(dates));
			dates.showDate(month, year, selectedDate);
			slide.slideDown(dates);
			status = 1;
		}
	}

	private void commandYear() {
		if (status != 3) {
			status = 3;
			Years years = new Years();
			years.setEvent(getEventYear());
			startYear = years.showYear(year);
			slide.slideDown(years);
		} else {
			Dates dates = new Dates();
			dates.setForeground(getForeground());
			dates.setEvent(getEventDay(dates));
			dates.showDate(month, year, selectedDate);
			slide.slideDown(dates);
			status = 1;
		}
	}

	private void commandBackwardKeyPressed(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.VK_UP) {
			Component component = slide.getComponent(0);
			if (component instanceof Dates dates) {
				dates.up();
			}
		} else if (event.getKeyCode() == KeyEvent.VK_DOWN) {
			Component component = slide.getComponent(0);
			if (component instanceof Dates dates) {
				dates.down();
			}
		} else if (event.getKeyCode() == KeyEvent.VK_LEFT) {
			Component component = slide.getComponent(0);
			if (component instanceof Dates dates) {
				dates.backward();
			}
		} else if (event.getKeyCode() == KeyEvent.VK_RIGHT) {
			Component component = slide.getComponent(0);
			if (component instanceof Dates dates) {
				dates.forward();
			}
		}
	}

	public String getSelectedDateText() {
		return selectedDate.getYear() + "-" + selectedDate.getMonth() + "-" + selectedDate.getDay();
	}

	public SelectedDate getSelectedDate() {
		return selectedDate;
	}

	public void setSelectedDate(SelectedDate selectedDate) {
		this.selectedDate = selectedDate;
		day = selectedDate.getDay();
		month = selectedDate.getMonth();
		year = selectedDate.getYear();
		Dates dates = new Dates();
		dates.setForeground(getForeground());
		dates.setEvent(getEventDay(dates));
		dates.setSelected(day);
		dates.showDate(month, year, selectedDate);
		slide.slideNon(dates);
		commandMonth.setText(monthsArray[month - 1]);
		commandYear.setText(String.valueOf(year));
		setText(true, 0);
		status = 1;
	}

	@Override
	public void setForeground(Color foreground) {
		super.setForeground(foreground);
		if (header != null) {
			header.setBackground(foreground);
			today();
		}
	}

}