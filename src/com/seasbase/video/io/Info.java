package com.seasbase.video.io;


import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;


import org.apache.hadoop.io.WritableComparable;



public class Info implements WritableComparable<Object> {
	int frameid;
	int targetnum;
	public int getTargetnum() {
		return targetnum;
	}
	public void setTargetnum(int targetnum) {
		this.targetnum = targetnum;
	}
	/*public int getSX() {
		return sx;
	}
	public void setSX(int sx) {
		this.sx = sx;
	}
	public int getSY() {
		return sy;
	}
	public void setSY(int sy) {
		this.sy = sy;
	}
	public int getEX() {
		return ex;
	}
	public void setEX(int ex) {
		this.ex = ex;
	}
	public int getEY() {
		return ey;
	}
	public void setEY(int ey) {
		this.ey = ey;
	}*/
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	byte[] image = null;
	public byte[] getImage() {
		return image;
	}
	public void setImage(byte[] image) {
		this.image = image;
	}
	/*int sx;
	int sy;
	int ex;
	int ey;*/
	int x;
	int y;
	int width;
	int height;
	String number;
	String type;
	String color;

	public int getFrameid() {
		return frameid;
	}
	public void setFrameid(int frameid) {
		this.frameid = frameid;
	}
	public String getNumber(){
		 return number;	
		}
	public void setNumber(String number) {
		this.number = number;
	}
		public String getType(){
			 return type;	
			}
		public void setType(String type) {
			this.type = type;
		}
		public String getColor(){
			 return color;	
			}
		public void setColor(String color) {
			this.color = color;
		}
	public Info(){
		
	}

	public void init(Info info) {
		this.frameid = info.frameid;
		this.targetnum = info.targetnum;
		/*this.sx = info.sx;
		this.sy = info.sy;
		this.ex = info.ex;
		this.ey =info. ey;*/
		this.x = info.x;
		this.y = info.y;
		this.width = info.width;
		this.height =info. height;
		this.number = info.number;
		this.type = info.type;
		this.color = info.color;
		this.image = info.image;
	}			
	public Info(int frame,int targetnum, byte[] image,int x, int y, int width,int height, // int sx, int sy, int ex,int ey, 
			String number,String type,String color
			){
		this.frameid = frame;
		this.targetnum = targetnum;
		this.image = image;
		/*this.sx = sx;
		this.sy = sy;
		this.ex = ex;
		this.ey = ey;*/
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.number = number;
		this.type = type;
		this.color = color;
	}

	
@Override
public void readFields(DataInput in) throws IOException {
	frameid = in.readInt();
	  targetnum = in.readInt();
	  x = in.readInt();
	  y = in.readInt();
	  width = in.readInt();
	  height = in.readInt();
	/*  sx = in.readInt();
	  sy = in.readInt(); 
	  ex = in.readInt();
	  ey = in.readInt();*/
	  
	  number = in.readLine();
	  type = in.readLine();
	  color = in.readLine();
	 
	  int imageSize = in.readInt();
	  
	  byte[] bytes = new byte[imageSize];
	   in.readFully(bytes, 0, imageSize);
}

@Override
public void write(DataOutput out) throws IOException {
	out.writeInt(frameid);
	out.writeInt(targetnum);
	
	out.writeInt(x);
	out.writeInt(y);
	out.writeInt(width);
	out.writeInt(height);
	/*out.writeInt(sx);
	out.writeInt(sy);
	out.writeInt(ex);
	out.writeInt(ey);*/
    out.writeChars(number);
	out.writeChars(type);
	out.writeChars(color);

	out.writeInt(image.length);	
	out.write(image);
    
}

@Override
public int compareTo(Object o) {
	return 0;
}

}
