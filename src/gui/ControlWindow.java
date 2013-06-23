package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import main.Application;
import main.Main;
import dk.dtu.cdio.ANIMAL.computer.ControlCenter;

public class ControlWindow extends JPanel implements Runnable {
	
	public static int SELECTED_COLOR = 0;
	public PropertyManager prop;
	JFrame frame;
	JComboBox<String> comboBox;
	
		Slider[] slides = { new Slider("Lower Hue", Application.HUE, 0, 179),
			new Slider("Upper Hue", Application.UPPERHUE, 0, 179),
			new Slider("Saturation", Application.SATURATION, 0, 255),
			new Slider("Value", Application.VALUE, 0, 255)
			 };
	
	public ControlWindow() {
		prop = new PropertyManager();
		
	}
	

	@Override
	public void run() {
		
		
		frame = new JFrame("AutoNomous IMAgeprocessing Lego");
		comboBox = new JComboBox<String>();

		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//		ComboBox comboBox = new ComboBox();
		for(int i = 0; i <= 4; i++) {
			comboBox.addItem(Application.colorName(i));
		}
		comboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SELECTED_COLOR = ((JComboBox)e.getSource()).getSelectedIndex();
				
				for(int i = 0; i < 4; i++) {
					slides[i].setValue(Application.THRESHOLDS[SELECTED_COLOR][i]);
					
				}
			}
		});
		
		JButton saveButton = new JButton("SAVE");
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Saved");
				prop.save();
			}
		});
		
		JCheckBox runRobots = new JCheckBox("Run?");
		runRobots.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Main.READY = ((JCheckBox) e.getSource()).isSelected();
				System.out.println(Main.READY ? "Starting": "Stopping");
				ControlCenter.running = ((JCheckBox) e.getSource()).isSelected();
				
			}
		});
		
		JCheckBox drawAll = new JCheckBox("Draw route?");
		drawAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Main.DRAW_ALL = ((JCheckBox) e.getSource()).isSelected();
				
			}
		});

		frame.setLayout(null);
		slides[0].setBounds(1, 51, 400, 100);
		slides[1].setBounds(1, 151, 400, 100);
		slides[2].setBounds(1, 251, 400, 100);
		slides[3].setBounds(1, 351, 400, 100);
		comboBox.setBounds(1, 1, 100, 50);
		saveButton.setBounds(105, 5, 70, 20);
		runRobots.setBounds(180,5 ,80,20);
		drawAll.setBounds(270,5,100,20);
		for(int i = 0; i < 4; i++) {
			frame.add(slides[i]);
		}
		frame.add(slides[0]);
		frame.add(slides[1]);
		frame.add(slides[2]);
		frame.add(slides[3]);
		frame.add(comboBox);
		frame.add(saveButton);
		frame.add(runRobots);
		frame.add(drawAll);

		// Display the window.
		frame.pack();
		frame.setSize(410, 600);
		frame.setVisible(true);
		comboBox.setSelectedIndex(0);
	
		
	}
	

}
