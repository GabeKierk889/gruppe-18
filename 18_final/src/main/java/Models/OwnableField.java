package Models;

import Controllers.GameController;
import Controllers.ViewController;

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
        int currentPlayerNum = GameController.getInstance().getCurrentPlayerNum();
        // buy field or pay money
        if (ownerNum == 0) {
            buyField(currentplayerobject);
        }
        else if (ownerNum != currentPlayerNum
                && !GameController.getInstance().getPlayerObject(ownerNum).getIsInJail()
                && !isMortgaged) {
            updateRent();
            currentplayerobject.getAccount().transferMoney(currentRent,ownerNum);
            // TODO: gui
//             write message to gui that a rent has been paid;
        }
        else if (ownerNum != currentPlayerNum
                && GameController.getInstance().getPlayerObject(ownerNum).getIsInJail())
            ;
            // TODO: gui
//             write message to gui that owner is in jail so no rent needs to be paid
        else if (ownerNum != currentPlayerNum && isMortgaged)
            ;
        // TODO: gui
//         write message to gui that field is mortgaged so no rent needs to be paid
        }

    public void buyField(Player currentplayerobject) {
        boolean playerWantsToBuyField;
        playerWantsToBuyField = ViewController.getInstance().showMessageAndGetBooleanUserInput(13,15,16,fieldName,""+PRICE);
        int currentPlayerNum = GameController.getInstance().getCurrentPlayerNum();
        if (playerWantsToBuyField) { // use gui to get user input on whether a player wants to buy a field
            buyFieldTransaction(currentPlayerNum,currentplayerobject, PRICE);
        }
        else
            auctionField();
    }

    private void buyFieldTransaction(int currentPlayerNum, Player currentplayerobject, int purchasePrice) {
        Board board = GameController.getInstance().getBoard();
        int fieldArrayNum = board.getFieldArrayNumber(fieldName);
        setOwnerNum(currentPlayerNum);
        currentplayerobject.getAccount().withdrawMoney(purchasePrice);
        board.updateRentForAllFieldsOfSameType(fieldArrayNum);
        ViewController.getInstance().updateGUIBalance();
        ViewController.getInstance().showTakeTurnMessageWithPlayerName(14,fieldName,""+purchasePrice);
        ViewController.getInstance().formatFieldBorder(fieldArrayNum);
    }

    public void auctionField() {
        // TODO: for later, lower priority
    }

    public void mortgageField(Player currentplayerobject) {
        if (!isMortgaged) {
            isMortgaged = true;
            currentplayerobject.getAccount().depositMoney(MORTGAGEVALUE);
            // TODO: gui, for later, lower priority
            // output message via gui that field has been mortgaged and money deposited
        }
    }

    public void unMortgageField(Player currentplayerobject) {
        if(isMortgaged) {
            // calculate and round interest to nearest int round
            int round = GameSettings.MORTGAGE_INTEREST_ROUNDING;
            int interest = (int) Math.round(MORTGAGEVALUE*GameSettings.MORTGAGE_INTEREST_MULTIPLIER /round) * round;
            currentplayerobject.getAccount().withdrawMoney(MORTGAGEVALUE+interest);
            isMortgaged = false;
            // TODO: gui, for later, lower priority
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
        if (playerNum >= 0 && playerNum <= GameController.getInstance().getTotalPlayers())
            ownerNum = playerNum;
    }

    public int getFieldPrice() {
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
