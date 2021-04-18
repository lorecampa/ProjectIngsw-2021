package it.polimi.ingsw.util;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class JacksonMapper {
    private static ObjectMapper mapper = null;

    public static ObjectMapper getInstance(){
        if (mapper == null){
            mapper = new ObjectMapper();
            mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        }
        return mapper;
    }

}
