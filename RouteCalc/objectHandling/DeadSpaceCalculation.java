package objectHandling;

import java.util.ArrayList;

import Objects.Block;
import Objects.Cord;

public class DeadSpaceCalculation {

	private static ArrayList<Block> blocks = new ArrayList<Block>();

	public static boolean addBlock(Block b){
		return blocks.add(b);
	}
	
	public static boolean removeBlock(Block b){
		return blocks.remove(b);
	}
	
	public static Block collisionDetection(Cord p){

		for(Block b : blocks){
			for(int x = b.getX(); x <= b.getX()+b.getL(); x++){
				if(p.getX() == x){
					for(int y = b.getY(); y >= b.getY()-b.getH(); y--){
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
