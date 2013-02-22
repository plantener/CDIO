package main;
import com.googlecode.javacv.FrameGrabber.Exception;
import com.googlecode.javacv.OpenCVFrameGrabber;
import com.googlecode.javacv.cpp.*;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
//import com.googlecode.javacpp.Loader.*;
public class Main {

	public static void main(String[] args) {

captureFrame();

	}

	private static void captureFrame() {
		IplImage img;
		
		
		// 0-default camera, 1 - next...so on
		final OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(0);
		
			try {
				grabber.start();
				img = grabber.grab();
			} catch (Exception e) {
				e.printStackTrace();
			}

			//thresholdImage(img);

	}
	
	public IplImage thresholdImage(IplImage img){
		IplImage imgHSV;
		IplImage imgThreshold;
				
		imgHSV = opencv_core.cvCreateImage(opencv_core.cvGetSize(img), 8, 3);
		imgThreshold = opencv_core.cvCreateImage(opencv_core.cvGetSize(img), 8, 1);
		
		//Convert RGB image to HSV
		opencv_imgproc.cvCvtColor(img, imgHSV,opencv_imgproc.CV_BGR2HSV);
		
		//Upper and lower bounds, notice that cvScalar is on this form: BGR, and not RGB
		opencv_core.cvInRangeS(imgHSV, opencv_core.cvScalar(20, 100, 100,0),
				opencv_core.cvScalar(30, 255, 255,0), imgThreshold);
		
		//used for testing
		if (imgThreshold != null) {
			opencv_highgui.cvSaveImage("capture.jpg", imgThreshold);
		}
		return imgThreshold;
	}
}

