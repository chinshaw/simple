package com.simple.engine.hbase;

import java.io.IOException;

import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import com.simple.engine.ITaskExecutionDao;
import com.simple.engine.api.IMetricKey;
import com.simple.original.api.analytics.ITaskExecution;

public class HBaseTaskExecutionDao extends HBaseDao implements ITaskExecutionDao {

	public static final String TABLE_NAME ="taskexecution";
	
	public HBaseTaskExecutionDao() throws IOException {
		super(new HBaseConfiguration(), TABLE_NAME);
	}
	
	public ITaskExecution find(String column, String qualifier, IMetricKey key) throws IOException {
		Get get = new Get(key.toBytes());
		get.addColumn(Bytes.toBytes(column), Bytes.toBytes(qualifier));
		get.setMaxVersions(1);
		Result result = getTable().get(get);
		byte[] bytes = result.getValue(Bytes.toBytes("rexp"), null);
		return null;
	}

	public ITaskExecution findLastTaskExecution(IMetricKey taskId) {
		return null;
	}

	@Override
	public void save(ITaskExecution execution) {

	}

	@Override
	public ITaskExecution find(IMetricKey key) {
		return null;
	}

	@Override
	public ITaskExecution findLastTaskExecution(Long taskId) {
		// TODO Auto-generated method stub
		return null;
	}


}
