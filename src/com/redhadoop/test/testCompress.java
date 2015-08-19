package com.redhadoop.test;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bytedeco.javacpp.opencv_core.IplImage;

import com.seasbase.video.io.Info;
import com.sun.image.codec.jpeg.ImageFormatException;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import com.seasbase.video.io.VideoReader;



public class testCompress {
	
	
	
	public static void main(String args[]) {
		
		// CvCapture capture = cvCreateFileCapture(fpath);
		final VideoReader video = new VideoReader();
		
		Thread t = new Thread(new Runnable() {
			public void run() {
				boolean bq = true;
				int pos = 0;
				List<Info> lstInfo = new ArrayList<Info>();
				while (bq == true) {
					
					try {
						lstInfo.clear();
						bq = video.retrieveInfo(lstInfo);

					} catch (Exception e) {
						e.printStackTrace();
					}
					for (Info info : lstInfo) {
						Info value = new Info();				
						ByteArrayOutputStream out = new ByteArrayOutputStream();
						DataOutputStream dataOut = new DataOutputStream(out);
						ByteArrayOutputStream out2 = new ByteArrayOutputStream();
						DataOutputStream dataOut2 = new DataOutputStream(out2);
						try {
							info.write(dataOut2);
							dataOut2.flush();
							dataOut2.close();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						
						ByteArrayInputStream in = new ByteArrayInputStream(out2.toByteArray());
						
						DataInputStream datain = new DataInputStream(in);
						try {
							out2.close();
							value.readFields(datain);
							datain.close();
							in.close();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						
						String path1 = "e:\\videoprocess\\image\\"
								+ new Date().getTime() + ".jpg";
//						IplImage frame = value.getImage();
						IplImage frame = null;
						BufferedImage tag = new BufferedImage(frame.width(),
								frame.height(), BufferedImage.TYPE_3BYTE_BGR);

						tag.getGraphics().drawImage(frame.getBufferedImage(), 0, 0,
								frame.width(), frame.height(), null);
						JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(dataOut);
						try {
							encoder.encode(tag);

							FileOutputStream out1 = new FileOutputStream(path1);
							JPEGImageEncoder encoder1 = JPEGCodec
									.createJPEGEncoder(out1);
							encoder1.encode(tag);
							out1.close();
							dataOut.close();
							out.close();
						} catch (ImageFormatException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						System.out.println("handle: target"+pos);
						pos++;
					}
				}
			}
		});
		
		t.start();
		String fpath = "E:\\videoprocess\\2.avi";
		int inx =1;
		video.DiffProc(fpath, 10, inx);
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
		
		
		// video.start();
	}
}
