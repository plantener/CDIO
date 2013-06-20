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
				
				switch (colorIndex) {
				case 1:
					SliderDemo.h_init = Application.red_h;
					SatSlider.s_init = Application.red_s;
					ValSlider.v_init = Application.red_v;
					UpperHSlider.upper_h_init = Application.red_upper_h;
					break;
				case 2:
					SliderDemo.h_init = Application.green_h;
					SatSlider.s_init = Application.green_s;
					ValSlider.v_init = Application.green_v;
					UpperHSlider.upper_h_init = Application.green_upper_h;
					break;
				case 3:
					SliderDemo.h_init = Application.lightBlue_h;
					SatSlider.s_init = Application.lightBlue_s;
					ValSlider.v_init = Application.lightBlue_v;
					UpperHSlider.upper_h_init = Application.lightBlue_upper_h;
					break;
				case 4: 
					SliderDemo.h_init = Application.blue_h;
					SatSlider.s_init = Application.blue_s;
					ValSlider.v_init = Application.blue_v;
					UpperHSlider.upper_h_init = Application.blue_upper_h;
					break;
				case 5:
					SliderDemo.h_init = Application.purple_h;
					SatSlider.s_init = Application.purple_s;
					ValSlider.v_init = Application.purple_v;
					UpperHSlider.upper_h_init = Application.purple_upper_h;
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
