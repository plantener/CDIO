package main;

import utilities.ImageUtils;
import com.googlecode.javacv.CanvasFrame;
import com.googlecode.javacv.cpp.opencv_core;
import com.googlecode.javacv.cpp.opencv_core.IplImage;

public class Application {
	private CaptureImage ci;
	private ImageUtils iu;
	private CanvasFrame canvasContours;
	
	IplImage grabbedFrame;
	IplImage resizedFrame;
	IplImage thresholdedFrame;
	IplImage contoursFrame;
	
	public Application(){
		ci = new CaptureImage();
		iu = new ImageUtils();
		canvasContours = new CanvasFrame("contours");
		
	}

	public void frameProcessing(){
		
		grabbedFrame = opencv_core.cvCloneImage(ci.grabImage());
		
		//below call used for testing purposes
		//grabbedFrame = (IplImage) opencv_highgui.cvLoadImage("testingImage.jpg");
		//cloning image due to highgui guidelines.
//		clonedImage = opencv_core.cvCloneImage(grabbedFrame);
		resizedFrame = iu.resizeImage(grabbedFrame);
		
		opencv_core.cvReleaseImage(grabbedFrame);
		
		thresholdedFrame = iu.thresholdImage(resizedFrame);
	
		contoursFrame = iu.findContours(thresholdedFrame, resizedFrame);
			
		canvasContours.showImage(contoursFrame);

		opencv_core.cvReleaseImage(resizedFrame);
		opencv_core.cvReleaseImage(thresholdedFrame);
		opencv_core.cvReleaseImage(contoursFrame);


	}
}
