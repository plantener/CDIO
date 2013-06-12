package main;

import java.util.ArrayList;
import java.util.Scanner;

import models.Box;
import models.ObjectOnMap;
import routeCalculation.Track;
import sun.awt.windows.ThemeReader;
import dk.dtu.cdio.ANIMAL.computer.ControlCenter;
import dk.dtu.cdio.ANIMAL.computer.Navigator;

public class Main {

	private static ObjectOnMap[] objectList;
	private static ArrayList<ObjectOnMap> ports;
	private static ArrayList<Box> redBoxes;
	private static ArrayList<Box> greenBoxes;

	public static void main(String[] args) {
		Application app = new Application();
//		Navigator nav = new Navigator(app);
		ControlCenter control = new ControlCenter(app);

		Scanner sc = new Scanner(System.in);
		int i = 0;
		long startTime = System.nanoTime();
		app.frameProcessing();
		ports = app.sortedPorts;
		redBoxes = app.redBoxes;
		greenBoxes = app.greenBoxes;
//		objectList = app.objectList;
		Track t = new Track(ports, redBoxes, greenBoxes);
		int frames = 1;
		for(i=0; i < 200; i++) {			
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
		Long endTime = System.nanoTime();
		double fps = (double)frames/((endTime-startTime)/1000000000);
		System.out.println("FPS: " + fps);
		System.out.println("############################");
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		sc.nextLine();		
//		nav.feedBreakpoints(Track.getCompleteList());
//		new Thread(nav).start();
		new Thread(control).start();
		while(true) {
			app.frameProcessing();
			ports = app.sortedPorts;
			redBoxes = app.redBoxes;
			greenBoxes = app.greenBoxes;
//			i++;
//			System.out.println("BILLEDE NUMMER: " + i);
//			t.updateObjects(ports, redBoxes, greenBoxes);
		}
//		System.out.println("FPS: " + fps);
	}
	
}

