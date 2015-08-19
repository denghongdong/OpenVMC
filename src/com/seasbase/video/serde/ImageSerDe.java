package com.seasbase.video.serde;

import static org.bytedeco.javacpp.opencv_highgui.cvSaveImage;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hive.serde.serdeConstants;
import org.apache.hadoop.hive.serde2.SerDe;
import org.apache.hadoop.hive.serde2.SerDeException;
import org.apache.hadoop.hive.serde2.SerDeStats;
import org.apache.hadoop.hive.serde2.binarysortable.InputByteBuffer;
import org.apache.hadoop.hive.serde2.io.ByteWritable;
import org.apache.hadoop.hive.serde2.lazy.ByteArrayRef;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.typeinfo.StructTypeInfo;
import org.apache.hadoop.hive.serde2.typeinfo.TypeInfo;
import org.apache.hadoop.hive.serde2.typeinfo.TypeInfoFactory;
import org.apache.hadoop.hive.serde2.typeinfo.TypeInfoUtils;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;

import com.seasbase.video.io.Info;


public class ImageSerDe implements SerDe {
  
  private StructTypeInfo rowTypeInfo;
  private ObjectInspector rowOI;
  private List<String> colNames;
  private List<Object> row = new ArrayList<Object>();
  static  int frameid = 0;
  //InputByteBuffer inputByteBuffer = new InputByteBuffer();
  
  @Override
  public void initialize(Configuration conf, Properties tbl) throws SerDeException {
    // Get a list of the table's column names.
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
  public Object deserialize(Writable blob) throws SerDeException {
    row.clear();
    
    Info info= (Info) blob; 

    row.add(frameid++);
    row.add(info.getTargetnum());
    row.add(info.getX());
    row.add(info.getY());
    row.add(info.getWidth());
    row.add(info.getHeight());
    row.add(String.valueOf(info.getMR())+","+String.valueOf(info.getMG())+","+String.valueOf(info.getMB()));
    row.add(info.getMR());
    row.add(info.getMG());
    row.add(info.getMB());
    row.add(info.getHm());
    row.add(info.getHs());
    row.add(info.getHt());	 
    byte[] ibyte = null;
	try {
		ibyte = serialize(info);
	} catch (IOException e) {
		e.printStackTrace();
	}
	//BytesWritable b = new BytesWritable();
	//b.set(ibyte, 0, ibyte.length);
    row.add(ibyte);
    
//	if((frameid+1)%20==0){
//	String fname = "E:\\hadoop\\image\\ser"+frameid + ".jpg";
//	
//
//	int[]  params = new int[3];
//	params[0] = 1;params[1] = 85;params[2] = 0;
//	cvSaveImage(fname, info.getImage(),params);
//	}
    
    return row;
    
   /* if (byteArrayRef == null) {
        byteArrayRef = new ByteArrayRef();
      }
      if (field instanceof BytesWritable) {
        BytesWritable b = (BytesWritable) field;
        if (b.getLength() == 0) {
          return null;
        }
        // For backward-compatibility with hadoop 0.17
        byteArrayRef.setData(b.getBytes());
        cachedLazyBinaryStruct.init(byteArrayRef, 0, b.getLength());
      } else if (field instanceof Text) {
        Text t = (Text) field;
        if (t.getLength() == 0) {
          return null;
        }
        byteArrayRef.setData(t.getBytes());
        cachedLazyBinaryStruct.init(byteArrayRef, 0, t.getLength());
      } else {
        throw new SerDeException(getClass().toString()
            + ": expects either BytesWritable or Text object!");
      }
      lastOperationSerialize = false;
      lastOperationDeserialize = true;
      return cachedLazyBinaryStruct;*/
      
  }
  
 
  
  public static byte[] serialize(Writable writable) throws IOException {
	    ByteArrayOutputStream out = new ByteArrayOutputStream();
	    DataOutputStream dataOut = new DataOutputStream(out);
	    writable.write(dataOut);
	    dataOut.close();
	    return out.toByteArray();
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
  

  @Override
  public ObjectInspector getObjectInspector() throws SerDeException {
    return rowOI;
  }

  /**
   * Unimplemented
   */
  @Override
  public SerDeStats getSerDeStats() {
    return null;
  }


  @Override
  public Class<? extends Writable> getSerializedClass() {
    return BytesWritable.class;
  }

@Override
public Writable serialize(Object obj, ObjectInspector objInspector)
		throws SerDeException {
	System.out.println(obj);
	return new Text("abc");
}


}
