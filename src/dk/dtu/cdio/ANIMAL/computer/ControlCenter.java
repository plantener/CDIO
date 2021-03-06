package dk.dtu.cdio.ANIMAL.computer;

import main.Application;
import main.Main;
import models.Box;
import models.Robot;
import routeCalculation.Track;

public class ControlCenter implements Runnable {
	
	private static final double DISTANCE_THRESHOLD = 75;
	
	private Application app;
	private Navigator navA, navB;
	private Thread nA, nB;
	
	public static boolean running = false;
	
	public ControlCenter(Application app) {
		this.app = app;
		navA = new Navigator(true, this.app);
		navB = new Navigator(false, this.app);
		nA = new Thread(navA);
		nB = new Thread(navB);
	}
	

	@Override
	public void run() {
		navA.feedBreakpoints(Track.getCompleteList());
		navB.feedBreakpoints(Track.getCompleteList());
		nA.start();
		nB.start();
		
		// cant stop now
		while(true) {
			boolean newStart = true;
				
	 		while(running) {
	 			if(newStart) {
					newStart = false;
	 				
	 				// Start the one in front)
	 				System.out.format("Starting %s:%n", (theOneBehind() == navA ? navB : navA).name);
	 				(theOneBehind() == navA ? navB : navA).running = true;
	 				
					try {
						while(DISTANCE_THRESHOLD > Utilities.getDistance(navA.robot, navB.robot))
						{
								Thread.sleep(500);
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
	 				System.out.format("Starting %s:%n", theOneBehind());
					theOneBehind().running = true;
	 			}
	 			
				if (System.currentTimeMillis() - navA.last > 10000 || System.currentTimeMillis() - navB.last > 10000 ||  navA.com.reconnect || navB.com.reconnect) {
					System.out.println("!!! Restart");
					navA.running = false;
					navB.running = false;
					navA.reconnect();
					navB.reconnect();
					navA.running = true;
					navB.running = true;
				} else if (Track.newRoute) {
					System.out.println("!!! New track");
					navA.paused = true;
					navB.paused = true;
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					if(isBoxInRobotProximity(navA.robot)) {
						navA.gen.backup(500);
					}
					
					if(isBoxInRobotProximity(navB.robot)) {
						navB.gen.backup(500);
					}
					
					navA.feedBreakpoints(Track.getCompleteList());
					navB.feedBreakpoints(Track.getCompleteList());
					Track.newRoute = false;
					navA.paused = false;
					navB.paused = false;
					navA.newTrack = true;
					navB.newTrack = true;
					
				} else if(DISTANCE_THRESHOLD > Utilities.getDistance(navA.robot, navB.robot)) {
					if(!navA.paused && !navB.paused) {
						Navigator temp = theOneBehind();
						temp.paused = true;
						System.out.format("Pausing %s%n", temp.info.name);
					}
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
					// We get about 30 fps, so no need to run this more often
					Thread.sleep(30);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
			}
	 		
	 		try {
	 			// If control loop is not currently running, we throttle
				Thread.sleep(1000);
			} catch (InterruptedException e) {
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
		if(angleB > edge && angleA < -edge) {
			return navA;
		}
		return (angleA > angleB) ? navA : navB;
	}
	
	private boolean isBoxInRobotProximity(Robot r) {
		int minimumDistance = 20;
		for(Box b : Main.redBoxes) {
			if(Utilities.getDistance(navA.robot.getFrontMidX(), navA.robot.getFrontmidY(), b.getMidX(), b.getMidY()) < minimumDistance) {
				return true;
			}
		}
		for(Box b : Main.greenBoxes) {
			if(Utilities.getDistance(navA.robot.getFrontMidX(), navA.robot.getFrontmidY(), b.getMidX(), b.getMidY()) < minimumDistance) {
				return true;
			}
		}
		return false;
	}

}
