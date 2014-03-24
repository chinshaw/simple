package com.simple.engine.hbase;

import java.io.IOException;

import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import com.simple.api.orchestrator.IMetric;

public class HBaseMetricDao {

	private final HTable table;
	
	protected HBaseMetricDao(final String tableName) throws IOException {
		table = new HTable(new HBaseConfiguration(), tableName);
	}
	
	
	public IMetric find(String column, String qualifier, IHbaseKey key) throws IOException {
		Get get = new Get(key.toBytes());
		get.addColumn(Bytes.toBytes(column), Bytes.toBytes(qualifier));
		get.setMaxVersions(1);
		Result result = table.get(get);
		byte[] bytes = result.getValue(Bytes.toBytes("rexp"), null);
		return null;
		
	}
}
