package utilities;

public enum Threshold {
	YELLOW_LOWER(24,85,85), YELLOW_UPPER(32,255,255),
	GREEN_LOWER (40 ,90,90 ), GREEN_UPPER (60 ,255,255), 
//	YELLOW_LOWER(82 ,80 ,80 ), YELLOW_UPPER(95,255,255), //teal
	BLUE_LOWER  (100,75 ,75 ), BLUE_UPPER  (115,255,255),
	PURPLE_LOWER(130,75 ,75 ), PURPLE_UPPER(167,255,255),
	RED_LOWER   (169,100,80 ), RED_UPPER   (180,255,255);
//	PURPLE_LOWER(150,100,100), PURPLE_UPPER(165,255,255);
	
	//Thresholds for testing purposes in lab
//	GREEN_LOWER(50,100,70), GREEN_UPPER(70,255,255), 
//	RED_LOWER(165,60,60), RED_UPPER(180,255,255),
//	YELLOW_LOWER(80,80,80), YELLOW_UPPER(100,255,255), //teal
//	BLUE_LOWER(102,40,40), BLUE_UPPER(125,255,255),
//	PURPLE_LOWER(133,60,60), PURPLE_UPPER(165,255,255);
	
	//Updated thresholds for lab
//	GREEN_LOWER(39,100,80), GREEN_UPPER(75,255,255), 
//	RED_LOWER(160,100,100), RED_UPPER(179,255,255),
//	BLUE_LOWER(100,100,100), BLUE_UPPER(125,255,255),
//	PURPLE_LOWER(135,120,120), PURPLE_UPPER(159,255,255);
		
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
