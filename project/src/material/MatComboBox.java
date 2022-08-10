package material;

import app.App;
import utilities.Line;
import utilities.Theme;
import utilities.Utilities;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.plaf.basic.ComboPopup;
import java.awt.*;
import java.awt.event.FocusEvent;

import static material.MatTextField.hintOpacityMask;
import static material.MatTextField.lineOpacityMask;

public class MatComboBox<T> extends JComboBox<T> {

	private final Line line = new Line(this);
	private final Color accentColor = Theme.LIGHT_BLUE.color;

	public MatComboBox() {

		setModel(new DefaultComboBoxModel<>());
		setRenderer(new FieldRenderer<>(this));
		setCursor(new Cursor(Cursor.HAND_CURSOR));

		setUI(new BasicComboBoxUI() {

			@Override
			public ComboPopup createPopup() {
				BasicComboPopup popupBox = new Popup(comboBox);
				popupBox.getAccessibleContext().setAccessibleParent(comboBox);
				return popupBox;
			}

			@Override
			public JButton createArrowButton() {
				return null;
			}

		});

		setOpaque(false);
		setBackground(Color.WHITE);

	}

	@Override
	public void processFocusEvent(FocusEvent event) {
		super.processFocusEvent(event);
		line.update();
	}

	@Override
	public void paint(Graphics graphics) {

		Graphics2D graphics2d = (Graphics2D) graphics;
		graphics2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		graphics2d.setColor(getBackground());
		graphics2d.fillRect(0, 0, getWidth(), getHeight() - 4);

		graphics.setFont(App.getFont().deriveFont(16f));
		graphics.setColor(getSelectedItem() == null ? Utilities.applyAlphaMask(getForeground(), hintOpacityMask) : getForeground());
		FontMetrics metrics = graphics.getFontMetrics(graphics.getFont());
		String hint = "";
		String text = getSelectedItem() != null ? getSelectedItem().toString() : hint;
		graphics.drawString(text, 0, metrics.getAscent() + (getHeight() - metrics.getHeight()) / 2);

		if (isFocusOwner()) {
			graphics2d.setColor(accentColor);
		}

		graphics2d.fillPolygon(new int[]{getWidth() - 5, getWidth() - 10, getWidth() - 15}, new int[]{getHeight() / 2 - 3, getHeight() / 2 + 3, getHeight() / 2 - 3}, 3);
		graphics2d.setColor(Utilities.applyAlphaMask(getForeground(), lineOpacityMask));
		graphics2d.fillRect(0, getHeight() - 4, getWidth(), 1);
		graphics2d.setColor(accentColor);
		graphics2d.fillRect((int) ((getWidth() - line.getWidth()) / 2), getHeight() - 5, (int) line.getWidth(), 2);

	}

	public static class FieldRenderer<T> extends JComponent implements ListCellRenderer<T> {

		private final MatComboBox comboBox;
		private String text;
		private boolean isMouseOver = false;
		private boolean isSelected = false;

		public FieldRenderer(MatComboBox comboBox) {
			this.comboBox = comboBox;
		}

		@Override
		public Component getListCellRendererComponent(JList list, Object object, int index, boolean isSelected, boolean cellHasFocus) {
			text = object != null ? object.toString() : "";
			setSize(list.getWidth(), 56);
			setPreferredSize(new Dimension(list.getWidth(), 32));
			setOpaque(true);
			isMouseOver = isSelected;
			this.isSelected = comboBox.getSelectedIndex() == index;
			return this;
		}

		@Override
		public void paint(Graphics graphics) {

			super.paint(graphics);
			Graphics2D graphics2d = (Graphics2D) graphics;
			graphics2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

			if (isMouseOver) {
				graphics.setColor(Utilities.isDark(comboBox.getBackground()) ? Utilities.brighten(comboBox.getBackground()) : Utilities.darken(comboBox.getBackground()));
			} else {
				graphics.setColor(comboBox.getBackground());
			}

			graphics.fillRect(0, 0, getWidth(), getHeight());
			graphics.setFont(App.getFont().deriveFont(16f));

			if (isSelected) {
				graphics2d.setColor(comboBox.accentColor);
			} else {
				graphics2d.setColor(comboBox.getForeground());
			}

			FontMetrics metrics = graphics.getFontMetrics(graphics.getFont());
			graphics.drawString(text, 24, metrics.getAscent() + (getHeight() - metrics.getHeight()) / 2);

		}

	}

	public static class Popup extends BasicComboPopup {

		public Popup(JComboBox comboBox) {
			super(comboBox);
			setBackground(comboBox.getBackground());
			setOpaque(true);
			setBorderPainted(false);
		}

		@Override
		public JScrollPane createScroller() {
			JScrollPane scrollPane = super.createScroller();
			scrollPane.setVerticalScrollBar(new ScrollBar(comboBox, Adjustable.VERTICAL));
			scrollPane.setBorder(new EmptyBorder(16, 0, 16, 0));
			return scrollPane;
		}

		@Override
		public Rectangle computePopupBounds(int px, int py, int pw, int ph) {
			return super.computePopupBounds(px, py - 2, (int) Math.max(comboBox.getPreferredSize().getWidth(), pw), ph);
		}

		@Override
		public void paint(Graphics graphics) {
			super.paint(graphics);
		}

	}

	public static class ScrollBar extends JScrollBar {

		public ScrollBar(final JComboBox comboBox, int orientation) {

			super(orientation);
			setPreferredSize(new Dimension(5, 100));

			setUI(new BasicScrollBarUI() {

				@Override
				public ScrollListener createScrollListener() {
					setUnitIncrement(15);
					setBlockIncrement(150);
					return super.createScrollListener();
				}

				@Override
				public void paintTrack(Graphics graphics, JComponent component, Rectangle rectangle) {
					graphics.setColor(comboBox.getBackground());
					graphics.fillRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
				}

				@Override
				public JButton createDecreaseButton(int orientation) {
					JButton button = new JButton();
					button.setPreferredSize(new Dimension(0, 0));
					return button;
				}

				@Override
				public JButton createIncreaseButton(int orientation) {
					JButton button = new JButton();
					button.setPreferredSize(new Dimension(0, 0));
					return button;
				}

				@Override
				public Dimension getMinimumThumbSize() {
					return new Dimension(5, 50);
				}

				@Override
				public void paintThumb(Graphics graphics, JComponent component, Rectangle rectangle) {
					if (!rectangle.isEmpty() && this.scrollbar.isEnabled()) {
						((Graphics2D) graphics).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
						boolean isVertical = ScrollBar.this.getOrientation() == Adjustable.VERTICAL;
						graphics.setColor(Theme.GRAY.color);
						graphics.fillRoundRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height, isVertical ? rectangle.width : rectangle.height, isVertical ? rectangle.width : rectangle.height);
					}
				}

				@Override
				public void layoutContainer(Container scrollbarContainer) {
					super.layoutContainer(scrollbarContainer);
					incrButton.setBounds(0, 0, 0, 0);
					decrButton.setBounds(0, 0, 0, 0);
				}

			});

		}

		@Override
		public Component add(Component component) {
			if (component != null) {
				return super.add(component);
			}
			return null;
		}

		@Override
		public void paint(Graphics graphics) {
			graphics.setColor(Color.WHITE);
			graphics.fillRect(0, 0, getWidth(), getHeight());
			super.paint(graphics);
		}

	}

}