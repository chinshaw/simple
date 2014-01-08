package com.simple.engine.service.hadoop;

import java.io.IOException;
import java.util.Map;
import java.util.Scanner;
import java.util.Map.Entry;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.rosuda.JRI.REXP;
import org.rosuda.JRI.Rengine;

public class OperationMapper extends Mapper<Text, Text, Text, Text> {
	
	
	private static final String args[] = { "--vanilla" };

	private Rengine engine;

	public OperationMapper() {
		engine = new Rengine(args, true, new ConsoleIO());
	}

	public void run(Context context) throws IOException, InterruptedException {
		Configuration configuration = context.getConfiguration();		
		String code = configuration.get(JobUtils.R_OPERATION_CODE);

		if (!engine.waitForR()) {
			throw new RuntimeException("Unable to connect to R");
		}
		
		Scanner scanner = new Scanner(code);
		while(scanner.hasNextLine()) {
			REXP rexp = engine.eval(scanner.nextLine());	
		}
		
		//System.out.println(rexp.toString());
	}
	
	private void debugEnvironment() {
		Map<String, String> environmentVariables = System.getenv();

		System.out.println("Environment Debug");
		for (Entry<String,String> entry : environmentVariables.entrySet()) {
			System.out.println(entry.getKey() + " -> " + entry.getValue()); 
		}
	}
}
