package utilities;

import static com.googlecode.javacv.cpp.opencv_core.CV_FILLED;
import static com.googlecode.javacv.cpp.opencv_core.CV_RGB;
import static com.googlecode.javacv.cpp.opencv_core.cvDrawContours;
import static com.googlecode.javacv.cpp.opencv_core.cvPoint;
import static com.googlecode.javacv.cpp.opencv_core.cvRectangle;
import models.Grid;

import com.googlecode.javacpp.Loader;
import com.googlecode.javacv.CanvasFrame;
import com.googlecode.javacv.cpp.opencv_core;
import com.googlecode.javacv.cpp.opencv_highgui;
import com.googlecode.javacv.cpp.opencv_imgproc;
import com.googlecode.javacv.cpp.opencv_core.CvContour;
import com.googlecode.javacv.cpp.opencv_core.CvMemStorage;
import com.googlecode.javacv.cpp.opencv_core.CvSeq;
import com.googlecode.javacv.cpp.opencv_core.IplImage;

public class ImageUtils {
	private CanvasFrame canvasResized;
	private IplImage resizedImage = null;
	private IplImage imgHSV = null;
	private IplImage imgThreshold = null;

	public ImageUtils(){
		canvasResized = new CanvasFrame("Resized");
	}

	/**
	 * Resized an image to 400x300, with colordepth 3.
	 * @param img
	 * @return resizedImage
	 */
	public IplImage resizeImage(IplImage img){

		resizedImage = null;
		resizedImage = opencv_core.cvCreateImage( opencv_core.cvSize( 400, 300 ), 8, 3 );
		opencv_imgproc.cvResize(img, resizedImage);

		//used for debugging purposes
		canvasResized.showImage(resizedImage);

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
		opencv_highgui.cvSaveImage("hsvConverted.jpg", imgHSV);

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
	public IplImage findContours(IplImage imgThreshold, IplImage img){

		//TODO Sort away "dummy" objects
		
		CvMemStorage  storage = null;
		CvSeq contours = null;

		storage = CvMemStorage.create();
		contours = new CvContour(null);


		int noOfContors = opencv_imgproc.cvFindContours(imgThreshold, storage, contours,
				Loader.sizeof(CvContour.class), opencv_imgproc.CV_RETR_CCOMP,
				opencv_imgproc.CV_CHAIN_APPROX_NONE, new opencv_core.CvPoint(0,0));

		CvSeq ptr = new CvSeq();

		int count =1;
		opencv_core.CvPoint p1 = new opencv_core.CvPoint(0,0), p2 = new opencv_core.CvPoint(0,0);

		try{
			for (ptr = contours; ptr != null; ptr = ptr.h_next()) {

				opencv_core.CvScalar color = opencv_core.CvScalar.BLUE;
				opencv_core.CvRect sq = opencv_imgproc.cvBoundingRect(ptr, 0);

				System.out.println("Contour No ="+count);
				System.out.println("X ="+ sq.x()+" Y="+ sq.y());
				System.out.println("Height ="+sq.height()+" Width ="+sq.width());
				System.out.println("");

				p1.x(sq.x());
				p2.x(sq.x()+sq.width());
				p1.y(sq.y());
				p2.y(sq.y()+sq.height());
				cvRectangle(img, p1,p2, CV_RGB(255, 0, 0), 2, 8, 0);
				cvDrawContours(img, ptr, color, CV_RGB(0,0,0), -1, CV_FILLED, 8, cvPoint(0,0));
				count++;

			}
			opencv_core.cvClearMemStorage(contours.storage());
		}catch(Exception E){
			System.out.println("No contours found");
			//TODO some kind of errorhandling here
		}
		//TODO Should maybe return x,y instead of image with contours.


		return img;


	}
	
	public Grid fillGrid(Grid grid, CvSeq contours){
		for (int i = 0; contours != null; contours = contours.h_next()) {
			
		}
		
		return grid;
	}

	

}
