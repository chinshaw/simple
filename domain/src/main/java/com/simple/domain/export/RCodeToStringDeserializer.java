package com.simple.domain.export;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map.Entry;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RCodeToStringDeserializer extends JsonDeserializer<String>{

    @Override
    public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        jp.setCodec(mapper);
        JsonNode tree = jp.readValueAsTree();
        Iterator<Entry<String, JsonNode>> fields = tree.fields();
        
        while (fields.hasNext()) {
            Entry<String, JsonNode> node = fields.next();
            
           // System.out.println("Value's are " + node.getKey());
            if (node.getKey().equals("code")) {
                
             //   System.out.println("Code is " + node.getValue().asText());
                return node.getValue().asText();
            }
        }
        return null;
    }

}
