package com.simple.engine.rest;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import com.dyuproject.protostuff.JsonIOUtil;
import com.simple.engine.api.IMetric;

@SuppressWarnings({"rawtypes", "unchecked"})
@Provider
public class MetricWriter implements MessageBodyWriter<IMetric> {

	@Override
	public boolean isWriteable(Class<?> type, Type genericType,
			Annotation[] annotations, MediaType mediaType) {
		return IMetric.class.isAssignableFrom(type);
	}

	@Override
	public long getSize(IMetric t, Class<?> type, Type genericType,
			Annotation[] annotations, MediaType mediaType) {
		return -1;
	}

	@Override
	public void writeTo(IMetric metric, Class<?> type, Type genericType,
			Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, Object> httpHeaders,
			OutputStream entityStream) throws IOException,
			WebApplicationException {
		
		if (mediaType.isCompatible(MediaType.APPLICATION_JSON_TYPE)) {
			JsonIOUtil.writeTo(entityStream, metric, metric.cachedSchema(), false);
		}
	}
}
