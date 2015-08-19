package com.redhadoop.hive.udtf;

import static org.bytedeco.javacpp.opencv_highgui.cvSaveImage;

import org.apache.commons.codec.binary.Base64;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDTF;
import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.ql.exec.UDFArgumentLengthException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.objectinspector.StructObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;
import org.bytedeco.javacpp.opencv_core.IplImage;

import com.redhadoop.io.obj.Image;


import com.seasbase.video.io.Info;
import com.seasbase.video.io.VideoReader;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TargetInfo extends GenericUDTF {

	VideoReader video = new VideoReader();
	Object[] newRes = new Object[14];
	@Override
	public void close() throws HiveException {
		video.Release();

	}
    @Override
    public StructObjectInspector initialize(ObjectInspector[] args)
            throws UDFArgumentException { 
        ArrayList<String> fieldNames = new ArrayList<String>();
        ArrayList<ObjectInspector> fieldOIs = new ArrayList<ObjectInspector>();
        fieldNames.add("frameid");
        fieldOIs.add(PrimitiveObjectInspectorFactory.javaIntObjectInspector);
        fieldNames.add("targetnum");
        fieldOIs.add(PrimitiveObjectInspectorFactory.javaIntObjectInspector);
        fieldNames.add("x");
        fieldOIs.add(PrimitiveObjectInspectorFactory.javaIntObjectInspector);
        fieldNames.add("y");
        fieldOIs.add(PrimitiveObjectInspectorFactory.javaIntObjectInspector);
        fieldNames.add("width");
        fieldOIs.add(PrimitiveObjectInspectorFactory.javaIntObjectInspector);
        fieldNames.add("height");
        fieldOIs.add(PrimitiveObjectInspectorFactory.javaIntObjectInspector);
        fieldNames.add("RGB");
        fieldOIs.add(PrimitiveObjectInspectorFactory.javaStringObjectInspector);
        fieldNames.add("R");
        fieldOIs.add(PrimitiveObjectInspectorFactory.javaFloatObjectInspector);
        fieldNames.add("G");
        fieldOIs.add(PrimitiveObjectInspectorFactory.javaFloatObjectInspector);
        fieldNames.add("B");
        fieldOIs.add(PrimitiveObjectInspectorFactory.javaFloatObjectInspector);
        fieldNames.add("HM");
        fieldOIs.add(PrimitiveObjectInspectorFactory.javaFloatObjectInspector);
        fieldNames.add("HS");
        fieldOIs.add(PrimitiveObjectInspectorFactory.javaFloatObjectInspector);
        fieldNames.add("HT");
        fieldOIs.add(PrimitiveObjectInspectorFactory.javaFloatObjectInspector);
        fieldNames.add("frame");
        fieldOIs.add(PrimitiveObjectInspectorFactory.javaStringObjectInspector);//JavaIntObjectInspector
        return ObjectInspectorFactory.getStandardStructObjectInspector(fieldNames,fieldOIs);
    }
	@Override
	public void process(Object[] arg0) throws HiveException {
	    
		byte[] bytes = (byte[]) arg0[1];
		int inx = Integer.parseInt(arg0[0].toString());

		Image image = new Image();
		// deserialize(image, bytes);
		ByteArrayInputStream in = new ByteArrayInputStream(bytes);
		// create DataInputStream
		DataInputStream datain = new DataInputStream(in);
		// read fields

		try {
			image.readFields(datain);

			datain.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		IplImage frame = image.getImage();
		List<Info> lstInfo = new ArrayList<Info>();

		video.DiffProc("", 10, inx);
		int i = 0;
		for (Info info : lstInfo) {
			newRes[0] = inx;
			newRes[1] = info.getTargetnum();
			newRes[2] = info.getX();
			newRes[3] = info.getY();
			newRes[4] = info.getWidth();
			newRes[5] = info.getHeight();
		//			newRes[6] = String.valueOf(info.getMR())+","+String.valueOf(info.getMG())+","+String.valueOf(info.getMB());
//			newRes[7] = info.getMR();
//			newRes[8] = info.getMG();
//			newRes[9] = info.getMB();
//			newRes[10] = info.getHm();
//			newRes[11] = info.getHs();
//			newRes[12] = info.getHt();			
//			String fname = "E:\\hadoop\\image\\ser" + new Date().getTime()+i + ".jpg";
			
			i++;
			int[]  params = new int[3];
			params[0] = 1;params[1] = 85;params[2] = 0;
//			cvSaveImage(fname, info.getImage(),params);

			File f = null;//new File(fname);
			byte[] buffer = new byte[(int) f.length()];
			if (f.exists()) {

				try {
					FileInputStream fis = new FileInputStream(f);

					fis.read(buffer);
					fis.close();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String dest = Base64.encodeBase64String(buffer);

				Pattern p = Pattern.compile("\r\n");
			    Matcher m = p.matcher( dest);
	
		        newRes[13] =  m.replaceAll("");
			}
			//newRes[13] = fname;
			forward(newRes);
		}
		lstInfo.clear();
	}

}
