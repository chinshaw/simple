package com.artisan.orchestrator.hadoop.hbase;

import java.io.IOException;

import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import com.simple.api.orchestrator.IMetricKey;
import com.simple.api.orchestrator.ITaskExecution;

public class HBaseTaskExecutionDao extends HBaseDao {

	public static final String TABLE_NAME ="taskexecution";
	
	public HBaseTaskExecutionDao() throws IOException {
		super(new HBaseClientConfiguration(), TABLE_NAME);
	}
	
	public ITaskExecution find(String column, String qualifier, IMetricKey key) throws IOException {
		Get get = new Get(key.toBytes());
		get.addColumn(Bytes.toBytes(column), Bytes.toBytes(qualifier));
		get.setMaxVersions(1);
		Result result = getTable().get(get);
	//	byte[] bytes = result.getValue(Bytes.toBytes("rexp"), null);
		return null;
	}

	public ITaskExecution findLastTaskExecution(IMetricKey taskId) {
		return null;
	}

	public void save(ITaskExecution execution) {

	}

	public ITaskExecution find(IMetricKey key) {
		return null;
	}

	public ITaskExecution findLastTaskExecution(Long taskId) {
		// TODO Auto-generated method stub
		return null;
	}


}
