package models;

public class Port extends ObjectOnMap implements Comparable<Port>{
	private int pairId;
	private int midX;
	private int midY;
	private Box red, green;

	public Port(Box red, Box green){
		this.red = red;
		this.green = green;
		setMidX(this.red,this.green);
		setMidY(this.red,this.green);
	}


	public int getPairId() {
		return pairId;
	}
	public void setPairId(int pairId) {
		this.pairId = pairId;
	}
	public int getMidX() {
		return midX;
	}
	public void setMidX(Box red, Box green) {
		int redMidX = red.getMidX();
		int greenMidX = green.getMidX();
		if (redMidX > greenMidX){
			midX = (redMidX - greenMidX) / 2 + greenMidX;
		}else if (greenMidX > redMidX){
			midX = (greenMidX - redMidX) / 2 + redMidX;
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
			midY = (redMidY - greenMidY) / 2 + greenMidY;
		}else if (greenMidY > redMidY){
			midY = (greenMidY - redMidY) / 2 + redMidY;
		}else{
			midY = redMidY;
		}

	}

	@Override
	public String toString() {
		return "Port [pairId=" + pairId + ", midX=" + midX + ", midY=" + midY
				+ ", red=" + red + ", green=" + green + "]";
	}


	@Override
	public int compareTo(Port comparePort) {
		int compareMidX = ((Port) comparePort).getMidX();
		
		if(comparePort.getMidY() < 150){
		return this.midX - compareMidX;
		}else{
			return compareMidX - this.midX;
		}
	}


}

