package Calibrate;
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

public class UpperHSlider extends JPanel implements ActionListener,ChangeListener{

		private static final int UPPER_H_MIN = 0;
		private static final int UPPER_H_MAX = 255;
		private static int upper_h_init;
		public static JSlider upper_h;

		public UpperHSlider(){
			setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
			
			JLabel valueLabel = null;
			valueLabel = new JLabel("Upper hue", JLabel.CENTER);
			valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
			
			switch (ComboBox.colorIndex) {
			case 0:
				upper_h_init = Application.red_upper_h;
				break;
			case 1:
				upper_h_init = Application.green_upper_h;
				break;
			case 2:
				upper_h_init = Application.lightBlue_upper_h;
				break;
			case 3:
				upper_h_init = Application.blue_upper_h;
				break;
			case 4:
				upper_h_init = Application.purple_upper_h;
				break;
			default:
				upper_h_init = Application.red_upper_h;
				break;
			}
			upper_h = new JSlider(JSlider.HORIZONTAL, UPPER_H_MIN, UPPER_H_MAX,
					upper_h_init);

			upper_h.addChangeListener(this);
			
			upper_h.setMajorTickSpacing(40);
			upper_h.setMinorTickSpacing(1);
			upper_h.setPaintTicks(true);
			upper_h.setPaintLabels(true);
			upper_h.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
			Font font = new Font("Serif", Font.ITALIC, 15);
			upper_h.setFont(font);
			

			add(valueLabel);
			add(upper_h);
			setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			
		}
		@Override
		public void stateChanged(ChangeEvent e) {
			JSlider source = (JSlider) e.getSource();
			if (source.getValueIsAdjusting()) {
				switch (ComboBox.colorIndex) {
				case 0:
					main.Application.red_upper_h = (int)source.getValue();
					break;
				case 1:
					main.Application.green_upper_h = (int)source.getValue();
					break;
				case 2:
					main.Application.lightBlue_upper_h = (int)source.getValue();
					break;
				case 3:
					main.Application.blue_upper_h = (int)source.getValue();
					break;
				case 4:
					main.Application.purple_upper_h = (int)source.getValue();
					break;
				default:
					main.Application.purple_upper_h = (int)source.getValue();
					break;
				}
			}
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}