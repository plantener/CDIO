package models;

public class Box extends ObjectOnMap{
	private int pairId;
	private int x;
	private int y;
	private int height;
	private int width;
	private int midX;
	private int midY;
	// Red = 1, Green = 2
	private int color;
	
	
	public int getPairId() {
		return pairId;
	}
	public void setPairId(int pairId) {
		this.pairId = pairId;
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
	public int getMidX() {
		midX = (width/2)+x;
		return midX;
	}
	
	public int getMidY() {
		midY = (height/2)+y;
		return midY;
	}

	public int getColor() {
		return color;
	}
	public void setColor(int color) {
		this.color = color;
	}
	
}
