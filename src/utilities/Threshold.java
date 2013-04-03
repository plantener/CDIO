package utilities;

public enum Threshold {
	GREEN_LOWER(45,100,100), GREEN_UPPER(70,255,255), 
	RED_LOWER(170,200,75), RED_UPPER(180,255,255);
	
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
