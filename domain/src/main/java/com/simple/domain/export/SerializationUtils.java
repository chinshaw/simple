package com.simple.domain.export;

import java.lang.reflect.Field;

import com.simple.domain.DatastoreObject;

public class SerializationUtils {

    public static void setId(DatastoreObject object, Long id) {
        try {
            Class<DatastoreObject> clazz = DatastoreObject.class;

            Field idField = clazz.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(object, id);

            // production code should handle these exceptions more gracefully
        } catch (NoSuchFieldException x) {
            x.printStackTrace();
        } catch (IllegalArgumentException x) {
            x.printStackTrace();
        } catch (IllegalAccessException x) {
            x.printStackTrace();
        }
    }
}
