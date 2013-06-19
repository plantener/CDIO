package utilities;

import main.Application;

public enum Threshold {
//	GREEN_LOWER(38,100,70), GREEN_UPPER(60,255,255), 
//	RED_LOWER(170,180,75), RED_UPPER(180,255,255),
//	YELLOW_LOWER(6,195,140), YELLOW_UPPER(15,255,255),
//	BLUE_LOWER(95,120,100), BLUE_UPPER(115,255,255),
//	PURPLE_LOWER(150,100,100), PURPLE_UPPER(170,255,255);
	
	//Thresholds for testing purposes in lab
//	GREEN_LOWER(50,100,70), GREEN_UPPER(70,255,255), 
//	RED_LOWER(165,180,75), RED_UPPER(180,255,255),
//	YELLOW_LOWER(80,100,90), YELLOW_UPPER(100,255,255), //teal
//	BLUE_LOWER(102,100,90), BLUE_UPPER(115,255,255),
//	PURPLE_LOWER(140,100,100), PURPLE_UPPER(160,255,255);
	
	//Updated thresholds for lab
	GREEN_LOWER(Application.green_h,Application.green_s,Application.green_v), GREEN_UPPER(Application.green_upper_h,255,255), 
	RED_LOWER(Application.red_h,Application.red_s,Application.red_v), RED_UPPER(Application.red_upper_h,255,255),
	YELLOW_LOWER(Application.lightBlue_h,Application.lightBlue_s,Application.lightBlue_v), YELLOW_UPPER(Application.lightBlue_upper_h,255,255),
	BLUE_LOWER(Application.blue_h,Application.blue_s,Application.blue_v), BLUE_UPPER(Application.blue_upper_h,255,255),
	PURPLE_LOWER(Application.purple_h,Application.purple_s,Application.purple_v), PURPLE_UPPER(Application.purple_upper_h,255,255);
		
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
