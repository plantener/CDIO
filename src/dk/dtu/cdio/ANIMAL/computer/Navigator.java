package dk.dtu.cdio.ANIMAL.computer;

import java.util.ArrayList;
import java.util.Scanner;

import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTInfo;
import models.BreakPoint;
import routeCalculation.Track;

public class Navigator {
	
	public static final int MM_PR_PIXEL = 1;
	
	private NXTInfo info_5a = new NXTInfo(NXTCommFactory.BLUETOOTH, "Gruppe5a", "00165308F127");
	private NXTInfo info_5b = new NXTInfo(NXTCommFactory.BLUETOOTH, "Gruppe5b", "0016530A6DEB");
	
	private PCCommunicator com;
	private CommandGenerator gen;
	
	private Scanner scanner;
	
	private ArrayList<Point> points;
	
	private double angle = 90;
	
	public Navigator() {
		com = new PCCommunicator(info_5a);
		gen = new CommandGenerator(com);
		points = new ArrayList<Point>();
//		scanner = new Scanner(System.in);
//		generatePoints();
//		scanner.nextLine();
//		go();
	}
	
	private void generatePoints() {
//		for(int i = 0; i < 10; i++) {
//			points.add(new Point(i * (int) (Math.random()*20), i * (int) (Math.random()*20)));
//		}
		points.add(new Point(0,0));
		points.add(new Point(50,50));
		points.add(new Point(0,100));
		points.add(new Point(50,50));
		points.add(new Point(100,100));
		points.add(new Point(50,50));
		points.add(new Point(100,0));
		points.add(new Point(50,50));
		points.add(new Point(0,0));
	}
	
	public void getAndConvertBreakpoints() {
		points.clear();
		ArrayList<BreakPoint> breakPoints = Track.getCompleteList();
		for(BreakPoint bp : breakPoints) {
			points.add(new Point(bp.getX(), 300-bp.getY()));
		}
	}
	
	public void printPoints() {
		for(Point p : points) {
			System.out.println(p);
		}
	}
	
//	public static void main(String[] args) {
//		System.out.println("Starting navigation unit...");
//		new Navigator();
//	}
	
	public void go() {
		com.connect();
		printPoints();
		
		boolean running = true;
		int elements = points.size();
		for(int i = 0; i < elements-1; i++) {
			Point a, b;
			a = points.get(i);
			b = points.get(i+1);
			if(a.x == b.x && a.y == b.y) {
				continue;
			}
			System.out.format("From %s to %s%n", a, b);
			double newAngle = Utilities.getAngle(a, b);
			double rotation = Utilities.getRotation(angle, newAngle);
			double distance = Utilities.getDistance(a, b);
			gen.doRotate((float) rotation);
			System.out.format("Angle: %f => ", angle);
			angle = (angle + rotation) % 360;
			System.out.format("%f%n", angle);
			
			gen.doTravel((int) (distance * MM_PR_PIXEL));
		}
		while(true) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		// the main loop
		// analyze next waypoint(s)
		// send necessary commands
		// monitor robot proximity
		// stop and clear if in danger
			// recalculate route
	}
	

}
