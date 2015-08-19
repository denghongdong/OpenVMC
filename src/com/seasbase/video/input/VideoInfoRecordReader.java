package com.seasbase.video.input;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

import com.redhadoop.io.input.VideoRecordReader;
import com.seasbase.video.io.Info;

public class VideoInfoRecordReader extends RecordReader<Text, Info> {
		
	 	public static final Log LOG = LogFactory.getLog(VideoRecordReader.class);
	 	public String fpath = null;
	 
		public VideoInfoRecordReader(InputSplit split,TaskAttemptContext job) {
			FileSplit sp = (FileSplit)split;
		    Path file = sp.getPath(); 
		    fpath = System.getProperty("java.io.tmpdir")+new Date().getTime()+"-"+sp.getStart()+"-"+sp.getLength()+".avi";
		    FSDataInputStream fs = null;
		    FileOutputStream osw = null;
		    LOG.info("<><><>"+sp.toString()+"-"+sp.getStart()+"-"+sp.getLength());
		    try {
			    FileSystem fileSystem = FileSystem.get(job.getConfiguration());
			    fs = fileSystem.open(file);
			    fs.seek(sp.getStart());
			   
		    	osw = new FileOutputStream(fpath);  
		    	IOUtils.copyBytes(fs,osw,sp.getLength(),false);
		    	//osw.write(buffer,0,buffer.length);  
		    	osw.flush(); 
		    	//file.getFileSystem(job).copyToLocalFile(file,localFile);
			} catch (IOException e) {
				e.printStackTrace();
			}finally {
				IOUtils.closeStream(fs);
				IOUtils.closeStream(osw);
			}
		}

		
	@Override
	public void close() throws IOException {
		File file= new File(fpath);
		file.delete();
	}

	@Override
	public Text getCurrentKey() throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Info getCurrentValue() throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public float getProgress() throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void initialize(InputSplit split, TaskAttemptContext job)
			throws IOException, InterruptedException {
	}

	@Override
	public boolean nextKeyValue() throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		return false;
	}
	
}
