package main;

import java.util.ArrayList;
import java.util.Scanner;

import models.Box;
import models.ObjectOnMap;
import models.Port;
import routeCalculation.Track;
//import sun.awt.windows.ThemeReader;
import dk.dtu.cdio.ANIMAL.computer.ControlCenter;
import dk.dtu.cdio.ANIMAL.computer.Navigator;
import dk.dtu.cdio.ANIMAL.computer.Utilities;

public class Main {
	public static int DEBUG_ROBOT = 0;
	
	private static ArrayList<Port> ports;
	private static ArrayList<Box> redBoxes;
	private static ArrayList<Box> greenBoxes;

	public static void main(String[] args) {
		boolean runRobots = true;
		if(args.length > 0) {
			runRobots = false;
		}
		Application app = new Application();
		ControlCenter control = null;
		if(runRobots) {
			 control = new ControlCenter(app);
		}

		Scanner sc = new Scanner(System.in);
		int i = 0;
		long startTime = System.currentTimeMillis();
		app.frameProcessing();
		ports = app.sortedPorts;
		redBoxes = app.redBoxes;
		greenBoxes = app.greenBoxes;
//		objectList = app.objectList;//		if(name.equals("Robot A")) {
//		calibrateLength();
//	}
		Track t = new Track(ports, redBoxes, greenBoxes);
		int frames = 1;
		for(i=0; i < 25; i++) {			
			app.frameProcessing();
			ports = app.sortedPorts;
			redBoxes = app.redBoxes;
			greenBoxes = app.greenBoxes;
			frames++;
//			i++;
//			System.out.println("BILLEDE NUMMER: " + i);
			t.updateObjects(ports, redBoxes, greenBoxes);
		}
		System.out.println("############################");
		long endTime = System.currentTimeMillis();
		double fps = (double) frames /((endTime-startTime)/1000);
		System.out.println("FPS: " + fps);
		System.out.println("############################");
		if(runRobots) {
			new Thread(control).start();
		}
		while(true) {
			app.frameProcessing();
			ports = app.sortedPorts;
			redBoxes = app.redBoxes;
			greenBoxes = app.greenBoxes;
//			i++;
//			System.out.println("BILLEDE NUMMER: " + i);
			if(!runRobots) {
				t.updateObjects(ports, redBoxes, greenBoxes);
			}
		}
//		System.out.println("FPS: " + fps);
	}
	
}

