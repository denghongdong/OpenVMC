package com.redhadoop.io.obj;

import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_highgui.*;
import static org.bytedeco.javacpp.opencv_imgproc.*;
import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.opencv_core;
import java.io.Serializable;

public class WindowInfo
  implements Serializable
{
  private int parentWidth = -1;

  private int parentHeight = -1;

  private int parentXOffset = -1;

  private int parentYOffset = -1;

  private int width = -1;

  private int height = -1;

  private int borderTop = -1;
  private int borderBottom = -1;
  private int borderLeft = -1;
  private int borderRight = -1;

  public void setParentInfo(int parentXOffset, int parentYOffset, int parentHeight, int parentWidth)
  {
    this.parentWidth = parentWidth;

    this.parentHeight = parentHeight;

    this.parentXOffset = parentXOffset;

    this.parentYOffset = parentYOffset;
  }
  public boolean isParentInfoValid() {
    if ((this.parentWidth < 0) || (this.parentHeight < 0) || (this.parentXOffset < 0) || (this.parentYOffset < 0)) {
      return false;
    }

    return true;
  }
  public void setBorder(int borderTop, int borderBottom, int borderLeft, int borderRight) {
    this.borderTop = borderTop;
    this.borderBottom = borderBottom;
    this.borderLeft = borderLeft;
    this.borderRight = borderRight;
  }
  public boolean isBorderValid() {
    if ((this.borderTop < 0) || (this.borderBottom < 0) || (this.borderLeft < 0) || (this.borderRight < 0)) {
      return false;
    }
    return true;
  }
  public void setWindowSize(int height, int width) {
    this.height = height;
    this.width = width;
  }
  public boolean isWindowSizeValid() {
    if ((this.height < 0) || (this.width < 0)) {
      return false;
    }
    return true;
  }

  public opencv_core.CvRect computeROI() {
    int newX = this.parentXOffset - this.borderLeft;

    int newY = this.parentYOffset - this.borderTop;

    int newWidth = this.width + this.borderLeft + this.borderRight;

    int newHeight = this.height + this.borderTop + this.borderBottom;

    return opencv_core.cvRect(newX, newY, newWidth, newHeight);
  }

  public int getWidth() {
    return this.width;
  }
  public int getHeight() {
    return this.height;
  }

  public int getParentWidth() {
    return this.parentWidth;
  }

  public int getParentHeight() {
    return this.parentHeight;
  }

  public int getParentXOffset() {
    return this.parentXOffset;
  }
  public int getParentYOffset() {
    return this.parentYOffset;
  }
  public int getBorderTop() {
    return this.borderTop;
  }

  public int getBorderBottom()
  {
    return this.borderBottom;
  }
  public int getBorderLeft() {
    return this.borderLeft;
  }

  public int getBorderRight() {
    return this.borderRight;
  }
}