package it.polimi.ingsw.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import it.polimi.ingsw.message.Handler;
import it.polimi.ingsw.message.Message;

public class JacksonSerializer<T extends Handler> {

    public JacksonSerializer() {}

    public  String serializeMessage(Message<T> message){
        String serializedMessage;
        try{
            serializedMessage = JacksonMapper.getInstance().writeValueAsString(message);
        }catch (JsonProcessingException e){
            return "Error in serialization";
        }
        return serializedMessage;

    }

    public Message<T> deserializeMessage(String serializedMessage){
        Message<T> message;
        try {
            message = JacksonMapper.getInstance().readValue(serializedMessage, new TypeReference<Message<T>>() {});
        } catch (JsonProcessingException e) {
            return null;
            //TODO send an error message
        }
        return message;

    }
}
