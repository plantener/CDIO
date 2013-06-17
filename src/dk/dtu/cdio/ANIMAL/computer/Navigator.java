package dk.dtu.cdio.ANIMAL.computer;

import java.util.ArrayList;

import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTInfo;
import main.Application;
import models.BreakPoint;
import models.Robot;

public class Navigator implements Runnable {

	public static float MM_PR_PIXEL = 3.0f;
	public int STOP_DIST = 100; // in mm
	public int ARC_RADIUS = 200; // in mm
	public static final int BITES = 350; // in mm
	
	public static final int X_RESOLUTION = 400;
	public static final int Y_RESOLUTION = 300;
	
	public static final int ROTATE_SPEED = 100;
	
	// number of calibrations before running
	public static final int CALIBRATIONS = 4;
	
	public String name;
	
	public static int DIST_THRESHOLD = 10; // pixels
	public static int ANGLE_THRESHOLD = 3; //degrees.
	
	private NXTInfo info_5a = new NXTInfo(NXTCommFactory.BLUETOOTH, "Gruppe5a", "00165308F127");
	private NXTInfo info_5b = new NXTInfo(NXTCommFactory.BLUETOOTH, "Gruppe5b", "0016530A6DEB");
	
	private PCCommunicator com;
	private CommandGenerator gen;
	private Application app;
	
	private WaypointQueue waypoints;
	
	boolean useRobotA;
	boolean paused = false;
	
	public NXTInfo info;
	public Robot robot;
	
	public Navigator(boolean useRobotA, Application app) {
		this.useRobotA = useRobotA;
		this.app = app;
		this.robot = (useRobotA) ? this.app.robotA : this.app.robotB;
		this.name = (useRobotA) ? "Robot A" : "Robot B";
		info = (useRobotA) ? info_5a : info_5b;
		com = new PCCommunicator(info);
		gen = new CommandGenerator(com);
		waypoints = new WaypointQueue();
		com.connect();
	}
	
	public void feedBreakpoints(ArrayList<BreakPoint> points) {
		waypoints.convertAndAdd(points);
	}
	
	public void calibrateLength() {
		float travelDistance = 300;
		double avgDistance = 0.0d;
		//get start position
		Waypoint start, end;
		for (int i = 0; i < CALIBRATIONS; i++) {
			start = new Waypoint(robot.getFrontMidX(), Y_RESOLUTION-robot.getFrontmidY());
			gen.doTravel((float) (travelDistance * Math.pow(-1, i)));
			end = new Waypoint(robot.getFrontMidX(), Y_RESOLUTION-robot.getFrontmidY());
			double distance = Utilities.getDistance(start, end);
			avgDistance = (distance + i * avgDistance) / (i + 1);
//			System.out.format("[Calibration: %d: %f mm/pixel]%n", i, travelDistance/distance);
		}
//		MM_PR_PIXEL = (float) (travelDistance / avgDistance);
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
		Waypoint next;

		outerloop:
		while(running) {
			while(paused) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			next = waypoints.getHead();
			System.out.format("%s : Next destination: %s%n", name, next);
			while(adjustingAngle) {
				double robotAngle = Utilities.getRobotAngle(robot);
//				System.out.format("[Robot: fX %d, fY %d, bX %d, bY %d]%n", robot.getFrontMidX(), Y_RESOLUTION-robot.getFrontmidY(), robot.getBackMidX(), Y_RESOLUTION-robot.getBackMidY());
				double rotation = Utilities.getRotation(robotAngle, Utilities.getAngle(robot, next));
				if(Math.abs(rotation) > ANGLE_THRESHOLD) {
					gen.doRotate((float) rotation);
				} else {
					adjustingAngle = false;
				}
				
			}
			
			float factor = 1.5f; // a scaling factor for arcradius
			float stopFactor = 1.9f; // a scaling factor for stop distance
			int extraStop = (int) ((factor * 180 + 60) / stopFactor);

			double distance;
			while((distance =  Utilities.getDistance(robot, next)) * MM_PR_PIXEL > extraStop+BITES) {
				gen.doTravel(BITES);
				adjustingAngle = true;
				continue outerloop;
			}
//			double breakpointDistance = Utilities.getDistance(next, waypoints.afterHead());

			double robotAngle = Utilities.getRobotAngle(robot);
			double newAngle = Utilities.getAngle(next, waypoints.afterHead());
			double angle = Math.abs(robotAngle - newAngle);
			if(angle > 180) {
				angle = 360 - angle;
			}
			ARC_RADIUS = (int) (factor * 180 + 60 - factor * angle); 
			STOP_DIST = (int) (ARC_RADIUS / stopFactor);
//			if(breakpointDistance * MM_PR_PIXEL < STOP_DIST) {
			
				if(distance * MM_PR_PIXEL > STOP_DIST) {
					gen.doTravel((float) (distance*MM_PR_PIXEL - STOP_DIST));
				} else {
					STOP_DIST = (int) (distance * MM_PR_PIXEL);
					ARC_RADIUS = (int) (STOP_DIST * stopFactor);
				}
	
				int arcDir; // 1 = left turn, -1 = right turn
				
				if(robotAngle < 0) {
					if(newAngle < robotAngle  || robotAngle+180 < newAngle) {
						arcDir = -1;
					} else {
						arcDir = 1;
					}
				} else {
					if(robotAngle < newAngle || newAngle < robotAngle - 180) {
						arcDir = 1;
					} else {
						arcDir = -1;
					}
				}
				
				System.out.format("# %s : Arc'ing: [%.2f -> %.2f : %.2f - STOP %d: , RADIUS: %d]%n", name, robotAngle, newAngle, angle, STOP_DIST, ARC_RADIUS);
				
				gen.doArc(arcDir*ARC_RADIUS, (float) (arcDir*angle));
				
//			}
			
			adjustingAngle = true;
			
			waypoints.shift();
			
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		gen.setRotateSpeed(ROTATE_SPEED);
		gen.setTravelSpeed(300);
		if(name.equals("Robot A")) {
			calibrateLength();
		}
		go();
	}
	

}
