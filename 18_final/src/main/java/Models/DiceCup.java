package Models;

//This code has been modified from previous assignment CDIO 2 by Maj Kyllesbech, Gabriel H, Kierkegaard, Mark Bidstrup & Xiao Chen handed in 29. October 2021

public class DiceCup {
    private Die die1, die2;

    public DiceCup() {
        die1 = new Die();
        die2 = new Die();
        setDiceNotSameFaceValue();
    }

    public void roll() {
        die1.roll();
        die2.roll();
    }

    public int getSum() {
        return die1.getFaceValue() + die2.getFaceValue();
    }

    public int getDie1Value() {
        return die1.getFaceValue();
    }
    public int getDie2Value() {
        return die2.getFaceValue();
    }

    public boolean sameFaceValue() {
        return die1.getFaceValue() == die2.getFaceValue();
    }

    public void setDiceNotSameFaceValue () { // scrambles the dice so that sameFaceValue is false
        die1.setFaceValue(1);
        die2.setFaceValue(2);
    }

    public String toString() {
        return "Die 1: " + die1.getFaceValue() + "\t" + "Die 2: " + die2.getFaceValue() + "\t" + "Sum: " + (die1.getFaceValue()+die2.getFaceValue());
    }

}
