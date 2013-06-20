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

public class SatSlider extends JPanel implements ActionListener,ChangeListener{
		
		private static final int S_MAX = 255;
		private static final int S_MIN = 0;
		public static int s_init;

		public SatSlider(){
			setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
			JLabel saturationLabel = null;
			
			switch (ComboBox.colorIndex) {
			case 0:
				s_init = Application.red_s;
				saturationLabel = new JLabel("Saturation red:" + Application.red_s, JLabel.CENTER);
				saturationLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
				break;
			case 1:
				s_init = Application.green_s;
				saturationLabel = new JLabel("Saturation green:" + Application.green_s, JLabel.CENTER);
				saturationLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
				break;
			case 2:
				s_init = Application.lightBlue_s;
				saturationLabel = new JLabel("Saturation lightBlue:" + Application.lightBlue_s, JLabel.CENTER);
				saturationLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
				break;
			case 3:
				s_init = Application.blue_s;
				saturationLabel = new JLabel("Saturation blue:" + Application.blue_s, JLabel.CENTER);
				saturationLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
				break;
			case 4:
				s_init = Application.purple_s;
				saturationLabel = new JLabel("Saturation purple:" + Application.purple_s, JLabel.CENTER);
				saturationLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
				break;
			default:
				s_init = Application.red_s;
				break;
			}
			
			JSlider saturation = new JSlider(JSlider.HORIZONTAL, S_MIN, S_MAX,
					s_init);
			
			saturation.addChangeListener(this);
			
			saturation.setMajorTickSpacing(40);
			saturation.setMinorTickSpacing(1);
			saturation.setPaintTicks(true);
			saturation.setPaintLabels(true);
			saturation.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
			Font font = new Font("Serif", Font.ITALIC, 15);
			saturation.setFont(font);
			
			add(saturationLabel);
			add(saturation);
			setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		}

		@Override
		public void stateChanged(ChangeEvent e) {
			JSlider source = (JSlider) e.getSource();
			if (source.getValueIsAdjusting()) {
				switch (ComboBox.colorIndex) {
				case 0:
					main.Application.red_s = (int)source.getValue();
					break;
				case 1:
					main.Application.green_s = (int)source.getValue();
					break;
				case 2:
					main.Application.lightBlue_s = (int)source.getValue();
					break;
				case 3:
					main.Application.blue_s = (int)source.getValue();
					break;
				case 4:
					main.Application.purple_s = (int)source.getValue();
					break;
				default:
					main.Application.purple_s = (int)source.getValue();
					break;
				}
			}
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}