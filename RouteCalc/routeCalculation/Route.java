package routeCalculation;

import java.util.ArrayList;

import objectHandling.DeadSpaceCalculation;

import models.Box;
import models.BreakPoint;
import models.Port;

public class Route {

	private CalculateRoute c;

	private Port start;
	private Port end;

	private ArrayList<BreakPoint> breaksPoints = new ArrayList<BreakPoint>();
	private ArrayList<BreakPoint> oldBreaksPoints = new ArrayList<BreakPoint>();

	private static boolean crash = false;
	
	public Route(Port start, Port end) {
		this.start = start;
		this.end = end;

		c = new CalculateRoute(start, end);
	}

	public Port getStart() {
		return start;
	}

	public Port getEnd() {
		return end;
	}

	public void update(Port start, Port end) {
		oldBreaksPoints = breaksPoints;
		this.start = start;
		this.end = end;
		System.out.println("update " + start + " " + end);
		c = new CalculateRoute(start, end);
	}

	public ArrayList<BreakPoint> getBreakPoints() {
		return breaksPoints;
	}

	public ArrayList<BreakPoint> getOldBreakPoints() {
		return oldBreaksPoints;
	}

	public void find() {
		ArrayList<BreakPoint> bP = c.routePositions();
		boolean collitions = true;

		while (collitions) {
			for (BreakPoint breakPoint : bP) {
				Box b;
				if ((b = DeadSpaceCalculation.collisionDetection(breakPoint)) != null) {
					System.out.println("found collision");
					c.addMid(b, breakPoint);
					bP = c.routePositions();
					collitions = true;
					break;
				}
				collitions = false;
			}
			if(crash){
				break;
			}
		}
		crash = false;
		breaksPoints = c.getBreakPoints();
		Track.addList(breaksPoints, oldBreaksPoints);
		System.out.println("Done!");
	}
	
	public static void setCrash(){
		crash = true;
	}
}
