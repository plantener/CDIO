package Objects;

public class Cord {

	private int x;
	private int y;
	
	public Cord(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return x;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public int getY() {
		return y;
	}
	
	public void setY(int y) {
		this.y = y;
	}

	@Override
	public String toString() {
		return "Cord [x=" + x + ", y=" + y + "]";
	}
	
	
}
