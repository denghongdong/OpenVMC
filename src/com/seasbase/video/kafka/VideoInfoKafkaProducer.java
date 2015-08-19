package com.seasbase.video.kafka;



import static org.bytedeco.javacpp.opencv_highgui.CV_CAP_PROP_FRAME_COUNT;
import static org.bytedeco.javacpp.opencv_highgui.cvCreateFileCapture;
import static org.bytedeco.javacpp.opencv_highgui.cvGetCaptureProperty;
import static org.bytedeco.javacpp.opencv_highgui.cvGrabFrame;
import static org.bytedeco.javacpp.opencv_highgui.cvRetrieveFrame;
import static org.bytedeco.javacpp.opencv_highgui.cvSaveImage;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacpp.opencv_highgui.CvCapture;

import com.redhadoop.utils.FileUtils;
import com.seasbase.video.io.Info;
public class VideoInfoKafkaProducer {

	static {
	     //System.loadLibrary("VideoReaderDll"); 
	}
	//public native void Prepare();
	//public native void DiffProc(String frame,int thd, int inx,List<Info> infoArray);
	//public native void Release();

	public static void main(String[] args) {

		String fpath = "E:\\svn\\SeasBase0.2\\tmp\\video\\test.mp4";
		CvCapture capture = cvCreateFileCapture(fpath);
		int inx = 0;	
		VideoInfoKafkaProducer video = new VideoInfoKafkaProducer();
		List<Info> lstInfo = new ArrayList<Info>();
		long nbFrames;
        nbFrames = (long) cvGetCaptureProperty(capture, CV_CAP_PROP_FRAME_COUNT);
        IplImage frame;
        int pos = 0;
        String path ;
        String sbyte ;
        byte[] ibyte;
        Producer producerThread = new Producer(KafkaProperties.topic);
        while (cvGrabFrame(capture) != 0 && (frame = cvRetrieveFrame(capture)) != null) {
        	path = "E:\\svn\\SeasBase0.2\\tmp\\img1\\"+(pos++)+".jpg";
            cvSaveImage(path, frame);
            ibyte = FileUtils.getBytes(path);
            sbyte = Base64.encodeBase64String(ibyte);
            System.out.println("P:"+path);
            producerThread.send(sbyte);
        }
	}
}
