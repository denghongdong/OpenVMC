package com.redhadoop.hive.udf;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.bytedeco.javacpp.opencv_core.IplImage;

import com.redhadoop.io.obj.Image;
import com.seasbase.video.io.Info;
import com.seasbase.video.io.VideoReader;


public final class S_DiffProc extends UDF {
	private final Text result = new Text();
	VideoReader video = new VideoReader();
	List<Info> lstInfo = new ArrayList<Info>();
	int inx = 0;
	
	public S_DiffProc() {}

	public static void deserialize(Writable writable, byte[] bytes) throws Exception {
		ByteArrayInputStream in = new ByteArrayInputStream(bytes);
		DataInputStream datain = new DataInputStream(in);
		writable.readFields(datain);
		datain.close();
	}

	public String evaluate(BytesWritable b) throws Exception {
		Image image = new Image();
		deserialize(image, b.getBytes());
		IplImage frame= image.getImage();
		inx++;
		List<Info> lstInfo = new ArrayList<Info>();
		String str=null;
		//video.DiffProc(frame, 10, inx,lstInfo);
		System.out.println("inx:"+inx);
		for(Info info: lstInfo)
		{
			str ="targetnum:"+info.getTargetnum();
			System.out.println("Position X:"+info.getX());
			System.out.println("Position Y:"+info.getY());
			System.out.println("width:"+info.getWidth());
			System.out.println("height:"+info.getHeight());
		}
		lstInfo.clear();
		
		return str;
	}
}
