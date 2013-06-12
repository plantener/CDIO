package dk.dtu.cdio.ANIMAL.computer;

import routeCalculation.Track;
import main.Application;

public class ControlCenter implements Runnable {
	
	private static final double DISTANCE_THRESHOLD = 500/Navigator.MM_PR_PIXEL;
	
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
		navA.feedBreakpoints(Track.getCompleteList());
		navB.feedBreakpoints(Track.getCompleteList());
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
		new Thread(navB).start();
		while(running) {
			if(DISTANCE_THRESHOLD > Utilities.getDistance(navA.robot, navB.robot)) {
				theOneBehind().paused = true;
			}
			else
			{
				navA.paused = false;
				navB.paused = false;
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
		if(angleA > 135 && angleB < -135) {
			return navB;
		}
		if(angleB > 135 && angleB < -135) {
			return navA;
		}
		return (angleA > angleB) ? navA : navB;
	}

}
