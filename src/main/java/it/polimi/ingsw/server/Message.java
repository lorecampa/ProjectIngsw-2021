package it.polimi.ingsw.server;

import com.google.gson.Gson;

public class Message {
    String type;
    int num;

    public Message(String type, int num) {
        this.type = type;
        this.num = num;
    }

    public String serialize(){
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
