package dk.dtu.cdio.ANIMAL.computer;

public class Pose extends Point {
	
	double angle;
	
	public Pose(int x, int y, double angle) {
		super(x, y);
		this.angle = angle;
	}
}
