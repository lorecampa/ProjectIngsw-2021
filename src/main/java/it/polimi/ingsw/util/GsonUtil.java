package it.polimi.ingsw.util;

import com.google.gson.Gson;
import it.polimi.ingsw.message.Message;
import it.polimi.ingsw.message.NormalMessage;

public final class GsonUtil {
    private final static Gson gson = new Gson();

    public static String serialize(Message message){
        return gson.toJson(message);
    }

    public static Message deserialize(String serializedMessage){
        Message message;
        try{
            message = gson.fromJson(serializedMessage, Message.class);
        }catch (Exception e){
            return new NormalMessage("Cannot deserialize message");
        }

        return message;
    }
}
