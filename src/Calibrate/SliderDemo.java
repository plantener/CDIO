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

import javax.swing.*;
import javax.swing.event.*;
import javax.xml.bind.Validator;

import main.Application;

import org.omg.CORBA.portable.ApplicationException;

/*
 * SliderDemo.java requires all the files in the images/doggy
 * directory.
 */
public class SliderDemo extends JPanel implements ActionListener,
		ChangeListener {
	// Set up animation parameters.
	private final int H_MIN = 0;
	private final int H_MAX = 180;
	private int h_init;

	// This label uses ImageIcon to show the doggy pictures.
	JLabel picture;

	public SliderDemo() {
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

		// Create the label.
		JLabel hueLabel = null;

		switch (ComboBox.colorIndex) {
		case 0:
			h_init = Application.red_h;
			hueLabel = new JLabel("Hue red: " + Application.red_h, JLabel.CENTER);
			hueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
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
		// Create the slider.
		JSlider hue = new JSlider(JSlider.HORIZONTAL, H_MIN, H_MAX, h_init);
		

		hue.addChangeListener(this);

		// Turn on labels at major tick marks.

		hue.setMajorTickSpacing(40);
		hue.setMinorTickSpacing(1);
		hue.setPaintTicks(true);
		hue.setPaintLabels(true);
		hue.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
		Font font = new Font("Serif", Font.ITALIC, 15);
		hue.setFont(font);
		

		// Create the label that displays the animation.
		picture = new JLabel();
		picture.setHorizontalAlignment(JLabel.CENTER);
		picture.setAlignmentX(Component.CENTER_ALIGNMENT);
		picture.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLoweredBevelBorder(),
				BorderFactory.createEmptyBorder(10, 10, 10, 10)));
		
		// Put everything together.
		add(hueLabel);
		add(hue);
		setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	}

	/** Listen to the slider. */
	public void stateChanged(ChangeEvent e) {
		JSlider source = (JSlider) e.getSource();
		if (source.getValueIsAdjusting()) {
			switch (ComboBox.colorIndex) {
			case 0:
				main.Application.red_h = (int)source.getValue();
				break;
			case 1:
				main.Application.green_h = (int)source.getValue();
				break;
			case 2:
				main.Application.lightBlue_h = (int)source.getValue();
				break;
			case 3:
				main.Application.blue_h = (int)source.getValue();
				break;
			case 4:
				main.Application.purple_h = (int)source.getValue();
				break;
			default:
				main.Application.purple_h = (int)source.getValue();
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
		JFrame frame = new JFrame("Über kuul program");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		SliderDemo animator = new SliderDemo();
		SatSlider satDemo = new SatSlider();
		ValSlider valSlider = new ValSlider();
		ComboBox comboBox = new ComboBox();
		UpperHSlider upperh = new UpperHSlider();
		

		// Add content to the window.

		frame.setLayout(null);
		upperh.setBounds(1, 51, 400, 100);
		animator.setBounds(1, 151, 400, 100);
		satDemo.setBounds(1, 251, 400, 100);
		valSlider.setBounds(1,351, 400, 100);
		comboBox.setBounds(1, 1, 100, 50);
		frame.add(upperh);
		frame.add(animator);
		frame.add(satDemo);
		frame.add(valSlider);
		frame.add(comboBox);
		

		// Display the window.
		frame.pack();
		frame.setSize(410, 500);
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		/* Turn off metal's use of bold fonts */
		UIManager.put("swing.boldMetal", Boolean.FALSE);

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
		// TODO Auto-generated method stub

	}
}
