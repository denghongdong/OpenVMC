package com.seasbase.video.io;



import java.util.ArrayList;
import java.util.List;





import java.util.concurrent.LinkedBlockingQueue;

public class VideoInfoReader {

	private LinkedBlockingQueue<Info> list = new LinkedBlockingQueue<Info>(100);  

	static {
		System.loadLibrary("VideoReaderDll"); 
	}
	//public native void Prepare();
	public native void DiffProc(String frame,int thd, int inx);
	public native void Release();
	
	public  void appendInfo(Info info){
		try {

			list.put(info);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public boolean retrieveInfo(List<Info> lstInfo) {
		for (int i = 1; i <= 10; ++i) {
			try {
				Info info = list.take();
				if(info.getTargetnum() == -1)
					return false;
				lstInfo.add(info);
			} catch (InterruptedException e) {
				e.printStackTrace();
				break;
			}
		}
		return true;
	}
	
	public static void main(String[] args) {

		String fpath = "F:\\video\\VIDEO_0_20111103070026906.avi";
		//CvCapture capture = cvCreateFileCapture(fpath);
		//long nbFrames = (long) cvGetCaptureProperty(capture, CV_CAP_PROP_FRAME_COUNT);

		int inx = 0;	
		final VideoReader video = new VideoReader();
		//List<Info> lstInfo = new ArrayList<Info>();
		//video.Prepare();
		//while (cvGrabFrame(capture) != 0 && (frame = cvRetrieveFrame(capture)) != null) {	
		//video.start();
		
		Thread  t=new Thread(new Runnable(){
			   public void run(){
				   List<Info> lstInfo = new ArrayList<Info>();
				   boolean bq = true;
				   while(true){
					    try{
					    	bq = video.retrieveInfo(lstInfo);
					    }catch (Exception e) {
					     e.printStackTrace();
					    }
						for(Info info: lstInfo)
						{
							System.out.print("frameid:"+info.getFrameid()+"\t");
							System.out.print("targetnum:"+info.getTargetnum()+"\t");
							System.out.print("Position X:"+info.getX()+"\t");
							System.out.print("Position Y:"+info.getY()+"\t");
							System.out.print("width:"+info.getWidth()+"\t");
							System.out.print("height:"+info.getHeight()+"\t");
							System.out.print("RGB:"+info.getMR()+","+info.getMG()+","+info.getMB()+"\t");
							System.out.print("RGB:"+info.getSR()+","+info.getSG()+","+info.getSB()+"\t");
							System.out.print("RGB:"+info.getTR()+","+info.getTG()+","+info.getTB()+"\t");
							System.out.print("H:"+info.getHm()+","+info.getHs()+","+info.getHt()+"\n");
							
						}
						if(false == bq)
							return;
						lstInfo.clear();
				   }
			   }
		});
		t.start();

		video.DiffProc(fpath, 10, inx);
		//video.stop();
		inx++;
		try {
			t.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//lstInfo.clear();
		//}
		video.Release();
		System.exit(0);
	}
	
}

