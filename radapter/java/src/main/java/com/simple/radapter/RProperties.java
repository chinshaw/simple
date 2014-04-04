package com.simple.radapter;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class RProperties extends Properties {

    /**
     * Serialization Id
     */
    private static final long serialVersionUID = -2408326077844379149L;
    
    private static RProperties instance = null;

    private RProperties() throws IOException {
        InputStream stream = RProperties.class.getClassLoader().getResourceAsStream(
                "module.properties");
        load(stream);
    }

    public static synchronized RProperties getInstance() throws IOException {
        if (instance == null) {
            instance = new RProperties();
        }
        return instance;
    }

}
