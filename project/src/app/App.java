package app;

import frames.Welcome;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class App {

	private static String title, database, file;
	private static Image icon;
	private static Font font;

	static {
		try {
			title = "Fitbliss";
			database = "fitbliss";
			file = "Fitbliss-History.txt";
			icon = new ImageIcon(Objects.requireNonNull(App.class.getResource("/icon.png"))).getImage();
			font = Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(App.class.getResourceAsStream("/font.ttf")));
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new Welcome();
	}

	public static String getTitle() {
		return title;
	}

	public static String getDatabase() {
		return database;
	}

	public static String getFile() {
		return file;
	}

	public static Image getIcon() {
		return icon;
	}

	public static Font getFont() {
		return font;
	}

}
