package models;

public class BreakPoint {
	private int x;
	private int y;
	private int port;

	public BreakPoint(int x, int y) {
		this.x = x;
		this.y = y;
		this.port = 0;
	}
	
	public BreakPoint(int x, int y, int port) {
		this.x = x;
		this.y = y;
		this.port = port;
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
	
	public int getPort() {
		return port;
	}
	
	public void setPort(int port) {
		this.port = port;
	}

	@Override
	public String toString() {
		return "Cord[x=" + x + ", y=" + y + ",port=" + port + "]";
	}
}
