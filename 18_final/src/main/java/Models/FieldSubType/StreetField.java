package Models.FieldSubType;

import Controllers.GameController;
import Models.Board;

public class StreetField extends OwnableField {
    private final String STREETCOLOR;
    private final int HOUSEPRICE;
    private int numOfHouses; // 0 to 4
    private boolean hasHotel;
    public static final int MAXNUMOFHOUSES = 4;

    public StreetField(String fieldName, int price, int housePrice, int[] rentArray, String streetColor) {
        super(fieldName, price, rentArray);
        STREETCOLOR = streetColor;
        numOfHouses = 0;
        hasHotel = false;
        HOUSEPRICE = housePrice;
    }

    @Override
    public void updateRent() {
        // conditional operator checks if there is a hotel or not
        // if not, rent is determined by numHouses. if there is, rent is the last index in the array
        currentRent = (!hasHotel) ? RENTARRAY[numOfHouses] : RENTARRAY[RENTARRAY.length-1];
        // double rent if the street is unbuilt and player owns all street of same color
        Board board = GameController.getInstance().getBoard();
        int fieldArrayNum = board.getFieldArrayNumber(fieldName);
        if (numOfHouses == 0 && !hasHotel && board.ownsAllFieldsOfSameType(fieldArrayNum))
            currentRent = 2*RENTARRAY[0];
    }

    @Override
    public void setOwnerNum (int playerNum) {
        // only executes for unbuilt fields
        if (numOfHouses == 0 && !hasHotel &&
            playerNum >= 0 && playerNum <= GameController.getInstance().getTotalPlayers())
            ownerNum = playerNum;
    }

    public int getNumOfHouses() {
        return numOfHouses;
    }

    public void setNumOfHouses(int numOfHouses) {
        if (numOfHouses >= 0 && numOfHouses <= MAXNUMOFHOUSES)
            this.numOfHouses = numOfHouses;
    }

    public boolean hasHotel() {
        return hasHotel;
    }

    public void setHasHotel(boolean hasHotel) {
        this.hasHotel = hasHotel;
    }

    public String getStreetColor() {
        return STREETCOLOR;
    }

    public int getHousePrice() {
        return HOUSEPRICE;
    }

    @Override
    public boolean isStreetField() {
        return true;
    }
}
