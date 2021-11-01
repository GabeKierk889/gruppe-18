public class DiceCup {
    private Die die1, die2;

    public DiceCup() {
        die1 = new Die();
        die2 = new Die();
    }

    public void roll() {
        die1.roll();
        die2.roll();
    }

    public int getSum() {
        int sum = die1.getFaceValue() + die2.getFaceValue();
        return sum;
    }

    public int getDie1Value() {
        return die1.getFaceValue();
    }
    public int getDie2Value() {
        return die2.getFaceValue();
    }

    public Die getDie1() {
        return die1;
    }
    public Die getDie2() {
        return die2;
    }

    public int sameFaceValue() {
        int result = 0;
        if (die1.getFaceValue() == die2.getFaceValue()) {
            result = 1;
        }
        return result;
    }

    public String toString() {
        String result = "Die 1: " + die1.getFaceValue() + "\t" + "Die 2: " + die2.getFaceValue() + "\t" + "Sum: " + (die1.getFaceValue()+die2.getFaceValue());
        return result;
    }
}
