package main;

import static com.googlecode.javacv.cpp.opencv_core.CV_FILLED;
import static com.googlecode.javacv.cpp.opencv_core.CV_RGB;
import static com.googlecode.javacv.cpp.opencv_core.cvDrawContours;
import static com.googlecode.javacv.cpp.opencv_core.cvPoint;
import static com.googlecode.javacv.cpp.opencv_core.cvRectangle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import models.*;
import utilities.ImageUtils;
import utilities.Threshold;
import com.googlecode.javacv.CanvasFrame;
import com.googlecode.javacv.cpp.opencv_core;
import com.googlecode.javacv.cpp.opencv_core.CvSeq;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import com.googlecode.javacv.cpp.opencv_highgui;
import com.googlecode.javacv.cpp.opencv_imgproc;

public class Application {
	private CaptureImage ci;
	private ImageUtils iu;
	private IplImage grabbedFrame;
	private IplImage resizedFrame;
	private IplImage thresholdedFrame;
	private IplImage contoursFrame;
	private CvSeq redObjects;
	private CvSeq greenObjects;
	public ObjectOnMap[] objectList;
	Box box = new Box();
	private CvSeq yellowObjects;
	private CvSeq blueObjects;
	private CvSeq purpleObjects;
	private ObjectOnMap[] sortedPorts;
	private ArrayList<Port> sortedUpperPorts;
	private ArrayList<Port> sortedLowerPorts;

	public Application() {
		ci = new CaptureImage();
		iu = new ImageUtils();
		initializeObjectList();
	}

	private void initializeObjectList() {
		sortedPorts = new ObjectOnMap[6];
		sortedUpperPorts = new ArrayList<Port>();
		sortedLowerPorts = new ArrayList<Port>();
		objectList = new ObjectOnMap[20];
		objectList[0] = new Box(); // redbox
		objectList[1] = new Box(); // redbox
		objectList[2] = new Box(); // redbox
		objectList[3] = new Box(); // redbox
		objectList[4] = new Box(); // redbox
		objectList[5] = new Box(); // redbox
		objectList[6] = new Box(); // greenbox
		objectList[7] = new Box(); // greenbox
		objectList[8] = new Box(); // greenbox
		objectList[9] = new Box(); // greenbox
		objectList[10] = new Box(); // greenbox
		objectList[11] = new Box(); // greenbox
		objectList[12] = new Robot(); // Robot1
		objectList[13] = new Robot(); // Robot1
	}

	public void frameProcessing() {

		//grabbedFrame = opencv_core.cvCloneImage(ci.grabImage());

		// below call used for testing purposes
		grabbedFrame = (IplImage) opencv_highgui.cvLoadImage("nolightmap.jpg");

		resizedFrame = iu.resizeImage(grabbedFrame);
		opencv_core.cvReleaseImage(grabbedFrame);

		thresholdGreen();
		thresholdRed();
		//thresholdYellow();
		//thresholdBlue();
		//thresholdPurple();

		findPort();
		iu.drawLine(resizedFrame);

		// Prints the objectList
		for (int i = 0; i < objectList.length; i++) {
			if (i <= 11) {
				System.out.println(objectList[i].toString());
			}
		}

		// prints all ports
		for (int i = 14; i < objectList.length; i++) {
			System.out.println(objectList[i].toString());
		}

		opencv_core.cvReleaseImage(resizedFrame);
		opencv_core.cvReleaseImage(thresholdedFrame);

		// opencv_core.cvReleaseImage(contoursFrame);

	}

	public void thresholdRed() {
		opencv_core.CvPoint p1 = new opencv_core.CvPoint(0, 0), p2 = new opencv_core.CvPoint(
				0, 0);

		thresholdedFrame = iu.thresholdFrame(resizedFrame, Threshold.RED_LOWER,
				Threshold.RED_UPPER);
		redObjects = iu.findContours(thresholdedFrame, resizedFrame);
		try {
			for (int i = 0; redObjects != null && i < 6; redObjects = redObjects
					.h_next()) {
				opencv_core.CvRect sq = opencv_imgproc.cvBoundingRect(
						redObjects, 0);
				if (sq.width() < 6 || sq.height() < 6) {
					continue;
				}
				if (objectList[i].getClass().isInstance(box)) {
					((Box) objectList[i]).setX(sq.x());
					((Box) objectList[i]).setY(sq.y());
					((Box) objectList[i]).setHeight(sq.height());
					((Box) objectList[i]).setWidth(sq.width());
					((Box) objectList[i]).setColor(1); // 1 means red
				}
				i++;

				// Used for debugging
				System.out.println("Y er: " + sq.y());
				System.out.println("X er: " + sq.x());
				System.out.println("Højde er: " + sq.height());
				System.out.println("Bredde er: " + sq.width());
				System.out.println("\n");

				// Below used for debugging
				opencv_core.CvScalar color = opencv_core.CvScalar.BLUE;
				p1.x(sq.x());
				p2.x(sq.x() + sq.width());
				p1.y(sq.y());
				p2.y(sq.y() + sq.height());
				cvRectangle(resizedFrame, p1, p2, CV_RGB(255, 0, 0), 2, 8, 0);
				cvDrawContours(resizedFrame, redObjects, color,
						CV_RGB(0, 0, 0), -1, CV_FILLED, 8, cvPoint(0, 0));

			}

		} catch (Exception e) {
			System.err.println("No red contours found");
		}

	}

	public void thresholdGreen() {
		opencv_core.CvPoint p1 = new opencv_core.CvPoint(0, 0), p2 = new opencv_core.CvPoint(
				0, 0);

		thresholdedFrame = iu.thresholdFrame(resizedFrame,
				Threshold.GREEN_LOWER, Threshold.GREEN_UPPER);
		greenObjects = iu.findContours(thresholdedFrame, resizedFrame);
		try {
			for (int i = 6; greenObjects != null && i < 12; greenObjects = greenObjects
					.h_next()) {
				opencv_core.CvRect sq = opencv_imgproc.cvBoundingRect(
						greenObjects, 0);
				if (sq.width() < 6 || sq.height() < 6) {
					continue;
				}
				if (objectList[i].getClass().isInstance(box)) {
					((Box) objectList[i]).setX(sq.x());
					((Box) objectList[i]).setY(sq.y());
					((Box) objectList[i]).setHeight(sq.height());
					((Box) objectList[i]).setWidth(sq.width());
					((Box) objectList[i]).setColor(2); // 2 means green

				}
				i++;

				// Used for debugging
				System.out.println("Y er: " + sq.y());
				System.out.println("X er: " + sq.x());
				System.out.println("Højde er: " + sq.height());
				System.out.println("Bredde er: " + sq.width());
				System.out.println("\n");

				// Below used for debugging
				opencv_core.CvScalar color = opencv_core.CvScalar.BLUE;
				p1.x(sq.x());
				p2.x(sq.x() + sq.width());
				p1.y(sq.y());
				p2.y(sq.y() + sq.height());
				cvRectangle(resizedFrame, p1, p2, CV_RGB(255, 0, 0), 2, 8, 0);
				cvDrawContours(resizedFrame, greenObjects, color,
						CV_RGB(0, 0, 0), -1, CV_FILLED, 8, cvPoint(0, 0));

			}
		} catch (Exception e) {
			System.err.println("No green contours found");
		}

	}

	public void thresholdYellow() {
		opencv_core.CvPoint p1 = new opencv_core.CvPoint(0, 0), p2 = new opencv_core.CvPoint(
				0, 0);
		thresholdedFrame = iu.thresholdFrame(resizedFrame,
				Threshold.YELLOW_LOWER, Threshold.YELLOW_UPPER);
		yellowObjects = iu.findContours(thresholdedFrame, resizedFrame);

		try {
			for (int i = 0; yellowObjects != null && i < 6; yellowObjects = yellowObjects
					.h_next()) {
				opencv_core.CvRect sq = opencv_imgproc.cvBoundingRect(
						yellowObjects, 0);
				if (sq.width() < 6 || sq.height() < 6) {
					continue;
				}

				// Used for debugging
				System.out.println("Y er: " + sq.y());
				System.out.println("X er: " + sq.x());
				System.out.println("Højde er: " + sq.height());
				System.out.println("Bredde er: " + sq.width());
				System.out.println("\n");

				// Below used for debugging
				opencv_core.CvScalar color = opencv_core.CvScalar.BLUE;
				p1.x(sq.x());
				p2.x(sq.x() + sq.width());
				p1.y(sq.y());
				p2.y(sq.y() + sq.height());
				cvRectangle(resizedFrame, p1, p2, CV_RGB(255, 0, 0), 2, 8, 0);
				cvDrawContours(resizedFrame, yellowObjects, color,
						CV_RGB(0, 0, 0), -1, CV_FILLED, 8, cvPoint(0, 0));

			}

		} catch (Exception e) {
			System.err.println("No yellow contours found");
		}
	}

	public void thresholdBlue() {
		opencv_core.CvPoint p1 = new opencv_core.CvPoint(0, 0), p2 = new opencv_core.CvPoint(
				0, 0);
		thresholdedFrame = iu.thresholdFrame(resizedFrame,
				Threshold.BLUE_LOWER, Threshold.BLUE_UPPER);
		blueObjects = iu.findContours(thresholdedFrame, resizedFrame);

		try {
			for (int i = 0; blueObjects != null && i < 6; blueObjects = blueObjects
					.h_next()) {
				opencv_core.CvRect sq = opencv_imgproc.cvBoundingRect(
						blueObjects, 0);
				if (sq.width() < 6 || sq.height() < 6) {
					continue;
				}

				// Used for debugging
				System.out.println("Y er: " + sq.y());
				System.out.println("X er: " + sq.x());
				System.out.println("Højde er: " + sq.height());
				System.out.println("Bredde er: " + sq.width());
				System.out.println("\n");

				// Below used for debugging
				opencv_core.CvScalar color = opencv_core.CvScalar.BLUE;
				p1.x(sq.x());
				p2.x(sq.x() + sq.width());
				p1.y(sq.y());
				p2.y(sq.y() + sq.height());
				cvRectangle(resizedFrame, p1, p2, CV_RGB(255, 0, 0), 2, 8, 0);
				cvDrawContours(resizedFrame, blueObjects, color,
						CV_RGB(0, 0, 0), -1, CV_FILLED, 8, cvPoint(0, 0));

			}

		} catch (Exception e) {
			System.err.println("No blue contours found");
		}
	}

	public void thresholdPurple() {
		opencv_core.CvPoint p1 = new opencv_core.CvPoint(0, 0), p2 = new opencv_core.CvPoint(
				0, 0);
		thresholdedFrame = iu.thresholdFrame(resizedFrame,
				Threshold.PURPLE_LOWER, Threshold.PURPLE_UPPER);
		purpleObjects = iu.findContours(thresholdedFrame, resizedFrame);

		try {
			for (int i = 0; purpleObjects != null && i < 6; purpleObjects = purpleObjects
					.h_next()) {
				opencv_core.CvRect sq = opencv_imgproc.cvBoundingRect(
						purpleObjects, 0);
				if (sq.width() < 6 || sq.height() < 6) {
					continue;
				}

				// Used for debugging
				System.out.println("Y er: " + sq.y());
				System.out.println("X er: " + sq.x());
				System.out.println("Højde er: " + sq.height());
				System.out.println("Bredde er: " + sq.width());
				System.out.println("\n");

				// Below used for debugging
				opencv_core.CvScalar color = opencv_core.CvScalar.BLUE;
				p1.x(sq.x());
				p2.x(sq.x() + sq.width());
				p1.y(sq.y());
				p2.y(sq.y() + sq.height());
				cvRectangle(resizedFrame, p1, p2, CV_RGB(255, 0, 0), 2, 8, 0);
				cvDrawContours(resizedFrame, purpleObjects, color,
						CV_RGB(0, 0, 0), -1, CV_FILLED, 8, cvPoint(0, 0));

			}

		} catch (Exception e) {
			System.err.println("No purple contours found");
		}
	}

	public void findPort() {
		sortedUpperPorts.clear();
		sortedLowerPorts.clear();
		for (int i = 0; i < 6; i++) {
			int redMidX = 0;
			int redMidY = 0;
			int tempJ = 0;
			double distance = 200;
//			if (objectList[i].getClass().isInstance(box)) {
				redMidX = ((Box) objectList[i]).getMidX();
				redMidY = ((Box) objectList[i]).getMidY();
				for (int j = 6; j < 12; j++) {
					int greenMidX = 0;
					int greenMidY = 0;
//					if (objectList[j].getClass().isInstance(box)) {
						greenMidX = ((Box) objectList[j]).getMidX();
						greenMidY = ((Box) objectList[j]).getMidY();
						int xDifference = Math.abs(redMidX - greenMidX);
						int yDifference = Math.abs(redMidY - greenMidY);
						// pythagoras
						double portDistance = Math
								.sqrt(Math.pow(xDifference, 2)
										+ Math.pow(yDifference, 2));
						if (portDistance < distance && portDistance > 35) {
							distance = portDistance;
							tempJ = j;
						}
//					}
				}
				Port tempPort= new Port(((Box) objectList[i]),
						((Box) objectList[tempJ]));
				tempPort.setPairId(i +1);
				if(tempPort.getMidY() < 150){
					sortedUpperPorts.add(tempPort);		
				}else {
					sortedLowerPorts.add(tempPort);
				}
//			}
		}

		sortPorts();
	}
	//Sorts the last 6 elements in objectList, in the order we want to visit the ports
	public void sortPorts(){
		int count = 0;

		Collections.sort(sortedUpperPorts);
		Collections.sort(sortedLowerPorts);

		for (int i = 0; i < sortedUpperPorts.size(); i++) {
			objectList[i+14] = sortedUpperPorts.get(i);	
		}

		for (int i = 14 + sortedUpperPorts.size() ; count < sortedLowerPorts.size(); i++) {
			objectList[i] = sortedLowerPorts.get(count);
				count++;
			}

		}
	}

