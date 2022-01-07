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
        int currentPlayerNum = GameController.getInstance.getCurrentPlayerNum;
        Player ownerPlayerObject = GameController.getInstance.getPlayerObject(ownerNum);
        // buy field or pay money
        if (ownerNum == 0) {
            buyField(currentplayerobject);
        }
        else if (ownerNum != currentPlayerNum
                && !ownerPlayerObject.getIsInJail()
                && !isMortgaged) {
            updateRent();
            currentplayerobject.getAccount().transferMoney(currentRent,ownerNum);
//             write message to gui that a rent has been paid;
        }
        else if (ownerNum != currentPlayerNum
                && ownerPlayerObject.getIsInJail())
            ;
//             write message to gui that owner is in jail so no rent needs to be paid
        else if (ownerNum != currentPlayerNum && isMortgaged)
            ;
//         write message to gui that field is mortgaged so no rent needs to be paid
        }

    public void buyField(Player currentplayerobject) {
        boolean playerWantsToBuyField; // send a message to gui and get a boolean result
        Board board = GameController.getInstance.getBoard;
        int currentPlayerNum = GameController.getInstance.getCurrentPlayerNum;
        int fieldArrayNum = board.getFieldArrayNumber(fieldName);
        if (playerWantsToBuyField) {
            setOwnerNum(currentPlayerNum);
            currentplayerobject.getAccount().withdrawMoney(PRICE);
            board.updateRentForAllFieldsOfSameType(fieldArrayNum);
            // show message in GUI that a field has been bought
        }
        else
            auctionField();
    }

    private void auctionField() { }

    public void mortgageField(Player currentplayerobject) {
        // does not output any messages to gui
        if(!isMortgaged) {
            isMortgaged = true;
            currentplayerobject.getAccount().depositMoney(MORTGAGEVALUE);
            // output message via gui that field has been mortgaged and money deposited
        }
    }

    public void unMortgageField(Player currentplayerobject) {
        // does not output any messages to gui
        if(isMortgaged) {
            // calculate and round interest to nearest int round
            int round = GameSettings.MORTGAGE_INTEREST_ROUNDING;
            int interest = (int) Math.round(MORTGAGEVALUE*GameSettings.MORTGAGE_INTEREST_MULTIPLIER /round) * round;
            currentplayerobject.getAccount().withdrawMoney(MORTGAGEVALUE+interest);
            isMortgaged = false;
            // output message via gui that field has been unmortgaged and money paid
        }
    }

    public int getRent() {
        return currentRent;
    }

    public abstract void updateRent();

    public int getOwnerNum() {
        return ownerNum;
    }

    public void setOwnerNum (int playerNum) {
        ownerNum = playerNum;
    }

    public int getPRICE() {
        return PRICE;
    }
    protected void setMortgageStatus(boolean status) {this.isMortgaged = status; }

    protected int getMortgageValue() { return MORTGAGEVALUE; }

    public void tradeField() { }


    @Override
    public boolean isOwnableField() {
        return true;
    }

}
