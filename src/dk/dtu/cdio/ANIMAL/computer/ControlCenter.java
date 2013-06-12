package dk.dtu.cdio.ANIMAL.computer;

import routeCalculation.Track;
import main.Application;

public class ControlCenter implements Runnable {
	
	private static final double DISTANCE_THRESHOLD = 300/Navigator.MM_PR_PIXEL;
	
	private Application app;
	private Navigator navA, navB;
	
	private Waypoint middle = new Waypoint(Navigator.X_RESOLUTION/2, Navigator.Y_RESOLUTION/2);
	
	public ControlCenter(Application app) {
		this.app = app;
		navA = new Navigator(true, app);
		navB = new Navigator(false, app);
	}

	@Override
	public void run() {
		boolean running = true;
		boolean anyPaused = false;
		navA.feedBreakpoints(Track.getCompleteList());
		navB.feedBreakpoints(Track.getCompleteList());
		System.out.println("Starting A:");
		new Thread(navA).start();
		while(DISTANCE_THRESHOLD > Utilities.getDistance(navA.robot, navB.robot))
		{
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("Starting B:");
		new Thread(navB).start();
		while(running) {
			if(DISTANCE_THRESHOLD > Utilities.getDistance(navA.robot, navB.robot)) {
				Navigator temp = theOneBehind();
				temp.paused = true;
				System.out.format("Pausing %s%n", temp.info.name);
			}
			else
			{
				navA.paused = false;
				navB.paused = false;
				System.out.println("Unpausing both");
			}
			
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	private Navigator theOneBehind() {
		double angleA = Utilities.getAngleRelativeToCenter(navA.robot);
		double angleB = Utilities.getAngleRelativeToCenter(navB.robot);
		System.out.format("Angle A: %.2f, Angle B: %.2f%n", angleA, angleB);
		if(angleA > 110 && angleB < -110) {
			return navB;
		}
		if(angleB > 110 && angleB < -110) {
			return navA;
		}
		return (angleA > angleB) ? navA : navB;
	}

}
