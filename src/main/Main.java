package main;

import gui.ControlWindow;

import java.util.ArrayList;

import javax.swing.UIManager;

import models.Box;
import models.Port;
import routeCalculation.Track;
import dk.dtu.cdio.ANIMAL.computer.ControlCenter;

public class Main {
	public static int DEBUG_ROBOT = 0;
	
	private static ArrayList<Port> ports;
	public static ArrayList<Box> redBoxes;
	public static ArrayList<Box> greenBoxes;
	public static boolean READY = false, DRAW_ALL = false;

	public static void main(String[] args) {
		UIManager.put("swing.boldMetal", Boolean.FALSE);
		
		boolean runRobots = !(args.length > 0);
		
		Application app = new Application();
		ControlWindow gui = new ControlWindow();
		// Schedule a job for the event-dispatching thread:
		// creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(gui);
		
		ControlCenter control = null;
		if(runRobots) {
			 control = new ControlCenter(app);
		}
		
		app.frameProcessing();
		ports = app.sortedPorts;
		redBoxes = app.redBoxes;
		greenBoxes = app.greenBoxes;
		
		Track t = new Track(ports, redBoxes, greenBoxes);
		if(runRobots) {
			new Thread(control).start();
		}
		while (true){
			ports = app.sortedPorts;
			redBoxes = app.redBoxes;
			greenBoxes = app.greenBoxes;
			
			app.frameProcessing();
			
			t.updateObjects(ports, redBoxes, greenBoxes);
		}
	}
	
}

