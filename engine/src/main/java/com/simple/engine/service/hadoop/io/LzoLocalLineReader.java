package com.simple.engine.service.hadoop.io;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import org.apache.commons.codec.binary.Base64;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.io.compress.CompressionCodecFactory;
import org.apache.hadoop.mapreduce.Counter;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.util.LineReader;

import com.twitter.elephantbird.mapreduce.input.LzoInputFormat;
import com.twitter.elephantbird.mapreduce.input.LzoLineRecordReader;
import com.twitter.elephantbird.mapreduce.input.LzoRecordReader;
import com.twitter.elephantbird.mapreduce.input.MapredInputFormatCompatible;
import com.twitter.elephantbird.mapreduce.io.BinaryConverter;
import com.twitter.elephantbird.mapreduce.io.BinaryWritable;
import com.twitter.elephantbird.util.Codecs;
import com.twitter.elephantbird.util.HadoopCompat;
import com.twitter.elephantbird.util.TypeRef;

/**
 * Reads line from an lzo compressed text file, base64 decodes it, and then
 * deserializes that into the templatized object.
 * 
 * <p>
 * A small fraction of bad records are tolerated. See {@link LzoRecordReader}
 * for more information on error handling.
 */
public class LzoLocalLineReader<M, W extends BinaryWritable<M>> extends LzoRecordReader<LongWritable, W> implements
		MapredInputFormatCompatible<LongWritable, W> {

	private LineReader lineReader_;

	private final Text line_ = new Text();
	private LongWritable key_ = new LongWritable();
	private W value_;
	private TypeRef<M> typeRef_;
	private int maxLineLen = Integer.MAX_VALUE;

	private FSDataInputStream fileIn_;

	private final Base64 base64_ = Codecs.createStandardBase64();
	private final BinaryConverter<M> converter_;

	private Counter linesReadCounter;
	private Counter emptyLinesCounter;
	private Counter recordsReadCounter;
	private Counter recordErrorsCounter;
	private Counter truncatedLinesCounter;

	public LzoLocalLineReader(TypeRef<M> typeRef, W protobufWritable, BinaryConverter<M> protoConverter) {
		typeRef_ = typeRef;
		converter_ = protoConverter;
		value_ = protobufWritable;
	}

	@Override
	public synchronized void close() throws IOException {
		super.close();
		if (lineReader_ != null) {
			lineReader_.close();
		}
	}

	@Override
	public LongWritable getCurrentKey() throws IOException, InterruptedException {
		return key_;
	}

	@Override
	public W getCurrentValue() throws IOException, InterruptedException {
		return value_;
	}


	public void createInputReader(String path, Configuration conf) throws IOException {
		maxLineLen = conf.getInt(LzoLineRecordReader.MAX_LINE_LEN_CONF, Integer.MAX_VALUE);
		
		FileInputStream fis = new FileInputStream(path);
		//DataInputStream dis = new DataInputStream(fis);
		
		lineReader_ = new LineReader(fis, conf);
		Path fsPath = new Path(path);
		
		CompressionCodecFactory compressionCodecs = new CompressionCodecFactory(conf);
		final CompressionCodec codec = compressionCodecs.getCodec(fsPath);
		if (codec == null) {
			throw new IOException("No codec for file " + fsPath + " found, cannot run");
		}
		
		codec.createInputStream(fis);
		
		fileIn_.seek(0);
		skipToNextSyncPoint(false);
		start_ = fileIn_.getPos();
	}

	@Override
	public void initialize(InputSplit genericSplit, TaskAttemptContext context) throws IOException, InterruptedException {

	}

	@Override
	protected void createInputReader(InputStream input, Configuration conf) throws IOException {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected void skipToNextSyncPoint(boolean atFirstRecord) throws IOException {
		if (!atFirstRecord) {
			lineReader_.readLine(new Text(), maxLineLen);
		}
	}

	@Override
	public void setKeyValue(LongWritable key, W value) {
		key_ = key;
		value_ = value;
	}

	/**
	 * Read the next key, value pair.
	 * <p>
	 * A small fraction of bad records in input are tolerated. See
	 * {@link LzoRecordReader} for more information on error handling.
	 * 
	 * @return true if a key/value pair was read
	 * @throws IOException
	 * @throws InterruptedException
	 */
	@Override
	public boolean nextKeyValue() throws IOException, InterruptedException {
		// Since the lzop codec reads everything in lzo blocks, we can't stop if
		// pos == end.
		// Instead we wait for the next block to be read in, when pos will be >
		// end.
		while (pos_ <= end_) {
			key_.set(pos_);

			int newSize = lineReader_.readLine(line_, maxLineLen);
			if (newSize == 0) {
				return false;
			}
			HadoopCompat.incrementCounter(linesReadCounter, 1);
			pos_ = getLzoFilePos();
			if (line_.getLength() == 0 || line_.charAt(0) == '\n') {
				HadoopCompat.incrementCounter(emptyLinesCounter, 1);
				continue;
			}
			if (line_.getLength() >= maxLineLen) {
				HadoopCompat.incrementCounter(truncatedLinesCounter, 1);
			}

			M protoValue = null;

			Throwable decodeException = null;

			try {
				byte[] lineBytes = Arrays.copyOf(line_.getBytes(), line_.getLength());
				protoValue = converter_.fromBytes(base64_.decode(lineBytes));
			} catch (Throwable t) {
				decodeException = t;
			}

			if (protoValue == null) {
				HadoopCompat.incrementCounter(recordErrorsCounter, 1);
				continue;
			}

			HadoopCompat.incrementCounter(recordsReadCounter, 1);
			value_.set(protoValue);
			return true;
		}

		return false;
	}


}
