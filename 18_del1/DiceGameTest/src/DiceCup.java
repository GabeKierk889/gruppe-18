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

    public int sameFaceValue() {
        int result = 0;
        if (die1.getFaceValue() == die2.getFaceValue()) {
            result = 1;
        }
        return result;
    }

    public String toString() {
        String str = "Die 1 rolled: " + die1.getFaceValue() + "\n" + "Die 2 rolled: " + die2.getFaceValue() +
                "\n" + "Sum: " + (die1.getFaceValue() + die2.getFaceValue());
        return str;
    }
}
