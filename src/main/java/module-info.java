module it.polimi.ingsw {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.databind;

    exports it.polimi.ingsw.client.GUI;
    opens it.polimi.ingsw.client.GUI to javafx.fxml;
}