package dk.dtu.cdio.ANIMAL.computer;

import java.util.Queue;

public class CommandGenerator {
	
	private PCCommunicator com;
	private Queue<Command> queue;
	
	public CommandGenerator(PCCommunicator communicator, Queue<Command> queue) {
		this.com = communicator;
		this.queue = queue;
	}
	
	public void doTravel(float distance) {
		queue.add(new Command(NavCommand.TRAVEL, distance, 0,0,false));
		com.sendData(NavCommand.TRAVEL.ordinal(), distance, 0, 0, false);
	}
	
	public void doTravelArc(float radius, float distance) {
		queue.add(new Command(NavCommand.TRAVEL_ARC, distance, 0,0,false));
		com.sendData(NavCommand.TRAVEL_ARC.ordinal(), radius, distance, 0, false);
	}
	
	public void doRotate(float angle) {
		queue.add(new Command(NavCommand.ROTATE, angle, 0,0,false));
		com.sendData(NavCommand.ROTATE.ordinal(), angle, 0, 0, false);
	}
	
	public void setTravelSpeed(float speed) {
		com.sendData(NavCommand.SET_TRAVELSPEED.ordinal(), speed, 0, 0, false);
	}
	
	public void setRotateSpeed(float speed) {
		com.sendData(NavCommand.SET_ROTATESPEED.ordinal(), speed, 0, 0, false);
	}
	
	public void setAcceleration(int accel) {
		com.sendData(NavCommand.SET_ACCELERATION.ordinal(), accel, 0, 0, false);
	}
	
	public void sendStop() {
		com.sendData(NavCommand.STOP.ordinal(), 0, 0, 0, false);
	}
	
	public void sendStopAndClear() {
		queue.clear();
		com.sendData(NavCommand.STOP_AND_CLEAR.ordinal(), 0, 0, 0, false);
	}
	
	public void sendClear() {
		queue.clear();
		com.sendData(NavCommand.CLEAR.ordinal(), 0, 0, 0, false);
	}
	
	// 2 * radius * pi
	public void forwardArcRight(float angle, float radius) {
		
	}

}
