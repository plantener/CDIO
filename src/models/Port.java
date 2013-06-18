package models;

import dk.dtu.cdio.ANIMAL.computer.Navigator;

public class Port extends ObjectOnMap implements Comparable<Port>{
	private int pairId;
	private int midX;
	private int midY;
	private Box red, green;
	private final int OFFSET = 0;
	private double angleRelativeToCenter;

	public Port(Box red, Box green){
		this.red = red;
		this.green = green;
		setMidX(this.red,this.green);
		setMidY(this.red,this.green);
		angleRelativeToCenter = dk.dtu.cdio.ANIMAL.computer.Utilities.getAngleRelativeToCenter(red.getMidX(), red.getMidY());
	}
	
	public Port(int midX, int midY, int pairId){
		this.midX = midX;
		this.midY = midY;
		this.pairId = pairId;
	}

	public int getPairId() {
		return pairId;
	}
	public void setPairId(int pairId) {
		this.pairId = pairId;
		red.setPairId(pairId);
		green.setPairId(pairId);
		
	}
	public int getMidX() {
		return midX;
	}
	public void setMidX(Box red, Box green) {
		int redMidX = red.getMidX();
		int greenMidX = green.getMidX();
		if (redMidX > greenMidX){
			midX = (redMidX - OFFSET  - greenMidX) / 2 + greenMidX;
		}else if (greenMidX > redMidX){
			midX = (greenMidX - redMidX + OFFSET) / 2 + redMidX;
		}else{
			midX = redMidX;
		}

	}
	public int getMidY() {
		return midY;
	}
	//TODO CHECKE THESE CALCULATIONS!!!
	public void setMidY(Box red, Box green) {
		int redMidY = red.getMidY();
		int greenMidY = green.getMidY();
		if (redMidY > greenMidY){
			midY = (redMidY - OFFSET - greenMidY) / 2 + greenMidY;
		}else if (greenMidY > redMidY){
			midY = (greenMidY - redMidY + OFFSET) / 2 + redMidY;
		}else{
			midY = redMidY;
		}

	}

	@Override
	public String toString() {
		return "Port [pairId=" + pairId + ", midX=" + midX + ", midY=" + midY
				+ ", red=" + red + ", green=" + green + "]\n";
	}

	@Override
	public int compareTo(Port comparePort) {
		double comparePortAngle = comparePort.getAngleRelativeToCenter();
		return (int) (this.angleRelativeToCenter - comparePortAngle);
	}

	public double getAngleRelativeToCenter() {
		return angleRelativeToCenter;
	}

	public void setAngleRelativeToCenter(double angleRelativeToCenter) {
		this.angleRelativeToCenter = angleRelativeToCenter;
	}


}

