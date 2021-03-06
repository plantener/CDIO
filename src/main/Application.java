package main;

import static com.googlecode.javacv.cpp.opencv_core.CV_FILLED;
import static com.googlecode.javacv.cpp.opencv_core.CV_RGB;
import static com.googlecode.javacv.cpp.opencv_core.cvDrawContours;
import static com.googlecode.javacv.cpp.opencv_core.cvPoint;
import static com.googlecode.javacv.cpp.opencv_core.cvRectangle;

import gui.ControlWindow;

import java.util.ArrayList;
import java.util.Collections;

import models.Box;
import models.ObjectOnMap;
import models.Port;
import models.Robot;
import utilities.ImageUtils;

import com.googlecode.javacv.cpp.opencv_core;
import com.googlecode.javacv.cpp.opencv_core.CvSeq;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import com.googlecode.javacv.cpp.opencv_highgui;
import com.googlecode.javacv.cpp.opencv_imgproc;

public class Application {
	private CaptureImage ci;
	private ImageUtils iu;
	private IplImage grabbedFrame;
	public static IplImage resizedFrame;
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
	private int contourCount;
	private boolean purpleFound;
	private boolean blueFound;
	private boolean yellowFound;
	
	private static long last = 0;
	private static boolean[] foundColor = new boolean[5];
	
	public static boolean robotsDetected = false;
	public static final int SQ_THRESHOLD = 6;
	
	// Color definitions
	public static final int RED = 0;
	public static final int GREEN = 1;
	public static final int BLUE = 3;
	public static final int PURPLE = 4;
	public static final int YELLOW = 2;
	
	// Color attribute definitions
	public static final int HUE = 0;
	public static final int UPPERHUE = 1;
	public static final int SATURATION = 2;
	public static final int VALUE = 3;
	
	public static int[][] THRESHOLDS = new int[5][4];
	
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
		for (int i = 0; i < foundColor.length; i++) {
			foundColor[i] = false;
		}
	}

	public void frameProcessing() {

//		 grabbedFrame = opencv_core.cvCloneImage(ci.grabImage());

		// below call used for testing purposes
		grabbedFrame = (IplImage) opencv_highgui.cvLoadImage("Lib6.jpg");

		 
		resizedFrame = iu.resizeImage(grabbedFrame);
		opencv_core.cvReleaseImage(grabbedFrame);
		
		
		if(Main.READY || Main.DRAW_ALL) {
			greenBoxes.clear();
			redBoxes.clear();
			purpleFound = false;
			blueFound = false;
			yellowFound = false;
			
			if (yellowFound == false || purpleFound == false || blueFound == false){
				robotsDetected = false;
			}else{
				robotsDetected = true;
			}
		
			thresholdColour(RED);
			thresholdColour(GREEN);
			thresholdColour(BLUE);
			thresholdColour(PURPLE);
			thresholdColour(YELLOW);
		} else {
			thresholdColour(ControlWindow.SELECTED_COLOR);
		}

		//Set variable in Main class. Prints info on both robots
		if(Main.DEBUG_ROBOT == 1){
			System.out.format("A FX: %3d, FY: %3d     B FX: %3d, FY: %3d%n",  robotA.getFrontMidX(), robotA.getFrontmidY(),robotB.getFrontMidX(), robotB.getFrontmidY());
			System.out.format("A BX: %3d, BY: %3d     B BX: %3d, BY: %3d%n%n",  robotA.getBackMidX(), robotA.getBackMidY(), robotB.getBackMidX(), robotB.getBackMidY());
		}

		if(Main.DRAW_ALL || Main.READY) {
			findPort();
			iu.drawText(resizedFrame, sortedPorts);
			iu.drawLine(resizedFrame);
		}
		
		iu.showImage(resizedFrame);

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
	
	public void thresholdColour(int colour){
		opencv_core.CvPoint p1 = new opencv_core.CvPoint(0, 0), p2 = new opencv_core.CvPoint(
				0, 0);

		thresholdedFrame = iu.thresholdFrame(resizedFrame, colour);
		contours = iu.findContours(thresholdedFrame, resizedFrame);
		try {
			foundColor[colour] = !contours.isNull();
			if(!foundColor[colour]) {
				if(System.currentTimeMillis() - last > 1000) {
					System.out.print("CANT DETECT: ");
					for (int i = 0; i < foundColor.length; i++) {
						if (!foundColor[i]) {
							System.out.print(colorName(i) + ", ");
						}
					}
					System.out.println();
					last = System.currentTimeMillis();
				}
				return;
			}
			
			for (; contours != null 
					&& !contours.isNull();
					contours = contours.h_next()) {
				sq = opencv_imgproc.cvBoundingRect(contours, 0);
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
				opencv_core.cvCircle(resizedFrame, new opencv_core.CvPoint(sq.x() + sq.width() / 2, sq.y() + sq.height() / 2), 20, opencv_core.CvScalar.BLACK , 1, 8, 0);
				cvRectangle(resizedFrame, p1, p2, CV_RGB(0, 0, 0), 2, 8, 0);
				cvDrawContours(resizedFrame, contours, drawColor,
						CV_RGB(0, 0, 0), -1, CV_FILLED, 8, cvPoint(0, 0));
			}
			contourCount = 0;
			
		} catch (Exception e) {
			System.err.println("Error occured thresholding " + colorName(colour));
			e.printStackTrace();
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
			blueFound = true;
			robotList[0].setRobotId("a");
			robotList[0].setFrontX(sq.x());
			robotList[0].setFrontY(sq.y());
			robotList[0].setFrontWidth(sq.width());
			robotList[0].setFrontHeight(sq.height());
			break;

		case PURPLE:
			purpleFound = true;
			robotList[1].setRobotId("b");
			robotList[1].setFrontX(sq.x());
			robotList[1].setFrontY(sq.y());
			robotList[1].setFrontWidth(sq.width());
			robotList[1].setFrontHeight(sq.height());
			break;

		case YELLOW:
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
	
	public static String colorName(int color) {

		switch (color) {
		case RED:
			return "RED";
		case BLUE:
			return "BLUE";
		case YELLOW:
			return "YELLOW";
		case GREEN:
			return "GREEN";
		case PURPLE:
			return "PURPLE";
		default:
			return "Unknown color";
		}
	}

}
