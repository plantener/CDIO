package main;

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




		opencv_core.cvReleaseImage(resizedFrame);
		opencv_core.cvReleaseImage(thresholdedFrame);
		//opencv_core.cvReleaseImage(contoursFrame);


	}

	public void thresholdRed(){
		thresholdedFrame = iu.thresholdFrame(resizedFrame, Threshold.RED_LOWER, Threshold.RED_UPPER);
		redObjects = iu.findContours(thresholdedFrame, resizedFrame);

		try{
			for (; redObjects != null; redObjects = redObjects.h_next()) {
				opencv_core.CvRect sq = opencv_imgproc.cvBoundingRect(redObjects, 0);
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
				

			}
			
		}catch(Exception e){
			System.err.println("No red contours found");
		}

	}

	public void thresholdGreen(){
		
		thresholdedFrame = iu.thresholdFrame(resizedFrame, Threshold.GREEN_LOWER, Threshold.GREEN_UPPER);
		greenObjects = iu.findContours(thresholdedFrame, resizedFrame);
		try{
			for (; greenObjects != null; greenObjects = greenObjects.h_next()) {
				opencv_core.CvRect sq = opencv_imgproc.cvBoundingRect(greenObjects, 0);
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

			}
		}catch(Exception e){
			System.err.println("No green contours found");
		}
		

	}
}
