package main;

import com.googlecode.javacv.cpp.opencv_core;
import com.googlecode.javacv.cpp.opencv_highgui;
import com.googlecode.javacv.cpp.opencv_imgproc;
import com.googlecode.javacv.cpp.opencv_core.IplImage;

public class ProcessImage {

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
