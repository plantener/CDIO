package dk.dtu.cdio.ANIMAL.computer;

import main.Application;
import routeCalculation.Track;

public class ControlCenter implements Runnable {
	
	private static final double DISTANCE_THRESHOLD = 75;
	
	private Application app;
	private Navigator navA, navB;
	private Thread nA, nB;
	
	public ControlCenter(Application app) {
		this.app = app;
		navA = new Navigator(true, this.app);
		navB = new Navigator(false, this.app);
		nA = new Thread(navA);
		nB = new Thread(navB);
	}
	

	@Override
	public void run() {
		boolean running = true;
		navA.feedBreakpoints(Track.getCompleteList());
		navB.feedBreakpoints(Track.getCompleteList());
		System.out.println("Starting A:");
		nA.start();
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
		nB.start();
		while(running) {
			if(DISTANCE_THRESHOLD > Utilities.getDistance(navA.robot, navB.robot)) {
				if(!navA.paused && !navB.paused) {
					Navigator temp = theOneBehind();
					temp.paused = true;
					System.out.format("Pausing %s%n", temp.info.name);
				}
			} else if (System.currentTimeMillis() - navA.last > 12000 || System.currentTimeMillis() - navB.last > 12000) {
				System.out.println("!!! Restart");
				navA.close();
				navB.close();
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				navA = new Navigator(true, app);
				navB = new Navigator(false, app);
				navA.feedBreakpoints(Track.getCompleteList());
				navB.feedBreakpoints(Track.getCompleteList());
				nA = new Thread(navA);
				nB = new Thread(navB);
				nA.start();
				nB.start();
				
			} else if (Track.newRoute) {
				navA.paused = true;
				navB.paused = true;
				navA.newTrack = true;
				navB.newTrack = true;
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				navA.feedBreakpoints(Track.getCompleteList());
				navB.feedBreakpoints(Track.getCompleteList());
				Track.newRoute = false;
				navA.paused = false;
				navB.paused = false;
				
			} else {
				if(navA.paused) {
					navA.paused = false;
					System.out.println("Unpausing A");
				}
				if(navB.paused) {
					navB.paused = false;
					System.out.println("Unpausing B");
				}
			}
			
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	private Navigator theOneBehind() {
		double angleA = Utilities.getAngleRelativeToCenter(navA.robot);
		double angleB = Utilities.getAngleRelativeToCenter(navB.robot);
		int edge = 90;
		System.out.format("Angle A: %.2f, Angle B: %.2f%n", angleA, angleB);

		if(angleA > edge && angleB < -edge) {
			return navB;
		}
		if(angleB > edge && angleB < -edge) {
			return navA;
		}
		return (angleA > angleB) ? navA : navB;
	}

}
