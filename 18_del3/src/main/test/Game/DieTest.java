package Game;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DieTest {

    @Test
    public void frequencyDistribution() {
        // tests that each face value occurs approx. 1/6 of the times
        Die d1 = new Die();
        int[] frequency = new int[6];
        for (int i = 0; i < 6000; i++) {
            d1.roll();
            int value = d1.getFaceValue();
            frequency[value - 1]++;
        }
        assertEquals(frequency[0], 1000, 100);
        assertEquals(frequency[1], 1000, 100);
        assertEquals(frequency[2], 1000, 100);
        assertEquals(frequency[3], 1000, 100);
        assertEquals(frequency[4], 1000, 100);
        assertEquals(frequency[5], 1000, 100);


    }
    @Test
    public void maxMinValues() {
        // verifies the maximum and minimum value that the die can take
        Die d1 = new Die();
        int max = 0;
        int min = 7;
        for (int i = 0; i < 6000; i++) {
            d1.roll();
            int value = d1.getFaceValue();
            if (value > max)
                max = value;
            if (value < min)
                min = value;
        }
        assertEquals(max,6);
        assertEquals(min,1);
    }

}