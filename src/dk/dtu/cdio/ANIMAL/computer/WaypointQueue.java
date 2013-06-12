package dk.dtu.cdio.ANIMAL.computer;

import java.util.ArrayList;
import java.util.LinkedList;

import models.BreakPoint;
import models.Robot;

public class WaypointQueue {
	
	private LinkedList<Waypoint> list;
	
	public WaypointQueue() {
		this.list = new LinkedList<Waypoint>();
	}
	
	public Waypoint getHead() {
		return list.peek();
	}
	
	public Waypoint afterHead() {
		return list.get(1);
	}
	
	public void shift() {
		Waypoint p = list.poll();
		list.add(p);
	}
	
	public void convertAndAdd(ArrayList<BreakPoint> points) {
		list.clear();
		for(BreakPoint p : points) {
			list.add(new Waypoint(p.getX(), Navigator.Y_RESOLUTION-p.getY()));
		}
	}
	
	public void printPoints() {
		for(Waypoint p : list) {
			System.out.println(p);
		}
	}
	
	public void shiftToNextBreakpoint(Robot r) {
//		Waypoint middle = new Waypoint(, y)
		double robotAngle = Utilities.getAngle(Navigator.X_RESOLUTION/2, Navigator.Y_RESOLUTION/2, r.getFrontMidX(), r.getFrontmidY());
		if (robotAngle > 0) {
			
		}
		
	}
	
	public void generatePoints() {
		list.clear();
		list.add(new Waypoint(0,0));
		list.add(new Waypoint(50,50));
		list.add(new Waypoint(0,100));
		list.add(new Waypoint(50,50));
		list.add(new Waypoint(100,100));
		list.add(new Waypoint(50,50));
		list.add(new Waypoint(100,0));
		list.add(new Waypoint(50,50));
		list.add(new Waypoint(0,0));
	}

}
