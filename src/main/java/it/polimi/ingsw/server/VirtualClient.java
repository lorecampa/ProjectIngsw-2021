package it.polimi.ingsw.server;

import it.polimi.ingsw.message.Message;
import it.polimi.ingsw.util.GsonUtil;

public class VirtualClient{
    int id;
    String username;
    ClientHandler clientHandler;

    public VirtualClient(int id, String username, ClientHandler clientHandler) {
        this.id = id;
        this.username = username;
        this.clientHandler = clientHandler;
    }

    public void sendMessage(Message message) {
        String serializedMessage = GsonUtil.serialize(message);
        clientHandler.writeToStream(serializedMessage);
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public ClientHandler getClientHandler() {
        return clientHandler;
    }



    @Override
    public String toString() {
        return "VirtualClient{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", clientHandler=" + clientHandler +
                '}';
    }
}
