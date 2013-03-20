package models;

public class Grid {

	private byte[][] grid;
	
	public Grid(){
		//TODO Add functionality to assign gridsize from elsewhere
		grid = new byte [300][400];
	}
	
	
	public void setGridToZero(){
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[0].length; j++) {
				grid[i][j] = 0;
			}
			
		}
		
	}
	
	public byte[][] getGrid(){
		return grid;
	}
	
	public void setGridPosition(byte x, byte y, byte color){
		grid[y][x] = color;
		
	}
}

