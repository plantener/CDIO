package dk.dtu.cdio.ANIMAL.computer;

import java.util.ArrayList;
import java.util.Scanner;

import routeCalculation.Connect;

import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTInfo;
import main.Application;
import models.BreakPoint;
import models.Robot;

public class Navigator implements Runnable {
	
	public static float MM_PR_PIXEL = 3.0f;
	public static final	int ARC_RADIUS = 70;
	
	public static final int X_RESOLUTION = 800;
	public static final int Y_RESOLUTION = 600;
	
	public static int DIST_THRESHOLD = 14;
	public static int ANGLE_THRESHOLD = 2;
	
	private NXTInfo info_5a = new NXTInfo(NXTCommFactory.BLUETOOTH, "Gruppe5a", "00165308F127");
	private NXTInfo info_5b = new NXTInfo(NXTCommFactory.BLUETOOTH, "Gruppe5b", "0016530A6DEB");
	
	private PCCommunicator com;
	private CommandGenerator gen;
	private Application app;
	
	private Scanner scanner;
	private WaypointQueue waypoints;
	
	boolean a = true;
	
	private NXTInfo info = (a) ? info_5a : info_5b;
	private Robot robotRef;
	private Robot robot;
	
	public Navigator() {
		com = new PCCommunicator(info);
		gen = new CommandGenerator(com);
		waypoints = new WaypointQueue();
//		scanner = new Scanner(System.in);
//		scanner.nextLine();
//		com.connect();
//		gen.setRotateSpeed(150);
//		gen.setTravelSpeed(500);
		
//		calibrateLength();
		
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
		this.robotRef = (a) ? app.robotA : app.robotB;
	}
	
	public void feedBreakpoints(ArrayList<BreakPoint> points) {
		waypoints.convertAndAdd(points);
	}
	
	public void calibrateLength() {
		float travelDistance = 300;
		double avgDistance = 0.0d;
		//get start position
		Waypoint start, end;
		for (int i = 0; i < 6; i++) {
			start = new Waypoint(robot.getFrontMidX(), Y_RESOLUTION-robot.getFrontmidY());
			gen.doTravel((float) (travelDistance * Math.pow(-1, i)));
			end = new Waypoint(robot.getFrontMidX(), Y_RESOLUTION-robot.getFrontmidY());
			double distance = Utilities.getDistance(start, end);
			avgDistance = (distance + i * avgDistance) / (i + 1);
			System.out.format("[Calibration: %d: %f mm/pixel]%n", i, travelDistance/distance);
		}
		MM_PR_PIXEL = (float) (travelDistance / avgDistance);
		System.out.format("Length calibration: %f%n", MM_PR_PIXEL);
	}
	
//	public static void main(String[] args) {
//		System.out.println("Starting navigation unit...");
//		new Navigator();
//	}
	
	public boolean reachedDestination(Waypoint p) {
		return Utilities.getDistance(robot, p) <= DIST_THRESHOLD;
	}
	
	public void go() {
		boolean running = true;
		boolean adjustingAngle = true;
		
		while(running) {
			Waypoint next = waypoints.getHead();
			System.out.format("Next destination: %s%n", next);
			while(adjustingAngle) {
				robot = robotRef.clone();
				double robotAngle = Utilities.getRobotAngle(robot);
				System.out.format("[Robot: fX %d, fY %d, bX %d, bY %d]%n", robot.getFrontMidX(), Y_RESOLUTION-robot.getFrontmidY(), robot.getBackMidX(), Y_RESOLUTION-robot.getBackMidY());
				double rotation = Utilities.getRotation(robotAngle, Utilities.getAngle(robot, next));
				if(Math.abs(rotation) > ANGLE_THRESHOLD) {
					gen.doRotate((float) rotation);
				} else {
					adjustingAngle = false;
				}
				
			}
			
			robot = robotRef.clone();
			
			double distance = Utilities.getDistance(robot, next);
			gen.doTravel((float) (distance*MM_PR_PIXEL-ARC_RADIUS));
			double robotAngle = Utilities.getRobotAngle(robot);
			double newAngle = Utilities.getAngle(next, waypoints.afterHead());
			double angle = Math.abs(robotAngle - newAngle);
			
			gen.doArc(ARC_RADIUS, (float) angle);
			
			adjustingAngle = true;
			
			waypoints.shift();
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		com.connect();
		calibrateLength();
		go();
	}
	

}
