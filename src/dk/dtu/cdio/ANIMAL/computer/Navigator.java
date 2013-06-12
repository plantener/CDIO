package dk.dtu.cdio.ANIMAL.computer;

import java.util.ArrayList;
import java.util.Scanner;

import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTInfo;
import main.Application;
import models.BreakPoint;
import models.Robot;

public class Navigator {
	
	public static float MM_PR_PIXEL = 3.0f;
	
	private NXTInfo info_5a = new NXTInfo(NXTCommFactory.BLUETOOTH, "Gruppe5a", "00165308F127");
	private NXTInfo info_5b = new NXTInfo(NXTCommFactory.BLUETOOTH, "Gruppe5b", "0016530A6DEB");
	
	private PCCommunicator com;
	private CommandGenerator gen;
	private Application app;
	
	private Scanner scanner;
	private WaypointQueue waypoints;
	
	boolean a = true;
	
	private NXTInfo info = (a) ? info_5a : info_5b;
	private Robot robot;
	
//	private double angle = 90;
	
	public Navigator() {
		com = new PCCommunicator(info);
		gen = new CommandGenerator(com);
		waypoints = new WaypointQueue();
		scanner = new Scanner(System.in);
		scanner.nextLine();
		com.connect();
		gen.setRotateSpeed(200);
		
//		calibrateLength();
		int i;
		while((i = scanner.nextInt()) != 0) {
			gen.doRotate(i);
		}
		
//		scanner.nextLine();
		
//		gen.doRotate(150);
//		gen.doTravel(250);
//		gen.doRotate(180);
//		gen.doTravel(250);
//		gen.setAcceleration(500);
//		waypoints.generatePoints();
		
//		while(true) {
//			scanner.nextLine();
//			com.testLatency();
//		}
	}
	
	public Navigator(Application app) {
		this();
		this.app = app;
		this.robot = (a) ? app.robotA : app.robotB;
	}
	
	public void feedBreakpoints(ArrayList<BreakPoint> points) {
		waypoints.convertAndAdd(points);
	}
	
	public void calibrateLength() {
		float travelDistance = 300;
		//get start position
		Waypoint start = new Waypoint(robot.getFrontMidX(), robot.getFrontmidY());
		gen.doTravel(travelDistance);
		//get end position
		Waypoint end = new Waypoint(robot.getFrontMidX(), robot.getFrontmidY());
		double distance = Utilities.getDistance(start, end);
		
		MM_PR_PIXEL = (float) (travelDistance / distance);
	}
	
	public static void main(String[] args) {
		System.out.println("Starting navigation unit...");
		new Navigator();
	}
	
	public boolean reachedDestination(Waypoint p) {
		return Utilities.getDistance(robot, p) <= 1;
	}
	
	public void go() {
		boolean running = true;
		
		while(running) {
			Waypoint next = waypoints.getHead();
			
			while(!reachedDestination(next)) {
				double robotAngle = Utilities.getRobotAngle(robot);
				double rotation = Utilities.getRotation(robotAngle, Utilities.getAngle(robot, next));
				double distance;
				if(Math.abs(rotation) > 1) {
					gen.doRotate((float) rotation);
//					while(!com.reader.replyReady.get() && com.reader.reply == NavCommand.ROTATE);
//					com.reader.replyReady.set(false);
				} else {
					distance = Utilities.getDistance(robot, next);
					gen.doTravel((float) distance * MM_PR_PIXEL);
//					while(!com.reader.replyReady.get() && com.reader.reply == NavCommand.TRAVEL);
//					com.reader.replyReady.set(false);
				}
			}
			
			waypoints.shift();
			
//			int elements = points.size();
//			for(int i = 0; i < elements-1; i++) {
//				Waypoint a, b;
//				a = points.get(i);
//				b = points.get(i+1);
//				if(a.x == b.x && a.y == b.y) {
//					continue;
//				}
//				System.out.format("From %s to %s%n", a, b);
//				double newAngle = Utilities.getAngle(a, b);
//				double rotation = Utilities.getRotation(angle, newAngle);
//				double distance = Utilities.getDistance(a, b);
//				gen.doRotate((float) rotation);
//				System.out.format("Angle: %f => ", angle);
//				angle = (angle + rotation) % 360;
//				System.out.format("%f%n", angle);
//				
//				gen.doTravel((int) (distance * MM_PR_PIXEL));
//			}
		}
//		while(true) {
//			try {
//				Thread.sleep(500);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
//		}
	}
	

}
