package main;

import java.util.ArrayList;

import models.Box;
import models.ObjectOnMap;
import models.Port;
import routeCalculation.Track;

public class Main {
	
	//TODO OPRYDNING AF KODE! MANGE UNUSED IMPORTS!

	private static ObjectOnMap[] objectList;
	private static ArrayList<Box> redBoxes;
	private static ArrayList<Box> greenBoxes;
	private static ArrayList<Port> sortedPorts;

	public static void main(String[] args) {
		Application app = new Application();
		int i = 0;
		long startTime = System.nanoTime();
		app.frameProcessing();
		redBoxes = app.redBoxes;
		greenBoxes = app.greenBoxes;
		sortedPorts = app.sortedPorts;
//		objectList = app.objectList;
//		Track t = new Track(objectList);
		int frames = 1;
		while(i < frames){			
			app.frameProcessing();
			System.out.println(sortedPorts.toString());
//			objectList = app.objectList;	
			i++;
			System.out.println("BILLEDE NUMMER: " + i);
//			t.updateObjects(objectList);
		}
		Long endTime = System.nanoTime();
		double fps = (double)frames/((endTime-startTime)/1000000000);
		System.out.println("FPS: " + fps);
		System.out.println("FPS: " + fps);
	}
	
}

