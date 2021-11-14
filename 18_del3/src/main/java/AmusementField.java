public class AmusementField extends Field {
    private int price;
    private int ownerNum;
    private String fieldColor;

    public AmusementField(String fieldColor, String fieldName, int price) {
        super(fieldName);
        this.fieldColor = fieldColor;
        this.price = price;
        ownerNum = 0;
    }

    @Override
    public void landOnField(Player currentplayerobject) {
        // set up booth or pay money
        if (ownerNum == 0) {
            setupBooth(currentplayerobject);
        }
        if (ownerNum != MonopolyGame.game.getCurrentPlayerNumber()) {
            if (! Board.onePlayerOwnsAllFieldsofSameColor(fieldNumber)) {
                currentplayerobject.getAccount().transferMoney(price,ownerNum);
                MonopolyGame.payBoothPriceMessage(); }
            else if (Board.onePlayerOwnsAllFieldsofSameColor(fieldNumber)) {
                currentplayerobject.getAccount().transferMoney(2*price,ownerNum);
                MonopolyGame.payDoubleBoothPriceMessage(); }
        }
    }

    public void setupBooth (Player currentplayerobject) {
        setOwnerNum(MonopolyGame.game.getCurrentPlayerNumber());
        currentplayerobject.getAccount().withdrawMoney(price);
        MonopolyGame.setupBoothMessage();
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

    public String getFieldColor() { return fieldColor; }

}
