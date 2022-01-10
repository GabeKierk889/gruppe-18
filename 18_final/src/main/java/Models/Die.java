package Models;

//This code has been modified from previous assignment CDIO 3 by Maj Kyllesbech, Gabriel H, Kierkegaard, Mark Bidstrup & Xiao Chen handed in 26. November 2021

public class Die {
    private int faceValue;
    private final int NUMOFSIDES = 6;

    public Die() {
        faceValue = 0;
    } // deliberately set to 0 in the beginning to distinguish new dice

    public int roll() {
        faceValue = (int) (Math.random() * NUMOFSIDES) + 1;
        return faceValue;
    }

    public int getFaceValue() {
        return faceValue;
    }

    public String toString () {
        return Integer.toString(faceValue);
    }
}
