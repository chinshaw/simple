package com.simple.orchestrator.hbase;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

public class HBaseDao {

	private final HTable table;
	
	protected HBaseDao(final String tableName) throws IOException {
		this(new HBaseConfiguration(), tableName);
	}
	
	protected HBaseDao(final Configuration conf, final String tableName) throws IOException {
		table = new HTable(conf, tableName);
	}
	
	public <T> T find(Class<T> clazz, String column, String qualifier, IHbaseKey key) throws IOException {
		Get get = new Get(key.toBytes());
		get.addColumn(Bytes.toBytes(column), Bytes.toBytes(qualifier));
		get.setMaxVersions(1);
		Result result = table.get(get);
		byte[] bytes = result.getValue(Bytes.toBytes("rexp"), null);
		return null;
	}
	
	
	public HTable getTable() {
		return table;
	}
	
	public static HBaseDao create(String tableName) throws IOException  {
		return new HBaseDao(tableName);
	}
	
	
}
