package Calibrate;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JApplet;
import javax.swing.JComboBox;
import javax.swing.JPanel;

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
			}
		}); 
		add(c);
	}
	
	public void init(){
		
				
	}

}
