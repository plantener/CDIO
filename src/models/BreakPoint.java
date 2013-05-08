package models;

public class BreakPoint {
	private int x;
	private int y;
	private double a;

	public BreakPoint(int x, int y, double a) {
		this.x = x;
		this.y = y;
		this.a = a;
	}
	public BreakPoint(int x, int y) {
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
	
	public double getA() {
		return a;
	}
	
	public void setA(double a) {
		this.a = a;
	}

	@Override
	public String toString() {
		return "Cord [x=" + x + ", y=" + y + "]";
	}
}
