package com.simple.engine.service.hadoop;

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

public class HttpRecordReader extends RecordReader<LongWritable, Text> {

	private static final Log LOG = LogFactory.getLog(HttpRecordReader.class);

	private final String requestUrl;

	private String urlResult;

	private long pos;
	private Scanner lineScanner;
	private LongWritable key;
	private Text value;

	public HttpRecordReader(String requestUrl) {
		this.requestUrl = requestUrl;
	}

	@Override
	public void initialize(InputSplit split, TaskAttemptContext context) throws IOException, InterruptedException {
		urlResult = doGet(requestUrl);
		lineScanner = new Scanner(urlResult);
		FileUtils.writeStringToFile(new File("/tmp/stocks.txt"), urlResult);
		System.out.println("Result is " + urlResult);
	}

	@Override
	public boolean nextKeyValue() throws IOException, InterruptedException {
		if (!lineScanner.hasNext()) {
			key = null;
			value = null;
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
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void close() throws IOException {
		// TODO Auto-generated method stub

	}

	private String doGet(String requestUrl) {
		URL url;
		HttpURLConnection conn;
		BufferedReader rd;
		String line;
		String result = "";
		try {
			System.out.println("Initializing URL " + requestUrl);
			url = new URL(requestUrl);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			while ((line = rd.readLine()) != null) {
				result += line;
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
