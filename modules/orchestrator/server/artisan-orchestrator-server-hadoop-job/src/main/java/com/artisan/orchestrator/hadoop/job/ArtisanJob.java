package com.artisan.orchestrator.hadoop.job;

import java.io.IOException;
import java.util.List;

import org.apache.hadoop.classification.InterfaceAudience.Private;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapreduce.Cluster;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.JobStatus;

import com.artisan.orchestrator.hadoop.job.config.ArtisanConfiguration;
import com.artisan.orchestrator.hadoop.job.config.OperationConfiguration;
import com.simple.orchestrator.api.conf.impl.ConfigurationException;
import com.simple.orchestrator.api.dataprovider.IDataProvider;
import com.simple.orchestrator.api.hadoop.operation.IOperation;
import com.simple.orchestrator.api.hadoop.operation.IOperationInput;
import com.simple.orchestrator.api.hadoop.operation.Operation;

public class ArtisanJob extends Job {
	
	
	public ArtisanJob() throws IOException {
		this(new ArtisanConfiguration());
	}

	public ArtisanJob(ArtisanConfiguration conf) throws IOException {
		super(conf);
	}
	
	public ArtisanJob(ArtisanConfiguration conf, String jobName)
			throws IOException {
		super(conf, jobName);
	}

	
	/**
	 * Creates a new {@link Job} with no particular {@link Cluster} . A Cluster
	 * will be created with a generic {@link Configuration}.
	 * 
	 * @return the {@link Job} , with no connection to a cluster yet.
	 * @throws IOException
	 */
	public static ArtisanJob getInstance() throws IOException {
		// create with a null Cluster
		return getInstance(new ArtisanConfiguration());
	}

	/**
	 * Creates a new {@link Job} with no particular {@link Cluster} and a given
	 * {@link Configuration}.
	 * 
	 * The <code>Job</code> makes a copy of the <code>Configuration</code> so
	 * that any necessary internal modifications do not reflect on the incoming
	 * parameter.
	 * 
	 * A Cluster will be created from the conf parameter only when it's needed.
	 * 
	 * @param conf
	 *            the configuration
	 * @return the {@link Job} , with no connection to a cluster yet.
	 * @throws IOException
	 */
	public static ArtisanJob getInstance(ArtisanConfiguration conf) throws IOException {
		// create with a null Cluster
		return new ArtisanJob(conf);
	}

	/**
	 * Creates a new {@link Job} with no particular {@link Cluster} and a given
	 * jobName. A Cluster will be created from the conf parameter only when it's
	 * needed.
	 * 
	 * The <code>Job</code> makes a copy of the <code>Configuration</code> so
	 * that any necessary internal modifications do not reflect on the incoming
	 * parameter.
	 * 
	 * @param conf
	 *            the configuration
	 * @return the {@link Job} , with no connection to a cluster yet.
	 * @throws IOException
	 */
	public static ArtisanJob getInstance(ArtisanConfiguration conf, String jobName)
			throws IOException {
		// create with a null Cluster
		ArtisanJob result = getInstance(conf);
		result.setJobName(jobName);
		return result;
	}

	/**
	 * Creates a new {@link Job} with no particular {@link Cluster} and given
	 * {@link Configuration} and {@link JobStatus}. A Cluster will be created
	 * from the conf parameter only when it's needed.
	 * 
	 * The <code>Job</code> makes a copy of the <code>Configuration</code> so
	 * that any necessary internal modifications do not reflect on the incoming
	 * parameter.
	 * 
	 * @param status
	 *            job status
	 * @param conf
	 *            job configuration
	 * @return the {@link Job} , with no connection to a cluster yet.
	 * @throws IOException
	 */
	/*
	public static ArtisanJob getInstance(JobStatus status, Configuration conf)
			throws IOException {
		return  Job.getInstance(status, conf);
	}
	*/

	/**
	 * Creates a new {@link Job} with no particular {@link Cluster}. A Cluster
	 * will be created from the conf parameter only when it's needed.
	 * 
	 * The <code>Job</code> makes a copy of the <code>Configuration</code> so
	 * that any necessary internal modifications do not reflect on the incoming
	 * parameter.
	 * 
	 * @param ignored
	 * @return the {@link Job} , with no connection to a cluster yet.
	 * @throws IOException
	 * @deprecated Use {@link #getInstance()}
	 */
	@Deprecated
	public static ArtisanJob getInstance(Cluster ignored) throws IOException {
		return getInstance();
	}

	/**
	 * Creates a new {@link Job} with no particular {@link Cluster} and given
	 * {@link Configuration}. A Cluster will be created from the conf parameter
	 * only when it's needed.
	 * 
	 * The <code>Job</code> makes a copy of the <code>Configuration</code> so
	 * that any necessary internal modifications do not reflect on the incoming
	 * parameter.
	 * 
	 * @param ignored
	 * @param conf
	 *            job configuration
	 * @return the {@link Job} , with no connection to a cluster yet.
	 * @throws IOException
	 * @deprecated Use {@link #getInstance(Configuration)}
	 */
	@Deprecated
	public static ArtisanJob getInstance(Cluster ignored, ArtisanConfiguration conf)
			throws IOException {
		return getInstance(conf);
	}

	/**
	 * Creates a new {@link Job} with no particular {@link Cluster} and given
	 * {@link Configuration} and {@link JobStatus}. A Cluster will be created
	 * from the conf parameter only when it's needed.
	 * 
	 * The <code>Job</code> makes a copy of the <code>Configuration</code> so
	 * that any necessary internal modifications do not reflect on the incoming
	 * parameter.
	 * 
	 * @param cluster
	 *            cluster
	 * @param status
	 *            job status
	 * @param conf
	 *            job configuration
	 * @return the {@link Job} , with no connection to a cluster yet.
	 * @throws IOException
	 */
	@Private
	public static ArtisanJob getInstance(Cluster cluster, JobStatus status,
			Configuration conf) throws IOException {
		throw new RuntimeException("Not supported");
	}

	/**
	 * Helper method to set the analytics operation for the job.
	 * This uses {@link OperationConfiguration#setOperation(Configuration, Operation)}
	 * to set the operation in configuration.
	 * 
	 * @param operation
	 * @throws ConfigurationException
	 */
	public void setMapperOperation(IOperation operation)
			throws ConfigurationException {
		OperationConfiguration.setMapperOperation(getConfiguration(), operation);
	}

	/**
	 * Helper method to set the analytics operation for the job.
	 * This uses {@link OperationConfiguration#setOperation(Configuration, AnalyticsOperation)}
	 * to set the operation in configuration.
	 * 
	 * @param operation
	 * @throws ConfigurationException
	 */
	public void setReducerOperation(IOperation operation)
			throws ConfigurationException {
		OperationConfiguration.setReducerOperation(getConfiguration(), operation);
	}
	
	/**
	 * Get the underlying operation to be executed
	 * @return the analytics operation
	 * @throws ConfigurationException
	 */
	public Operation getOperation() throws ConfigurationException {
		return OperationConfiguration.getOperation(getConfiguration());
	}

	/**
	 * Set data providers
	 * @param dataProviders data providers
	 * @throws ConfigurationException
	 */
	public void setDataProviders(List<IDataProvider> dataProviders)
			throws ConfigurationException {
		OperationConfiguration.setDataProviders(getConfiguration(),dataProviders);
	}

	/**
	 * Get data providers
	 * @return dataproviders
	 * @throws ConfigurationException
	 */
	public List<IDataProvider> getDataProviders() throws ConfigurationException {
		return OperationConfiguration.getDataProviders(getConfiguration());
	}

	/**
	 * Set operation inputs
	 * @param operationInputs operation inputs
	 * @throws ConfigurationException
	 */
	public void setOperationInputs(List<IOperationInput> operationInputs)
			throws ConfigurationException {
		OperationConfiguration.setOperationInputs(getConfiguration(),operationInputs);
	}

	/**
	 * Get the operation inputs
	 * @return operation inputs
	 * @throws ConfigurationException
	 */
	public List<IOperationInput> getOperationInputs()
			throws ConfigurationException {
		return OperationConfiguration.getOperationInputs(getConfiguration());
	}
}
