package utilities;


import java.util.ArrayList;

import models.BreakPoint;
import routeCalculation.Track;

import com.googlecode.javacpp.Loader;
import com.googlecode.javacv.CanvasFrame;
import com.googlecode.javacv.cpp.opencv_core;
import com.googlecode.javacv.cpp.opencv_imgproc;
import com.googlecode.javacv.cpp.opencv_core.CvContour;
import com.googlecode.javacv.cpp.opencv_core.CvMemStorage;
import com.googlecode.javacv.cpp.opencv_core.CvSeq;
import com.googlecode.javacv.cpp.opencv_core.IplImage;

import dk.dtu.cdio.ANIMAL.computer.Navigator;

public class ImageUtils {
	private CanvasFrame canvasResized;
	private IplImage resizedImage = null;
	private IplImage imgHSV = null;
	private IplImage imgThreshold = null;

	CvSeq contours;

	private CanvasFrame canvasAlgorithm;


	public ImageUtils(){
		canvasAlgorithm = new CanvasFrame("algorithm");
//		canvasResized = new CanvasFrame("resized");
	}

	/**
	 * Resized an image to 400x300, with colordepth 3.
	 * @param img
	 * @return resizedImage
	 */
	public IplImage resizeImage(IplImage img){

		resizedImage = null;
		resizedImage = opencv_core.cvCreateImage( opencv_core.cvSize( Navigator.X_RESOLUTION, Navigator.Y_RESOLUTION ), 8, 3 );
		opencv_imgproc.cvResize(img, resizedImage);

		//used for debugging purposes
//		canvasResized.showImage(resizedImage);

		return resizedImage;
	}


	/**
	 * Return a binary image where the specified color is marked "white"
	 * @param img
	 * @param threshold_lower
	 * @param threshold_upper
	 * @return thresholdImage
	 */
	public IplImage thresholdFrame(IplImage img, Threshold threshold_lower, Threshold threshold_upper){
		imgHSV = opencv_core.cvCreateImage(opencv_core.cvGetSize(img), 8, 3);
		imgThreshold = opencv_core.cvCreateImage(opencv_core.cvGetSize(img), 8, 1);

		//Convert RGB image to HSV
		opencv_imgproc.cvCvtColor(img, imgHSV,opencv_imgproc.CV_BGR2HSV);

		//Upper and lower bounds, notice that cvScalar is on this form: BGR, and not RGB
		opencv_core.cvInRangeS(imgHSV, opencv_core.cvScalar(threshold_lower.getB(),threshold_lower.getG(), threshold_lower.getR(),0),
				opencv_core.cvScalar(threshold_upper.getB(), threshold_upper.getG(), threshold_upper.getR(),0), imgThreshold);

		opencv_core.cvReleaseImage(imgHSV);

		return imgThreshold;
	}

	/**
	 * Takes thresholded image, and original (resized) image. Return original (resized) image with contours.
	 * @param imgThreshold
	 * @param img
	 * @return img
	 */
	public CvSeq findContours(IplImage imgThreshold, IplImage img){

		CvMemStorage  storage = null;
		CvSeq contours = null;

		storage = CvMemStorage.create();
		contours = new CvContour(null);

		int noOfContors = opencv_imgproc.cvFindContours(imgThreshold, storage, contours,
				Loader.sizeof(CvContour.class), opencv_imgproc.CV_RETR_CCOMP,
				opencv_imgproc.CV_CHAIN_APPROX_NONE, new opencv_core.CvPoint(0,0));

		CvSeq ptr = new CvSeq();
		ptr = contours;

		opencv_core.cvReleaseImage(imgThreshold);

		return ptr;

	}

	public void drawLine(IplImage resized){
		ArrayList<BreakPoint> breakpoints = Track.getCompleteList();
		BreakPoint firstCoord = null;
		BreakPoint secondCoord = null;
		opencv_core.CvScalar color = opencv_core.CvScalar.YELLOW;

		for (int i = 0; i < breakpoints.size(); i++) {			
			firstCoord = breakpoints.get(i);
			
			if(i < breakpoints.size()-1){
				secondCoord = breakpoints.get(i+1);
			}else secondCoord = breakpoints.get(0);
			opencv_core.CvPoint p1 = new opencv_core.CvPoint(firstCoord.getX(), firstCoord.getY());
			opencv_core.CvPoint p2 = new opencv_core.CvPoint(secondCoord.getX(), secondCoord.getY());
			opencv_core.cvLine(resized, p1, p2, color, 2, opencv_core.CV_AA, 0);

		}

		canvasAlgorithm.showImage(resized);
		//		opencv_core.cvReleaseImage(resized);
	}

}
