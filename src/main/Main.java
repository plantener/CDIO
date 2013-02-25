package main;
import com.googlecode.javacv.CanvasFrame;
import com.googlecode.javacv.FrameGrabber.Exception;
import com.googlecode.javacv.OpenCVFrameGrabber;
//import com.googlecode.javacv.cpp.*;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
//import com.googlecode.javacpp.Loader.*;
public class Main {

	public static void main(String[] args) {
		ProcessImage pi = new ProcessImage();
		IplImage img = null;
		
		// 0-default camera, 1 - next...so on
		final OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(0);
		CanvasFrame canvas2 = new CanvasFrame("threshold");
		CanvasFrame canvas = new CanvasFrame("Out");

		try {
			grabber.start();
			
		
			img = grabber.grab();
		} catch (Exception e) {
			System.err.print("Failed to initialize camera");
			e.printStackTrace();
		}
		

		IplImage imgThreshold = pi.thresholdImage(img);
		pi.findContours(imgThreshold);
		
		//setup windows for live images, used for debugging
		canvas2.showImage(imgThreshold);	
		canvas.showImage(img);

	}
}
