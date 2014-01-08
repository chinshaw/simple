package com.simple.engine.service.hadoop;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class JobUtils {

	public static final String R_OPERATION_PARAM = "R_OPERATION_PARAM";
	
	
	public static final String R_OPERATION_CODE = "R_OPERATION_CODE";

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
}
