package com.redhadoop.test;


import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_highgui.*;
import static org.bytedeco.javacpp.opencv_imgproc.*;
import static org.bytedeco.javacpp.opencv_objdetect.*;

public class Test {  

	CvMemStorage storage = null;

	// Create a new Haar classifier

	CvHaarClassifierCascade classifier = null;
	
	int count =0;

	// List of classifiers

	String[] classifierName = {
	"C:\\Users\\Administrator\\Desktop\\opencv\\data\\haarcascades\\haarcascade_frontalface_alt.xml",
	"C:\\Users\\Administrator\\Desktop\\opencv\\data\\haarcascades\\haarcascade_frontalface_alt2.xml",
	"C:\\Users\\Administrator\\Desktop\\opencv\\data\\haarcascades\\haarcascade_profileface.xml" };
	
	 public Test() {
		// Allocate the memory storage
			storage = CvMemStorage.create();

			// Load the HaarClassifierCascade
			classifier = new CvHaarClassifierCascade(cvLoad(classifierName[2]));
			// Make sure the cascade is loaded

			if (classifier.isNull()) {
				System.err.println("Error loading classifier file");
			}
	}
	 
	 
	public int run(String path){
    	//Clear the memory storage which was used before
		 cvClearMemStorage(storage);
		 if(!classifier.isNull()){
		
		 IplImage image=cvLoadImage(path);  
		       
		        
		 // Detect the objects and store them in the sequence
		 CvSeq faces = cvHaarDetectObjects(image, classifier,storage, 1.1, 3, CV_HAAR_DO_CANNY_PRUNING);
		 // Loop the number of faces found.
		 //Draw red box around face.
		 int total = faces.total();
			return total;	
        }
		 return 0;
	}
    public static void main(String args[]){ 
    	System.out.println(System.getProperty("java.library.path"));
    	
    	Test test = new Test();
    	System.out.println(test.run("c:\\mei.jpg"));
		
        //Smoother.smooth("c:\\lena.jpg");  
    }  
  
}  