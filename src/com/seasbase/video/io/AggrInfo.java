package com.seasbase.video.io;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.io.WritableComparable;

public class AggrInfo implements WritableComparable<Object> {
	public int getFrameid() {
		return frameid;
	}

	public void setFrameid(int frameid) {
		this.frameid = frameid;
	}

	public List<InfoDesc> getLstInfo() {
		return lstInfo;
	}

	public void setLstInfo(List<InfoDesc> lstInfo) {
		this.lstInfo = lstInfo;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	int frameid;
	List<InfoDesc> lstInfo = new ArrayList<InfoDesc>();
	String image = null;

	public AggrInfo(int frameid, String image)
	{
		this.frameid = frameid;
		//this.lstInfo = lstInfo;
		this.image = image;
	}
	public AggrInfo()
	{

	}
	public void init(AggrInfo info) {
		this.frameid = info.frameid;
		this.lstInfo = info.lstInfo;
		this.image = info.image;
	}
	
	@Override
	public void readFields(DataInput in) throws IOException {
		frameid = in.readInt();
		  int targetcount = in.readInt();
		  for(int i = 0; i<targetcount; i++)
		  {
			  InfoDesc info = new InfoDesc();
			  info.readFields(in);
			  lstInfo.add(info);  
		  }
		  int imageSize = in.readInt();
		  
		  image = in.readUTF();
		
	}

	@Override
	public void write(DataOutput out) throws IOException {
		out.writeInt(frameid);
		
		out.writeInt(lstInfo.size());
		for(InfoDesc info: lstInfo)
		{
			info.write(out);
		}
		out.writeUTF(image);


		
	}

	@Override
	public int compareTo(Object arg0) {
		// TODO Auto-generated method stub
		return 0;
	}
}
