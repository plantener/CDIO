package main;

import static com.googlecode.javacv.cpp.opencv_core.CV_FILLED;
import static com.googlecode.javacv.cpp.opencv_core.CV_RGB;
import static com.googlecode.javacv.cpp.opencv_core.cvDrawContours;
import static com.googlecode.javacv.cpp.opencv_core.cvPoint;
import static com.googlecode.javacv.cpp.opencv_core.cvRectangle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import models.Box;
import models.ObjectOnMap;
import models.Port;
import models.Robot;
import utilities.ImageUtils;
import utilities.THold;

import com.googlecode.javacv.cpp.opencv_core;
import com.googlecode.javacv.cpp.opencv_core.CvSeq;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import com.googlecode.javacv.cpp.opencv_highgui;
import com.googlecode.javacv.cpp.opencv_imgproc;

import dk.dtu.cdio.ANIMAL.computer.Utilities;

public class Application {
	private CaptureImage ci;
	private ImageUtils iu;
	private IplImage grabbedFrame;
	public IplImage resizedFrame;
	private IplImage thresholdedFrame;
	private IplImage contoursFrame;
	public ObjectOnMap[] objectList;
	Box box = new Box();
	public ArrayList<Port> sortedPorts;
	public ArrayList<Box> redBoxes;
	public ArrayList<Box> greenBoxes;
	private Robot[] robotList;
	public Robot robotA;
	public Robot robotB;
	private CvSeq contours;
	private opencv_core.CvRect sq;
	private boolean calibrateRobot = false;
	private int contourCount;
	private boolean purpleFound;
	private boolean blueFound;
	private boolean yellowFound;
	
	private static final int PORTS_TO_BE_FOUND = 6;
	
	public static THold GREEN_UPPER, GREEN_LOWER, RED_UPPER, RED_LOWER, YELLOW_UPPER, YELLOW_LOWER,
	BLUE_UPPER, BLUE_LOWER, PURPLE_UPPER, PURPLE_LOWER;
	
	public int yellowAmount=0, blueAmount=0, purpleAmount=0;
	
	public static boolean robotsDetected = false;
	public static final int SQ_THRESHOLD = 6;
	public static final int RED = 1;
	public static final int GREEN = 2;
	public static final int BLUE = 3;
	public static final int PURPLE = 4;
	public static final int YELLOW = 5;

	public Application() {
		ci = new CaptureImage();
		iu = new ImageUtils();
		initializeObjectList();
		
	}

	private void initializeObjectList() {
		sortedPorts = new ArrayList<Port>();
		redBoxes = new ArrayList<Box>();
		greenBoxes = new ArrayList<Box>();
		robotList = new Robot[2];
		robotList[0] = new Robot();
		robotList[1] = new Robot();
		robotA = robotList[0];
		robotB = robotList[1];
		
		initThresholds();
		
		int variation = 20;
		boolean upperDescending = false, lowerDescending = false;
		frameProcessing();
//		while(sortedPorts.size() != PORTS_TO_BE_FOUND) {
//			frameProcessing();
//		}
		Scanner sc = new Scanner(System.in);
		sc.nextLine();
		for(int ui = YELLOW_UPPER.b, li = YELLOW_LOWER.b;
			yellowAmount != 2;
			YELLOW_UPPER.b += (upperDescending) ? -1 : 1,
			YELLOW_LOWER.b += (lowerDescending) ? -1 : 1) {
			System.out.format("Yellow: [upper %d (%d), lower %d (%d)]%n", YELLOW_UPPER.b, ui, YELLOW_LOWER.b, li);
			
			frameProcessing();
			if(Math.abs(ui - YELLOW_UPPER.b) == variation)
				upperDescending = !upperDescending;
			if(Math.abs(li - YELLOW_LOWER.b) == variation)
				lowerDescending = !lowerDescending;
			try {
				Thread.sleep(25);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		upperDescending = false; lowerDescending = false;
		for(int ui = PURPLE_UPPER.b, li = PURPLE_LOWER.b;
			purpleAmount != 1;
			PURPLE_UPPER.b += (upperDescending) ? -1 : 1,
			PURPLE_LOWER.b += (lowerDescending) ? -1 : 1) {
			System.out.format("Purple: upper %d lower %d%n", PURPLE_UPPER.b, PURPLE_LOWER.b);
			
			frameProcessing();
			if(Math.abs(ui - PURPLE_UPPER.b) == variation)
				upperDescending = !upperDescending;
			if(Math.abs(li - PURPLE_LOWER.b) == variation)
				lowerDescending = !lowerDescending;
			try {
				Thread.sleep(25);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		upperDescending = false; lowerDescending = false;
		for(int ui = BLUE_UPPER.b, li = BLUE_LOWER.b;
			blueAmount != 1;
			BLUE_UPPER.b += (upperDescending) ? -1 : 1,
			BLUE_LOWER.b += (lowerDescending) ? -1 : 1) {
			
			System.out.format("BLUE: upper %d lower %d%n", BLUE_UPPER.b, BLUE_LOWER.b);
			
			frameProcessing();
			if(Math.abs(ui - BLUE_UPPER.b) == variation)
				upperDescending = !upperDescending;
			if(Math.abs(li - BLUE_LOWER.b) == variation)
				lowerDescending = !lowerDescending;
			try {
				Thread.sleep(25);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private void initThresholds() {
		GREEN_UPPER = new THold(60 ,255,255);
		GREEN_LOWER = new THold(38 ,80,70 );
		
		YELLOW_LOWER = new THold(24 ,100 ,100 );
		YELLOW_UPPER = new THold(32,255,255); //teal
		
		BLUE_LOWER = new THold (90,55 ,60 );
		BLUE_UPPER= new THold  (100,255,255);
		
		PURPLE_LOWER= new THold(130,70 ,70 );
		PURPLE_UPPER= new THold(165,255,255);
		
		RED_LOWER  = new THold (167,100,75 );
		RED_UPPER  = new THold (180,255,255);
		
	}

	public void frameProcessing() {

		 grabbedFrame = opencv_core.cvCloneImage(ci.grabImage());

		// below call used for testing purposes
//		grabbedFrame = (IplImage) opencv_highgui.cvLoadImage("Lib6.jpg");
		 
		resizedFrame = iu.resizeImage(grabbedFrame);
		opencv_core.cvReleaseImage(grabbedFrame);
		
//		if(!calibrateRobot){
//		iu.calibrateRobot(Threshold.YELLOW_LOWER, Threshold.YELLOW_UPPER, resizedFrame);
//		calibrateRobot = true;
//		}
		
		greenBoxes.clear();
		redBoxes.clear();
		purpleFound = false;
		blueFound = false;
		yellowFound = false;
		
		yellowAmount = blueAmount = purpleAmount = 0;
		
//		thresholdColour(RED_LOWER, RED_UPPER, RED);
//		thresholdColour(GREEN_LOWER, GREEN_UPPER, GREEN);
		thresholdColour(BLUE_LOWER, BLUE_UPPER, BLUE);
		thresholdColour(PURPLE_LOWER, PURPLE_UPPER, PURPLE);
		thresholdColour(YELLOW_LOWER, YELLOW_UPPER, YELLOW);
//		
		if (yellowFound == false || purpleFound == false || blueFound == false){
			robotsDetected = false;
		}else{
			robotsDetected = true;
		}

		//Set variable in Main class. Prints info on both robots
		if(Main.DEBUG_ROBOT == 1){
			System.out.format("A FX: %3d, FY: %3d     B FX: %3d, FY: %3d%n",  robotA.getFrontMidX(), robotA.getFrontmidY(),robotB.getFrontMidX(), robotB.getFrontmidY());
			System.out.format("A BX: %3d, BY: %3d     B BX: %3d, BY: %3d%n%n",  robotA.getBackMidX(), robotA.getBackMidY(), robotB.getBackMidX(), robotB.getBackMidY());
//			System.out.println("Robot A information:");
//			System.out.println("Front X: " + robotA.getFrontX());
//			System.out.println("Front Y: " + robotA.getFrontY());
//			System.out.println("Back X: " + robotA.getBackX());
//			System.out.println("Back Y: " + robotA.getBackY()+"\n");
//			System.out.println("Robot B information:");
//			System.out.println("Front X: " + robotB.getFrontX());
//			System.out.println("Front Y: " + robotB.getFrontY());
//			System.out.println("Back X: " + robotB.getBackX());
//			System.out.println("Back Y: " + robotB.getBackY()+"\n");
		}

		findPort();
		iu.drawText(resizedFrame, sortedPorts);
		iu.drawLine(resizedFrame);

		opencv_core.cvReleaseImage(resizedFrame);
		opencv_core.cvReleaseImage(thresholdedFrame);

	}


	public void findPort() {
		sortedPorts.clear();
		Box tempGreenBox = new Box();
		int i = 0;
		for (Box redBox : redBoxes) {
			i++;
			int redMidX = 0;
			int redMidY = 0;
			double distance = 200;
			redMidX = redBox.getMidX();
			redMidY = redBox.getMidY();
			for (Box greenBox : greenBoxes) {
				int greenMidX = greenBox.getMidX();
				int greenMidY = greenBox.getMidY();
				int xDifference = Math.abs(redMidX - greenMidX);
				int yDifference = Math.abs(redMidY - greenMidY);
				//pythagoras
				double portDistance = Math.sqrt(Math.pow(xDifference, 2)+ Math.pow(yDifference, 2));
				if(portDistance < distance && portDistance > 35){
					distance = portDistance;
					tempGreenBox = greenBox;
				}
			}
			Port tempPort = new Port(redBox,tempGreenBox);
			sortedPorts.add(tempPort);
		}
		sortPorts();
	}

	//Sort all elements in the sortedPorts array using their relative angle to the centre.
	public void sortPorts() {
		Collections.sort(sortedPorts);
		int i = 0;
		for (Port port : sortedPorts) {
			i++;
			port.setPairId(i);
		}
	}
	public void thresholdColour(THold lowerThreshold, THold upperThreshold, int colour){
		opencv_core.CvPoint p1 = new opencv_core.CvPoint(0, 0), p2 = new opencv_core.CvPoint(
				0, 0);

		thresholdedFrame = iu.thresholdFrame(resizedFrame,
				lowerThreshold, upperThreshold);
		contours = iu.findContours(thresholdedFrame, resizedFrame);
		try {
			for (;contours != null; contours = contours.h_next()) {
			 sq = opencv_imgproc.cvBoundingRect(
					contours, 0);
				if (sq.width() < SQ_THRESHOLD || sq.height() < SQ_THRESHOLD) {
					continue;
				}
				colorOperation(colour);
				
				//Update displayed image with threshold information
				opencv_core.CvScalar drawColor = opencv_core.CvScalar.BLACK;
				p1.x(sq.x());
				p2.x(sq.x() + sq.width());
				p1.y(sq.y());
				p2.y(sq.y() + sq.height());
				cvRectangle(resizedFrame, p1, p2, CV_RGB(0, 0, 0), 2, 8, 0);
				cvDrawContours(resizedFrame, contours, drawColor,
						CV_RGB(0, 0, 0), -1, CV_FILLED, 8, cvPoint(0, 0));
			}
			contourCount = 0;

		} catch (Exception e) {
			System.err.println("Error occured thresholding " + colour);
		}
	}
	public void colorOperation(int colour){
		Box tempBox;
		switch (colour) {
		case RED:

			tempBox = new Box();
			tempBox.setX(sq.x());
			tempBox.setY(sq.y());
			tempBox.setHeight(sq.height());
			tempBox.setWidth(sq.width());
			tempBox.setColor(RED); 
			redBoxes.add(tempBox);
			break;

		case GREEN:	

			tempBox = new Box();
			tempBox.setX(sq.x());
			tempBox.setY(sq.y());
			tempBox.setHeight(sq.height());
			tempBox.setWidth(sq.width());
			tempBox.setColor(GREEN); 			
			greenBoxes.add(tempBox);		
			break;

		case BLUE:
//			if(Utilities.getDistance(robotA.getBackX(), robotA.getBackY(), sq.x(), sq.y()) < 25)
				blueAmount++;
			blueFound = true;
			robotList[0].setRobotId("a");
			robotList[0].setFrontX(sq.x());
			robotList[0].setFrontY(sq.y());
			robotList[0].setFrontWidth(sq.width());
			robotList[0].setFrontHeight(sq.height());
			break;

		case PURPLE:
//			if(Utilities.getDistance(robotB.getBackX(), robotB.getBackY(), sq.x(), sq.y()) < 25)
				purpleAmount++;
			purpleFound = true;
			robotList[1].setRobotId("b");
			robotList[1].setFrontX(sq.x());
			robotList[1].setFrontY(sq.y());
			robotList[1].setFrontWidth(sq.width());
			robotList[1].setFrontHeight(sq.height());
			break;

		case YELLOW:
			yellowAmount++;
			contourCount++;
			
			if (contourCount == 2){
				yellowFound = true;
			}
			//Find closest robot front color to the current yellow point
			double minDistance = Double.MAX_VALUE;
			int robot = 0;
			for (int p = 0; p < robotList.length; p++)	
			{
				double x = Math.abs(robotList[p].getFrontMidX() - (sq.x()+sq.width()/2));
				double y = Math.abs(robotList[p].getFrontmidY() - (sq.y()+sq.height()/2));
				double distance = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
				if(distance < minDistance)
				{
					minDistance = distance;
					robot = p;
				}
			}
			robotList[robot].setBackX(sq.x());
			robotList[robot].setBackY(sq.y());
			robotList[robot].setBackWidth(sq.width());
			robotList[robot].setBackHeight(sq.height());

			break;

		default:

			break;
		}
	}

}
