package main;

import utilities.ImageUtils;

import com.googlecode.javacv.CanvasFrame;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import com.googlecode.javacv.cpp.opencv_highgui;

public class Application {
	private CaptureImage ci;
	private ImageUtils iu;
	private CanvasFrame canvasContours;
	
	public Application(){
		ci = new CaptureImage();
		iu = new ImageUtils();
		canvasContours = new CanvasFrame("contours");
	}

	public void frameProcessing(){
		
		IplImage grabbedFrame;
		IplImage resizedFrame;
		IplImage thresholdedFrame;
		
		grabbedFrame = ci.grabImage();
		//below call used for testing purposes
		//grabbedFrame = (IplImage) opencv_highgui.cvLoadImage("testingImage.jpg");
		resizedFrame = iu.resizeImage(grabbedFrame);
	
		thresholdedFrame = iu.thresholdImage(resizedFrame);
		IplImage contoursFrame = iu.findContours(thresholdedFrame, resizedFrame);
			
		canvasContours.showImage(contoursFrame);
	
		

	}
}
