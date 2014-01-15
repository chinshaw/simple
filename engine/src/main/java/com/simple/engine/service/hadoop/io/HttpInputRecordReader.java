package com.simple.engine.service.hadoop.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;

public class HttpInputRecordReader extends RecordReader<LongWritable, Text> {

	private static final Log LOG = LogFactory.getLog(HttpInputRecordReader.class);

	private final String requestUrl;

	private String urlResult;

	private long pos;
	private Scanner lineScanner;
	private LongWritable key;
	private Text value;

	public HttpInputRecordReader(String requestUrl) {
		this.requestUrl = requestUrl;
	}

	@Override
	public void initialize(InputSplit split, TaskAttemptContext context) throws IOException, InterruptedException {
		urlResult = doGet(requestUrl);
		lineScanner = new Scanner(urlResult);
		//FileUtils.writeStringToFile(new File("/tmp/stocks.txt"), urlResult);
		//System.out.println("Result is " + urlResult);
	}

	@Override
	public boolean nextKeyValue() throws IOException, InterruptedException {
		if (! lineScanner.hasNext()) {
			key = null;
			value = null;
			
			System.err.println("NO MORE LINES");
			return false;
		}
		
		if (key == null) {
			key = new LongWritable();
		}
		if (value == null) {
			value = new Text();
		}

		key.set(pos++);
		value.set(lineScanner.nextLine());
		System.err.println("YES MORE LINES");
		return true;
	}

	@Override
	public LongWritable getCurrentKey() throws IOException, InterruptedException {
		return key;
	}

	@Override
	public Text getCurrentValue() throws IOException, InterruptedException {
		return value;
	}

	@Override
	public float getProgress() throws IOException, InterruptedException {
		return 0;
	}

	@Override
	public void close() throws IOException {
	}

	private String doGet(String requestUrl) {
		URL url;
		HttpURLConnection conn;
		BufferedReader rd;
		String line;
		String result = "";
		try {
			url = new URL(requestUrl);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			while ((line = rd.readLine()) != null) {
				result += line + "\n"; // Have to re-add the newline since realine chomps it.
			}
			rd.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

}
