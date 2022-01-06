package Models;

//This code has been modified from previous assignment CDIO 3 by Maj Kyllesbech, Gabriel H, Kierkegaard, Mark Bidstrup & Xiao Chen handed in 29. October 2021

public class Board {

    private Field[];

    public boolean ownsAllOfSameType(int fieldArrayNum);
    public void updateRentForAllOfTheSameType(int fieldArrayNum);
    public int numOfShippingFieldsOwned(int playerNum);
    public int getTotalNumOfFields();

    public void buildHouse();
    public void buildHotel();
    public void sellHouse();
}
