package models;

public class Grid {

	private int[][] grid;
	
	public Grid(){
		grid = new int [266][400];
	}
	
	
	public void setGridToZero(){
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[0].length; j++) {
				grid[i][j] = 0;
			}
			
		}
		
	}
	
	public int[][] getGrid(){
		return grid;
	}
	
	public void setGridPosition(int x, int y, int color){
		grid[y][x] = color;
		
	}
}

