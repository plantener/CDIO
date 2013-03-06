package main;
import com.googlecode.javacv.CanvasFrame;
import com.googlecode.javacv.FrameGrabber.Exception;
import com.googlecode.javacv.OpenCVFrameGrabber;
import com.googlecode.javacv.cpp.opencv_core;
import com.googlecode.javacv.cpp.opencv_highgui;
//import com.googlecode.javacv.cpp.*;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import com.googlecode.javacv.cpp.opencv_imgproc;

//import com.googlecode.javacpp.Loader.*;

//import static com.googlecode.javacv.cpp.opencv_imgproc.*;
//import static com.googlecode.javacv.cpp.opencv_calib3d.*;
//import static com.googlecode.javacv.cpp.opencv_objdetect.*;



public class Main {

	public static void main(String[] args) {
		ProcessImage pi = new ProcessImage();
		IplImage img = null;
		IplImage resizedImage = null;
		
		// 0-default camera, 1 - next...so on
		final OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(1);
		CanvasFrame canvas2 = new CanvasFrame("threshold");
		CanvasFrame canvas = new CanvasFrame("Out");

		
		
//		Comment below try/catch clause when testing
		try {
			grabber.start();
			
		//A grabbed image from Logitech webcam is in following resolution: 1200x800px
			img = grabber.grab();
		} catch (Exception e) {
			System.err.print("Failed to initialize camera");
			e.printStackTrace();
		}
		
		//resize the original image
		resizedImage = opencv_core.cvCreateImage( opencv_core.cvSize( 400, 266 ), 8, 3 );
		opencv_imgproc.cvResize(img, resizedImage);
		
		//Static image used for testing purposes		
//		img = (IplImage) opencv_highgui.cvLoadImage("testingImage.jpg");

		IplImage imgThreshold = pi.thresholdImage(resizedImage);
		pi.findContours(imgThreshold);
		
		//setup windows for live images, used for debugging
//		canvas2.showImage(imgThreshold);	
//		canvas.showImage(img);
		}
	}

