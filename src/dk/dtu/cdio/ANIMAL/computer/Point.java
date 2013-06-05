package dk.dtu.cdio.ANIMAL.computer;

public class Point {
	
	int x, y;
	
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public String toString() {
		return String.format("[Point: %d, %d]", x, y);
	}

}
