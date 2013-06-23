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

public class ValSlider extends JPanel implements ActionListener,ChangeListener{

		private static final int V_MIN = 0;
		private static final int V_MAX = 255;
		private static int v_init;
		public static JSlider value;

		public ValSlider(){
			setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
			
			JLabel valueLabel = null;
			valueLabel = new JLabel("Value", JLabel.CENTER);
			valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

			switch (ComboBox.colorIndex) {
			case 0:
				v_init = Application.red_v;
				break;
			case 1:
				v_init = Application.green_v;
				break;
			case 2:
				v_init = Application.lightBlue_v;
				break;
			case 3:
				v_init = Application.blue_v;
				break;
			case 4:
				v_init = Application.purple_v;
				break;
			default:
				v_init = Application.red_v;
				break;
			}
			value = new JSlider(JSlider.HORIZONTAL, V_MIN, V_MAX,
					v_init);

			value.addChangeListener(this);

			SliderDemo.val_value.setText("Value: " + value.getValue());
			
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

				SliderDemo.val_value.setText("Value: " + value.getValue());
				switch (ComboBox.colorIndex) {
				case 0:
					main.Application.red_v = (int)source.getValue();
					break;
				case 1:
					main.Application.green_v = (int)source.getValue();
					break;
				case 2:
					main.Application.lightBlue_v = (int)source.getValue();
					break;
				case 3:
					main.Application.blue_v = (int)source.getValue();
					break;
				case 4:
					main.Application.purple_v = (int)source.getValue();
					break;
				default:
					main.Application.purple_v = (int)source.getValue();
					break;
				}
			}
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}