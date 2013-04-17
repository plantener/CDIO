package main;

public class Main {

	public static void main(String[] args) {
		Application app = new Application();
		int i = 0;
		long startTime = System.nanoTime();
		while(i < 100){			
			app.frameProcessing();
			//i++;
		}
		Long endTime = System.nanoTime();
		double fps = (endTime-startTime)/100000000;
		System.out.println("FPS: " + fps);
	}
	
}

