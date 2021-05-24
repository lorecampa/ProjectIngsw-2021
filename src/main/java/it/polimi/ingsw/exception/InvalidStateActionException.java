package it.polimi.ingsw.exception;

import it.polimi.ingsw.message.clientMessage.ErrorType;

public class InvalidStateActionException extends Exception{
    public InvalidStateActionException() {
        super(ErrorType.INVALID_ACTION.getMessage());
    }

}
