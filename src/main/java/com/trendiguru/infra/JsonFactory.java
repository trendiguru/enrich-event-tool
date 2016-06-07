
package com.trendiguru.infra;

import java.util.HashMap;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Jackson used over GSON - faster - {@link http://programmerbruce.blogspot.co.il/2011/06/gson-v-jackson.html)
 * @author Jeremy
 */
public class JsonFactory {
    private static final Logger logger = Logger.getLogger(JsonFactory.class.getName());
    
    private static final JsonFactory instanse = new JsonFactory();

    private ObjectMapper mapper;
    
    private JsonFactory() {
        mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.ALL, Visibility.NONE);
        mapper.setVisibility(PropertyAccessor.FIELD, Visibility.NONE);
        //mapper.writerWithDefaultPrettyPrinter();
        //mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }
    
    public static JsonFactory getInstance() {
        return instanse;
    }
    
    public String toJson(Object src){
        try {
            /*
            if (src instanceof Collection) {
                return new ObjectMapper().writerWithType(TypeFactory.collectionType(List.class, Base.class)).writeValueAsString(src);
            } else {
            * */
                return  mapper.writeValueAsString(src); 
            //}
        } catch (Exception e) {
            logger.fatal("Error while converting java object to json using Jackson!", e);
            //e.printStackTrace();
        }
        
        return null;        
    }
    
    public HashMap<Integer,HashMap> fromJson(String fromStr, TypeReference typeRef) {
        try {
            return mapper.readValue(fromStr, typeRef);
        } catch (Exception ex) {
            logger.fatal("Error while converting json to java object using Jackson!", ex);
        } 
        return null;
    }

    public ObjectMapper getMapper() {
        return mapper;
    }
    
    
}
