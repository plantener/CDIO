package utilities;

import java.util.ArrayList;

import main.Application;
import main.Main;
import models.BreakPoint;
import models.ObjectOnMap;
import models.Port;
import routeCalculation.Track;

import com.googlecode.javacpp.Loader;
import com.googlecode.javacv.CanvasFrame;
import com.googlecode.javacv.cpp.opencv_core;
import com.googlecode.javacv.cpp.opencv_core.CvScalar;
import com.googlecode.javacv.cpp.opencv_imgproc;
import com.googlecode.javacv.cpp.opencv_core.CvContour;
import com.googlecode.javacv.cpp.opencv_core.CvMemStorage;
import com.googlecode.javacv.cpp.opencv_core.CvSeq;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import com.googlecode.javacv.cpp.opencv_core.CvFont;
import static com.googlecode.javacv.cpp.opencv_core.cvPoint;

import dk.dtu.cdio.ANIMAL.computer.Navigator;

public class ImageUtils {
	private CanvasFrame canvasResized;
	private IplImage resizedImage = null;
	private IplImage imgHSV = null;
	private IplImage imgThreshold = null;

	CvSeq contours;

	private CanvasFrame canvasAlgorithm;
	private CanvasFrame imgSmoothed;
	private CanvasFrame canvasConturs;

	public ImageUtils() {
		canvasAlgorithm = new CanvasFrame("algorithm");
		canvasAlgorithm.setLocation(550, 50);
		// canvasResized = new CanvasFrame("resized");
		// imgSmoothed= new CanvasFrame("smoothed");
		canvasConturs = new CanvasFrame("Conturs");
		canvasConturs.setLocation(550, 450);
		canvasConturs.setVisible(false);
	}

	/**
	 * Resized an image to 400x300, with colordepth 3.
	 * 
	 * @param img
	 * @return resizedImage
	 */
	public IplImage resizeImage(IplImage img) {

		resizedImage = null;
		resizedImage = opencv_core.cvCreateImage(opencv_core.cvSize(
				Navigator.X_RESOLUTION, Navigator.Y_RESOLUTION), 8, 3);
		opencv_imgproc.cvResize(img, resizedImage);

		// used for debugging purposes
//		 canvasResized.showImage(resizedImage);
		smoothImage(resizedImage);
		// imgSmoothed.showImage(resizedImage);

		return resizedImage;
	}

	/**
	 * Return a binary image where the specified color is marked "white"
	 * 
	 * @param img
	 * @param threshold_lower
	 * @param threshold_upper
	 * @return thresholdImage
	 */
	public IplImage thresholdFrame(IplImage img, int color) {
		imgHSV = opencv_core.cvCreateImage(opencv_core.cvGetSize(img), 8, 3);
		imgThreshold = opencv_core.cvCreateImage(opencv_core.cvGetSize(img), 8,
				1);
		
		int[] values = Application.THRESHOLDS[color];
		int threshold_upper_s = 255;
		int threshold_upper_v = 255;
		
		// Convert RGB image to HSV
		opencv_imgproc.cvCvtColor(img, imgHSV, opencv_imgproc.CV_BGR2HSV);

		// Upper and lower bounds, notice that cvScalar is on this form: BGR,
		// and not RGB
		opencv_core.cvInRangeS(
				imgHSV,
				opencv_core.cvScalar(values[Application.HUE],
						values[Application.SATURATION], values[Application.VALUE], 0),
				opencv_core.cvScalar(values[Application.UPPERHUE],
						threshold_upper_s, threshold_upper_v, 0),
				imgThreshold);

		opencv_imgproc.cvErode(imgThreshold, imgThreshold,
				null /* 3x3 square */, 1/* iterations */);
		opencv_imgproc.cvDilate(imgThreshold, imgThreshold,
				null /* 3x3 square */, 1 /* iterations */);
		if(!Main.DRAW_ALL) {
			canvasConturs.showImage(imgHSV);
		}
		opencv_core.cvReleaseImage(imgHSV);

		return imgThreshold;
	}

	/**
	 * Takes thresholded image, and original (resized) image. Return original
	 * (resized) image with contours.
	 * 
	 * @param imgThreshold
	 * @param img
	 * @return img
	 */
	public CvSeq findContours(IplImage imgThreshold, IplImage img) {

		CvMemStorage storage = null;
		CvSeq contours = null;

		storage = CvMemStorage.create();
		contours = new CvContour(null);

		int noOfContors = opencv_imgproc.cvFindContours(imgThreshold, storage,
				contours, Loader.sizeof(CvContour.class),
				opencv_imgproc.CV_RETR_CCOMP,
				opencv_imgproc.CV_CHAIN_APPROX_NONE, new opencv_core.CvPoint(0,
						0));

		CvSeq ptr = new CvSeq();
		ptr = contours;

		opencv_core.cvReleaseImage(imgThreshold);

		return ptr;

	}

	public void drawLine(IplImage resized) {
		ArrayList<BreakPoint> breakpoints = Track.getCompleteList();
		BreakPoint firstCoord = null;
		BreakPoint secondCoord = null;
		opencv_core.CvScalar color = opencv_core.CvScalar.YELLOW;

		for (int i = 0; i < breakpoints.size(); i++) {
			firstCoord = breakpoints.get(i);

			if (i < breakpoints.size() - 1) {
				secondCoord = breakpoints.get(i + 1);
			} else
				secondCoord = breakpoints.get(0);
			opencv_core.CvPoint p1 = new opencv_core.CvPoint(firstCoord.getX(),
					firstCoord.getY());
			opencv_core.CvPoint p2 = new opencv_core.CvPoint(
					secondCoord.getX(), secondCoord.getY());
			opencv_core.cvLine(resized, p1, p2, color, 2, opencv_core.CV_AA, 0);

		}

		canvasAlgorithm.showImage(resized);
		// opencv_core.cvReleaseImage(resized);
	}

	public void drawText(IplImage resizedFrame, ArrayList<Port> sortedPorts) {
		CvFont font = new CvFont(1, 1, 1);
		for (ObjectOnMap obj : sortedPorts) {
			opencv_core.cvPutText(resizedFrame,
					"" + (((Port) obj).getPairId()),
					cvPoint(((Port) obj).getMidX(), ((Port) obj).getMidY()),
					font, CvScalar.BLUE);

		}
	}

	public void smoothImage(IplImage threshold) {
		opencv_imgproc.cvSmooth(threshold, threshold,
				opencv_imgproc.CV_GAUSSIAN, 3);

	}
	public void showImage(IplImage img){
		canvasAlgorithm.showImage(img);
	}

}
