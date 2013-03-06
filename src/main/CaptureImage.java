package main;

import com.googlecode.javacv.OpenCVFrameGrabber;
import com.googlecode.javacv.FrameGrabber.Exception;
import com.googlecode.javacv.cpp.opencv_core;
import com.googlecode.javacv.cpp.opencv_imgproc;
import com.googlecode.javacv.cpp.opencv_core.IplImage;

public class CaptureImage {
	final OpenCVFrameGrabber grabber;
	IplImage img = null;
	
	
	public CaptureImage(){
		// 0-default camera, 1 - next...so on
				grabber = new OpenCVFrameGrabber(1);
	}
	
	public void grabImage(){
		try {
			grabber.start();
			
		//A grabbed image from Logitech webcam is in following resolution: 1200x800px
			img = grabber.grab();
		} catch (Exception e) {
			System.err.print("Failed to initialize camera");
			e.printStackTrace();
		}
	}
	
	public IplImage resizeImage(IplImage img){
		IplImage resizedImage = opencv_core.cvCreateImage( opencv_core.cvSize( 400, 266 ), 8, 3 );
		opencv_imgproc.cvResize(img, resizedImage);
		
		return resizedImage;
	}

}
