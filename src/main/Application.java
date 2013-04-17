package main;

import static com.googlecode.javacv.cpp.opencv_core.CV_FILLED;
import static com.googlecode.javacv.cpp.opencv_core.CV_RGB;
import static com.googlecode.javacv.cpp.opencv_core.cvDrawContours;
import static com.googlecode.javacv.cpp.opencv_core.cvPoint;
import static com.googlecode.javacv.cpp.opencv_core.cvRectangle;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Observer;

import models.*;
import utilities.ImageUtils;
import utilities.Threshold;
import com.googlecode.javacv.CanvasFrame;
import com.googlecode.javacv.cpp.opencv_core;
import com.googlecode.javacv.cpp.opencv_core.CvSeq;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import com.googlecode.javacv.cpp.opencv_highgui;
import com.googlecode.javacv.cpp.opencv_core.CvSeq;
import com.googlecode.javacv.cpp.opencv_imgproc;

public class Application {
	private CaptureImage ci;
	private ImageUtils iu;
	private CanvasFrame canvasContours;
	private IplImage grabbedFrame;
	private IplImage resizedFrame;
	private IplImage thresholdedFrame;
	private IplImage contoursFrame;
	private CvSeq redObjects;
	private CvSeq greenObjects;
	private ObjectOnMap[] objectList;
	Box box = new Box();

	public Application() {
		ci = new CaptureImage();
		iu = new ImageUtils();
		canvasContours = new CanvasFrame("contours");
		initializeObjectList();

	}

	private void initializeObjectList() {
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

		// grabbedFrame = opencv_core.cvCloneImage(ci.grabImage());

		// below call used for testing purposes
		grabbedFrame = (IplImage) opencv_highgui.cvLoadImage("nolightmap.jpg");

		resizedFrame = iu.resizeImage(grabbedFrame);
		opencv_core.cvReleaseImage(grabbedFrame);

		thresholdGreen();
		thresholdRed();

		canvasContours.showImage(resizedFrame);

		// Prints the objectList
		for (int i = 0; i < objectList.length; i++) {
			if (i<=11) {
				System.out.println(objectList[i].toString());
			}
		}

		opencv_core.cvReleaseImage(resizedFrame);
		opencv_core.cvReleaseImage(thresholdedFrame);

		//opencv_core.cvReleaseImage(contoursFrame);


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
			for (int i = 6; greenObjects != null && i <= 11; greenObjects = greenObjects
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
}
