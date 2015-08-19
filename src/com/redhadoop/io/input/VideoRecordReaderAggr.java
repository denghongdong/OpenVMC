package com.redhadoop.io.input;

import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_highgui.*;
import static org.bytedeco.javacpp.opencv_imgproc.*;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileSplit;
import org.apache.hadoop.mapred.InputSplit;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.RecordReader;
import org.apache.hadoop.util.ReflectionUtils;

import com.seasbase.video.io.AggrInfo;
import com.seasbase.video.io.VideoReader;

public class VideoRecordReaderAggr implements RecordReader<Text, AggrInfo> {
	
	private long start = 0;	
	private long pos = 0;
	private long curMaxLen = 0;
	
	private long end = 10000;
    private  CvCapture capture;
    protected Configuration conf;
    private String fpath;
    final VideoReader video = new VideoReader();
    List<AggrInfo> lstInfo = new ArrayList<AggrInfo>();
    //Info imageInfo = new Info();
    Thread  t=null;
    boolean bq = true;
    boolean bCompleted = false;
    public static final Log LOG = LogFactory.getLog(VideoRecordReaderAggr.class);


    
    FileOutputStream olog ; 
	public VideoRecordReaderAggr(InputSplit split,JobConf job) {
		FileSplit sp = (FileSplit)split;
	    Path file = sp.getPath(); 
    
	    fpath = System.getProperty("java.io.tmpdir")+new Date().getTime()+"-"+sp.getStart()+"-"+sp.getLength()+".avi";
	   // Path localFile = new Path(fpath);
	    FSDataInputStream fs = null;
	    FileOutputStream osw = null;
	    try {
	    	

		    FileSystem fileSystem = FileSystem.get(job); 
		    fs = fileSystem.open(file);
		    fs.seek(sp.getStart());
		    
	    	osw = new FileOutputStream(fpath);  
	    	IOUtils.copyBytes(fs,osw,sp.getLength(),false);
	    	//osw.write(buffer,0,buffer.length);  
	    	osw.flush(); 
	  
	    	//file.getFileSystem(job).copyToLocalFile(file,localFile);
	    	olog =  new FileOutputStream("e:\\videoprocess\\sasa.txt"); 
	    	
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			IOUtils.closeStream(fs);
			IOUtils.closeStream(osw);
		}
	    
    	PrintWriter pw=new PrintWriter(olog,true);
    	pw.println("<><><>run-begin()");
	    
	}

	@Override
	public void close() throws IOException {
//    	try {
//    		//olog.close();
//			//t.join();
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	public Class<?> getKeyClass() { return Text.class; }
	public Class<?> getValueClass() { return AggrInfo.class;}

	@Override
		  public Text createKey() {
		    return (Text) ReflectionUtils.newInstance(getKeyClass(), conf);
		  }
		  @Override
		  public AggrInfo createValue() {
			  return (AggrInfo) ReflectionUtils.newInstance(getValueClass(), conf);
			  //return imageInfo;
		  }

	@Override
	public long getPos() throws IOException {
		return pos;
	}

	@Override
	public float getProgress() throws IOException {
		if(bCompleted)
			return 1.0f;
		else {
		    return Math.min(0.1f, (pos) / (float)(end));
		}
	}

	@Override
	public boolean next(Text key, AggrInfo value) throws IOException {
		PrintWriter pw=new PrintWriter(olog,true);
		if(t==null){
		    t=new Thread(new Runnable(){
				   public void run(){	
					   
					   video.DiffProc2(fpath, 10, 0);	
				   }
			});
			t.start();
		}

	    try{
	    	lstInfo.clear();
	    	bq = video.retrieveInfoAggr(lstInfo);
	    	curMaxLen += lstInfo.size();
	    }catch (Exception e) {
	     e.printStackTrace();
	    }
		
		
		if(bq == false ) {
			pw.println("<><><>run-DiffProcget1() end "+pos);
			bCompleted = true;
			return false;
		}
			
		//int idx = lstInfo.size()-(int)(curMaxLen-pos);
		if(lstInfo.size() !=0)
			value.init(lstInfo.get(0));
		else
			return false;

		key  = new Text();
        String str = new String();
        key.set(String.valueOf(pos));
		pos++;
		    
	    return true;
		  
	}
	
	
	/*
	void initIOContext(FileSplit split, JobConf job,Class inputFormatClass, RecordReader recordReader) throws IOException {
		super.initIOContext(split, job, inputFormatClass, recordReader);

	    final Path file = split.getPath();
		
	    FileSystem fileSystem = file.getFileSystem(job);
	    
	    String fpath = "/home/hadoop/xxxxx"+new Date().hashCode()+".mp4";
	    Path localFile = new Path(fpath);
	    
	    fileSystem.copyToLocalFile(file,localFile);
        capture = cvCreateFileCapture(fpath);
        
        System.out.println(capture);
	}



	private Text key = null;
	private Image value = null;
	
	private long start;	
	private long pos;
	private String filename;
	private long end;

    private  CvCapture capture;

	@Override
	public void close() throws IOException {
		cvReleaseCapture(capture);
	}

	@Override
	public Object createKey() {
		// TODO Auto-generated method stub
		return key;
	}

	@Override
	public Object createValue() {
		// TODO Auto-generated method stub
		return value;
	}

	@Override
	public long getPos() throws IOException {
		// TODO Auto-generated method stub
		return pos;
	}

	@Override
	public float getProgress() throws IOException {
		if (start == end) {
		    return 0.0f;
		} 
		else {
		    return Math.min(1.0f, (pos - start) / (float)(end - start));
		}
	}

	@Override
	public boolean next(Object arg0, Object arg1) throws IOException {
		long nbFrames;
        nbFrames = (long) cvGetCaptureProperty(capture, CV_CAP_PROP_FRAME_COUNT);
        IplImage frame;
        if (cvGrabFrame(capture) != 0 && (frame = cvRetrieveFrame(capture)) != null) {
        	pos++;
            key = new Text();
            String str = new String();
            key.set(str.valueOf(pos));
            value = new Image(frame);
            System.out.println(value+"--"+value.getImage());
            cvSaveImage("/home/hadoop/zzz1/"+"--"+pos+".jpg", frame);
            return true;
        }else{
        	return false;
        }
	}

	@Override
	public void doClose() throws IOException {
		// TODO Auto-generated method stub
		
	}
	*/
/*
	private Text key = null;
	private Image value = null;
	
	private long start;	
	private long pos;
	private String filename;
	private long end;

    private  CvCapture capture;


	@Override
	public void initialize(InputSplit genericSplit, TaskAttemptContext context) throws IOException, InterruptedException {
		FileSplit split = (FileSplit) genericSplit;
	    Configuration conf = context.getConfiguration();
	    final Path file = split.getPath();
		
	    FileSystem fileSystem = file.getFileSystem(conf);
	    
	    String fpath = "/home/hadoop/xxxxx"+new Date().hashCode()+".mp4";
	    Path localFile = new Path(fpath);
	    
	    fileSystem.copyToLocalFile(file,localFile);
        capture = cvCreateFileCapture(fpath);
        
        System.out.println(capture);
	}
	
	@Override
	public void close() throws IOException {
		cvReleaseCapture(capture);
	}

	@Override
	public Text getCurrentKey() throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		return key;
	}

	@Override
	public Image getCurrentValue() throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		return value;
	}

	@Override
	public boolean nextKeyValue() throws IOException, InterruptedException { 
		long nbFrames;
        nbFrames = (long) cvGetCaptureProperty(capture, CV_CAP_PROP_FRAME_COUNT);
        IplImage frame;
        if (cvGrabFrame(capture) != 0 && (frame = cvRetrieveFrame(capture)) != null) {
        	pos++;
            key = new Text();
            String str = new String();
            key.set(str.valueOf(pos));
            value = new Image(frame);
            System.out.println(value+"--"+value.getImage());
            cvSaveImage("/home/hadoop/zzz1/"+"--"+pos+".jpg", frame);
            return true;
        }else{
        	return false;
        }
        
	}

	
	@Override
	public float getProgress() throws IOException, InterruptedException {
		if (start == end) {
		    return 0.0f;
		} 
		else {
		    return Math.min(1.0f, (pos - start) / (float)(end - start));
		}
	}
*/
}
