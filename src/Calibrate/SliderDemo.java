/*
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package Calibrate;

import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeListener;
import java.util.Properties;

import javax.swing.*;
import javax.swing.event.*;
import javax.xml.bind.Validator;

import main.Application;
import main.Main;

import org.omg.CORBA.portable.ApplicationException;

import sun.awt.geom.AreaOp.AddOp;

/*
 * SliderDemo.java requires all the files in the images/doggy
 * directory.
 */
public class SliderDemo extends JPanel implements ActionListener,
		ChangeListener {
	// Set up animation parameters.
	private final int H_MIN = 0;
	private final int H_MAX = 180;
	public static int h_init;
	public static JSlider hue;
	public static JTextField hue_value = new JTextField(15);
	public static JTextField sat_value = new JTextField(15);
	public static JTextField val_value = new JTextField(15);
	public static JTextField upper_h_value = new JTextField(15);
	
	private static PropertyManager prop;

	public SliderDemo() {
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

		// Create the label.
		JLabel hueLabel = null;
		hueLabel = new JLabel("Hue", JLabel.CENTER);
		hueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		// Create the slider.
		switch (ComboBox.colorIndex) {
		case 0:
			h_init = Application.red_h;
			break;
		case 1:
			h_init = Application.green_h;
			break;
		case 2:
			h_init = Application.lightBlue_h;
			break;
		case 3:
			h_init = Application.blue_h;
			break;
		case 4:
			h_init = Application.purple_h;
			break;
		default:
			h_init = Application.red_h;
			break;
		}
		hue = new JSlider(JSlider.HORIZONTAL, H_MIN, H_MAX, h_init);

		hue.addChangeListener(this);
		hue_value.setText("Hue: " + hue.getValue());


		// Turn on labels at major tick marks.

		hue.setMajorTickSpacing(40);
		hue.setMinorTickSpacing(1);
		hue.setPaintTicks(true);
		hue.setPaintLabels(true);
		hue.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
		Font font = new Font("Serif", Font.ITALIC, 15);
		hue.setFont(font);

		// Put everything together.
		add(hue_value);
		add(upper_h_value);
		add(sat_value);
		add(val_value);
		add(hueLabel);
		add(hue);

		setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	}

	/** Listen to the slider. */
	public void stateChanged(ChangeEvent e) {
		JSlider source = (JSlider) e.getSource();

		if (source.getValueIsAdjusting()) {

			hue_value.setText("Hue: " + source.getValue());
			switch (ComboBox.colorIndex) {
			case 0:
				main.Application.red_h = (int) source.getValue();
				break;
			case 1:
				main.Application.green_h = (int) source.getValue();
				break;
			case 2:
				main.Application.lightBlue_h = (int) source.getValue();
				break;
			case 3:
				main.Application.blue_h = (int) source.getValue();
				break;
			case 4:
				main.Application.purple_h = (int) source.getValue();
				break;
			default:
				main.Application.purple_h = (int) source.getValue();
				break;
			}
		}
	}

	/**
	 * Create the GUI and show it. For thread safety, this method should be
	 * invoked from the event-dispatching thread.
	 */
	public static void createAndShowGUI() {
		// Create and set up the window.
		JFrame frame = new JFrame("�ber kuul program");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		SliderDemo animator = new SliderDemo();
		SatSlider satDemo = new SatSlider();
		ValSlider valSlider = new ValSlider();
		ComboBox comboBox = new ComboBox();
		UpperHSlider upperh = new UpperHSlider();
		
		JButton saveButton = new JButton("SAVE");
		saveButton.addActionListener(animator);
		saveButton.setActionCommand("saveValues");
		
		JButton readyButton = new JButton("READY");
		readyButton.addActionListener(animator);
		readyButton.setActionCommand("ready");
		// Add content to the window.

		frame.setLayout(null);
		animator.setBounds(1, 51, 400, 200);
		upperh.setBounds(1, 251, 400, 100);
		satDemo.setBounds(1, 351, 400, 100);
		valSlider.setBounds(1, 451, 400, 100);
		comboBox.setBounds(1, 1, 100, 50);
		saveButton.setBounds(115, 5, 100, 20);
		readyButton.setBounds(225,5 ,100,20);
		frame.add(animator);
		frame.add(upperh);
		frame.add(satDemo);
		frame.add(valSlider);
		frame.add(comboBox);
		frame.add(saveButton);
		frame.add(readyButton);

		// Display the window.
		frame.pack();
		frame.setSize(410, 600);
		frame.setVisible(true);
	}
	
	public static void loadValues() {
		try {
			Application.blue_h = Integer.parseInt(prop.getProperty("blue_hue"));
			Application.blue_upper_h = Integer.parseInt(prop.getProperty("blue_upper_hue"));
			Application.blue_s = Integer.parseInt(prop.getProperty("blue_saturation"));
			Application.blue_v = Integer.parseInt(prop.getProperty("blue_value"));
			
			Application.green_h = Integer.parseInt(prop.getProperty("green_hue"));
			Application.green_upper_h = Integer.parseInt(prop.getProperty("green_upper_hue"));
			Application.green_s = Integer.parseInt(prop.getProperty("green_saturation"));
			Application.green_v = Integer.parseInt(prop.getProperty("green_value"));
	
			Application.red_h = Integer.parseInt(prop.getProperty("red_hue"));
			Application.red_upper_h = Integer.parseInt(prop.getProperty("red_upper_hue"));
			Application.red_s = Integer.parseInt(prop.getProperty("red_saturation"));
			Application.red_v = Integer.parseInt(prop.getProperty("red_value"));
	
			Application.purple_h = Integer.parseInt(prop.getProperty("purple_hue"));
			Application.purple_upper_h = Integer.parseInt(prop.getProperty("purple_upper_hue"));
			Application.purple_s = Integer.parseInt(prop.getProperty("purple_saturation"));
			Application.purple_v = Integer.parseInt(prop.getProperty("purple_value"));
	
			Application.lightBlue_h = Integer.parseInt(prop.getProperty("lightBlue_hue"));
			Application.lightBlue_upper_h = Integer.parseInt(prop.getProperty("lightBlue_upper_hue"));
			Application.lightBlue_s = Integer.parseInt(prop.getProperty("lightBlue_saturation"));
			Application.lightBlue_v = Integer.parseInt(prop.getProperty("lightBlue_value"));
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
	}
	
	public static void saveValues() {
		
		prop.setProperty("blue_hue", Application.blue_h);
		prop.setProperty("blue_upper_hue", Application.blue_upper_h);
		prop.setProperty("blue_saturation", Application.blue_s);
		prop.setProperty("blue_value", Application.blue_v);
		
		prop.setProperty("green_hue", Application.green_h);
		prop.setProperty("green_upper_hue", Application.green_upper_h);
		prop.setProperty("green_saturation", Application.green_s);
		prop.setProperty("green_value", Application.green_v);
		
		prop.setProperty("red_hue", Application.red_h);
		prop.setProperty("red_upper_hue", Application.red_upper_h);
		prop.setProperty("red_saturation", Application.red_s);
		prop.setProperty("red_value", Application.red_v);
		
		prop.setProperty("purple_hue", Application.purple_h);
		prop.setProperty("purple_upper_hue", Application.purple_upper_h);
		prop.setProperty("purple_saturation", Application.purple_s);
		prop.setProperty("purple_value", Application.purple_v);
		
		prop.setProperty("lightBlue_hue", Application.lightBlue_h);
		prop.setProperty("lightBlue_upper_hue", Application.lightBlue_upper_h);
		prop.setProperty("lightBlue_saturation", Application.lightBlue_s);
		prop.setProperty("lightBlue_value", Application.lightBlue_v);
		
		prop.save();
	}
	
	public static void main(String[] args) {
		/* Turn off metal's use of bold fonts */
		UIManager.put("swing.boldMetal", Boolean.FALSE);
		prop = new PropertyManager();
		loadValues();
		

		// Schedule a job for the event-dispatching thread:
		// creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getActionCommand().equals("saveValues")) {
			saveValues();
			System.out.println("save pressed");
		} else if (arg0.getActionCommand().equals("ready")) {
			Main.READY = true;
			System.out.println("ready pressed");
			JButton src = (JButton) arg0.getSource();
			src.setEnabled(false);
		}

	}
}
