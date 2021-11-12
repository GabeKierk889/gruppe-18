public class AmusementField extends Field {
    private int price;
    private int ownerNum;

    public AmusementField(String fieldName, int price) {
        super(fieldName);
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
            currentplayerobject.getAccount().transferMoney(price,ownerNum);
        }
    }

    public void setupBooth (Player currentplayerobject) {
        setOwnerNum(MonopolyGame.game.getCurrentPlayerNumber());
        currentplayerobject.getAccount().withdrawMoney(price);
        // Make a GUI message for setup booth later
    }

    public void setOwnerNum (int playerNum) {
        ownerNum = playerNum;
    }

    public int getPrice() {
        return price;
    }
}
