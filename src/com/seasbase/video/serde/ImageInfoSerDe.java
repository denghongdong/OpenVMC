package com.seasbase.video.serde;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hive.serde.serdeConstants;
import org.apache.hadoop.hive.serde2.AbstractSerDe;
import org.apache.hadoop.hive.serde2.SerDeException;
import org.apache.hadoop.hive.serde2.SerDeStats;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.typeinfo.StructTypeInfo;
import org.apache.hadoop.hive.serde2.typeinfo.TypeInfo;
import org.apache.hadoop.hive.serde2.typeinfo.TypeInfoFactory;
import org.apache.hadoop.hive.serde2.typeinfo.TypeInfoUtils;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.bytedeco.javacpp.opencv_core.IplImage;

import com.seasbase.video.io.Info;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

public class ImageInfoSerDe extends AbstractSerDe {
	  private StructTypeInfo rowTypeInfo;
	  private ObjectInspector rowOI;
	  private List<String> colNames;
	  private List<Object> row = new ArrayList<Object>();
//	  static  int frameid = 0;
	  
	@Override
	public Object deserialize(Writable arg0) throws SerDeException {
		  row.clear();
		    
		    Info info= (Info) arg0; 

		    row.add(info.getFrameid());
		    row.add(info.getTargetnum());
		    row.add(info.getX());
		    row.add(info.getY());
		    row.add(info.getWidth());
		    row.add(info.getHeight());
		   /* row.add(String.valueOf(info.getMR())+","+String.valueOf(info.getMG())+","+String.valueOf(info.getMB()));
		    row.add(info.getMR());
		    row.add(info.getMG());
		    row.add(info.getMB());
		    row.add(info.getHm());
		    row.add(info.getHs());
		    row.add(info.getHt());	*/ 
		    row.add(info.getNumber());
		    row.add(info.getType());
		    row.add(info.getColor());
		
//		    byte[] ibyte = null;
//			try {
//				ibyte = serialize(info);
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
			//BytesWritable b = new BytesWritable();
			//b.set(ibyte, 0, ibyte.length);
		    row.add(info.getImage());
		    return row;
	}
	  public static byte[] serialize(Writable writable) throws IOException {
		    ByteArrayOutputStream out = new ByteArrayOutputStream();
		    DataOutputStream dataOut = new DataOutputStream(out);
		    //writable.write(dataOut);
		    Info in = (Info)writable;
//		    IplImage  image = in.getImage();
//		    
//		     BufferedImage tag = new BufferedImage(image.width(), image.height(), BufferedImage.TYPE_3BYTE_BGR);    
//		    
//		     tag.getGraphics().drawImage(image.getBufferedImage(), 0, 0, image.width(), image.height(), null);      
//		     JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(dataOut);  
//		     encoder.encode(tag); 
//		     String path1= "e:\\videoprocess\\image\\"+new Date().getTime()+".jpg";
//		     FileOutputStream out1 = new FileOutputStream(path1);    
//		     JPEGImageEncoder encoder1 = JPEGCodec.createJPEGEncoder(out1);    
//		     encoder1.encode(tag); 
//		     out1.close();
		     dataOut.close();

		    return out.toByteArray();
		}
	@Override
	public ObjectInspector getObjectInspector() throws SerDeException {
		// TODO Auto-generated method stub
		return rowOI;
	}

	@Override
	public SerDeStats getSerDeStats() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<? extends Writable> getSerializedClass() {
		return BytesWritable.class;
	}

	@Override
	public void initialize(Configuration conf, Properties tbl)
			throws SerDeException {
		 String colNamesStr = tbl.getProperty(serdeConstants.LIST_COLUMNS);
		    colNames = Arrays.asList(colNamesStr.split(","));
		    
		    // Get a list of TypeInfos for the columns. This list lines up with
		    // the list of column names.
		    String colTypesStr = tbl.getProperty(serdeConstants.LIST_COLUMN_TYPES);
		    List<TypeInfo> colTypes = TypeInfoUtils.getTypeInfosFromTypeString(colTypesStr);
		    
		    rowTypeInfo =
		        (StructTypeInfo) TypeInfoFactory.getStructTypeInfo(colNames, colTypes);
		    rowOI =
		        TypeInfoUtils.getStandardJavaObjectInspectorFromTypeInfo(rowTypeInfo);
		
	}

	@Override
	public Writable serialize(Object arg0, ObjectInspector arg1) throws SerDeException {
		return new Text("abc");
	}

	
}
