package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

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

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
		
		JCheckBox runRobots = new JCheckBox("Run robots?");
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
		comboBox.setBounds(5, 5, 95, 25);
		saveButton.setBounds(320, 400, 70, 20);
		runRobots.setBounds(280,430 ,100,20);
		drawAll.setBounds(280,460,100,20);
		for(int i = 0; i < 4; i++) {
			slides[i].setBounds(1, 50+i*75, 400, 75);
			frame.add(slides[i]);
		}
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
