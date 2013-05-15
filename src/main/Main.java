package main;

import models.ObjectOnMap;
import routeCalculation.Track;

public class Main {
	
	//TODO OPRYDNING AF KODE! MANGE UNUSED IMPORTS!

	private static ObjectOnMap[] objectList;

	public static void main(String[] args) {
		Application app = new Application();
		int i = 0;
		long startTime = System.nanoTime();
		app.frameProcessing();
		objectList = app.objectList;
		Track t = new Track(objectList);
		int frames = 1;
		while(i < frames){			
			app.frameProcessing();
			objectList = app.objectList;	
			i++;
			System.out.println("BILLEDE NUMMER: " + i);
			t.updateObjects(objectList);
		}
		Long endTime = System.nanoTime();
		double fps = (double)frames/((endTime-startTime)/1000000000);
		System.out.println("FPS: " + fps);
		System.out.println("FPS: " + fps);
	}
	
}

