package main;

import gui.ControlWindow;

import java.util.ArrayList;

import javax.swing.UIManager;

import models.Box;
import models.Port;
import routeCalculation.Track;
import dk.dtu.cdio.ANIMAL.computer.ControlCenter;
//import sun.awt.windows.ThemeReader;

public class Main {
	public static int DEBUG_ROBOT = 0;
	
	private static ArrayList<Port> ports;
	public static ArrayList<Box> redBoxes;
	public static ArrayList<Box> greenBoxes;
	public static boolean READY = false, DRAW_ALL = false;

	public static void main(String[] args) {
		UIManager.put("swing.boldMetal", Boolean.FALSE);
		boolean runRobots = true;
		if(args.length > 0) {
			runRobots = false;
		}
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
//		while(true) {
//			app.frameProcessing();
//			ports = app.sortedPorts;
//			redBoxes = app.redBoxes;
//			greenBoxes = app.greenBoxes;
////			System.out.println("h:" + Application.red_h + "\n s:" + Application.red_s + "\n v:" + Application.red_v + "\n upper h" + Application.red_upper_h);
////			i++;
////			System.out.println("BILLEDE NUMMER: " + i);
////			if(!runRobots) {
//				t.updateObjects(ports, redBoxes, greenBoxes);
////			}
//		}
//		System.out.println("FPS: " + fps);
	}
	
}

