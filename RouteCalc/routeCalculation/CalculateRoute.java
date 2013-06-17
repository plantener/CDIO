package routeCalculation;

import java.util.ArrayList;

import main.Application;
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
	
	private int port;
	
	public CalculateRoute(Port start, Port end) {
		this.port = start.getPairId();
		this.end = new BreakPoint(end.getMidX(), end.getMidY(), port);
		this.nextStart = new BreakPoint(start.getMidX(), start.getMidY(), port);
		this.nextEnd = this.end;
		breakPoints.add(this.nextStart);
		breakPoints.add(this.end);
		System.out.println("start" + breakPoints.size());
		setSlope(this.nextStart, this.end);
	}

	public int addMid(Box b, BreakPoint c) {
		System.out.println("Crash: " + c.toString() + " " + port);
		if(pre != null){
			if(c.getX() == pre.getX() && c.getY() == pre.getY()){
				System.out.println("double: " + c.toString());
				Route.setCrash();
				return 0;
			}
		}
		if (b.getColor() == Application.RED) { // red
			if (b.getX() == c.getX()) { // Left
				if(b.getPairId() == port){
					breakPoints.add(breakPoints.indexOf(end), new BreakPoint(b.getX()-offset,
							b.getY(), port));
				}else{
					breakPoints.add(breakPoints.indexOf(end), new BreakPoint(b.getX()+b.getWidth(),
							b.getY()+b.getHeight()+offset, port));
				}
			} else if (b.getY() == c.getY()) { // Top
				if(b.getPairId() == port){
					breakPoints.add(breakPoints.indexOf(end), new BreakPoint(b.getX()+b.getWidth(),
							b.getY()+b.getHeight()+offset, port));
				}else{
					breakPoints.add(breakPoints.indexOf(end), new BreakPoint(b.getX()+b.getWidth(),
							b.getY()-offset, port));
				}
			} else if (b.getX() + b.getWidth() == c.getX()) { // Right
				if(b.getPairId() == port){
					breakPoints.add(breakPoints.indexOf(end), new BreakPoint(b.getX()-offset,
							b.getY()+b.getHeight(), port));
				}else{
					breakPoints.add(breakPoints.indexOf(end), new BreakPoint(b.getX()+b.getWidth()+offset,
							b.getY()+b.getHeight()+offset, port));
				}
			} else if (b.getY() + b.getHeight() == c.getY()) { // Bottom
				if(b.getPairId() == port){
					breakPoints.add(breakPoints.indexOf(end), new BreakPoint(b.getX()-offset,
							b.getY()+b.getHeight(), port));
				}else{
					breakPoints.add(breakPoints.indexOf(end), new BreakPoint(b.getX()-offset,
							b.getY()+b.getHeight(), port));
				}
			}
		} else if (b.getColor() == Application.GREEN) {
			if (b.getX() == c.getX()) { // Left
				breakPoints.add(breakPoints.indexOf(end), new BreakPoint(b.getX()-offset,
						b.getY()+b.getHeight(), port));
			} else if (b.getX() + b.getWidth() == c.getX()) { // Right
				breakPoints.add(breakPoints.indexOf(end), new BreakPoint(b.getX()+ b.getWidth()+offset,
						b.getY()-offset, port));
			} else if (b.getY() == c.getY()) { // Top
				breakPoints.add(breakPoints.indexOf(end), new BreakPoint(b.getX()-offset,
						b.getY()-offset, port));
			} else if (b.getY() + b.getHeight() == c.getY()) { // Bottom
				breakPoints.add(breakPoints.indexOf(end), new BreakPoint(b.getX()+b.getWidth()+offset,
						b.getY()+b.getHeight()+offset, port));
			}
		}
		System.out.println("mid" + breakPoints.size());
		pre = c;
		
		return breakPoints.size();
	}

	public void setSlope(BreakPoint start, BreakPoint end) {
		this.slope = (float) (end.getY() - start.getY())
				/ (end.getX() - start.getX());
	}

	public BreakPoint calcPosition(int count, int check) {
		if (check == 1) {
			return new BreakPoint(Math.round((count - nextEnd.getY()) / slope
					+ nextEnd.getX()), count, port);
		} else if (check == 0) {
			return new BreakPoint(count, Math.round(slope
					* (count - nextEnd.getX()) + nextEnd.getY()), port);
		}
		return null;
	}

	public ArrayList<BreakPoint> routePositions() {
		System.out.println("end" + breakPoints.size() + " " + breakPoints.get(0));
		nextStart = breakPoints.get(0);
		nextEnd = breakPoints.get(1);
		route.clear();
		for (int i = 0; i < breakPoints.size() - 1; i++) {
			setSlope(nextStart, nextEnd);

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
		return route;
	}

	public ArrayList<BreakPoint> getBreakPoints() {
		return breakPoints;
	}
}
