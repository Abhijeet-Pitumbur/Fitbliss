package frames;

import app.App;
import material.MatButton;
import material.MatComboBox;
import material.MatPieChart;
import material.MatTextField;
import utilities.Database;
import utilities.Exercise;
import utilities.PieSegment;
import utilities.Theme;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Dashboard extends JFrame {

	private final String username, currentDate;
	private final int dailyGoal;
	private final JPanel leftPanel, rightPanel;
	private final GridBagConstraints constraints;

	public Dashboard(String username) {

		setTitle(App.getTitle());
		setIconImage(App.getIcon());
		setLayout(new GridLayout(1, 2));

		this.username = username;

		currentDate = LocalDate.now().toString();
		dailyGoal = getDailyGoal();

		MatPieChart pieChart = new MatPieChart();
		refreshPieChart(pieChart);

		leftPanel = new JPanel(new BorderLayout());
		leftPanel.add(pieChart, BorderLayout.CENTER);
		leftPanel.setBackground(Color.WHITE);

		rightPanel = new JPanel(new GridBagLayout());
		constraints = new GridBagConstraints();
		constraints.gridwidth = constraints.gridheight = 100;
		constraints.gridx = constraints.gridy = 0;

		addLabel("Hello, " + username + "!", 40f, Theme.LIGHT_BLUE.color, 0, 40);
		addLabel("What exercises did you do today?", 24f, Theme.DARK_BLUE.color, 0, 25);

		Map<String, Exercise> exerciseMap = getExerciseMap();
		String[] exerciseArray = getExerciseArray(exerciseMap);

		addSpacing(25, 25);
		MatComboBox<String> exercises = new MatComboBox<>();
		exercises.setModel(new DefaultComboBoxModel<>(exerciseArray));
		exercises.setPreferredSize(new Dimension(420, 45));
		exercises.setFont(App.getFont().deriveFont(20f));
		rightPanel.add(exercises, constraints);

		addSpacing(25, 25);
		MatTextField duration = new MatTextField();
		duration.setLabel("Duration (minutes)");
		duration.setPreferredSize(new Dimension(420, 55));
		duration.setFont(App.getFont().deriveFont(20f));
		rightPanel.add(duration, constraints);

		MatButton addExercise = addButton("Add exercise", 20f, Theme.LIGHT_BLUE.color, Color.WHITE, 420, 20, 20);

		JLabel addExerciseError = addLabel(" ", 17f, Theme.RED.color, 0, 30);

		addExercise.addActionListener(event -> {
			Exercise selectedExercise = getSelectedExercise(String.valueOf(exercises.getSelectedItem()), exerciseMap);
			int selectedDuration = getSelectedDuration(duration.getText(), addExerciseError);
			if (selectedExercise == null) {
				addExerciseError.setText("Exercise type required");
			} else if (selectedDuration != 0) {
				addExercise(selectedExercise, selectedDuration);
				refreshPieChart(pieChart);
				exercises.setSelectedIndex(0);
				duration.setText("");
			}
		});

		MatButton viewHistory = addButton("View exercise history", 20f, Color.WHITE, Theme.LIGHT_BLUE.color, 420, 0, 30);

		viewHistory.addActionListener(event -> {
			new History(username, dailyGoal);
			dispose();
		});

		addLabel("Signed in as " + username, 17f, Theme.GRAY.color, 25, 7);
		MatButton signOut = addButton("Sign out", 17f, Color.WHITE, Theme.RED.color, 200, 0, 0);

		signOut.addActionListener(event -> {
			new Welcome();
			dispose();
		});

		pieChart.setFont(App.getFont().deriveFont(12f));

		add(leftPanel);
		add(rightPanel);

		setupWindow();

	}

	private void addExercise(Exercise exercise, int duration) {

		Database.executeUpdate("INSERT INTO histories (username, exerciseID, duration, date) VALUES ('" + username + "', " + exercise.getId() + ", " + duration + ", '" + currentDate + "')");

		int calories = exercise.getCaloriesPerMin() * duration;
		LocalDate rawDate = LocalDate.parse(currentDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		String date = rawDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));

		try {
			String directory = System.getProperty("user.dir");
			File path = new File(directory + File.separator + App.getFile());
			if (!path.exists()) {
				Formatter file = new Formatter(directory + File.separator + App.getFile());
				file.format("%25s %s %24s %n %n", " ", "---------- EXERCISE HISTORY ----------", " ");
				file.format("%-20s %-20s %-15s %-15s %-15s %n %n", "USERNAME", "EXERCISE", "DURATION", "CALORIES", "DATE");
				file.close();
			}
			FileWriter writer = new FileWriter(directory + File.separator + App.getFile(), true);
			Formatter file = new Formatter(writer);
			file.format("%-20s %-20s %-15s %-15s %-15s %n", username, exercise.getName(), duration, calories, date);
			file.close();
			writer.close();
		} catch (Exception ignored) {

		}

	}

	private void refreshPieChart(MatPieChart pieChart) {

		ResultSet results = Database.executeQuery("SELECT e.exerciseID, e.exerciseName, e.caloriesPerMin, h.duration \n" +
				"FROM histories AS h \n" +
				"INNER JOIN exercises AS e \n" +
				"ON h.exerciseID = e.exerciseID \n" +
				"WHERE h.username = '" + username + "' AND h.date = '" + currentDate + "' \n" +
				"ORDER BY e.exerciseID; ");

		pieChart.clearData();
		Map<Exercise, Integer> exercises = new LinkedHashMap<>();

		try {
			assert results != null;
			while (results.next()) {
				int id = results.getInt("exerciseID");
				String name = results.getString("exerciseName");
				int caloriesPerMin = results.getInt("caloriesPerMin");
				Exercise exercise = new Exercise(id, name, caloriesPerMin);
				int durationToAdd = results.getInt("duration");
				int duration = exercises.getOrDefault(exercise, 0);
				duration += durationToAdd;
				exercises.put(exercise, duration);
			}
		} catch (Exception exception) {
			Database.showError();
		}

		int totalCalories = 0;

		for (Exercise exercise : exercises.keySet()) {
			int id = exercise.getId();
			String name = exercise.getName();
			int duration = exercises.get(exercise);
			int calories = exercise.getCaloriesPerMin() * duration;
			totalCalories += calories;
			Color color;
			if (id <= 3) {
				color = Theme.DARK_BLUE.color;
			} else if (id <= 6) {
				color = Theme.BLUE.color;
			} else if (id <= 9) {
				color = Theme.LIGHT_BLUE.color;
			} else {
				color = Theme.TURQUOISE.color;
			}
			pieChart.addData(new PieSegment(name, calories, color));
		}

		if (totalCalories < dailyGoal) {
			int toBurn = dailyGoal - totalCalories;
			pieChart.addData(new PieSegment("To burn", toBurn, Theme.LIGHT_GRAY.color));
		}

		int percentage = (int) (((double) totalCalories / (double) dailyGoal) * 100);
		pieChart.setInfo(percentage, totalCalories, dailyGoal);

	}

	private Exercise getSelectedExercise(String name, Map<String, Exercise> map) {
		return map.get(name);
	}

	private int getSelectedDuration(String duration, JLabel error) {
		try {
			if (Objects.equals(duration, "")) {
				error.setText("Duration required");
			} else if ((Integer.parseInt(duration) < 1) || (Integer.parseInt(duration) > 250)) {
				error.setText("Duration must be between 1 and 250");
			} else {
				error.setText(" ");
				return Integer.parseInt(duration);
			}
		} catch (Exception exception) {
			error.setText("Invalid duration");
		}
		return 0;
	}

	private int getDailyGoal() {
		ResultSet results = Database.executeQuery("SELECT height, weight, birthYear, gender FROM users WHERE username = BINARY '" + username + "'");
		int dailyGoal = 0;
		try {
			assert results != null;
			results.next();
			int height = results.getInt("height");
			int weight = results.getInt("weight");
			int birthYear = results.getInt("birthYear");
			String gender = results.getString("gender");
			int currentYear = Calendar.getInstance().get(Calendar.YEAR);
			if (gender.equals("Male")) {
				dailyGoal = (int) (88.362 + (13.397 * weight) + (4.799 * (height) - (5.677 * (currentYear - birthYear))));
			} else {
				dailyGoal = (int) (447.593 + (9.247 * weight) + (3.098 * (height) - (4.330 * (currentYear - birthYear))));
			}
			dailyGoal = (int) (Math.round(dailyGoal / 10.0) * 10);
		} catch (Exception exception) {
			Database.showError();
		}
		return dailyGoal;
	}

	private Map<String, Exercise> getExerciseMap() {
		ResultSet results = Database.executeQuery("SELECT exerciseID, exerciseName, caloriesPerMin FROM exercises");
		Map<String, Exercise> exercises = new LinkedHashMap<>();
		try {
			assert results != null;
			while (results.next()) {
				int id = results.getInt("exerciseID");
				String name = results.getString("exerciseName");
				int caloriesPerMin = results.getInt("caloriesPerMin");
				Exercise exercise = new Exercise(id, name, caloriesPerMin);
				exercises.put(name, exercise);
			}
		} catch (Exception exception) {
			Database.showError();
		}
		return exercises;
	}

	private String[] getExerciseArray(Map<String, Exercise> exerciseMap) {
		String[] exercises = new String[exerciseMap.size() + 1];
		int index = 0;
		exercises[index] = "Exercise type";
		for (String exercise : exerciseMap.keySet()) {
			exercises[++index] = exercise;
		}
		return exercises;
	}

	private JLabel addLabel(String text, float fontSize, Color color, int top, int bottom) {
		constraints.gridy += 200;
		constraints.insets = new Insets(top, 0, bottom, 0);
		JLabel label = new JLabel(text);
		label.setFont(App.getFont().deriveFont(fontSize));
		label.setForeground(color);
		rightPanel.add(label, constraints);
		return label;
	}

	private MatButton addButton(String text, float fontSize, Color background, Color foreground, int width, int top, int bottom) {
		addSpacing(top, bottom);
		MatButton button = new MatButton();
		button.setText(text);
		button.setBorderRadius(10);
		button.setFont(App.getFont().deriveFont(fontSize));
		button.setBackground(background);
		button.setForeground(foreground);
		button.setPreferredSize(new Dimension(width, 50));
		rightPanel.add(button, constraints);
		return button;
	}

	private void addSpacing(int top, int bottom) {
		constraints.gridy += 200;
		constraints.insets = new Insets(top, 0, bottom, 0);
	}

	private void setupWindow() {
		pack();
		setResizable(false);
		setLocationRelativeTo(null);
		leftPanel.setBackground(Color.WHITE);
		rightPanel.setBackground(Color.WHITE);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setMinimumSize(Toolkit.getDefaultToolkit().getScreenSize());
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setVisible(true);
	}

}