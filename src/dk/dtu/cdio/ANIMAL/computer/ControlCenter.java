package dk.dtu.cdio.ANIMAL.computer;

import main.Application;
import routeCalculation.Track;

public class ControlCenter implements Runnable {
	
	private static final double DISTANCE_THRESHOLD = 600; // in mm
	
	private Application app;
	private Navigator navA, navB;
	
	public ControlCenter(Application app) {
		this.app = app;
		navA = new Navigator(true, this.app);
//		navB = new Navigator(false, this.app);
	}

	@Override
	public void run() {
		boolean running = true;
		navA.feedBreakpoints(Track.getCompleteList());
//		navB.feedBreakpoints(Track.getCompleteList());
		System.out.println("Starting A:");
		new Thread(navA).start();
//		while(DISTANCE_THRESHOLD / Navigator.MM_PR_PIXEL > Utilities.getDistance(navA.robot, navB.robot))
//		{
//			try {
//				Thread.sleep(500);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		System.out.println("Starting B:");
//		new Thread(navB).start();
//		while(running) {
//			if(DISTANCE_THRESHOLD / Navigator.MM_PR_PIXEL > Utilities.getDistance(navA.robot, navB.robot)) {
//				if(!navA.paused && !navB.paused) {
//					Navigator temp = theOneBehind();
//					temp.paused = true;
//					System.out.format("Pausing %s%n", temp.info.name);
//				}
//			} else {
//				if(navA.paused) {
//					navA.paused = false;
//					System.out.println("Unpausing A");
//				}
//				if(navB.paused) {
//					navB.paused = false;
//					System.out.println("Unpausing B");
//				}
//			}
//			
//			try {
//				Thread.sleep(100);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
//		}
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
