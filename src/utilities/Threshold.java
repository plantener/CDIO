package utilities;

public enum Threshold {
	YELLOW_LOWER(25 ,80 ,80 ), YELLOW_UPPER(33,255,255), //teal
	GREEN_LOWER (38 ,80,70 ), GREEN_UPPER (70 ,255,255), 
	BLUE_LOWER  (99,75 ,75 ), BLUE_UPPER  (125,255,255),
	PURPLE_LOWER(135,70 ,70 ), PURPLE_UPPER(167,255,255),
	RED_LOWER   (170,80,75 ), RED_UPPER   (180,255,255);
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
//	YELLOW_LOWER(23,100,150), YELLOW_UPPER(33,255,255),
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
