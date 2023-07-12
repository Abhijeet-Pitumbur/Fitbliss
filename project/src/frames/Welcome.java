package frames;

import app.App;
import com.google.common.hash.Hashing;
import material.MatButton;
import material.MatComboBox;
import material.MatPasswordField;
import material.MatTextField;
import utilities.Database;
import utilities.Theme;

import javax.swing.*;
import java.awt.*;
import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.Objects;
import java.util.regex.Pattern;

public class Welcome extends JFrame {

	private final JPanel leftPanel, rightPanel, signInPanel, signUpPanel;
	private int spacing;

	public Welcome() {

		setTitle(App.getTitle());
		setIconImage(App.getIcon());
		setLayout(new GridLayout(1, 2));

		leftPanel = new JPanel();
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
		JLabel welcome = new JLabel(new ImageIcon(getWelcomeImage()));
		welcome.setAlignmentX(Component.LEFT_ALIGNMENT);
		leftPanel.add(welcome);

		rightPanel = new JPanel(new CardLayout());

		signInPanel = new JPanel(new GridBagLayout());
		signInPanel.setBackground(Color.WHITE);
		GridBagConstraints signInConstraints = getConstraints();

		spacing = 0;
		addLabel(signInPanel, signInConstraints, 50, 50, "Welcome!", 50f, Theme.LIGHT_BLUE.color);
		addLabel(signInPanel, signInConstraints, 0, 50, "Sign in to your account", 30f, Theme.DARK_BLUE.color);

		MatTextField signInUsername = addUsernameField(signInPanel, signInConstraints, 50);
		MatPasswordField signInPassword = addPasswordField(signInPanel, signInConstraints, 50);

		MatButton signIn = addButton(signInPanel, signInConstraints, 30, "Sign in", Theme.LIGHT_BLUE.color, Color.WHITE);

		JLabel signInError = addLabel(signInPanel, signInConstraints, 0, 40, " ", 17f, Theme.RED.color);

		signIn.addActionListener(event -> {
			String username = signInUsername.getText();
			String password = String.valueOf(signInPassword.getPassword());
			if (validateSignIn(username, password, signInError)) {
				new Dashboard(username);
				dispose();
			}
		});

		addLabel(signInPanel, signInConstraints, 0, 20, "Don't have an account yet?", 20f, Theme.DARK_BLUE.color);
		MatButton showSignUp = addButton(signInPanel, signInConstraints, 50, "Sign up", Color.WHITE, Theme.LIGHT_BLUE.color);

		signUpPanel = new JPanel(new GridBagLayout());
		signUpPanel.setBackground(Color.WHITE);
		GridBagConstraints signUpConstraints = getConstraints();

		spacing = 0;
		addLabel(signUpPanel, signUpConstraints, 0, 20, "Hey there!", 50f, Theme.LIGHT_BLUE.color);
		addLabel(signUpPanel, signUpConstraints, 0, 35, "Sign up to get started", 30f, Theme.DARK_BLUE.color);

		MatTextField signUpUsername = addUsernameField(signUpPanel, signUpConstraints, 25);
		MatPasswordField signUpPassword = addPasswordField(signUpPanel, signUpConstraints, 25);

		spacing += 100;
		signUpConstraints.gridy = spacing;
		MatTextField signUpHeight = addMiniTextField(signUpPanel, signUpConstraints, 0, 330, "Height (cm)");
		MatTextField signUpWeight = addMiniTextField(signUpPanel, signUpConstraints, 120, 0, "Weight (kg)");

		spacing += 100;
		signUpConstraints.gridy = spacing;
		String[] genders = getGenders();
		String[] years = getYears();

		MatComboBox<String> signUpBirthYear = addMiniComboBox(signUpPanel, signUpConstraints, 0, 330, years);
		MatComboBox<String> signUpGender = addMiniComboBox(signUpPanel, signUpConstraints, 120, 0, genders);

		MatButton signUp = addButton(signUpPanel, signUpConstraints, 20, "Sign up", Theme.LIGHT_BLUE.color, Color.WHITE);

		JLabel signUpError = addLabel(signUpPanel, signInConstraints, 0, 20, " ", 17f, Theme.RED.color);

		signUp.addActionListener(event -> {
			String username = signUpUsername.getText();
			String password = String.valueOf(signUpPassword.getPassword());
			String height = signUpHeight.getText();
			String weight = signUpWeight.getText();
			String birthYear = String.valueOf(signUpBirthYear.getSelectedItem());
			String gender = String.valueOf(signUpGender.getSelectedItem());
			if (validateSignUp(username, password, height, weight, birthYear, gender, signUpError)) {
				new Dashboard(username);
				dispose();
			}
		});

		addLabel(signUpPanel, signUpConstraints, 0, 15, "Already have an account?", 20f, Theme.DARK_BLUE.color);
		MatButton showSignIn = addButton(signUpPanel, signUpConstraints, 30, "Sign in", Color.WHITE, Theme.LIGHT_BLUE.color);

		showSignIn.addActionListener(event -> showSignInCard(true, signInError, signUpError));
		showSignUp.addActionListener(event -> showSignInCard(false, signInError, signUpError));

		rightPanel.add(signInPanel);
		rightPanel.add(signUpPanel);

		showSignInCard(true, signInError, signUpError);

		add(leftPanel);
		add(rightPanel);

		setupWindow();

		Database.checkConnection();

	}

	private boolean validateSignIn(String username, String password, JLabel error) {

		if (Objects.equals(username, "")) {
			error.setText("Username required");
			return false;
		} else if (Objects.equals(password, "")) {
			error.setText("Password required");
			return false;
		} else if ((username.length() < 5) || (username.length() > 15)) {
			error.setText("Username must be between 5 and 15 characters");
			return false;
		} else if (!Pattern.matches("^[A-Za-z0-9]{5,15}$", username)) {
			error.setText("Username must be alphanumeric");
			return false;
		} else if ((password.length() < 5) || (password.length() > 15)) {
			error.setText("Password must be between 5 and 15 characters");
			return false;
		}

		try {
			ResultSet matchedUsername = Database.executeQuery("SELECT username FROM users WHERE username = BINARY '" + username + "'");
			assert matchedUsername != null;
			if (matchedUsername.next()) {
				String passwordHash = hashPassword(password);
				ResultSet matchedPassword = Database.executeQuery("SELECT username FROM users WHERE username = BINARY '" + username + "' AND password = '" + passwordHash + "'");
				assert matchedPassword != null;
				if (matchedPassword.next()) {
					error.setText(" ");
					return true;
				} else {
					error.setText("Invalid password");
					return false;
				}
			} else {
				error.setText("Username doesn't exist");
				return false;
			}
		} catch (Exception exception) {
			Database.showError();
			return false;
		}

	}

	private boolean validateSignUp(String username, String password, String height, String weight, String birthYear, String gender, JLabel error) {

		try {
			if (Objects.equals(username, "")) {
				error.setText("Username required");
				return false;
			} else if (Objects.equals(password, "")) {
				error.setText("Password required");
				return false;
			} else if (Objects.equals(height, "")) {
				error.setText("Height required");
				return false;
			} else if (Objects.equals(weight, "")) {
				error.setText("Weight required");
				return false;
			} else if (Objects.equals(birthYear, "Birth year")) {
				error.setText("Birth year required");
				return false;
			} else if (Objects.equals(gender, "Gender")) {
				error.setText("Gender required");
				return false;
			} else if ((username.length() < 5) || (username.length() > 15)) {
				error.setText("Username must be between 5 and 15 characters");
				return false;
			} else if (!Pattern.matches("^[A-Za-z0-9]{5,15}$", username)) {
				error.setText("Username must be alphanumeric");
				return false;
			} else if ((password.length() < 5) || (password.length() > 15)) {
				error.setText("Password must be between 5 and 15 characters");
				return false;
			} else if ((Integer.parseInt(height) < 10) || (Integer.parseInt(height) > 250)) {
				error.setText("Height must be between 10 and 250");
				return false;
			} else if ((Integer.parseInt(weight) < 10) || (Integer.parseInt(weight) > 250)) {
				error.setText("Weight must be between 10 and 250");
				return false;
			}
		} catch (Exception exception) {
			error.setText("Invalid height or weight");
			return false;
		}

		try {
			ResultSet matchedUsername = Database.executeQuery("SELECT username FROM users WHERE username = BINARY '" + username + "'");
			assert matchedUsername != null;
			if (!matchedUsername.next()) {
				String passwordHash = hashPassword(password);
				int updatedRows = Database.executeUpdate("INSERT INTO users (username, height, weight, birthYear, gender, password) VALUES ('" + username + "', " + height + ", " + weight + ", " + birthYear + ", '" + gender + "', '" + passwordHash + "')");
				if (updatedRows > 0) {
					error.setText(" ");
					return true;
				} else {
					throw new Exception();
				}
			} else {
				error.setText("Username already exists");
				return false;
			}
		} catch (Exception exception) {
			Database.showError();
			return false;
		}

	}

	private JLabel addLabel(JPanel panel, GridBagConstraints constraints, int top, int bottom, String text, float fontSize, Color color) {
		addSpacing(constraints, top, bottom);
		JLabel label = new JLabel(text);
		label.setFont(App.getFont().deriveFont(fontSize));
		label.setForeground(color);
		panel.add(label, constraints);
		return label;
	}

	private MatTextField addUsernameField(JPanel panel, GridBagConstraints constraints, int bottom) {
		addSpacing(constraints, 0, bottom);
		MatTextField usernameField = new MatTextField();
		usernameField.setLabel("Username");
		usernameField.setPreferredSize(new Dimension(420, 55));
		usernameField.setFont(App.getFont().deriveFont(20f));
		panel.add(usernameField, constraints);
		return usernameField;
	}

	private MatPasswordField addPasswordField(JPanel panel, GridBagConstraints constraints, int bottom) {

		addSpacing(constraints, 0, bottom);

		MatPasswordField passwordField = new MatPasswordField();
		passwordField.setLabel("Password");
		passwordField.setPreferredSize(new Dimension(420, 55));
		passwordField.setFont(App.getFont().deriveFont(20f));
		passwordField.setEchoChar('•');

		MatButton showHide = new MatButton();
		showHide.setText("Show");
		showHide.setBorderRadius(13);
		showHide.setFont(App.getFont().deriveFont(15f));
		showHide.setBackground(Color.WHITE);
		showHide.setForeground(Theme.LIGHT_BLUE.color);
		showHide.setType(MatButton.ButtonType.FLAT);
		showHide.setPreferredSize(new Dimension(60, 35));

		showHide.addActionListener(event -> {
			if (Objects.equals(showHide.getText(), "Show")) {
				showHide.setText("Hide");
				passwordField.setEchoChar((char) 0);
			} else {
				showHide.setText("Show");
				passwordField.setEchoChar('•');
			}
		});

		constraints.insets = new Insets(0, 250, bottom + 4, 0);
		panel.add(showHide, constraints);
		constraints.insets = new Insets(0, 0, bottom, 100);
		panel.add(passwordField, constraints);

		return passwordField;

	}

	private MatButton addButton(JPanel panel, GridBagConstraints constraints, int bottom, String text, Color background, Color foreground) {
		addSpacing(constraints, 0, bottom);
		MatButton button = new MatButton();
		button.setText(text);
		button.setBorderRadius(13);
		button.setFont(App.getFont().deriveFont(20f));
		button.setBackground(background);
		button.setForeground(foreground);
		button.setPreferredSize(new Dimension(420, 50));
		panel.add(button, constraints);
		return button;
	}

	private MatTextField addMiniTextField(JPanel panel, GridBagConstraints signUpConstraints, int left, int right, String label) {
		signUpConstraints.insets = new Insets(0, left, 25, right);
		MatTextField textField = new MatTextField();
		textField.setLabel(label);
		textField.setPreferredSize(new Dimension(190, 55));
		textField.setFont(App.getFont().deriveFont(20f));
		panel.add(textField, signUpConstraints);
		return textField;
	}

	private MatComboBox<String> addMiniComboBox(JPanel panel, GridBagConstraints signUpConstraints, int left, int right, String[] list) {
		signUpConstraints.insets = new Insets(0, left, 40, right);
		MatComboBox<String> comboBox = new MatComboBox<>();
		comboBox.setModel(new DefaultComboBoxModel<>(list));
		comboBox.setPreferredSize(new Dimension(190, 45));
		comboBox.setFont(App.getFont().deriveFont(20f));
		panel.add(comboBox, signUpConstraints);
		return comboBox;
	}

	private void showSignInCard(boolean show, JLabel signInError, JLabel signUpError) {
		signInError.setText(" ");
		signUpError.setText(" ");
		signInPanel.setVisible(show);
		signUpPanel.setVisible(!show);
	}

	private void addSpacing(GridBagConstraints constraints, int top, int bottom) {
		spacing += 100;
		constraints.gridy = spacing;
		constraints.insets = new Insets(top, 0, bottom, 100);
	}

	@SuppressWarnings("UnstableApiUsage")
	private String hashPassword(String password) {
		return Hashing.sha256().hashString(password, StandardCharsets.UTF_8).toString().toUpperCase();
	}

	private GridBagConstraints getConstraints() {
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridwidth = constraints.gridheight = 100;
		constraints.gridx = 0;
		return constraints;
	}

	private Image getWelcomeImage() {
		return new ImageIcon(Objects.requireNonNull(getClass().getResource("/welcome.png")))
				.getImage()
				.getScaledInstance(-1, (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 1.1), Image.SCALE_SMOOTH);
	}

	private String[] getGenders() {
		return new String[]{"Gender", "Male", "Female"};
	}

	private String[] getYears() {
		int minAge = 5;
		int maxAge = 100;
		String[] years = new String[maxAge - minAge + 2];
		years[0] = "Birth year";
		int year = Calendar.getInstance().get(Calendar.YEAR) - minAge;
		for (int i = 1; i < years.length; i++) {
			years[i] = Integer.toString(year);
			year--;
		}
		return years;
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