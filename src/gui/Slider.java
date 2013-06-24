package gui;

import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import main.Application;

public class Slider extends JPanel implements ChangeListener {

	public JSlider slider;
	
	String name;
	int init, property, min, max;
	JLabel label;

	public Slider(String name, int property, int min, int max) {
		this.name = name;
		this.property = property;
		this.min = min;
		this.max = max;
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		label = new JLabel(this.name, JLabel.CENTER);
		label.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		init = Application.THRESHOLDS[ControlWindow.SELECTED_COLOR][property];
	
		slider = new JSlider(JSlider.HORIZONTAL, min, max, init);

		slider.addChangeListener(this);

		slider.setMajorTickSpacing(40);
		slider.setMinorTickSpacing(1);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		slider.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
		Font font = new Font("Serif", Font.ITALIC, 15);
		slider.setFont(font);

		add(label);
		add(slider);
		setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	}
	
	public void setValue(int value) {
		slider.setValue(value);
		label.setText(name + ": " + value);
		Application.THRESHOLDS[ControlWindow.SELECTED_COLOR][property] = value;
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		JSlider source = (JSlider) e.getSource();
		if (source.getValueIsAdjusting()) {
			setValue(source.getValue());
		}
	}
}