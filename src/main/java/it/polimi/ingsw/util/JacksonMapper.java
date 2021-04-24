package it.polimi.ingsw.util;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.message.Message;
import it.polimi.ingsw.message.MessageType;
import it.polimi.ingsw.message.NormalMessage;

public final class JacksonMapper {
    private static ObjectMapper mapper = null;

    public static ObjectMapper getInstance(){
        if (mapper == null){
            mapper = new ObjectMapper();
            mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        }
        return mapper;
    }

    public static String serializeMessage(Message message){
        String serializedMessage;
        try{
            serializedMessage = getInstance().writeValueAsString(message);
        }catch (JsonProcessingException e){
            Message errorMessage = new NormalMessage(MessageType.ERROR);
            try {
                serializedMessage = getInstance().writeValueAsString(errorMessage);
            } catch (JsonProcessingException e1) {
                //it will never happen
                serializedMessage = null;
            }
        }
        return serializedMessage;

    }

    public static Message deserializeMessage(String serializedMessage){
        Message message;
        try {
            message = getInstance().readValue(serializedMessage, Message.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            message = new NormalMessage(MessageType.ERROR);
        }
        return message;

    }

}
