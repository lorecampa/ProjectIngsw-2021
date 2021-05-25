package it.polimi.ingsw.client.data;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ColorDataTest {

    @Test
    void getResourceTest() {
        ColorData colorData = ColorData.BLUE;
        System.out.println(colorData.getMarbleResource());
    }
}