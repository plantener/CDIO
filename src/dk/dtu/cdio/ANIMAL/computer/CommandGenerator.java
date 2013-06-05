package dk.dtu.cdio.ANIMAL.computer;

public class CommandGenerator {
	
	private PCCommunicator com;
	
	public CommandGenerator(PCCommunicator communicator) {
		this.com = communicator;
	}
	
	public void doTravel(float distance) {
		com.sendData(NavCommand.TRAVEL.ordinal(), distance, 0, 0, false);
	}
	
	public void doTravelArc(float radius, float distance) {
		com.sendData(NavCommand.TRAVEL_ARC.ordinal(), radius, distance, 0, false);
	}
	
	public void doRotate(float angle) {
		com.sendData(NavCommand.ROTATE.ordinal(), angle, 0, 0, false);
	}
	
	// 2 * radius * pi
	public void forwardArcRight(float angle, float radius) {
		
	}

}
