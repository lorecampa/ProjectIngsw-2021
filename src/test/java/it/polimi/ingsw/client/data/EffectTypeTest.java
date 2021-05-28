package it.polimi.ingsw.client.data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class EffectTypeTest {

    private EffectType typeDes;
    private String typeSer;

    @Test
    void testSerAndDes(){
        ObjectMapper mapper = new ObjectMapper();
        EffectType type = EffectType.WAREHOUSE;
        assertDoesNotThrow(()->typeSer = mapper.writeValueAsString(type));
        assertDoesNotThrow(()->typeDes = mapper.readValue(typeSer, EffectType.class));
    }
}