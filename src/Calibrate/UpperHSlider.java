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

		private static final int V_MIN = 0;
		private static final int V_MAX = 255;
		private static int v_init;

		public UpperHSlider(){
			setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
			
			JLabel valueLabel = null;
			
			switch (ComboBox.colorIndex) {
			case 0:
				v_init = Application.red_upper_h;
				valueLabel = new JLabel("Upper hue red: " + Application.red_upper_h, JLabel.CENTER);
				valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
				break;
			case 1:
				v_init = Application.green_upper_h;
				break;
			case 2:
				v_init = Application.lightBlue_upper_h;
				break;
			case 3:
				v_init = Application.blue_upper_h;
				break;
			case 4:
				v_init = Application.purple_upper_h;
				break;
			default:
				v_init = Application.red_upper_h;
				break;
			}
			JSlider value = new JSlider(JSlider.HORIZONTAL, V_MIN, V_MAX,
					v_init);

			value.addChangeListener(this);
			
			value.setMajorTickSpacing(40);
			value.setMinorTickSpacing(1);
			value.setPaintTicks(true);
			value.setPaintLabels(true);
			value.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
			Font font = new Font("Serif", Font.ITALIC, 15);
			value.setFont(font);
			

			add(valueLabel);
			add(value);
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