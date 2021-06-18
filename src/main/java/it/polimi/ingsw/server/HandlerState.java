package it.polimi.ingsw.server;

/**
 * The possible state of the Client Connection Handler
 */
public enum HandlerState {
    FIRST_CONTACT,
    NUM_OF_PLAYER,
    USERNAME,
    WAITING_LOBBY,
    LEADER_SETUP,
    RESOURCE_SETUP,
    WAITING_TO_BE_IN_MATCH,
    IN_MATCH
}
