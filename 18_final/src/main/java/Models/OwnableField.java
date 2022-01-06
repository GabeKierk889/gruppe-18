package Models;

public abstract class OwnableField extends Field {
    protected final int PRICE;
    protected int ownerNum;
    protected boolean isMortgaged;
    protected int currentRent;
    protected final int[] RENTARRAY;
    protected final int MORTGAGEVALUE;
    private final double MORTGAGE_PRICE_RATIO = 0.5;

    public OwnableField(String fieldName, int price, int[] rentArray) {
        super(fieldName);
        this.PRICE = price;
        ownerNum = 0;
        isMortgaged = false;
        this.RENTARRAY = rentArray;
        MORTGAGEVALUE = (int) (PRICE * MORTGAGE_PRICE_RATIO);
    }

    @Override
    public void landOnField(Player currentplayerobject) {
        // buy field or pay money
        if (ownerNum == 0) {
            buyField(currentplayerobject);
        }
        // placeholder
        }

    public void buyField(Player currentplayerobject) {
    }

    public int getRent() {
        return currentRent;
    }

    private void auctionField() { }

    public abstract void updateRent();

    public void setMortgageStatus(boolean status) {this.isMortgaged = status; }

    public int getOwnerNum() {
        return ownerNum;
    }

    public void setOwnerNum (int playerNum) {
        ownerNum = playerNum;
    }

    public int getPRICE() {
        return PRICE;
    }

    public double getMortgageValue() { return MORTGAGEVALUE; }

    @Override
    public boolean isOwnableField() {
        return true;
    }

}
