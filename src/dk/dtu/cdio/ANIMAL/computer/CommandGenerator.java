package dk.dtu.cdio.ANIMAL.computer;

public class CommandGenerator {
	
	private PCCommunicator com;
	
	public CommandGenerator(PCCommunicator communicator) {
		this.com = communicator;
	}
	
	public void backup(int time) {
		com.sendData(NavCommand.BACKUP.ordinal(), time);
	}
	
	public void doSteer(float turnRate) {
		com.sendData(NavCommand.STEER.ordinal(), turnRate);
	}
	
	public void setTravelSpeed(float speed) {
		com.sendData(NavCommand.SET_TRAVELSPEED.ordinal(), speed);
	}
	
	public void sendStop() {
		com.sendData(NavCommand.STOP.ordinal(), 0);
	}
}
