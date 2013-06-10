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
import com.googlecode.javacv.cpp.opencv_core.CvPoint;
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
	public ArrayList<ObjectOnMap> sortedPorts;
	public ArrayList<Port> sortedUpperPorts;
	public ArrayList<Port> sortedLowerPorts;
	public ArrayList<Box> redBoxes;
	public ArrayList<Box> greenBoxes;
	private Robot[] robotList;

	public Application() {
		ci = new CaptureImage();
		iu = new ImageUtils();
		initializeObjectList();
	}

	private void initializeObjectList() {
		sortedPorts = new ArrayList<ObjectOnMap>();
		sortedUpperPorts = new ArrayList<Port>();
		sortedLowerPorts = new ArrayList<Port>();
		redBoxes = new ArrayList<Box>();
		greenBoxes = new ArrayList<Box>();
		robotList = new Robot[2];
	}

	public void frameProcessing() {

		 grabbedFrame = opencv_core.cvCloneImage(ci.grabImage());

		// below call used for testing purposes
		//grabbedFrame = (IplImage) opencv_highgui.cvLoadImage("correctSetup.jpg");

		resizedFrame = iu.resizeImage(grabbedFrame);
		opencv_core.cvReleaseImage(grabbedFrame);

		thresholdGreen();
		thresholdRed();
		System.out.println(greenBoxes.toString());
		System.out.println(redBoxes.toString());
		// thresholdBlue();
		// thresholdPurple();
		// thresholdYellow();

		findPort();
		iu.drawLine(resizedFrame);

		opencv_core.cvReleaseImage(resizedFrame);
		opencv_core.cvReleaseImage(thresholdedFrame);

		// opencv_core.cvReleaseImage(contoursFrame);

	}

	public void thresholdRed() {
		opencv_core.CvPoint p1 = new opencv_core.CvPoint(0, 0), p2 = new opencv_core.CvPoint(
				0, 0);
		redBoxes.clear();
		thresholdedFrame = iu.thresholdFrame(resizedFrame, Threshold.RED_LOWER,
				Threshold.RED_UPPER);
		redObjects = iu.findContours(thresholdedFrame, resizedFrame);
		try {
			Box tempBox;
			for (; redObjects != null; redObjects = redObjects.h_next()) {
				opencv_core.CvRect sq = opencv_imgproc.cvBoundingRect(
						redObjects, 0);
				if (sq.width() < 6 || sq.height() < 6) {
					continue;
				}
				tempBox= new Box();
				tempBox.setX(sq.x());
				tempBox.setY(sq.y());
				tempBox.setHeight(sq.height());
				tempBox.setWidth(sq.width());
				tempBox.setColor(1); // 1 means red
				redBoxes.add(tempBox);

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
		greenBoxes.clear();
		opencv_core.CvPoint p1 = new opencv_core.CvPoint(0, 0), p2 = new opencv_core.CvPoint(
				0, 0);

		thresholdedFrame = iu.thresholdFrame(resizedFrame,
				Threshold.GREEN_LOWER, Threshold.GREEN_UPPER);
		greenObjects = iu.findContours(thresholdedFrame, resizedFrame);
		try {
			Box tempBox;
			for (; greenObjects != null; greenObjects = greenObjects.h_next()) {
				opencv_core.CvRect sq = opencv_imgproc.cvBoundingRect(
						greenObjects, 0);
				if (sq.width() < 6 || sq.height() < 6) {
					continue;
				}
				tempBox = new Box();
				tempBox.setX(sq.x());
				tempBox.setY(sq.y());
				tempBox.setHeight(sq.height());
				tempBox.setWidth(sq.width());
				tempBox.setColor(2); // 2 means green
				greenBoxes.add(tempBox);

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
		int i = 0;
		
		opencv_core.CvPoint p1 = new opencv_core.CvPoint(0, 0), p2 = new opencv_core.CvPoint(
				0, 0);
		thresholdedFrame = iu.thresholdFrame(resizedFrame,
				Threshold.YELLOW_LOWER, Threshold.YELLOW_UPPER);
		yellowObjects = iu.findContours(thresholdedFrame, resizedFrame);

		try {
			for (i = 0; yellowObjects != null; yellowObjects = yellowObjects
					.h_next()) {
				opencv_core.CvRect sq = opencv_imgproc.cvBoundingRect(
						yellowObjects, 0);
				if (sq.width() < 6 || sq.height() < 6) {
					continue;
				}

//Find closest robot front color to the current yellow point
				double minDistance = 100;
				for (int p = 0; p < robotList.length; p++)
				{
					double x = Math.abs(robotList[p].getMidX() - sq.x());
					double y = Math.abs(robotList[p].getMidY() - sq.y());
					double distance = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
					if (distance < minDistance)
					{
						robotList[i].setBackX(sq.x());
						robotList[i].setBackY(sq.y());
						robotList[i].setBackWidth(sq.width());
						robotList[i].setBackHeight(sq.height());
						minDistance = distance;
					}
				}
				i++;
				
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
			for (; blueObjects != null; blueObjects = blueObjects
					.h_next()) {
				opencv_core.CvRect sq = opencv_imgproc.cvBoundingRect(
						blueObjects, 0);
				if (sq.width() < 6 || sq.height() < 6) {
					continue;
				}
				//color 3 = blue
				robotList[1].setFrontColor(3);
				robotList[1].setFrontX(sq.x());
				robotList[1].setY(sq.y());
				robotList[1].setFrontWidth(sq.width());
				robotList[1].setFrontHeight(sq.height());

				// Below used for debugging
				opencv_core.CvScalar color = opencv_core.CvScalar.BLUE;
				p1.x(sq.x());
				p2.x(sq.x() + sq.width());
				p1.y(sq.y());
				p2.y(sq.y() + sq.height());
				cvRectangle(resizedFrame, p1, p2, CV_RGB(255, 0, 0), 2, 8, 0);
				cvDrawContours(resizedFrame, blueObjects, color,
						CV_RGB(0, 0, 0), -1, CV_FILLED, 8, cvPoint(0, 0));
				break;

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
			for (; purpleObjects != null ; purpleObjects = purpleObjects
					.h_next()) {
				opencv_core.CvRect sq = opencv_imgproc.cvBoundingRect(
						purpleObjects, 0);
				if (sq.width() < 6 || sq.height() < 6) {
					continue;
				}
				//color 4 = purple
				robotList[2].setFrontColor(4);
				robotList[2].setFrontX(sq.x());
				robotList[2].setY(sq.y());
				robotList[2].setFrontWidth(sq.width());
				robotList[2].setFrontHeight(sq.height());

				// Below used for debugging
				opencv_core.CvScalar color = opencv_core.CvScalar.BLUE;
				p1.x(sq.x());
				p2.x(sq.x() + sq.width());
				p1.y(sq.y());
				p2.y(sq.y() + sq.height());
				cvRectangle(resizedFrame, p1, p2, CV_RGB(255, 0, 0), 2, 8, 0);
				cvDrawContours(resizedFrame, purpleObjects, color,
						CV_RGB(0, 0, 0), -1, CV_FILLED, 8, cvPoint(0, 0));
				break;
			}

		} catch (Exception e) {
			System.err.println("No purple contours found");
		}
	}

	public void findPort() {
		sortedUpperPorts.clear();
		sortedLowerPorts.clear();
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
			tempPort.setPairId(i);
			if (tempPort.getMidY() < 150) {
				sortedUpperPorts.add(tempPort);
			}else {
				sortedLowerPorts.add(tempPort);
			}
		}
		for (Port upperPort : sortedUpperPorts) {
			upperPort.toString();
		}
		sortPorts();
	}

	// Sorts the last 6 elements in objectList, in the order we want to visit
	// the ports
	public void sortPorts() {
		Collections.sort(sortedUpperPorts);
		Collections.sort(sortedLowerPorts);
		sortedPorts.clear();
		int i = 0;
		for (Port upperPort : sortedUpperPorts) {
			i++;
			upperPort.setPairId(i);
			sortedPorts.add(upperPort);
		}
		for (Port lowerPort : sortedLowerPorts) {
			i++;
			lowerPort.setPairId(i);
			sortedPorts.add(lowerPort);
		}
	}
}
