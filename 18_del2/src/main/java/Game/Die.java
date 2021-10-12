package Game;

public class Die {
    private int faceValue;
    private final int MAX = 6;

    public Die() {
        faceValue = 1;
    }

    public int roll() {
        faceValue = (int) (Math.random() * MAX) + 1;
        return faceValue;
    }

    public int getFaceValue() {
        return faceValue;
    }

    public String toString () {
        String str = Integer.toString(faceValue);
        return str;
    }
}
