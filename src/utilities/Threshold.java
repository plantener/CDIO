package utilities;

public enum Threshold {
	GREEN_LOWER(38,100,70), GREEN_UPPER(60,255,255), 
	RED_LOWER(170,180,75), RED_UPPER(180,255,255),
//	YELLOW_LOWER(5,150,140), YELLOW_UPPER(15,255,255), // is actually orange
	YELLOW_LOWER(25,180,150), YELLOW_UPPER(33,255,255), // is actually yellow
	BLUE_LOWER(95,100,100), BLUE_UPPER(115,255,255),
	PURPLE_LOWER(150,100,100), PURPLE_UPPER(170,255,255);
		
	//Green should maybe be changed to lower: (45,160,70)
	
	
	private int b,g,r;
	
	private Threshold(int b, int g, int r) {
		this.b = b;
		this.g = g;
		this.r = r;
	}
	
	public int getB() {
		return b;
	}
	
	public int getG() {
		return g;
	}
	
	public int getR() {
		return r;
	}
}
