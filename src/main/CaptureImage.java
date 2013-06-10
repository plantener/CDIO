package main;

import com.googlecode.javacv.OpenCVFrameGrabber;
import com.googlecode.javacv.FrameGrabber.Exception;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import com.googlecode.javacv.cpp.opencv_highgui;


public class CaptureImage {
	private final OpenCVFrameGrabber grabber;
	private IplImage img = null;
	
	
	public CaptureImage(){
		// 0-default camera, 1 - next...so on
				grabber = new OpenCVFrameGrabber(1);
				try {
					grabber.start();
				} catch (Exception e) {
					System.err.print("Failed to initialize camera");
					e.printStackTrace();
				}
				
	}
	
	public IplImage grabImage(){
		
		
		try {
	
		//A grabbed image from Logitech webcam is in following resolution: 1600x1200px
			
			img = grabber.grab();
//			opencv_highgui.cvSaveImage("correctSetup5.jpg", img);
			
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		return img;
	}
	
	

}
