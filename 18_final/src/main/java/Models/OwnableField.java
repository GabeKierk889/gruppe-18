package Models;

public abstract class OwnableField extends Field {
    protected final int PRICE;
    protected int ownerNum;
    protected boolean isMortgaged;
    protected int currentRent;
    protected final int[] RENTARRAY;
    protected final int MORTGAGEVALUE;

    public OwnableField(String fieldName) {
        super(fieldName);
        ownerNum = 0;
        isMortgaged = false;
        // need to initialize PRICE, rentarray, mortgagevalue etc.
    }

    @Override
    public void landOnField(Player currentplayerobject) {
        // buy field or pay money
        if (ownerNum == 0) {
            buyField(currentplayerobject);
        }
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
