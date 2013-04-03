package models;

public class ObjectOnMap {
	
	private int x;
	private int y;
	private int height;
	private int width;
	private int color;
	private int midX;
	private int midY;
	
	
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
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getColor() {
		return color;
	}
	public void setColor(int color) {
		this.color = color;
	}
	public int getMidX() {
		midX = (width/2)+x;
		return midX;
	}
	
	public int getMidY() {
		midY = (height/2)+y;		
		return midY;
	}
	

}
