package objectHandling;

import java.util.ArrayList;

import models.Box;
import models.BreakPoint;

public class DeadSpaceCalculation {
	
	private static ArrayList<Box> Boxs = new ArrayList<Box>();

	public static boolean addBox(Box b){
		return Boxs.add(b);
	}
	
	public static boolean removeBox(Box b){
		return Boxs.remove(b);
	}
	
	public static void removeAll(){
		Boxs.clear();
	}
	
	public static boolean replaceBox(Box newBox, Box oldBox){
		return Boxs.remove(oldBox) && Boxs.add(newBox);
	}
	
	public static Box collisionDetection(BreakPoint p){
		System.out.println("Size: " + Boxs.size());
		for(Box b : Boxs){
			for(int x = b.getX(); x <= b.getX()+b.getWidth(); x++){
				if(p.getX() == x){
					for(int y = b.getY(); y <= b.getY()+b.getHeight(); y++){
						if(p.getY() == y){
							return b;
						}
					}
				}
			}
		}
		
		return null;
	}
}
