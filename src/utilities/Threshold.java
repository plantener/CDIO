package utilities;

public enum Threshold {
	GREEN_LOWER(40,100,100), GREEN_UPPER(75,255,255), 
	RED_LOWER(170,100,100), RED_UPPER(185,255,255);
	
	
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
