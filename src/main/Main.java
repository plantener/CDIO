package main;
import com.googlecode.javacv.OpenCVFrameGrabber;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import static com.googlecode.javacv.cpp.opencv_highgui.*;
import static com.googlecode.javacpp.Loader.*;
import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_imgproc.*;

public class Main {

	public static void main(String[] args) {

captureFrame();

	}

	private static void captureFrame() {
		IplImage imgHSV = null;
		IplImage img;
		
		// 0-default camera, 1 - next...so on
		final OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(0);
		try {
			grabber.start();

			img = grabber.grab();

			cvCvtColor(img, imgHSV,CV_RGB2HSV);

			if (img != null) {
				cvSaveImage("capture.jpg", imgHSV);

			}
		} catch (Exception e) {
			e.printStackTrace();

		}
	}
}

