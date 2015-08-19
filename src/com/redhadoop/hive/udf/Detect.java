package com.redhadoop.hive.udf;

import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_highgui.*;
import static org.bytedeco.javacpp.opencv_imgproc.*;
import static org.bytedeco.javacpp.opencv_objdetect.*;


import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.binary.Base64;
import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;


import  com.redhadoop.io.*;
import com.redhadoop.io.obj.Image;
public final class Detect extends UDF {
	
	// Create memory for calculations

			CvMemStorage storage = null;

			// Create a new Haar classifier

			CvHaarClassifierCascade classifier = null;
			
			int count =0;

			// List of classifiers

			String[] classifierName = {
					"D:\\opencv\\sources\\data\\haarcascades\\haarcascade_frontalface_alt.xml",
					"D:\\opencv\\sources\\data\\haarcascades\\haarcascade_frontalface_alt2.xml",
					"D:\\opencv\\sources\\data\\haarcascades\\haarcascade_profileface.xml" };
			byte[] buffer = new byte[1024*300];
			
	 public Detect() {
		// Allocate the memory storage
			storage = CvMemStorage.create();

			// Load the HaarClassifierCascade
			classifier = new CvHaarClassifierCascade(cvLoad(classifierName[0]));
			// Make sure the cascade is loaded

			if (classifier.isNull()) {
				System.err.println("Error loading classifier file");
			}
	}
			
	
	  public static void deserialize(Writable writable, byte[] bytes) throws Exception {  
	      // create ByteArrayInputStream  
	      ByteArrayInputStream in = new ByteArrayInputStream(bytes);  
	      // create DataInputStream  
	      DataInputStream datain = new DataInputStream(in);  
	      // read fields  
	      writable.readFields(datain);  
	      datain.close();  
	  } 
 
	  private final Text result = new Text();
	  
	 /*public Text evaluate(BytesWritable b){
		     if (b == null) {
		       return null;
		     }
		     byte[] bytes = new byte[b.getLength()];
		     System.arraycopy(b.getBytes(), 0, bytes, 0, b.getLength());
		     result.set(Base64.encodeBase64(bytes));
		     return result;
   }*/
	  
	public String evaluate(BytesWritable b) throws Exception {
			
			Image image = new Image();
			deserialize(image, b.getBytes());
			
			//Clear the memory storage which was used before
			 cvClearMemStorage(storage);
			 if(!classifier.isNull()){
			
			 // Detect the objects and store them in the sequence
			 CvSeq faces = cvHaarDetectObjects(image.getImage(), classifier, storage, 1.1, 3, CV_HAAR_DO_CANNY_PRUNING);
			 // Loop the number of faces found.
			 //Draw red box around face.
			 int total = faces.total();
			 for (int i = 0; i < total; i++) {
			 CvRect r = new CvRect(cvGetSeqElem(faces, i));
			
			 int x = r.x(), y = r.y(), w = r.width(), h = r.height();
			
			 cvRectangle(image.getImage(), cvPoint(x, y),
			
			 cvPoint(x + w, y + h), CvScalar.RED, 1, CV_AA, 0);
			 }
			
			 }
			 
			String fname = "c://tmp//seasbase//images//"+new Date().getTime()+".jpg"; 
			cvSaveImage(fname,((Image)image).getImage());
			
			 File f=new File(fname);
	         if(f.exists())
	         {
		              
		        FileInputStream fis = new FileInputStream(f);
		        if(buffer.length <f.length())
		        	buffer = new byte[(int)f.length()];
		    	String s_imageDate;
		    	
		    	fis.read(buffer);
		    	fis.close();
		    	
		        s_imageDate = Base64.encodeBase64String(buffer);
		        
		        Pattern p = Pattern.compile("\t|\r|\n");
		        Matcher m = p.matcher( s_imageDate);
		        String dest = m.replaceAll("");
		        //System.out.println(dest);
				return dest;	
	         }
	         return null;
	}
}
	



