package it.polimi.ingsw.message;

public class CommandMessage implements Message{
    MessageType messageType;
    int x;
    int y;

    public CommandMessage(MessageType messageType, int x, int y) {
        this.messageType = messageType;
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "CommandMessage{" +
                "messageType=" + messageType +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}
