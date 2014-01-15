package com.simple.engine.service.hadoop;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import javax.xml.bind.JAXBException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;

import com.simple.domain.model.AnalyticsOperation;
import com.simple.domain.model.dataprovider.DataProvider;
import com.simple.domain.model.dataprovider.HttpDataProvider;
import com.simple.engine.service.hadoop.io.HttpInputFormat;

public class OperationTool implements Tool {

	private Configuration configuration;
	
	@Override
	public int run(String args[]) throws IOException, InterruptedException, ClassNotFoundException, JAXBException {
		Job job = Job.getInstance(getConf());
		
		OperationConfig opConfig = new OperationConfig(getConf());
	
		List<DataProvider> dataproviders = opConfig.getDataProviders();
		DataProvider dp = dataproviders.get(0);
		
		AnalyticsOperation operation = opConfig.getOperation();
		String jobName = operation.getName();
		
		if (dp instanceof HttpDataProvider) {
			HttpInputFormat.setInput(job, ((HttpDataProvider) dp).getUrl());
		} else {
			throw new RuntimeException("Invalid dp type");
		}
		
		FileOutputFormat.setOutputPath(job, new Path("/tmp/" + "hd_" + jobName + "-" + UUID.randomUUID()));
		
		job.setJarByClass(OperationExecutor.class);
		job.setMapperClass(ROperationMapper.class);
 
		return job.waitForCompletion(true) ? 0 : 1;
	}

	@Override
	public void setConf(Configuration configuration) {
		this.configuration = configuration;
	}

	@Override
	public Configuration getConf() {
		return configuration;
	}
}
