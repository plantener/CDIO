package routeCalculation;

import java.util.ArrayList;

import objectHandling.DeadSpaceCalculation;

import Objects.Block;
import Objects.Cord;

public class Route implements Runnable {

	private CalculateRoute c;

	private Cord start;
	private Cord end;

	private ArrayList<Cord> breaksPoints = new ArrayList<Cord>();

	private boolean update = true;

	public Route(Cord start, Cord end) {
		this.start = start;
		this.end = end;

		c = new CalculateRoute(start, end);
	}

	public Cord getStart() {
		return start;
	}

	public Cord getEnd() {
		return end;
	}

	public void update(Cord start, Cord end) {
		this.end = end;
		c = new CalculateRoute(start, end);
		this.update = true;
	}

	public ArrayList<Cord> getBreakPoints() {
		return breaksPoints;
	}

	@Override
	public void run() {
		ArrayList<Cord> p = c.routePositions();
		boolean collitions = true;

		while (true) {
			if (update) {
				update = false;
				while (collitions) {
					for (Cord cord : p) {
						Block b;
						if ((b = DeadSpaceCalculation.collisionDetection(cord)) != null) {
							c.addMid(b, cord);
							p = c.routePositions();
							collitions = true;
							break;
						}
						collitions = false;
					}
				}
				if (!collitions){
					breaksPoints = c.getBreakPoints();
					System.out.println("Done!");
				}
			}
		}
	}

	// public ArrayList<Cord> route() {
	// ArrayList<Cord> p = c.routePositions();
	// boolean collitions = true;
	//
	// while(collitions){
	// for (Cord cord : p) {
	// Block b;
	// if ((b = DeadSpaceCalculation.collisionDetection(cord)) != null) {
	// c.addMid(b, cord);
	// p = c.routePositions();
	// collitions = true;
	// break;
	// }
	// collitions = false;
	// }
	// }
	// for(int i = 0; i < 100; i++){
	// for(int j = 0; j < 100; j++){
	// int c = 0;
	// for (Cord cord : p) {
	// if(cord.getX() == j && cord.getY() == 99-i){
	// c = 1;
	// break;
	// }
	// }
	// if(c == 0)
	// System.out.print("0");
	// if(c == 1)
	// System.out.print("1");
	// }
	// System.out.println();
	// }
	//
	// return p;
	// }
}
