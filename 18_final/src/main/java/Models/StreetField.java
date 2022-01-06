package Models;

public class StreetField extends OwnableField {
    private final String STREETCOLOR;
    private final int HOUSEPRICE;
    private int numOfHouses; // 0 to 4
    private boolean hasHotel;
    private final int MAXNUMOFHOUSES = 4;

    public StreetField(String fieldName, int price, int housePrice, int[] rentArray, String streetColor) {
        super(fieldName, price, rentArray);
        STREETCOLOR = streetColor;
        currentRent = rentArray[0];
        numOfHouses = 0;
        hasHotel = false;
        HOUSEPRICE = housePrice;
    }

    @Override
    public void updateRent() {
    }

    public int getNumOfHouses() {
        return numOfHouses;
    }

    public void setNumOfHouses(int numOfHouses) {
        if (numOfHouses >= 0 && numOfHouses <= MAXNUMOFHOUSES)
            this.numOfHouses = numOfHouses;
    }

    public boolean getHotelStatus() {
        return hasHotel;
    }

    public void setHotelStatus (boolean hasHotel) {
        this.hasHotel = hasHotel;
    }

    public String getStreetColor() {
        return STREETCOLOR;
    }

    public int getHOUSEPRICE() {
        return HOUSEPRICE;
    }
}
