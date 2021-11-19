package Game;

public class AmusementField extends Field {
    private int price, rent;
    private int ownerNum;
    private String fieldColor;

    public AmusementField(String fieldColor, String fieldName, int price) {
        super(fieldName);
        this.fieldColor = fieldColor;
        this.price = price;
        ownerNum = 0;
        rent = price;
    }

    @Override
    public void landOnField(Player currentplayerobject) {
        // set up booth or pay money
        if (ownerNum == 0) {
            setupBooth(currentplayerobject);
        }
        if (ownerNum != MonopolyGame.game.getCurrentPlayerNumber()) {
            currentplayerobject.getAccount().transferMoney(rent,ownerNum);
            MonopolyGame.payBoothPriceMessage(fieldNumber-1, ownerNum, rent);
        }
    }

    public void setupBooth (Player currentplayerobject) {
        setOwnerNum(MonopolyGame.game.getCurrentPlayerNumber());
        currentplayerobject.getAccount().withdrawMoney(price);
        Board.updateRentForAllFieldsOfSameColor(fieldNumber-1);
        MonopolyGame.setupBoothMessage(fieldNumber-1, price);
    }

    public int getOwnerNum() {
        return ownerNum;
    }

    public void setOwnerNum (int playerNum) {
        ownerNum = playerNum;
    }

    public int getPrice() {
        return price;
    }

    public int getRent() {
        return rent;
    }

    public String getFieldColor() { return fieldColor; }

    public void updateRent() {
        if (Board.onePlayerOwnsAllFieldsOfSameColor(fieldNumber-1))
            rent = 2*price;
        else
            rent = price; }

    public void resetRentToDefault() { rent = price; }

}
