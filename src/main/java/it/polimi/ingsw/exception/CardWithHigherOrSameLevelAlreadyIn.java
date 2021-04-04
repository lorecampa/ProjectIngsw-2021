package it.polimi.ingsw.exception;

public class CardWithHigherOrSameLevelAlreadyIn extends Exception{
    public CardWithHigherOrSameLevelAlreadyIn(String message) {
        super(message);
    }
}
