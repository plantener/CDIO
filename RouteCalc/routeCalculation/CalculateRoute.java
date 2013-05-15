package routeCalculation;

import java.util.ArrayList;

import models.Box;
import models.BreakPoint;
import models.Port;

public class CalculateRoute {

	private ArrayList<BreakPoint> route = new ArrayList<BreakPoint>();

	private ArrayList<BreakPoint> breakPoints = new ArrayList<BreakPoint>();

	private float slope;

	private int offset = 10;

	private BreakPoint end;
	private BreakPoint nextStart;
	private BreakPoint nextEnd;

	private BreakPoint pre;
	
	public CalculateRoute(Port start, Port end) {
		this.end = new BreakPoint(end.getMidX(), end.getMidY());
		this.nextStart = new BreakPoint(start.getMidX(), start.getMidY());
		this.nextEnd = this.end;
		breakPoints.add(this.nextStart);
		breakPoints.add(this.end);
		setSlope(this.nextStart, this.end);
	}

	public void addMid(Box b, BreakPoint c) {
		System.out.println("Crash: " + c.toString());
		if(pre != null)
			if(c.getX() == pre.getX() && c.getY() == pre.getY()){
				Route.setCrash();
			}
		if (b.getColor() == 2) { // green
			if (b.getX() == c.getX()) { // Left
				System.out.println(0);
				breakPoints.add(breakPoints.indexOf(end), new BreakPoint(b.getX()-offset,
						b.getY()-offset));
			} else if (b.getX() + b.getWidth() == c.getX()) { // Right
				System.out.println(1);
				breakPoints.add(breakPoints.indexOf(end), new BreakPoint(b.getX()+b.getWidth()+offset,
						b.getY()+b.getHeight()+offset));
			} else if (b.getY() == c.getY()) { // Top
				System.out.println(2);
				breakPoints.add(breakPoints.indexOf(end), new BreakPoint(b.getX()+b.getWidth()+offset,
						b.getY()-offset));
			} else if (b.getY() + b.getHeight() == c.getY()) { // Bottom
				System.out.println(3);
				breakPoints.add(breakPoints.indexOf(end), new BreakPoint(b.getX()-offset,
						b.getY()+b.getHeight()));
			}
		} else if (b.getColor() == 1) {
			if (b.getX() == c.getX()) { // Left

			} else if (b.getX() + b.getWidth() == c.getX()) { // Right

			} else if (b.getY() == c.getY()) { // Top

			} else if (b.getY() - b.getHeight() == c.getY()) { // Bottom

			}
		}
		pre = c;
	}

	public void setSlope(BreakPoint start, BreakPoint end) {
		this.slope = (float) (end.getY() - start.getY())
				/ (end.getX() - start.getX());
	}

	public BreakPoint calcPosition(int count, int check) {
		if (check == 1) {
			return new BreakPoint(Math.round((count - nextEnd.getY()) / slope
					+ nextEnd.getX()), count);
		} else if (check == 0) {
			return new BreakPoint(count, Math.round(slope
					* (count - nextEnd.getX()) + nextEnd.getY()));
		}
		return null;
	}

	public ArrayList<BreakPoint> routePositions() {
		nextStart = breakPoints.get(0);
		nextEnd = breakPoints.get(1);
		route.clear();
		for (int i = 0; i < breakPoints.size() - 1; i++) {
			setSlope(nextStart, nextEnd);
			System.out.println(breakPoints.get(0).toString() + " "
					+ breakPoints.get(1).toString());

			int count = 0, check = 0;
			if (slope >= 1 || slope <= -1) {
				count = nextStart.getY();
				check = 1;
			} else if (slope < 1 || slope > -1) {
				count = nextStart.getX();
				check = 0;
			}

			if (check == 1) {
				while (count != nextEnd.getY()) {
					// System.out.println(count + " " + nextEnd.getY());
					route.add(calcPosition(count, check));
					if (nextEnd.getY() >= nextStart.getY())
						count++;
					else
						count--;
				}
			} else if (check == 0) {
				while (count != nextEnd.getX()) {
					route.add(calcPosition(count, check));
					if (nextEnd.getX() >= nextStart.getX())
						count++;
					else
						count--;
				}
			}
			if (breakPoints.indexOf(nextStart) + 1 != breakPoints.size() - 1) {
				nextStart = nextEnd;
				nextEnd = breakPoints.get(breakPoints.indexOf(nextStart) + 1);
			}
		}
		System.out.println("Come out");
		return route;
	}

	public ArrayList<BreakPoint> getBreakPoints() {
		return breakPoints;
	}
}
