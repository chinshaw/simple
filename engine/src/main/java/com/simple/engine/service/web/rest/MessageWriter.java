package com.simple.engine.service.web.rest;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import org.codehaus.jackson.map.ObjectMapper;

import com.dyuproject.protostuff.JsonIOUtil;
import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.Message;
import com.dyuproject.protostuff.ProtobufIOUtil;
import com.dyuproject.protostuff.XmlIOUtil;
import com.simple.api.orchestrator.IMetric;

/**
 * This class is used to serialize protostuff messages.
 * It will take into account the requested media type and 
 * serialize the message to that type.
 * @author chris
 */
@Provider
@SuppressWarnings({"rawtypes", "unchecked"})
public class MessageWriter implements MessageBodyWriter<Message> {

	private static final ObjectMapper mapper = new ObjectMapper();
	
	
	@Override
	public boolean isWriteable(Class<?> type, Type genericType,
			Annotation[] annotations, MediaType mediaType) {
		return IMetric.class.isAssignableFrom(type);
	}

	@Override
	public long getSize(Message t, Class<?> type, Type genericType,
			Annotation[] annotations, MediaType mediaType) {
		return -1;
	}

	@Override
	public void writeTo(Message message, Class<?> type, Type genericType,
			Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, Object> httpHeaders,
			OutputStream entityStream) throws IOException,
			WebApplicationException {
		
		
		
		if (mediaType.isCompatible(MediaType.APPLICATION_JSON_TYPE)) {
			System.out.println("message" + message.getClass().getName());
			mapper.writeValue(entityStream, message);
			// Have to use the jackson object mapper so that it will honor the annotations.
			// The protostuff uses the native utf8 mapper and this does not add the @class annotation
			//JsonIOUtil.writeTo(entityStream, message, message.cachedSchema(), false);
			return;
		}
		
		if (mediaType.isCompatible(MediaType.APPLICATION_XML_TYPE)) {
			XmlIOUtil.writeTo(entityStream, message, message.cachedSchema());
			return;
		}
		
		if (mediaType.isCompatible(new MediaType("application", "x-protobuf"))) {
			LinkedBuffer buffer = LinkedBuffer.allocate(4096);
			ProtobufIOUtil.writeTo(entityStream, message, message.cachedSchema(), buffer);
			return;
		}
		
		JsonIOUtil.writeTo(entityStream, message, message.cachedSchema(), false);
	}
}