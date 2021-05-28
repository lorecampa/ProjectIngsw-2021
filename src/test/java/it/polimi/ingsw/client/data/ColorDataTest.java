package it.polimi.ingsw.client.data;

import org.junit.jupiter.api.Test;

class ColorDataTest {

    @Test
    void getResourceTest() {
        ColorData colorData = ColorData.BLUE;
        System.out.println(colorData.toMarbleResource());
    }
}