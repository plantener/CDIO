package main;

import static com.googlecode.javacv.cpp.opencv_core.CV_FILLED;
import static com.googlecode.javacv.cpp.opencv_core.CV_RGB;
import static com.googlecode.javacv.cpp.opencv_core.cvDrawContours;
import static com.googlecode.javacv.cpp.opencv_core.cvPoint;
import static com.googlecode.javacv.cpp.opencv_core.cvRectangle;

import java.util.ArrayList;
import java.util.List;

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
	private List<ObjectOnMap> objectList;

	public Application(){
		ci = new CaptureImage();
		iu = new ImageUtils();
		canvasContours = new CanvasFrame("contours");
		objectList = new ArrayList<ObjectOnMap>();
	}

	public void frameProcessing(){

		//grabbedFrame = opencv_core.cvCloneImage(ci.grabImage());

		//below call used for testing purposes
		grabbedFrame = (IplImage) opencv_highgui.cvLoadImage("nolightmap.jpg");

		resizedFrame = iu.resizeImage(grabbedFrame);
		opencv_core.cvReleaseImage(grabbedFrame);


		thresholdGreen();
		thresholdRed();

		objectList.clear();
		
		canvasContours.showImage(resizedFrame);



		opencv_core.cvReleaseImage(resizedFrame);
		opencv_core.cvReleaseImage(thresholdedFrame);
		//opencv_core.cvReleaseImage(contoursFrame);


		//TODO SMALL MEMORYLEAK! Caused after canvasframes being made from here. (threshold methods)


	}

	public void thresholdRed(){
		opencv_core.CvPoint p1 = new opencv_core.CvPoint(0,0), p2 = new opencv_core.CvPoint(0,0);

		thresholdedFrame = iu.thresholdFrame(resizedFrame, Threshold.RED_LOWER, Threshold.RED_UPPER);
		redObjects = iu.findContours(thresholdedFrame, resizedFrame);

		try{
			for (; redObjects != null; redObjects = redObjects.h_next()) {
				opencv_core.CvRect sq = opencv_imgproc.cvBoundingRect(redObjects, 0);
				if (sq.width() < 6 || sq.height() < 6){
					continue;
				}
				Box box = new Box();
				box.setX(sq.x());
				box.setY(sq.y());
				box.setHeight(sq.height());
				box.setWidth(sq.width());

				objectList.add(box);

				//Used for debugging
				System.out.println("Y er: " + sq.y());
				System.out.println("X er: " + sq.x());
				System.out.println("Højde er: " + sq.height());
				System.out.println("Bredde er: " + sq.width());
				System.out.println("\n");

				//Below used for debugging
				opencv_core.CvScalar color = opencv_core.CvScalar.BLUE;
				p1.x(sq.x());
				p2.x(sq.x()+sq.width());
				p1.y(sq.y());
				p2.y(sq.y()+sq.height());
				cvRectangle(resizedFrame, p1,p2, CV_RGB(255, 0, 0), 2, 8, 0);
				cvDrawContours(resizedFrame, redObjects, color, CV_RGB(0,0,0), -1, CV_FILLED, 8, cvPoint(0,0));


			}

		}catch(Exception e){
			System.err.println("No red contours found");
		}
		

	}

	public void thresholdGreen(){
		opencv_core.CvPoint p1 = new opencv_core.CvPoint(0,0), p2 = new opencv_core.CvPoint(0,0);

		thresholdedFrame = iu.thresholdFrame(resizedFrame, Threshold.GREEN_LOWER, Threshold.GREEN_UPPER);
		greenObjects = iu.findContours(thresholdedFrame, resizedFrame);
		try{
			for (; greenObjects != null; greenObjects = greenObjects.h_next()) {
				opencv_core.CvRect sq = opencv_imgproc.cvBoundingRect(greenObjects, 0);
				if (sq.width() < 6 || sq.height() < 6){
					continue;
				}
				Box box = new Box();
				box.setX(sq.x());
				box.setY(sq.y());
				box.setHeight(sq.height());
				box.setWidth(sq.width());

				objectList.add(box);

				//Used for debugging
				System.out.println("Y er: " + sq.y());
				System.out.println("X er: " + sq.x());
				System.out.println("Højde er: " + sq.height());
				System.out.println("Bredde er: " + sq.width());
				System.out.println("Liststørrelse: " + objectList.size());
				System.out.println("\n");

				//Below used for debugging
				opencv_core.CvScalar color = opencv_core.CvScalar.BLUE;
				p1.x(sq.x());
				p2.x(sq.x()+sq.width());
				p1.y(sq.y());
				p2.y(sq.y()+sq.height());
				cvRectangle(resizedFrame, p1,p2, CV_RGB(255, 0, 0), 2, 8, 0);
				cvDrawContours(resizedFrame, greenObjects, color, CV_RGB(0,0,0), -1, CV_FILLED, 8, cvPoint(0,0));

			}
		}catch(Exception e){
			System.err.println("No green contours found");
		}
		


	}
}
