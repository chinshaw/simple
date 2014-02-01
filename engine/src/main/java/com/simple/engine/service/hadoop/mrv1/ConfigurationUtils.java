package com.simple.engine.service.hadoop.mrv1;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class ConfigurationUtils {

	public static String serializeObject(Object object) throws IOException {
		String serializedOperation = null;
		ObjectOutputStream os = null;
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			os = new ObjectOutputStream(baos);
			os.writeObject(object);
			serializedOperation = baos.toString();
		} finally {
			os.close();
		}
		return serializedOperation;
	}
	
	public static <T> T unSerializeObject(String serializedObject) throws IOException, ClassNotFoundException {
		if (serializedObject == null || serializedObject.isEmpty()) {
			return null;
		}
		
		T unserializedObject = null;
		ObjectInputStream is = null;
		try {
			ByteArrayInputStream baos = new ByteArrayInputStream(serializedObject.getBytes());
			is = new ObjectInputStream(baos);
			unserializedObject = (T) is.readObject();
		} finally {
			if (is!= null) {
				is.close();	
			}
			
		}
		
		return unserializedObject;
	}
	
	public static String serializeToXml(Class<?> clazz, Object object) throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(clazz);
		Marshaller marshaller = context.createMarshaller();
		
		StringWriter strWriter = new StringWriter();
		marshaller.marshal(object, strWriter);
		
		return strWriter.toString();
	}
	
	public static <T> T unserializeXml(Class<T> clazz, String xml) throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(clazz);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		
		StringReader strReader = new StringReader(xml);
		return (T) unmarshaller.unmarshal(strReader);
	}
}
