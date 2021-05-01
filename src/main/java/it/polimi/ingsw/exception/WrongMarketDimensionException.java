package it.polimi.ingsw.exception;
import com.fasterxml.jackson.annotation.*;

public class WrongMarketDimensionException extends Exception{
    public WrongMarketDimensionException(String message) {
        super(message);
    }
}
