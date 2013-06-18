package dk.dtu.cdio.ANIMAL.computer;

import java.util.ArrayList;

import com.googlecode.javacv.cpp.opencv_core;

import lejos.nxt.Motor;
import lejos.nxt.remote.NXTCommand;
import lejos.nxt.remote.RemoteMotor;
import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommBluecove;
import lejos.pc.comm.NXTCommBluez;
import lejos.pc.comm.NXTCommException;
import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTConnector;
import lejos.pc.comm.NXTInfo;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.RegulatedMotorListener;
import lejos.robotics.navigation.DifferentialPilot;
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
	
	public static final int MARCH_SPEED = 400;
	
	public static final int ROTATE_SPEED = 100;
	
	// number of calibrations before running
	public static final int CALIBRATIONS = 4;
	
	public String name;
	
	public static final int DIST_THRESHOLD = 20;
	public static final int ANGLE_THRESHOLD = 3; //degrees.
	
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
	
	public void go() {
		boolean running = true;
		boolean adjustingAngle = true;
		Waypoint next = null;
		Waypoint previous = null;
		gen.setTravelSpeed(250);
		gen.doSteer(0);

		double breakpointAngle;
		double robotAngle, angle, turnRate, oldRate, distance, newAngle;
		int steer;
		long start, end;
		start = end = 0;
		oldRate = turnRate = 0;

		while(running) {
			next = waypoints.getHead();
			System.out.format("%n%n%n%s : Next destination: %s%n%n%n", name, next);
			while((distance = Utilities.getDistance(robot, next)) > DIST_THRESHOLD) {
				while(paused) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				try {
					opencv_core.cvCircle(app.resizedFrame, new opencv_core.CvPoint(next.x, Navigator.Y_RESOLUTION-next.y), 10, useRobotA ? opencv_core.CvScalar.BLUE : opencv_core.CvScalar.RED, 3, 8, 0);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} 
				long diff = start-end;
				start = System.currentTimeMillis();
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				robotAngle = Utilities.getRobotAngle(robot);
				newAngle = Utilities.getAngle(robot, next);
				angle = Math.abs(robotAngle - newAngle);
				if(angle > 180) {
					angle = 360 - angle;
				}
		
				if(robotAngle < 0) {
					if(newAngle < robotAngle  || robotAngle+180 < newAngle) {
						steer = -1;
					} else {
						steer = 1;
					}
				} else {
					if(robotAngle < newAngle || newAngle < robotAngle - 180) {
						steer = 1;
					} else {
						steer = -1;		
					}
				}

				oldRate = turnRate;
//				turnRate = Math.pow(Math.sin(Math.PI * angle / 100.0),2)*100;
				if(angle < 90) {
					turnRate = Math.log(angle / 3.0) * 30;
					turnRate = (turnRate < 0) ? 0 : turnRate;
				} else {
					turnRate = (10.0/9)*angle;
				}
//				turnRate = Math.min(100, turnRate);
				turnRate *= steer;
				if(Math.abs(oldRate - turnRate) > 5) {
					System.out.format("%s - distance: %.3f, RA: %.3f, NA: %.3f, Angle : %.3f - turnRate: %.3f, Diff: %d%n", name, distance, robotAngle, newAngle, angle, turnRate, diff);
					gen.doSteer((float) turnRate);
//					pilot.steer(turnRate);
				}
				end = System.currentTimeMillis();
			}
			
			waypoints.shift();
			
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
//		gen.setRotateSpeed(ROTATE_SPEED);
//		gen.setTravelSpeed(300);
//		if(name.equals("Robot A")) {
//			calibrateLength();
//		}
		go();
	}
	

}
