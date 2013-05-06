package main;

import Objects.Cord;
import models.ObjectOnMap;
import routeCalculation.Track;

public class Main {
	
	//TODO OPRYDNING AF KODE! MANGE UNUSED IMPORTS!

	private static ObjectOnMap[] objectList;
	private static Cord[] liste = new Cord[6];

	public static void main(String[] args) {
		Application app = new Application();
		int i = 0;
		long startTime = System.nanoTime();
		while(i < 1){			
			app.frameProcessing();
			objectList = app.objectList;
			for (int j = 14; j < objectList.length; j++) {
				liste[j-14] = new Cord(objectList[j].getMidX(),objectList[j].getMidY());
			}
			Track t = new Track(liste);
			new Thread(t).start();
			i++;
		}
		Long endTime = System.nanoTime();
		double fps = (endTime-startTime)/100000000;
		System.out.println("FPS: " + fps);
	}
	
}

