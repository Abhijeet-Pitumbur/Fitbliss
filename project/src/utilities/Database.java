package utilities;

import app.App;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static java.lang.System.exit;

public class Database {

	private static Connection connection = null;

	public static void checkConnection() {
		try {
			if (connection == null) {
				connection = DriverManager.getConnection("jdbc:mysql://localhost/" + App.getDatabase(), "root", "");
			}
		} catch (Exception exception) {
			showError();
		}
	}

	public static ResultSet executeQuery(String query) {
		try {
			assert connection != null;
			PreparedStatement statement = connection.prepareStatement(query);
			return statement.executeQuery();
		} catch (Exception exception) {
			showError();
			return null;
		}
	}

	public static int executeUpdate(String update) {
		try {
			assert connection != null;
			PreparedStatement statement = connection.prepareStatement(update);
			return statement.executeUpdate();
		} catch (Exception exception) {
			showError();
			return 0;
		}
	}

	public static void showError() {
		UIManager.put("OptionPane.background", Color.WHITE);
		UIManager.put("Panel.background", Color.WHITE);
		JLabel label = new JLabel("Error connecting to MySQL database '" + App.getDatabase() + "'");
		label.setFont(App.getFont().deriveFont(Font.BOLD, 20f));
		label.setBorder(new EmptyBorder(0, 30, 5, 30));
		label.setForeground(Theme.RED.color);
		JOptionPane.showOptionDialog(null, label, null, JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, new Object[]{}, null);
		exit(0);
	}

}