package com.simple.orchestrator.hadoop.io.format;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.logging.Logger;

import org.apache.hadoop.conf.Configurable;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.util.Base64;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.LineRecordReader;

import com.google.common.base.Charsets;
import com.simple.orchestrator.hadoop.Utils;
import com.simple.orchestrator.hadoop.config.HttpInputConf;

public class HttpInputFormat extends FileInputFormat<LongWritable, Text>
		implements Configurable {

	private static final Logger logger = Logger.getLogger(HttpInputFormat.class
			.getName());

	private Configuration configuration;

	@Override
	public void setConf(Configuration configuration) {
		this.configuration = configuration;
	}

	@Override
	public Configuration getConf() {
		return configuration;
	}

	/**
	 * This will fetch the data from the url and then output it to a temp file
	 * in hdfs so that we can call the fileinputformat to break it apart.
	 * 
	 */
	@Override
	public List<InputSplit> getSplits(JobContext context) throws IOException {
		String requestUrl = configuration.get(HttpInputConf.WEB_URL_PROPERTY);
		if (requestUrl == null) {
			logger.info("No request url was specified for HttpInputFormat");
			return null;
		}

		String encodedUrl = Base64.encodeBytes(requestUrl.getBytes());

		Path path = new Path("/tmp/httpadapter/datadump");
		context.getConfiguration().set(INPUT_DIR, path.toString());

		logger.info("Writing httpadapter output to " + path.toString());
		doGet(requestUrl, path);

		return super.getSplits(context);
	}
	
	@Override
	public RecordReader<LongWritable, Text> createRecordReader(
			InputSplit split, TaskAttemptContext context) throws IOException,
			InterruptedException {
	    String delimiter = context.getConfiguration().get(
	            "textinputformat.record.delimiter");
	        byte[] recordDelimiterBytes = null;
	        if (null != delimiter)
	          recordDelimiterBytes = delimiter.getBytes(Charsets.UTF_8);
	        return new LineRecordReader(recordDelimiterBytes);
	}



	private void doGet(String requestUrl, Path outputPath) throws IOException {
		URL url;
		HttpURLConnection conn;
		FileSystem fs = null;
		FSDataOutputStream outputStream = null;
		try {
			fs = FileSystem.get(getConf());
			outputStream = fs.create(outputPath);

			url = new URL(requestUrl);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			Utils.copyStream(conn.getInputStream(), outputStream);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fs != null)
				fs.close();
			if (outputStream != null)
				outputStream.close();
		}
	}
	
	/*

	public static void setInput(Job job, String url) {
		job.getConfiguration().set(HttpInputConf.WEB_URL_PROPERTY, url);
		job.setInputFormatClass(HttpInputFormat.class);
	}
	*/

}
