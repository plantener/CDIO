package dk.dtu.cdio.ANIMAL.computer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTConnector;
import lejos.pc.comm.NXTInfo;

public class PCCommunicator {

	private NXTInfo nxtInfo;
	private NXTConnector connector;
	private DataInputStream dataIn;
	private DataOutputStream dataOut;
	public Reader reader;

	public PCCommunicator(NXTInfo nxtInfo) {
		this.nxtInfo = nxtInfo;
		reader = new Reader();
	}
	
	public void testLatency(){
		long start = System.currentTimeMillis();
		sendData(NavCommand.LATENCY_TEST.ordinal(), 0, 0, 0, false);
		try {
			dataIn.readInt();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long end = System.currentTimeMillis();
		System.out.println(end-start);
	}
	
	public void sendData(int code, float v0, float v1, float v2, boolean bit) {
		try {
			dataOut.writeInt(code);
			dataOut.writeFloat(v0);
			dataOut.writeFloat(v1);
			dataOut.writeFloat(v2);
			dataOut.writeBoolean(bit);
			dataOut.flush();
			System.out.println("sent " + NavCommand.values()[code] + " " + v0
					+ " " + v1);
		} catch (IOException e) {
			System.out.println(" send failed ");
		}
		
		waitForReply();
	}

	public boolean connect() {
		System.out.println(" connecting to " + nxtInfo.name + " "
				+ nxtInfo.deviceAddress);
		connector = new NXTConnector();
		boolean connected = connector.connectTo(nxtInfo.name,
				nxtInfo.deviceAddress, NXTCommFactory.BLUETOOTH);
		System.out.println(" connect result " + connected);
		if (!connected) {
			return connected;
		}
		dataIn = new DataInputStream(connector.getInputStream());
		dataOut = new DataOutputStream(connector.getOutputStream());
		if (dataIn == null) {
			connected = false;
			return connected;
		}
//		if (!reader.isRunning) {
//			reader.start();
//		}
		return connected;
	}
	
	public void waitForReply() {
		try {
			dataIn.readInt();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	class Reader extends Thread {
		
		public boolean isRunning = false;
		public AtomicBoolean replyReady = new AtomicBoolean(false);
		public NavCommand reply;
		
		public void run() {
			int incoming;
			isRunning = true;
			while(isRunning) {
				try {
					incoming = dataIn.readInt();
					reply = NavCommand.values()[incoming];
					replyReady.set(true);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
