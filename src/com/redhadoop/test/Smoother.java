package com.redhadoop.test;

import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_highgui.*;
import static org.bytedeco.javacpp.opencv_imgproc.*;
import static org.bytedeco.javacpp.opencv_objdetect.*;
  
public class Smoother {  
      
    public static void smooth(String filename){  
        IplImage image=cvLoadImage(filename);  
        if(image!=null){  
            cvSaveImage("c:\\new-lena.jpg",image);  
            cvReleaseImage(image);  
        }  
    }  
  
} 