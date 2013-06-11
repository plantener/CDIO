package dk.dtu.cdio.ANIMAL.computer;

import models.Robot;

public class Utilities {
	
	public static double getAngle(Waypoint a, Waypoint b) {
//		double coeff;
//		try {
//			coeff = (b.y - a.y)/(b.x - a.x);
//		} catch(ArithmeticException e) {
//			coeff = 0;
//			System.out.println("Probably divided by zero");
//		}
//		double result =Math.toDegrees(Math.atan(coeff));
		double radians = Math.atan2(b.y-a.y, b.x-a.x);
		double degrees = Math.toDegrees(radians);
//		System.out.format("[%s ; %s] => %f radians : %f degrees%n", a, b, radians, degrees);
		return degrees;
	}
	
	public static double getAngle(int x1, int y1, int x2, int y2) {
		double radians = Math.atan2(y2-y1, x2-x1);
		double degrees = Math.toDegrees(radians);
		return degrees;
	}
	
	public static double getAngle(Robot r, Waypoint p) {
		return getAngle(r.getFrontMidX(), r.getFrontmidY(), p.x, p.y);
	}
	
	public static double getRobotAngle(Robot robot) {
		return getAngle(robot.getBackMidX(), robot.getBackMidY(), robot.getFrontMidX(), robot.getFrontmidY());
	}
	
	public static double getDistance(int x1, int y1, int x2, int y2) {
		return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
	}
	
	public static double getDistance(Robot r, Waypoint p) {
		return getDistance(r.getFrontMidX(), r.getFrontmidY(), p.x, p.y);
	}
	
	public static double getDistance(Waypoint a, Waypoint b) {
		return Math.sqrt(Math.pow(b.x - a.x, 2) + Math.pow(b.y - a.y, 2));
	}
	
	// a = 90
	// b = 300;
	// c = 210 // -150;
	public static double getRotation(double currentAngle, double newAngle) {
		double i = -(currentAngle - newAngle);
		System.out.println("Uncorrected rotation: " +i);
		if(Math.abs(i) > 180) {
			if(i > 0) {
				i = -(360 - i);
			} else {
				i = 360 + i;
			}
//			i = -(i - 180);
			
		}
		System.out.format("[Rotation: %f => %f : %f%n", currentAngle, newAngle, i);
		return i;
//		double diff = Math.abs(currentAngle - newAngle) % 360;
//		if(diff > 180) {
//			return -180 + Math.abs(180 - diff);
//		} else {
//			return diff;
//		}
		
	}
	
//	public static ArrayList<Point> breakpointToPoint(ArrayList<BreakPoint> list) {
//		
//	}

}
