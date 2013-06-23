package utilities;

import main.Application;

public enum Threshold {
	
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
