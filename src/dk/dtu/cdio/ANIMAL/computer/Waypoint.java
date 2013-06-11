package dk.dtu.cdio.ANIMAL.computer;

public class Waypoint {
	
	int x, y;
	
	public Waypoint(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public String toString() {
		return String.format("[Point: %d, %d]", x, y);
	}

}
