package com.simple.orchestrator.hadoop.job;

import java.io.IOException;
import java.util.List;

import org.apache.hadoop.classification.InterfaceAudience.Private;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapreduce.Cluster;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.JobStatus;

import com.simple.domain.model.AnalyticsOperation;
import com.simple.domain.model.dataprovider.DataProvider;
import com.simple.domain.model.ui.AnalyticsOperationInput;
import com.simple.orchestrator.hadoop.config.ConfigurationException;
import com.simple.orchestrator.hadoop.config.OperationConfig;

public class AnalyticsOperationHadoopJob extends Job implements IHadoopJob {
	
	
	public AnalyticsOperationHadoopJob() throws IOException {
		this(new Configuration());
	}

	public AnalyticsOperationHadoopJob(Configuration conf) throws IOException {
		super(conf);
	}
	
	public AnalyticsOperationHadoopJob(Configuration conf, String jobName)
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
	public static AnalyticsOperationHadoopJob getInstance() throws IOException {
		// create with a null Cluster
		return getInstance(new Configuration());
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
	public static AnalyticsOperationHadoopJob getInstance(Configuration conf) throws IOException {
		// create with a null Cluster
		return new AnalyticsOperationHadoopJob(conf);
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
	public static AnalyticsOperationHadoopJob getInstance(Configuration conf, String jobName)
			throws IOException {
		// create with a null Cluster
		AnalyticsOperationHadoopJob result = getInstance(conf);
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
	public static AnalyticsOperationHadoopJob getInstance(JobStatus status, Configuration conf)
			throws IOException {
		throw new RuntimeException("Not supported");
	}

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
	public static AnalyticsOperationHadoopJob getInstance(Cluster ignored) throws IOException {
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
	public static AnalyticsOperationHadoopJob getInstance(Cluster ignored, Configuration conf)
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
	public static AnalyticsOperationHadoopJob getInstance(Cluster cluster, JobStatus status,
			Configuration conf) throws IOException {
		throw new RuntimeException("Not supported");
	}

	/**
	 * Helper method to set the analytics operation for the job.
	 * This uses {@link OperationConfig#setOperation(Configuration, AnalyticsOperation)}
	 * to set the operation in configuration.
	 * 
	 * @param operation
	 * @throws ConfigurationException
	 */
	public void setOperation(AnalyticsOperation operation)
			throws ConfigurationException {
		OperationConfig.setOperation(getConfiguration(), operation);
	}

	public AnalyticsOperation getOperation() throws ConfigurationException {
		return OperationConfig.getOperation(getConfiguration());
	}

	public void setDataProviders(List<DataProvider> dataProviders)
			throws ConfigurationException {
		OperationConfig.setDataProviders(getConfiguration(),dataProviders);
	}

	public List<DataProvider> getDataProviders() throws ConfigurationException {
		return OperationConfig.getDataProviders(getConfiguration());
	}

	public void setOperationInputs(List<AnalyticsOperationInput> operationInputs)
			throws ConfigurationException {
		OperationConfig.setOperationInputs(getConfiguration(),operationInputs);
	}

	public List<AnalyticsOperationInput> getOperationInputs()
			throws ConfigurationException {
		return OperationConfig.getOperationInputs(getConfiguration());
	}
}
