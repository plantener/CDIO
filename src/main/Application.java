package main;

import models.Grid;
import utilities.ImageUtils;
import utilities.Threshold;

import com.googlecode.javacv.CanvasFrame;
import com.googlecode.javacv.cpp.opencv_core;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import com.googlecode.javacv.cpp.opencv_highgui;

public class Application {
	private CaptureImage ci;
	private ImageUtils iu;
	private CanvasFrame canvasContours;
	private IplImage grabbedFrame;
	private IplImage resizedFrame;
	private IplImage thresholdedFrame;
	private IplImage contoursFrame;
	private Grid grid;


	public Application(){
		ci = new CaptureImage();
		iu = new ImageUtils();
		canvasContours = new CanvasFrame("contours");
		grid = new Grid();

	}

	public void frameProcessing(){

		grabbedFrame = opencv_core.cvCloneImage(ci.grabImage());

		//below call used for testing purposes
		//grabbedFrame = (IplImage) opencv_highgui.cvLoadImage("nolightmap.jpg");
		//cloning image due to highgui guidelines.
		
		resizedFrame = iu.resizeImage(grabbedFrame);
		opencv_core.cvReleaseImage(grabbedFrame);
		thresholdGreen();
		thresholdRed();
		grid.setGridToZero();

		//TODO Set grid to zero before refilling it

		canvasContours.showImage(contoursFrame);


		opencv_core.cvReleaseImage(resizedFrame);
		opencv_core.cvReleaseImage(thresholdedFrame);
		opencv_core.cvReleaseImage(contoursFrame);


	}
	
	public void thresholdRed(){
		thresholdedFrame = iu.thresholdFrame(resizedFrame, Threshold.RED_LOWER, Threshold.RED_UPPER);
		contoursFrame = iu.findContours(thresholdedFrame, resizedFrame);
		grid = iu.fillGrid(grid, 1);
	}
	
	public void thresholdGreen(){
		thresholdedFrame = iu.thresholdFrame(resizedFrame, Threshold.GREEN_LOWER, Threshold.GREEN_UPPER);
		contoursFrame = iu.findContours(thresholdedFrame, resizedFrame);
		grid = iu.fillGrid(grid, 2);
	}
}
