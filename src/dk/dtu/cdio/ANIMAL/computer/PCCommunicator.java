package dk.dtu.cdio.ANIMAL.computer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTConnector;
import lejos.pc.comm.NXTInfo;

public class PCCommunicator {

	private NXTInfo nxtInfo;
	private NXTConnector connector;
	private DataInputStream dataIn;
	private DataOutputStream dataOut;
	
	public boolean reconnect = false;

	public PCCommunicator(NXTInfo nxtInfo) {
		this.nxtInfo = nxtInfo;
	}
	
	public void sendData(int code, float v0) {
		try {
			dataOut.writeInt(code);
			dataOut.writeFloat(v0);
			dataOut.flush();
		} catch (IOException e) {
			System.out.format("Send failed of %d , %f%n", code, v0);
			reconnect = true;
		}
		
	}
	
	public void close() {
		try {
			dataOut.close();
			connector.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean connect() {
		boolean connected = false;
		connector = new NXTConnector();
		do {
			System.out.println(" connecting to " + nxtInfo.name + " " + nxtInfo.deviceAddress);
			connected = connector.connectTo(nxtInfo.name,
				nxtInfo.deviceAddress, NXTCommFactory.BLUETOOTH);
			if(!connected) {
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} while (!connected);
		System.out.println(" connect result " + connected);
		dataOut = new DataOutputStream(connector.getOutputStream());
		return connected;
	}
	
	public void waitForReply() {
		try {
			dataIn.readInt();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
