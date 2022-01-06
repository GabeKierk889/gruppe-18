import org.junit.Test;
import static org.junit.Assert.*;

public class DiceCupTest {

    @Test
    public void getSum() {
        DiceCup cup = new DiceCup();
        for (int i = 0; i<100; i++) {
            cup.roll();
            assertTrue(cup.getSum()<13 && cup.getSum()>1);
        }
    }

    @Test
    public void getDie1Value() {
        DiceCup cup = new DiceCup();
        for (int i = 0; i<100; i++) {
            cup.roll();
            assertTrue(cup.getDie1Value()<7 && cup.getDie1Value()>0);
        }
    }

    @Test
    public void sameFaceValue() {
        DiceCup cup = new DiceCup();
        int[] frequency = new int[6];
        for (int i = 0; i < 3600; i++) {
            cup.roll();
            if (cup.sameFaceValue())
                frequency[cup.getSum()/2-1]++;
        }
        assertEquals(frequency[0], 100, 30);
        assertEquals(frequency[1], 100, 30);
        assertEquals(frequency[2], 100, 30);
        assertEquals(frequency[3], 100, 30);
        assertEquals(frequency[4], 100, 30);
        assertEquals(frequency[5], 100, 30);
    }
}