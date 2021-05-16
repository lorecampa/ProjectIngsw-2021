module it.polimi.ingsw {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.databind;

    exports it.polimi.ingsw;

    exports it.polimi.ingsw.client;
    exports it.polimi.ingsw.client.command;
    exports it.polimi.ingsw.client.data;
    exports it.polimi.ingsw.client.GUI;

    exports it.polimi.ingsw.controller;

    exports it.polimi.ingsw.exception;

    exports it.polimi.ingsw.message;
    exports it.polimi.ingsw.message.bothArchitectureMessage;
    exports it.polimi.ingsw.message.clientMessage;
    exports it.polimi.ingsw.message.serverMessage;

    exports it.polimi.ingsw.model;
    exports it.polimi.ingsw.model.card;
    exports it.polimi.ingsw.model.card.requirement;
    exports it.polimi.ingsw.model.card.Effect;
    exports it.polimi.ingsw.model.card.Effect.Activation;
    exports it.polimi.ingsw.model.card.Effect.Creation;

    exports it.polimi.ingsw.model.personalBoard;
    exports it.polimi.ingsw.model.personalBoard.cardManager;
    exports it.polimi.ingsw.model.personalBoard.faithTrack;
    exports it.polimi.ingsw.model.personalBoard.market;
    exports it.polimi.ingsw.model.personalBoard.resourceManager;

    exports it.polimi.ingsw.model.resource;
    exports it.polimi.ingsw.model.token;

    exports it.polimi.ingsw.observer;

    exports it.polimi.ingsw.server;


    opens it.polimi.ingsw;

    opens it.polimi.ingsw.client;
    opens it.polimi.ingsw.client.command;
    opens it.polimi.ingsw.client.data;
    opens it.polimi.ingsw.client.GUI;

    opens it.polimi.ingsw.controller;

    opens it.polimi.ingsw.exception;

    opens it.polimi.ingsw.message;
    opens it.polimi.ingsw.message.bothArchitectureMessage;
    opens it.polimi.ingsw.message.clientMessage;
    opens it.polimi.ingsw.message.serverMessage;

    opens it.polimi.ingsw.model;
    opens it.polimi.ingsw.model.card;
    opens it.polimi.ingsw.model.card.requirement;
    opens it.polimi.ingsw.model.card.Effect;
    opens it.polimi.ingsw.model.card.Effect.Activation;
    opens it.polimi.ingsw.model.card.Effect.Creation;

    opens it.polimi.ingsw.model.personalBoard;
    opens it.polimi.ingsw.model.personalBoard.cardManager;
    opens it.polimi.ingsw.model.personalBoard.faithTrack;
    opens it.polimi.ingsw.model.personalBoard.market;
    opens it.polimi.ingsw.model.personalBoard.resourceManager;

    opens it.polimi.ingsw.model.resource;
    opens it.polimi.ingsw.model.token;

    opens it.polimi.ingsw.observer;

    opens it.polimi.ingsw.server;
}