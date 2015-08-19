package com.seasbase.video.io;



import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

public class VideoReader {

	private LinkedBlockingQueue<Info> list = new LinkedBlockingQueue<Info>(100);  

	static {
		
		System.loadLibrary("VehicleTypeRecognition_D");
		System.loadLibrary("VideoReaderDll");
		
		
	}
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
		//System.out.print(System.getProperty("java.library.path"));
		String fpath = "E:\\video\\20130923_170148_0.avi";
		int inx = 0;	
		final VideoReader video = new VideoReader();
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
							/*System.out.print("Start X:"+info.getSX()+"\t");
							System.out.print("Start Y:"+info.getSY()+"\t");
							System.out.print("End X:"+info.getEX()+"\t");
							System.out.print("End Y:"+info.getEY()+"\t");*/
							System.out.print("Position X:"+info.getX()+"\t");
							System.out.print("Position Y:"+info.getY()+"\t");
							System.out.print("width:"+info.getWidth()+"\t");
							System.out.print("height:"+info.getHeight()+"\n");
							System.out.print("number:"+info.getNumber()+",type:"+info.getType()+",color:"+info.getColor()+"\n");
							
						}
						if(false == bq)
							return;
						lstInfo.clear();
				   }
			   }
		});
		t.start();

		video.DiffProc(fpath,10,inx);
	
		inx++;
		try {
			t.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		video.Release();
		System.exit(0);
	}
	
}
