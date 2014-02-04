package com.simple.engine.service.hadoop.mrv1;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import com.simple.engine.service.r.REXPProtos;
import com.twitter.elephantbird.mapreduce.io.ProtobufWritable;

public class ROperationReducer extends Reducer<Text, ProtobufWritable<REXPProtos.REXP>, Text, Text> {

}
