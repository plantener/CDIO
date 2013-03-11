package main;

import com.googlecode.javacv.OpenCVFrameGrabber;
import com.googlecode.javacv.FrameGrabber.Exception;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import com.googlecode.javacv.cpp.opencv_highgui;
import com.googlecode.javacv.cpp.opencv_highgui.CvCapture;

public class CaptureImage {
	private final OpenCVFrameGrabber grabber;
	private IplImage img = null;
	
	CvCapture capture = opencv_highgui.cvCreateCameraCapture(0);
	
	
	public CaptureImage(){
		// 0-default camera, 1 - next...so on
				grabber = new OpenCVFrameGrabber(0);
				try {
					grabber.start();
				} catch (Exception e) {
					System.err.print("Failed to initialize camera");
					e.printStackTrace();
				}
				
	}
	
	public IplImage grabImage(){
		
		img = opencv_highgui.cvQueryFrame(capture);
		return img;
//		try {
//	
//		//A grabbed image from Logitech webcam is in following resolution: 1200x800px
//			
//			img = grabber.grab();
//			
//			
//			
//		} catch (Exception e) {
//			
//			e.printStackTrace();
//		}
//		return img;
	}
	
	

}
