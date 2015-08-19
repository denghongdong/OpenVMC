package com.redhadoop.io.obj;

import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.opencv_core;

import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_highgui.*;
import static org.bytedeco.javacpp.opencv_imgproc.*;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serializable;
import java.nio.ByteBuffer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableUtils;


public class Image implements Writable, Serializable
{
  private static final Log LOG = LogFactory.getLog(Image.class);

  private opencv_core.IplImage image = null;
  private WindowInfo window = null;
  private byte[] bytes =  new byte[1024*300];

  public Image()
  {
  }

  public Image(opencv_core.IplImage image)
  {
    this.image = image;
    this.window = new WindowInfo();
  }

  public void init(opencv_core.IplImage image) {
    this.image = image;
    this.window = new WindowInfo();
  }

  public Image(int height, int width, int depth, int nChannels)
  {
    this.image = opencv_core.cvCreateImage(opencv_core.cvSize(width, height), depth, nChannels);
    this.window = new WindowInfo();
  }

  public Image(opencv_core.IplImage image, WindowInfo window)
  {
    this.image = image;
    this.window = window;
  }

  public opencv_core.IplImage getImage() {
    return this.image;
  }

  public WindowInfo getWindow()
  {
    return this.window;
  }

  public int getDepth()
  {
    return this.image.depth();
  }

  public int getNumChannel()
  {
    return this.image.nChannels();
  }

  public int getHeight()
  {
    return this.image.height();
  }

  public int getWidth()
  {
    return this.image.width();
  }

  public int getWidthStep()
  {
    return this.image.widthStep();
  }

  public int getImageSize()
  {
    return this.image.imageSize();
  }

  public void insertImage(Image sourceImage)
  {
    opencv_core.IplImage img1 = this.image;
    opencv_core.IplImage img2 = sourceImage.getImage();
    WindowInfo win = sourceImage.getWindow();

    if (win.isParentInfoValid()) {
      opencv_core.cvSetImageROI(
        img1, 
        opencv_core.cvRect(win.getParentXOffset(), win.getParentYOffset(), 
        win.getWidth(), win.getHeight()));
    }

    if (win.isBorderValid()) {
      opencv_core.cvSetImageROI(
        img2, 
        opencv_core.cvRect(win.getBorderLeft(), win.getBorderTop(), 
        win.getWidth(), win.getHeight()));
    }

    opencv_core.cvCopy(img2, img1, null);

    opencv_core.cvResetImageROI(img1);
  }

  public void readFields(DataInput in)
    throws IOException
  {
    int height = WritableUtils.readVInt(in);
    int width = WritableUtils.readVInt(in);
    int depth = WritableUtils.readVInt(in);
    int nChannels = WritableUtils.readVInt(in);
    int imageSize = WritableUtils.readVInt(in);

    int windowXOffest = WritableUtils.readVInt(in);
    int windowYOffest = WritableUtils.readVInt(in);
    int windowHeight = WritableUtils.readVInt(in);
    int windowWidth = WritableUtils.readVInt(in);

    int top = WritableUtils.readVInt(in);
    int bottom = WritableUtils.readVInt(in);
    int left = WritableUtils.readVInt(in);
    int right = WritableUtils.readVInt(in);

    int h = WritableUtils.readVInt(in);
    int w = WritableUtils.readVInt(in);
 
    this.window = new WindowInfo();
    this.window.setParentInfo(windowXOffest, windowYOffest, windowHeight, 
      windowWidth);
    this.window.setBorder(top, bottom, left, right);
    this.window.setWindowSize(h, w);

    if(imageSize > 1024*300)
    	this.bytes = new byte[imageSize];
    in.readFully(this.bytes, 0, imageSize);
      
    this.image = opencv_core.cvCreateImage(opencv_core.cvSize(width, height), depth, nChannels);
    this.image.imageData(new BytePointer(this.bytes));

  }

  public void write(DataOutput out)
    throws IOException
  {
    WritableUtils.writeVInt(out, this.image.height());
    WritableUtils.writeVInt(out, this.image.width());
    WritableUtils.writeVInt(out, this.image.depth());
    WritableUtils.writeVInt(out, this.image.nChannels());
    WritableUtils.writeVInt(out, this.image.imageSize());

    WritableUtils.writeVInt(out, this.window.getParentXOffset());
    WritableUtils.writeVInt(out, this.window.getParentYOffset());
    WritableUtils.writeVInt(out, this.window.getParentHeight());
    WritableUtils.writeVInt(out, this.window.getParentWidth());

    WritableUtils.writeVInt(out, this.window.getBorderTop());
    WritableUtils.writeVInt(out, this.window.getBorderBottom());
    WritableUtils.writeVInt(out, this.window.getBorderLeft());
    WritableUtils.writeVInt(out, this.window.getBorderRight());

    WritableUtils.writeVInt(out, this.window.getHeight());
    WritableUtils.writeVInt(out, this.window.getWidth());

    ByteBuffer buffer = this.image.getByteBuffer();
    while (buffer.hasRemaining())
      out.writeByte(buffer.get());
  }
}