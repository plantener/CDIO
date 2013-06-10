package main;

import java.util.ArrayList;

import models.Box;
import models.ObjectOnMap;
import models.Port;
import routeCalculation.Track;

public class Main {

	private static ObjectOnMap[] objectList;
	private static ArrayList<ObjectOnMap> ports;
	private static ArrayList<Box> redBoxes;
	private static ArrayList<Box> greenBoxes;

	public static void main(String[] args) {
		Application app = new Application();
		int i = 0;
		long startTime = System.nanoTime();
		app.frameProcessing();
		ports = app.sortedPorts;
		redBoxes = app.redBoxes;
		greenBoxes = app.greenBoxes;
//		objectList = app.objectList;
		Track t = new Track(ports, redBoxes, greenBoxes);
		int frames = 1000;
		while(i < frames){			
			app.frameProcessing();
			ports = app.sortedPorts;
			redBoxes = app.redBoxes;
			greenBoxes = app.greenBoxes;
			i++;
			System.out.println("BILLEDE NUMMER: " + i);
			t.updateObjects(ports, redBoxes, greenBoxes);
		}
		Long endTime = System.nanoTime();
		double fps = (double)frames/((endTime-startTime)/1000000000);
		System.out.println("FPS: " + fps);
		System.out.println("FPS: " + fps);
	}
	
}

