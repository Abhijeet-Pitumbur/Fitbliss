package material;

import utilities.PieSegment;
import utilities.Theme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MatPieChart extends JComponent {

	private final List<PieSegment> pieSegments;
	private final DecimalFormat formatter = new DecimalFormat("###,###");
	private int percentage, burntToday, dailyGoal;
	private int hoverIndex = -1;

	public MatPieChart() {
		pieSegments = new ArrayList<>();
		MouseAdapter adapter = new MouseAdapter() {
			@Override
			public void mouseMoved(MouseEvent event) {
				int index = checkMouseHover(event.getPoint());
				if (index != hoverIndex) {
					hoverIndex = index;
					repaint();
				}
			}
		};
		addMouseListener(adapter);
		addMouseMotionListener(adapter);
	}

	public void addData(PieSegment pieSegment) {
		pieSegments.add(pieSegment);
	}

	public void setInfo(int percentage, int burntToday, int dailyGoal) {
		this.percentage = percentage;
		this.burntToday = burntToday;
		this.dailyGoal = dailyGoal;
	}

	@Override
	public void paintComponent(Graphics graphics) {

		Graphics2D graphics2D = (Graphics2D) graphics.create();
		graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		double width = getWidth();
		double height = getHeight();
		double size = Math.min(width, height);
		size -= (size * 0.05) + 0.25 * size;
		double x = (width - size) / 2;
		double y = (height - size) / 2;
		double centerX = width / 2;
		double centerY = height / 2;
		double totalValue = getTotalValue();
		double drawAngle = 90;
		float fontSize = (float) (getFont().getSize() * size * 0.0035);

		if (hoverIndex >= 0) {
			graphics2D.setColor(pieSegments.get(hoverIndex).getColor());
			graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
			graphics2D.fill(createShape(hoverIndex));
		}

		graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));

		for (PieSegment pieSegment : pieSegments) {
			double angle = pieSegment.getValue() * 360 / totalValue;
			Area area = new Area(new Arc2D.Double(x, y, size, size, drawAngle, -angle, Arc2D.PIE));
			double s1 = size * 0.7;
			double x1 = (width - s1) / 2;
			double y1 = (height - s1) / 2;
			area.subtract(new Area(new Ellipse2D.Double(x1, y1, s1, s1)));
			graphics2D.setColor(pieSegment.getColor());
			graphics2D.fill(area);
			graphics2D.setColor(Color.WHITE);
			graphics2D.draw(area);
			drawAngle -= angle;
		}

		drawInfo(graphics2D);

		drawAngle = 90;

		for (int i = 0; i < pieSegments.size(); i++) {
			PieSegment pieSegment = pieSegments.get(i);
			double angle = pieSegment.getValue() * 360 / totalValue;
			double textAngle = -(drawAngle - angle / 2);
			double cosX = Math.cos(Math.toRadians(textAngle));
			double sinY = Math.sin(Math.toRadians(textAngle));
			graphics2D.setFont(getFont().deriveFont(fontSize));
			graphics2D.setColor(Color.WHITE);
			if (hoverIndex == i) {
				double labelSize = size / 2;
				double labelX = centerX + cosX * labelSize;
				double labelY = centerY + sinY * labelSize;
				String calories = getFormattedNumber((int) Math.round(pieSegment.getValue()));
				String percentage = getPercentage(pieSegment.getValue());
				String info = calories + " cal · " + percentage + "%";
				drawLabel(graphics2D, size, textAngle, labelX, labelY, pieSegment.getLabel(), info);
			}
			drawAngle -= angle;
		}

		graphics2D.dispose();
		super.paintComponent(graphics);

	}

	private void drawLabel(Graphics2D graphics2D, double size, double angle, double labelX, double labelY, String name, String info) {

		float fontSize = (float) (getFont().getSize() * size * 0.003);
		boolean isUp = !(angle > 0 && angle < 180);
		double space = size * 0.03;
		double spaceV = size * 0.01;
		double paceH = size * 0.03;
		FontMetrics plainMetrics = graphics2D.getFontMetrics(getFont().deriveFont(fontSize));
		FontMetrics boldMetrics = graphics2D.getFontMetrics(getFont().deriveFont(Font.BOLD, fontSize));
		Rectangle2D plainRec = plainMetrics.getStringBounds(name, graphics2D);
		Rectangle2D boldRec = plainMetrics.getStringBounds(info, graphics2D);
		double width = Math.max(plainRec.getWidth() + paceH * 2, boldRec.getWidth() + paceH * 2);
		double height = plainRec.getHeight() + boldRec.getHeight() + spaceV * 2;
		double recY = isUp ? labelY - height - space : labelY + space;
		double recX = labelX - (width / 2);
		RoundRectangle2D rec = new RoundRectangle2D.Double(recX, recY, width, height, 5, 5);

		graphics2D.fill(rec);
		graphics2D.setColor(Color.WHITE);
		graphics2D.draw(rec);
		graphics2D.setColor(getForeground());
		recX += paceH;
		graphics2D.setFont(getFont().deriveFont(fontSize));
		graphics2D.drawString(name, (float) recX, (float) (recY + plainMetrics.getAscent() + spaceV));
		graphics2D.setFont(getFont().deriveFont(Font.BOLD, fontSize));
		graphics2D.drawString(info, (float) recX, (float) (recY + height - boldRec.getHeight() + boldMetrics.getAscent() - spaceV));

	}

	private void drawInfo(Graphics2D graphics2D) {

		ImageIcon icon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/dashboard.png")));
		icon.paintIcon(this, graphics2D, 345, 300);

		graphics2D.setFont(getFont().deriveFont(50f));
		graphics2D.setColor(Theme.DARK_BLUE.color);
		String formattedPercentage = getFormattedNumber(percentage) + "%";
		graphics2D.drawString(formattedPercentage, (getWidth() / 2) - (formattedPercentage.length() * 10 + 16), 425);

		graphics2D.setFont(getFont().deriveFont(23f));
		graphics2D.setColor(Theme.BLUE.color);
		String formattedBurntToday = getFormattedNumber(burntToday);
		graphics2D.drawString(formattedBurntToday + " cal burnt today", 296 - formattedBurntToday.length() * 5, 470);

		graphics2D.setFont(getFont().deriveFont(18f));
		graphics2D.setColor(Theme.GRAY.color);
		String formattedDailyGoal = getFormattedNumber(dailyGoal);
		graphics2D.drawString("Daily goal is " + formattedDailyGoal + " cal", 316 - formattedDailyGoal.length() * 5, 505);

	}

	private Shape createShape(int index) {

		Shape shape = null;
		double width = getWidth();
		double height = getHeight();
		double size = Math.min(width, height);
		size -= (size * 0) + (0.25 * size);
		double x = (width - size) / 2;
		double y = (height - size) / 2;
		double totalValue = getTotalValue();
		double drawAngle = 90;

		for (int i = 0; i < pieSegments.size(); i++) {
			double angle = pieSegments.get(i).getValue() * 360 / totalValue;
			if (index == i) {
				Area area = new Area(new Arc2D.Double(x, y, size, size, drawAngle, -angle, Arc2D.PIE));
				size -= size * 0.05 - size * (float) 0 * 2;
				x = (width - size) / 2;
				y = (height - size) / 2;
				area.subtract(new Area(new Arc2D.Double(x, y, size, size, drawAngle, -angle, Arc2D.PIE)));
				shape = area;
				break;
			}
			drawAngle -= angle;
		}

		return shape;

	}

	private int checkMouseHover(Point point) {

		int index = -1;
		double width = getWidth();
		double height = getHeight();
		double size = Math.min(width, height);
		size -= (size * 0.05) + 0.25 * size;
		double x = (width - size) / 2;
		double y = (height - size) / 2;
		double totalValue = getTotalValue();
		double drawAngle = 90;

		for (int i = 0; i < pieSegments.size(); i++) {
			PieSegment pieSegment = pieSegments.get(i);
			double angle = pieSegment.getValue() * 360 / totalValue;
			Area area = new Area(new Arc2D.Double(x, y, size, size, drawAngle, -angle, Arc2D.PIE));
			double s1 = size * 0.7;
			double x1 = (width - s1) / 2;
			double y1 = (height - s1) / 2;
			area.subtract(new Area(new Ellipse2D.Double(x1, y1, s1, s1)));
			if (area.contains(point)) {
				index = i;
				break;
			}
			drawAngle -= angle;
		}

		return index;

	}

	private String getPercentage(double value) {
		double total = getTotalValue();
		return String.valueOf(Math.round(value * 100 / total));
	}

	private double getTotalValue() {
		double max = 0;
		for (PieSegment pieSegment : pieSegments) {
			max += pieSegment.getValue();
		}
		return max;
	}

	private String getFormattedNumber(int number) {
		return formatter.format(number);
	}

	public void clearData() {
		pieSegments.clear();
		repaint();
	}

}