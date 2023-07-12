package frames;

import app.App;
import material.MatDatePicker;
import material.MatProgressBar;
import material.MatScrollPane;
import material.MatTextField;
import utilities.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class History extends JFrame {

	private final String username;
	private final JPanel headerPanel, mainPanel;
	private final DecimalFormat formatter = new DecimalFormat("###,###");

	public History(String username, int dailyGoal) {

		setTitle(App.getTitle());
		setIconImage(App.getIcon());
		setLayout(new BorderLayout());

		this.username = username;

		headerPanel = new JPanel(new GridBagLayout());
		GridBagConstraints constraints = getConstraints();

		JLabel backToDashboard = addBackToDashboard();
		headerPanel.add(backToDashboard, constraints);

		addHeaderSpacing(constraints, 50, 160);
		addHeaderLabel(constraints, "Exercise history", 40f, Theme.LIGHT_BLUE.color);

		addHeaderSpacing(constraints, 50, 25);
		addHeaderLabel(constraints, "From", 25f, Theme.DARK_BLUE.color);

		addHeaderSpacing(constraints, 40, 25);
		LocalDate rawFromDate = LocalDate.now().minusDays(7);
		MatDatePicker fromDate = addDatePicker(constraints, rawFromDate.getYear(), rawFromDate.getMonthValue(), rawFromDate.getDayOfMonth());

		addHeaderSpacing(constraints, 50, 25);
		addHeaderLabel(constraints, "To", 25f, Theme.DARK_BLUE.color);

		addHeaderSpacing(constraints, 40, 25);
		LocalDate rawToDate = LocalDate.now();
		MatDatePicker toDate = addDatePicker(constraints, rawToDate.getYear(), rawToDate.getMonthValue(), rawToDate.getDayOfMonth());

		mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		mainPanel.setBorder(new EmptyBorder(0, 50, 0, 50));
		mainPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

		MatScrollPane scrollPane = new MatScrollPane(mainPanel);

		fromDate.addEventDatePicker((action, date) -> {
			String from = fromDate.getSelectedDateText();
			String to = toDate.getSelectedDateText();
			refreshHistory(from, to, dailyGoal);
		});

		toDate.addEventDatePicker((action, date) -> {
			String from = fromDate.getSelectedDateText();
			String to = toDate.getSelectedDateText();
			refreshHistory(from, to, dailyGoal);
		});

		toDate.setSelectedDate(toDate.getSelectedDate());

		add(headerPanel, BorderLayout.NORTH);
		add(scrollPane, BorderLayout.CENTER);

		setupWindow();
		backToDashboard.requestFocus();

	}

	private void refreshHistory(String from, String to, int dailyGoal) {

		ResultSet results = Database.executeQuery("SELECT e.exerciseID, e.exerciseName, e.caloriesPerMin, h.duration, h.date \n" +
				"FROM histories AS h \n" +
				"INNER JOIN exercises AS e \n" +
				"ON h.exerciseID = e.exerciseID \n" +
				"WHERE h.username = '" + username + "' AND h.date >= '" + from + "' AND h.date <= '" + to + "' \n" +
				"ORDER BY h.date DESC, h.historyID DESC \n" +
				"LIMIT 1000; ");

		mainPanel.removeAll();
		mainPanel.repaint();
		mainPanel.revalidate();

		try {
			assert results != null;
			if (!results.next()) {
				showEmptyHistory();
				return;
			}
			int count = 0;
			while (!results.isAfterLast()) {
				count++;
				Map<Exercise, Integer> exercises = new LinkedHashMap<>();
				String date = results.getString("date");
				JLabel percentagePanel = addProgressBarHeader(getFormattedDate(date));
				MatProgressBar progressBar = new MatProgressBar();
				mainPanel.add(progressBar);
				addSpacing(25);
				while (date.equals(results.getString("date"))) {
					getExercises(exercises, results);
					if (!results.next()) {
						break;
					}
				}
				int totalCalories, totalDuration;
				totalCalories = totalDuration = 0;
				for (Exercise exercise : exercises.keySet()) {
					String name = exercise.getName();
					int duration = exercises.get(exercise);
					int calories = exercise.getCaloriesPerMin() * duration;
					totalDuration += duration;
					totalCalories += calories;
					addExerciseLabel(name, duration, calories);
				}
				addExerciseLabel("Total", totalDuration, totalCalories);
				int percentage = (int) (((double) totalCalories / (double) dailyGoal) * 100);
				percentagePanel.setText(getFormattedNumber(percentage) + "%");
				progressBar.setPercentage(percentage);
			}
			if (count < 15) {
				mainPanel.setBorder(new EmptyBorder(0, 50, (20 - count) * 20, 50));
			} else {
				mainPanel.setBorder(new EmptyBorder(0, 50, 0, 50));
			}
		} catch (Exception exception) {
			Database.showError();
		}

	}

	private void getExercises(Map<Exercise, Integer> exercises, ResultSet results) {
		try {
			int id = results.getInt("exerciseID");
			String name = results.getString("exerciseName");
			int caloriesPerMin = results.getInt("caloriesPerMin");
			Exercise exercise = new Exercise(id, name, caloriesPerMin);
			int durationToAdd = results.getInt("duration");
			int duration = exercises.getOrDefault(exercise, 0);
			duration += durationToAdd;
			exercises.put(exercise, duration);
		} catch (Exception exception) {
			Database.showError();
		}
	}

	private void addHeaderLabel(GridBagConstraints constraints, String text, float fontSize, Color color) {
		JLabel label = new JLabel(text);
		label.setFont(App.getFont().deriveFont(fontSize));
		label.setForeground(color);
		headerPanel.add(label, constraints);
	}

	private JLabel addProgressBarHeader(String date) {

		JPanel panel = new JPanel(new BorderLayout());
		panel.setBackground(Color.WHITE);

		JLabel dateLabel = addLabel(date, 25f, Theme.DARK_BLUE.color);
		panel.add(dateLabel, BorderLayout.WEST);

		JLabel percentageLabel = addLabel("", 35f, Color.BLACK);
		panel.add(percentageLabel, BorderLayout.EAST);

		addSpacing(20);
		mainPanel.add(panel);
		addSpacing(10);

		return percentageLabel;

	}

	private void addExerciseLabel(String name, int duration, int calories) {

		JPanel panel = new JPanel(new GridLayout(1, 3));
		panel.setBackground(Color.WHITE);

		JLabel firstLabel = addLabel(name, 18f, Theme.DARK_BLUE.color);
		panel.add(firstLabel);

		JLabel secondLabel = addLabel(getFormattedNumber(duration) + (duration == 1 ? " minute" : " minutes"), 18f, Theme.GRAY.color);
		panel.add(secondLabel);

		JLabel thirdLabel = addLabel(getFormattedNumber(calories) + " cal", 18f, Theme.LIGHT_BLUE.color);
		panel.add(thirdLabel);

		if (Objects.equals(name, "Total")) {
			firstLabel.setFont(App.getFont().deriveFont(22f));
			firstLabel.setForeground(Color.BLACK);
			secondLabel.setForeground(Color.BLACK);
			thirdLabel.setForeground(Color.BLACK);
		}

		panel.setBorder(new EmptyBorder(0, 40, 0, 0));
		mainPanel.add(panel);
		addSpacing(10);

	}

	private JLabel addLabel(String text, float fontSize, Color color) {
		JLabel label = new JLabel(text);
		label.setFont(App.getFont().deriveFont(fontSize));
		label.setForeground(color);
		return label;
	}

	private MatDatePicker addDatePicker(GridBagConstraints constraints, int year, int month, int day) {

		MatTextField textField = new MatTextField();
		textField.setPreferredSize(new Dimension(325, 55));
		textField.setFont(App.getFont().deriveFont(20f));
		MatDatePicker datePicker = new MatDatePicker();
		datePicker.setTextField(textField);

		datePicker.addEventDatePicker((action, date) -> {
			if (action.getAction() == SelectedAction.selectedDay) {
				datePicker.hidePopup();
			}
		});

		datePicker.setSelectedDate(new SelectedDate(day, month, year));
		headerPanel.add(textField, constraints);

		return datePicker;

	}

	private void showEmptyHistory() {
		JPanel panel = new JPanel(new GridBagLayout());
		panel.setBackground(Color.WHITE);
		JLabel label = addLabel("Nothing to show for the selected date range", 23f, Theme.GRAY.color);
		panel.add(label, new GridBagConstraints());
		mainPanel.add(panel);
		mainPanel.setBorder(new EmptyBorder(0, 50, 0, 50));
	}

	private GridBagConstraints getConstraints() {
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridwidth = constraints.gridheight = 100;
		constraints.gridx = constraints.gridy = 0;
		constraints.insets = new Insets(50, 25, 0, 50);
		return constraints;
	}

	private void addHeaderSpacing(GridBagConstraints constraints, int top, int right) {
		constraints.gridx += 100;
		constraints.insets = new Insets(top, 10, 0, right);
	}

	private void addSpacing(int height) {
		mainPanel.add(Box.createRigidArea(new Dimension(0, height)));
	}

	private String getFormattedNumber(int number) {
		return formatter.format(number);
	}

	private String getFormattedDate(String date) {
		LocalDate rawDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		return rawDate.format(DateTimeFormatter.ofPattern("d MMMM yyyy"));
	}

	private JLabel addBackToDashboard() {

		Image mouseExitedIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/back-1.png"))).getImage();
		Image mouseEnteredIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/back-2.png"))).getImage();

		JLabel button = new JLabel();
		button.setIcon(new ImageIcon(mouseExitedIcon));
		button.setCursor(new Cursor(Cursor.HAND_CURSOR));

		button.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseEntered(MouseEvent event) {
				button.setIcon(new ImageIcon(mouseEnteredIcon));
			}

			@Override
			public void mouseExited(MouseEvent event) {
				button.setIcon(new ImageIcon(mouseExitedIcon));
			}

			@Override
			public void mouseClicked(MouseEvent event) {
				new Dashboard(username);
				dispose();
			}

		});

		return button;

	}

	private void setupWindow() {
		pack();
		setResizable(false);
		setLocationRelativeTo(null);
		headerPanel.setBackground(Color.WHITE);
		mainPanel.setBackground(Color.WHITE);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setMinimumSize(Toolkit.getDefaultToolkit().getScreenSize());
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setVisible(true);
	}

}