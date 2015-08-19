package com.seasbase.video.io;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.WritableComparable;

public class InfoDesc implements WritableComparable<Object> {
	int targetnum;
	int x;
	int y;
	int width;
	int height;
	float mr;
	float mg;
	float mb;
	float sr;
	float sg;
	float sb;
	float tr;
	float tg;
	float tb;
	float hm;
	float hs;
	float ht;
	
	public InfoDesc()
	{
		
	}
	public InfoDesc(int targetnum,int x, int y, int width,
			int height, float mr, float mg, float mb, float sr, float sg,
			float sb, float tr, float tg, float tb, float hm, float hs, float ht) {
	
		this.targetnum = targetnum;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.mr = mr;
		this.mg = mg;
		this.mb = mb;
		this.sr = sr;
		this.sg = sg;
		this.sb = sb;
		this.tr = tr;
		this.tg = tg;
		this.tb = tb;
		this.hm = hm;
		this.hs = hs;
		this.ht = ht;
	}

	public int getTargetnum() {
		return targetnum;
	}

	public void setTargetnum(int targetnum) {
		this.targetnum = targetnum;
	}

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

	public float getMr() {
		return mr;
	}

	public void setMr(float mr) {
		this.mr = mr;
	}

	public float getMg() {
		return mg;
	}

	public void setMg(float mg) {
		this.mg = mg;
	}

	public float getMb() {
		return mb;
	}

	public void setMb(float mb) {
		this.mb = mb;
	}

	public float getSr() {
		return sr;
	}

	public void setSr(float sr) {
		this.sr = sr;
	}

	public float getSg() {
		return sg;
	}

	public void setSg(float sg) {
		this.sg = sg;
	}

	public float getSb() {
		return sb;
	}

	public void setSb(float sb) {
		this.sb = sb;
	}

	public float getTr() {
		return tr;
	}

	public void setTr(float tr) {
		this.tr = tr;
	}

	public float getTg() {
		return tg;
	}

	public void setTg(float tg) {
		this.tg = tg;
	}

	public float getTb() {
		return tb;
	}

	public void setTb(float tb) {
		this.tb = tb;
	}

	public float getHm() {
		return hm;
	}

	public void setHm(float hm) {
		this.hm = hm;
	}

	public float getHs() {
		return hs;
	}

	public void setHs(float hs) {
		this.hs = hs;
	}

	public float getHt() {
		return ht;
	}

	public void setHt(float ht) {
		this.ht = ht;
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		  targetnum = in.readInt();
		  x = in.readInt();
		  y = in.readInt();


		  mr = in.readFloat();
		  mg = in.readFloat();
		  mb = in.readFloat();
		  sr = in.readFloat();
		  sg = in.readFloat();
		  sb = in.readFloat();
		  tr = in.readFloat();
		  tg = in.readFloat();
		  tb = in.readFloat();
		  
		  hm = in.readFloat();
		  hs = in.readFloat();
		  ht = in.readFloat();
		  width = in.readInt();
		  height = in.readInt();
	}

	@Override
	public void write(DataOutput out) throws IOException {
		out.writeInt(targetnum);
		out.writeInt(x);
		out.writeInt(y);

		out.writeFloat(mr);
		out.writeFloat(mg);
		out.writeFloat(mb);
		out.writeFloat(sr);
		out.writeFloat(sg);
		out.writeFloat(sb);
		out.writeFloat(tr);
		out.writeFloat(tg);
		out.writeFloat(tb);

		out.writeFloat(hm); 
		out.writeFloat(hs);
		out.writeFloat(ht);
		out.writeInt(width);
		out.writeInt(height);
		
	}

	@Override
	public int compareTo(Object arg0) {
		// TODO Auto-generated method stub
		return 0;
	}
}
