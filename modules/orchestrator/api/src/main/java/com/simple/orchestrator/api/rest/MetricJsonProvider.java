package com.simple.orchestrator.api.rest;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;

import com.simple.api.orchestrator.IMetric;

@Provider
@Consumes(MediaType.APPLICATION_JSON)
public class MetricJsonProvider implements MessageBodyReader<IMetric>,
		MessageBodyWriter<IMetric> {

	/**
	 * Our custom object mapper.
	 */
	private static final ObjectMapper mapper = new ObjectMapper();
	{
		mapper.getSerializationConfig().addMixInAnnotations(IMetric.class,
				IMetricMixin.class);
	}

	@Override
	public boolean isReadable(Class<?> type, Type genericType,
			Annotation[] annotations, MediaType mediaType) {
		return IMetric.class.isAssignableFrom(type);
	}

	/**
	 * Read a type from the {@link InputStream}.
	 * 
	 * @return the type that was read from the stream.
	 * @param type
	 *            the type that is to be read from the entity stream.
	 * @param genericType
	 *            the type of object to be produced. E.g. if the message body is
	 *            to be converted into a method parameter, this will be the
	 *            formal type of the method parameter as returned by
	 *            <code>Method.getGenericParameterTypes</code>.
	 * @param annotations
	 *            an array of the annotations on the declaration of the artifact
	 *            that will be initialized with the produced instance. E.g. if
	 *            the message body is to be converted into a method parameter,
	 *            this will be the annotations on that parameter returned by
	 *            <code>Method.getParameterAnnotations</code>.
	 * @param mediaType
	 *            the media type of the HTTP entity.
	 * @param httpHeaders
	 *            the read-only HTTP headers associated with HTTP entity.
	 * @param entityStream
	 *            the {@link InputStream} of the HTTP entity. The caller is
	 *            responsible for ensuring that the input stream ends when the
	 *            entity has been consumed. The implementation should not close
	 *            the input stream.
	 * @throws java.io.IOException
	 *             if an IO error arises
	 * @throws javax.ws.rs.WebApplicationException
	 *             if a specific HTTP error response needs to be produced. Only
	 *             effective if thrown prior to the response being committed.
	 */
	@Override
	public IMetric readFrom(Class<IMetric> type, Type genericType,
			Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, String> httpHeaders, InputStream entityStream)
			throws IOException, WebApplicationException {
		JsonParser jp = mapper.getJsonFactory().createJsonParser(entityStream);
		/*
		 * Important: we are NOT to close the underlying stream after mapping,
		 * so we need to instruct parser:
		 */
		jp.disable(JsonParser.Feature.AUTO_CLOSE_SOURCE);
		return mapper.readValue(jp, mapper.constructType(genericType));
	}

	@Override
	public boolean isWriteable(Class<?> type, Type genericType,
			Annotation[] annotations, MediaType mediaType) {
		return IMetric.class.isAssignableFrom(type);
	}

	@Override
	public long getSize(IMetric t, Class<?> type, Type genericType,
			Annotation[] annotations, MediaType mediaType) {
		return 0;
	}

	/**
	 * Write a type to an HTTP response. The response header map is mutable but
	 * any changes must be made before writing to the output stream since the
	 * headers will be flushed prior to writing the response body.
	 * 
	 * @param t
	 *            the instance to write.
	 * @param type
	 *            the class of object that is to be written.
	 * @param genericType
	 *            the type of object to be written, obtained either by
	 *            reflection of a resource method return type or by inspection
	 *            of the returned instance.
	 *            {@link javax.ws.rs.core.GenericEntity} provides a way to
	 *            specify this information at runtime.
	 * @param annotations
	 *            an array of the annotations on the resource method that
	 *            returns the object.
	 * @param mediaType
	 *            the media type of the HTTP entity.
	 * @param httpHeaders
	 *            a mutable map of the HTTP response headers.
	 * @param entityStream
	 *            the {@link OutputStream} for the HTTP entity. The
	 *            implementation should not close the output stream.
	 * @throws java.io.IOException
	 *             if an IO error arises
	 * @throws javax.ws.rs.WebApplicationException
	 *             if a specific HTTP error response needs to be produced. Only
	 *             effective if thrown prior to the response being committed.
	 */
	@Override
	public void writeTo(IMetric t, Class<?> type, Type genericType,
			Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, Object> httpHeaders,
			OutputStream entityStream) throws IOException,
			WebApplicationException {
		mapper.writeValue(entityStream, t);
	}

}
