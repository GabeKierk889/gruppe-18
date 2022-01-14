package Models;

public class GameSettings {

    public static final int MINNUMOFPLAYERS = 3;
    public static final int MAXNUMOFPLAYERS = 6;
    public static final int STARTINGBALANCE = 30000;
    public static final int STARTBONUS = 4000;
    public static final int JAILFEE = 1000;
    public static final int STATE_TAX_AMOUNT = 2000;
    public static final int INCOME_TAX_AMOUNT = 4000;
    public static final double INCOME_TAX_RATE = 0.1;
    public static final double HOUSE_RESELL_VALUE_MULTIPLIER = 0.5;
    public static final int GOTOJAIL_IF_THROW_SAME_DICE_X_TIMES = 3;

    // below has not been used / implemented
    public static final double MORTGAGE_INTEREST_MULTIPLIER = 0.1;
    public static final int MORTGAGE_INTEREST_ROUNDING = 100; // rounds mortgage interest to nearest 100

}
