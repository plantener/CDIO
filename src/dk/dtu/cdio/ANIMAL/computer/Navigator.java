package dk.dtu.cdio.ANIMAL.computer;

import java.util.ArrayList;

import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTInfo;
import main.Application;
import models.BreakPoint;
import models.Robot;

import com.googlecode.javacv.cpp.opencv_core;

public class Navigator implements Runnable {


	public static float MM_PR_PIXEL = 3.0f;
	
	public static final int X_RESOLUTION = 400;
	public static final int Y_RESOLUTION = 300;
	
	public static final int TRAVEL_SPEED = 250;
	
	public static final int DIST_THRESHOLD = 20;
	public static final int ANGLE_THRESHOLD = 3; //degrees.
	public static final int TURNRATE_THRESHOLD = 5;
	
	private static NXTInfo INFO_5A = new NXTInfo(NXTCommFactory.BLUETOOTH, "Gruppe5a", "00165308F127");
	private static NXTInfo INFO_5B = new NXTInfo(NXTCommFactory.BLUETOOTH, "Gruppe5b", "0016530A6DEB");
	
	private PCCommunicator com;
	private CommandGenerator gen;
	private Application app;
	private WaypointQueue waypoints;
	public NXTInfo info;
	public Robot robot;
	
	boolean useRobotA, paused = false;
	public String name;
	
	public Navigator(boolean useRobotA, Application app) {
		this.useRobotA = useRobotA;
		this.app = app;
		this.robot = (useRobotA) ? this.app.robotA : this.app.robotB;
		this.name = (useRobotA) ? "Robot A" : "Robot B";
		info = (useRobotA) ? INFO_5A : INFO_5B;

		com = new PCCommunicator(info);
		gen = new CommandGenerator(com);
		waypoints = new WaypointQueue();
		com.connect();
	}
	
	public void feedBreakpoints(ArrayList<BreakPoint> points) {
		waypoints.convertAndAdd(points);
		waypoints.shiftToNextBreakpoint(robot);
	}
	
	public void go() {
		boolean running = true;
		Waypoint next = null;
		gen.setTravelSpeed((useRobotA) ? 580 : 447);
		gen.doSteer(0);

		double robotAngle, angle, turnRate, oldRate, distance, newAngle;
		int steer, oldTravelSpeed = 0, travelSpeed = 0;
		long start, end, diff;
		boolean robotHasBeenStopped = false;
		start = end = 0;
		oldRate = turnRate = 0;

		while(running) {
			next = waypoints.getHead();
			System.out.format("%n%s : Next destination: %s%n", name, next);
			while((distance = Utilities.getDistance(robot, next)) > DIST_THRESHOLD) {
				// experimental - may not work as intended.
				while(paused || !Application.robotsDetected) {
					gen.sendStop();
					robotHasBeenStopped = true;
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
//				if((travelSpeed = ((int) (distance / 10)) * 10 + 200) != oldTravelSpeed ) {
//					gen.setTravelSpeed(travelSpeed);
//					oldTravelSpeed = travelSpeed;
//				}
				
				diff = start-end;
				start = System.currentTimeMillis();
//				try {
////					opencv_core.cvCircle(app.resizedFrame, new opencv_core.CvPoint(next.x, Navigator.Y_RESOLUTION-next.y), 10, useRobotA ? opencv_core.CvScalar.BLACK : opencv_core.CvScalar.RED, 3, 8, 0);
////					Thread.sleep(10);
//				} catch (Exception e) {
//					// interrupts should occur, so we just catch all
//					e.printStackTrace();
//				}
				
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
//				if(angle < 90) {
//					turnRate = Math.log(angle / 3.0) * 30;
//					turnRate = (turnRate < 0) ? 0 : turnRate;
//				} else {
					turnRate = (10.0/9)*angle;
//				}
					if(turnRate > 100) {
						turnRate = 100;
					}
				turnRate *= steer;
				if(Math.abs(oldRate - turnRate) > TURNRATE_THRESHOLD || robotHasBeenStopped) {
//					System.out.format("%s - distance: %.3f, RA: %.3f, NA: %.3f, Angle : %.3f - turnRate: %.3f, Diff: %d, speed: %d%n", name, distance, robotAngle, newAngle, angle, turnRate, diff, oldTravelSpeed);
					gen.doSteer((float) turnRate);
					robotHasBeenStopped = false;
					
				}
				end = System.currentTimeMillis();
			}
			
			waypoints.shift();
			
		}
	}

	@Override
	public void run() {
		go();
	}
	

}
