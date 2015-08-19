package com.redhadoop.io.input;

import java.io.IOException;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.InputSplit;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.RecordReader;
import org.apache.hadoop.mapred.Reporter;

import com.seasbase.video.io.AggrInfo;


public class VideoInputFormatAggr extends FileInputFormat<Text, AggrInfo> {
	public RecordReader<Text, AggrInfo> getRecordReader(InputSplit split,JobConf job, Reporter reporter) throws IOException {
		return (RecordReader) new VideoRecordReaderAggr(split,job);
	}

	@Override
	protected boolean isSplitable(FileSystem fs, Path filename) {
		return true;
	}
	
	
}
