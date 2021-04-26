package it.polimi.ingsw.server;

import it.polimi.ingsw.observer.ModelObserver;
import it.polimi.ingsw.observer.Observable;
import it.polimi.ingsw.observer.VirtualViewObserver;

public class VirtualClient extends Observable<VirtualViewObserver> implements ModelObserver{
    int id;
    String username;
    ClientConnectionHandler clientConnectionHandler;

    public VirtualClient(int id, String username, ClientConnectionHandler clientConnectionHandler) {
        this.id = id;
        this.username = username;
        this.clientConnectionHandler = clientConnectionHandler;
    }

    public void sendMessage(String message) {
        clientConnectionHandler.writeToStream(message);
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public ClientConnectionHandler getClientHandler() {
        return clientConnectionHandler;
    }



    @Override
    public String toString() {
        return "VirtualClient{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", clientHandler=" + clientConnectionHandler +
                '}';
    }
}
