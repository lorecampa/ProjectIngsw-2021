package it.polimi.ingsw.client;



public class CLIMessageHandler extends ClientMessageHandler {
    private final Client client;

    public CLIMessageHandler(Client client) {
        super(client);
        this.client = client;
    }


}