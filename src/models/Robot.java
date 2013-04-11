package models;

public class Robot extends ObjectOnMap {

	
	private int frontX;
	private int frontY;
	private int backX;
	private int backY;
	private int frontHeight;
	private int frontWidth;
	private int backHeight;
	private int backWidth;
	private int frontMidX;
	private int frontmidY;
	private int backMidX;
	private int backMidY;
	private int frontColor;
	
	public int getFrontX() {
		return frontX;
	}
	public void setFrontX(int x) {
		this.frontX = x;
	}
	public int getY() {
		return frontY;
	}
	public void setY(int y) {
		this.frontY = y;
	}
	public int getFrontHeight() {
		return frontHeight;
	}
	public void setFrontHeight(int height) {
		this.frontHeight = height;
	}
	public int getFrontWidth() {
		return frontWidth;
	}
	public void setFrontWidth(int width) {
		this.frontWidth = width;
	}
	public int getFrontMidX() {
		frontMidX = (frontWidth / 2) + frontX;
		return frontMidX;
	}
	//TODO Hvis MidX og MidY ikke passer på robot, tjek farvegenkendelsesalgoritme. Muligvis forkert boks.
	public int getFrontmidY() {
		frontmidY = (frontHeight / 2) + frontY;
		return frontmidY;
	}
	
	public int getBackMidX() {
		backMidX = (backWidth / 2) + backX;
		return backMidX;
	}
	
	public int getBackMidY() {
		backMidY = (backHeight / 2) + backY;
		return backMidY;
	}

	public int getFrontColor() {
		return frontColor;
	}
	public void setFrontColor(int frontColor) {
		this.frontColor = frontColor;
	}
	public int getBackWidth() {
		return backWidth;
	}
	public void setBackWidth(int backWidth) {
		this.backWidth = backWidth;
	}
	public int getBackY() {
		return backY;
	}
	public void setBackY(int backY) {
		this.backY = backY;
	}
	public int getBackX() {
		return backX;
	}
	public void setBackX(int backX) {
		this.backX = backX;
	}
	public int getBackHeight() {
		return backHeight;
	}
	public void setBackHeight(int backHeight) {
		this.backHeight = backHeight;
	}
}