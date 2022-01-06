package Models;

//This code has been modified from previous assignment CDIO 3 by Maj Kyllesbech, Gabriel H, Kierkegaard, Mark Bidstrup & Xiao Chen handed in 26. November 2021

public abstract class ChanceCard {
    protected final String TEXT;

    public ChanceCard(String text) {
        TEXT = text;
    }

    public String getChanceCardText() {
        return TEXT;
    }

    public abstract void effect(Player currentplayerobject);

}
