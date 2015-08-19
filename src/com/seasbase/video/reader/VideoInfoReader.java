package com.seasbase.video.reader;



import java.util.ArrayList;
import java.util.List;



import static org.bytedeco.javacpp.opencv_highgui.*;

import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacpp.opencv_highgui.CvCapture;

import com.seasbase.video.io.Info;
public class VideoInfoReader {

	static {
	     System.loadLibrary("VideoReaderDll"); 
	}
	//public native void Prepare();
	public native void DiffProc(String frame,int thd, int inx,List<Info> infoArray);
	public native void Release();

	public static void main(String[] args) {

		String fpath = "F:\\视频数据\\异动分析\\90_90_IP监控点01_20141203132000_20141203132059_12006156.mp4";
		CvCapture capture = cvCreateFileCapture(fpath);

		int inx = 0;	
		VideoInfoReader video = new VideoInfoReader();
		List<Info> lstInfo = new ArrayList<Info>();
		long nbFrames;
        nbFrames = (long) cvGetCaptureProperty(capture, CV_CAP_PROP_FRAME_COUNT);
        IplImage frame;
        int pos = 0;
        while (cvGrabFrame(capture) != 0 && (frame = cvRetrieveFrame(capture)) != null) {
            cvSaveImage("c:\\home\\"+"--"+(pos++)+".jpg", frame);
            System.out.print("c:\\home\\"+"--"+(pos++)+".jpg");
            
        }
     
	
	}
}
