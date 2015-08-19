package com.seasbase.video.input;

import java.io.IOException;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;

import com.seasbase.video.io.Info;

public class VideoInfoInputFormat extends FileInputFormat<Text, Info> {

	@Override
	protected boolean isSplitable(JobContext context, Path filename) {
		return super.isSplitable(context, filename);
	}

	@Override
	public RecordReader<Text, Info> createRecordReader(InputSplit split,
			TaskAttemptContext job) throws IOException, InterruptedException {
		return (RecordReader) new VideoInfoRecordReader(split,job);
	}
	
}
