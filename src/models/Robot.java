package models;

public class Robot extends ObjectOnMap implements Cloneable {

	
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
	private String robotId;
	
	public int getFrontX() {
		return frontX;
	}
	public void setFrontX(int x) {
		this.frontX = x;
	}
	public int getFrontY() {
		return frontY;
	}
	public void setFrontY(int y) {
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
public String getRobotId(){
	return robotId;
}
public void setRobotId(String robotId){
	this.robotId = robotId;
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
	
	@Override
	public String toString() {
		return "Robot [frontX=" + frontX + ", frontY=" + frontY + ", backX="
				+ backX + ", backY=" + backY + ", frontHeight=" + frontHeight
				+ ", frontWidth=" + frontWidth + ", backHeight=" + backHeight
				+ ", backWidth=" + backWidth + ", frontMidX=" + frontMidX
				+ ", frontmidY=" + frontmidY + ", backMidX=" + backMidX
				+ ", backMidY=" + backMidY; 
	}
	
	public Robot clone() {
		Robot clone = new Robot();
		clone.setBackHeight(backHeight);
		clone.setBackWidth(backWidth);
		clone.setBackX(backX);
		clone.setBackY(backY);
		clone.setFrontHeight(frontHeight);
		clone.setFrontWidth(frontWidth);
		clone.setFrontX(frontX);
		clone.setFrontY(frontY);
		clone.setRobotId(robotId);
		return clone;
	}
	
}
