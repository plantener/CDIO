package Calibrate;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JApplet;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import main.Application;

public class ComboBox extends JPanel {
	public String[] calibrationColor = {"red","green","lightBlue","blue", "purple"};
	public static int colorIndex = 0;
	
	private JComboBox c = new JComboBox();
	
	public ComboBox() {
		for (int i = 0; i < calibrationColor.length; i++) {
			c.addItem(calibrationColor[i]);
		}
		c.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				colorIndex = ((JComboBox)e.getSource()).getSelectedIndex();
				
				switch (colorIndex+1) {
				case 1:
					SliderDemo.hue.setValue(Application.red_h);
					SatSlider.saturation.setValue(Application.red_s);
					ValSlider.value.setValue(Application.red_v);
					UpperHSlider.upper_h.setValue(Application.red_upper_h);
					break;
				case 2:
					SliderDemo.hue.setValue(Application.green_h);
					SatSlider.saturation.setValue(Application.green_s);
					ValSlider.value.setValue(Application.green_v);
					UpperHSlider.upper_h.setValue(Application.green_upper_h);
					break;
				case 3:
					SliderDemo.hue.setValue(Application.lightBlue_h);
					SatSlider.saturation.setValue(Application.lightBlue_s);
					ValSlider.value.setValue(Application.lightBlue_v);
					UpperHSlider.upper_h.setValue(Application.lightBlue_upper_h);
					break;
				case 4: 
					SliderDemo.hue.setValue(Application.blue_h);
					SatSlider.saturation.setValue(Application.blue_s);
					ValSlider.value.setValue(Application.blue_v);
					UpperHSlider.upper_h.setValue(Application.blue_upper_h);
					break;
				case 5:
					SliderDemo.hue.setValue(Application.purple_h);
					SatSlider.saturation.setValue(Application.purple_s);
					ValSlider.value.setValue(Application.purple_v);
					UpperHSlider.upper_h.setValue(Application.purple_upper_h);
					break;
				default:
					break;
				}
			}
		}); 
		add(c);
	}
	
	public void init(){
		
				
	}

}
