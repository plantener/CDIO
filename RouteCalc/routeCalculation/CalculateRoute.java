package routeCalculation;

import java.util.ArrayList;

import Objects.Block;
import models.BreakPoint;
import models.Port;

public class CalculateRoute {
	
	private ArrayList<BreakPoint> route = new ArrayList<BreakPoint>();
	
	private ArrayList<BreakPoint> breakPoints = new ArrayList<BreakPoint>(); 
	
	private float slope;
	
	private int offset = 5;
	
	private BreakPoint end;
	private BreakPoint nextStart;
	private BreakPoint nextEnd;
	
	public CalculateRoute(Port start, Port end){
		this.end = new BreakPoint(end.getMidX(), end.getMidY());
		this.nextStart = new BreakPoint(start.getMidX(), start.getMidY());
		this.nextEnd = this.end;
		breakPoints.add(this.nextStart);
		breakPoints.add(this.end);
		setSlope(this.nextStart,this.end);
	}
	
	public void addMid(Block b, BreakPoint c){
		if(b.getX() == c.getX()){
			breakPoints.add(breakPoints.indexOf(end), new BreakPoint(b.getX()-offset, b.getY()+offset));
		}else if(b.getY() == c.getY()){
			breakPoints.add(breakPoints.indexOf(end), new BreakPoint(b.getX()+b.getL()+offset, b.getY()+offset));
		}else if(b.getY()-b.getH() == c.getY()){
			breakPoints.add(breakPoints.indexOf(end), new BreakPoint(b.getX()+offset, b.getY()-b.getH()-offset));
		}else if(b.getX()+b.getL() == c.getX()){
			breakPoints.add(breakPoints.indexOf(end), new BreakPoint(b.getX()+b.getL()+offset, b.getY()-b.getH()-offset));
		}
	}
	
	public void setSlope(BreakPoint start, BreakPoint end){
		this.slope = (float) (end.getY()-start.getY())/(end.getX()-start.getX());
	}
	
	public BreakPoint calcPosition(int count, int check){
		if(check == 1){
			return new BreakPoint(Math.round((count-nextEnd.getY())/slope+nextEnd.getX()), count);
		}else if (check == 0){
			return new BreakPoint(count, Math.round(slope*(count-nextEnd.getX())+nextEnd.getY()));
		}
		return null;
	}
	
	public ArrayList<BreakPoint> routePositions(){
		nextStart = breakPoints.get(0);
		nextEnd = breakPoints.get(1);
		route.clear();
		for (int i = 0; i < breakPoints.size()-1; i++) {
			setSlope(nextStart, nextEnd);
			System.out.println(breakPoints.get(0).toString() + " " + breakPoints.get(1).toString());
			
			int count = 0, check = 0;
			if(slope >= 1 || slope <= -1){
				count = nextStart.getY();
				check = 1;
			}else if(slope < 1 || slope > -1){
				count = nextStart.getX();
				check = 0;
			}
			
			if(check == 1){
				while(count != nextEnd.getY()){
					//System.out.println(count + " " + nextEnd.getY());
					route.add(calcPosition(count, check));
					if(nextEnd.getY() >= nextStart.getY())
						count++;
					else
						count--;
				}
			}else if(check == 0){
				while(count != nextEnd.getX()){
					route.add(calcPosition(count, check));
					if(nextEnd.getX() >= nextStart.getX())
						count++;
					else
						count--;
				}
			}
			if(breakPoints.indexOf(nextStart)+1 != breakPoints.size()-1){
				nextStart = nextEnd;
				nextEnd = breakPoints.get(breakPoints.indexOf(nextStart)+1);
			}
		}
		
		return route;
	}
	
	public ArrayList<BreakPoint> getBreakPoints(){
		return breakPoints;
	}
}
