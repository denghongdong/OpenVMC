package com.seasbase.video.serde;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hive.serde.serdeConstants;
import org.apache.hadoop.hive.serde2.SerDe;
import org.apache.hadoop.hive.serde2.SerDeException;
import org.apache.hadoop.hive.serde2.SerDeStats;
import org.apache.hadoop.hive.serde2.binarysortable.InputByteBuffer;
import org.apache.hadoop.hive.serde2.io.ByteWritable;
import org.apache.hadoop.hive.serde2.lazy.ByteArrayRef;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.typeinfo.ListTypeInfo;
import org.apache.hadoop.hive.serde2.typeinfo.StructTypeInfo;
import org.apache.hadoop.hive.serde2.typeinfo.TypeInfo;
import org.apache.hadoop.hive.serde2.typeinfo.TypeInfoFactory;
import org.apache.hadoop.hive.serde2.typeinfo.TypeInfoUtils;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;

import com.redhadoop.io.input.VideoRecordReaderAggr;
import com.seasbase.video.io.AggrInfo;
import com.seasbase.video.io.Info;
import com.seasbase.video.io.InfoDesc;


public class ImageSerDeAggr implements SerDe {
  
  private StructTypeInfo rowTypeInfo;
  private ObjectInspector rowOI;
  private List<String> colNames;
  private List<Object> row = new ArrayList<Object>();
  static  int frameid = 0;
//  List<Integer> target = new ArrayList<Integer>();
//  List<Integer> x = new ArrayList<Integer>();
//  List<Integer> y = new ArrayList<Integer>();
//  List<Integer> width = new ArrayList<Integer>();
//  List<Integer> height = new ArrayList<Integer>();
//
//	ArrayList<Float> hm = new ArrayList<Float>();
//	ArrayList<Float> hs = new ArrayList<Float>();
//	ArrayList<Float> ht = new ArrayList<Float>();
  FileOutputStream olog ; 
  
  @Override
  public void initialize(Configuration conf, Properties tbl) throws SerDeException {
    // Get a list of the table's column names.
    String colNamesStr = tbl.getProperty(serdeConstants.LIST_COLUMNS);
    colNames = Arrays.asList(colNamesStr.split(","));
    try {
		olog =  new FileOutputStream("e:\\videoprocess\\zizi.txt");
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
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
	  PrintWriter pw=new PrintWriter(olog,true);
  	pw.println("<><><>run-begin()");
	    
    row.clear();
    AggrInfo info= (AggrInfo) blob; 
    int count = info.getLstInfo().size();
    
//    List<Integer> target = new ArrayList<Integer>(count);
//    List<Integer> x = new ArrayList<Integer>(count);
//    List<Integer> y = new ArrayList<Integer>(count);
//    List<Integer> width = new ArrayList<Integer>(count);
//    List<Integer> height = new ArrayList<Integer>(count);
//
//	ArrayList<Float> hm = new ArrayList<Float>(count);
//	ArrayList<Float> hs = new ArrayList<Float>(count);
//	ArrayList<Float> ht = new ArrayList<Float>(count);
    int ii = 0;
//    target.clear();
//    x.clear();
//    y.clear();
//    width.clear();
//    height.clear();
//    hm.clear();
//    hs.clear();
//    ht.clear();
//    for(InfoDesc in : info.getLstInfo())
//    {
//    	target.add(ii, in.getTargetnum());
//    	x.add(ii,in.getX());
//    	y.add(ii, in.getY());
//    	width.add(ii,in.getWidth());
//    	height.add(ii,in.getHeight());
//    	hm.add(ii, in.getHm());
//    	hs.add(ii,in.getHs());
//    	ht.add(ii,in.getHt());
//    	ii++;
//    	pw.println("<><><>run-deserialize()"+ii);
//    }
  
    row.add(info.getFrameid());
    ///////////////////////////////
    List<Object> lstRow = new ArrayList<Object>(info.getLstInfo().size());
    String columnName = "detaillist";
    ListTypeInfo columnType = (ListTypeInfo)rowTypeInfo.getStructFieldTypeInfo(columnName);
    StructTypeInfo elemTypeInfo = (StructTypeInfo)columnType.getListElementTypeInfo();
    ArrayList<TypeInfo> structTypes = elemTypeInfo.getAllStructFieldTypeInfos(); 
   ArrayList<String> structNames = elemTypeInfo.getAllStructFieldNames(); 
   
   
   for(InfoDesc in : info.getLstInfo()) 
   {
	   List<Object> structRow = new ArrayList<Object>(structTypes.size());
	   for (int i = 0; i < structNames.size(); i++)
	   {
		   String field = structNames.get(i).toLowerCase();
		
		   if  (field.equals("targetnum")){
			   structRow.add(in.getTargetnum()); 
			   pw.println("<><><>run-deserialize()11"+in.getTargetnum());
		   }
		   if  (field.equals("x"))
			   structRow.add(in.getX()); 
		   if  (field.equals("y"))
			   structRow.add(in.getY()); 
		   if  (field.equals("width"))
			   structRow.add(in.getWidth()); 
		   if  (field.equals("height"))
			   structRow.add(in.getHeight()); 
		   if  (field.equals("hs"))
			   structRow.add(in.getHm()); 
		   if  (field.equals("hm"))
			   structRow.add(in.getHs()); 
		   if  (field.equals("ht"))
			   structRow.add(in.getHt());	   
	   }
	   lstRow.add(structRow);
	   //structRow.clear();
   }
   row.add(lstRow);
    ///////////////////////////////
//    row.add(target);
//    row.add(x);
//    row.add(y);
//    row.add(width);
//    row.add(height);
//    row.add(hm);
//    row.add(hs);
//    row.add(ht);
   
    row.add(info.getImage().toString());
    pw.println("<><><>run-deserialize()"+info.getFrameid());
   
    return row;
     
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
    return Text.class;
  }

@Override
public Writable serialize(Object obj, ObjectInspector objInspector)
		throws SerDeException {
	System.out.println(obj);
	return new Text("abc");
}


}
