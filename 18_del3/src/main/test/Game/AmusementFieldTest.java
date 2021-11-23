package Game;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AmusementFieldTest {
    AmusementField amusementField = new AmusementField("color", "name", 2);

    @Test
    void landOnField() {
    }

    @Test
    void setupBooth() {
    }

    @Test
    void getOwnerNum() {
    }

    // tested by Mark 2021-11-23
    @Test
    void setOwnerNum() {
        assertEquals(0,amusementField.getOwnerNum());
        amusementField.setOwnerNum(1);
        assertEquals(1,amusementField.getOwnerNum());
    }

    @Test
    void getPrice() {
    }

    @Test
    void getRent() {
    }

    @Test
    void getFieldColor() {
    }

    @Test
    void updateRent() {
    }

    @Test
    void resetRentToDefault() {
    }
}