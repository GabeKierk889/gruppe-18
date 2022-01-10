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
        currentRent = rentArray[0];
    }

    @Override
    public void landOnField(Player currentplayerobject) {
        Board board = GameController.getInstance().getBoard();
        int currentPlayerNum = GameController.getInstance().getCurrentPlayerNum();
        // buy field
        if (ownerNum == 0) {
            buyField(currentplayerobject);
        }
        else if (ownerNum != 0) { // pay rent
            String ownerName = GameController.getInstance().getPlayerName(ownerNum);
            if (ownerNum != currentPlayerNum
                && !GameController.getInstance().getPlayerObject(ownerNum).getIsInJail()
                && !isMortgaged) {
            this.updateRent();
            currentplayerobject.getAccount().transferMoney(currentRent,ownerNum);
             // gui message to pay rent
            int lineArray = -1; // pass in -1 to method if no extra message needs to be displayed
            String str = "";
            // if owner owns several shipping fields, display message that rent is higher
            if (this.isShippingField() && 1 < board.numOfShippingFieldsOwned(ownerNum))
                lineArray = 21;
            // if owner owns several brewery fields, display message that rent is higher
            else if (this.isBreweryField() && board.ownsAllFieldsOfSameType(board.getFieldArrayNumber(fieldName)))
                lineArray = 22;
            // if owner owns can collect higher rent from street fields
            else if (this.isStreetField()) {
                boolean unBuilt = !((StreetField) this).hasHotel() && ((StreetField) this).getNumOfHouses() == 0;
                // if streetField is unbuilt and player owns all of same color
                if (board.ownsAllFieldsOfSameType(board.getFieldArrayNumber(fieldName)) && unBuilt ) {
                    lineArray = 20;
                    str = ((StreetField) this).getStreetColor();
                }
                // if streetField has houses/ hotels
                else if (false) {
                    lineArray = 23;
//                    str = ((StreetField) this).getStreetColor();
                }
            }
        ViewController.getInstance().showTakeTurnMessageWithPlayerName(19,lineArray,28,ownerName,str,""+currentRent);
        ViewController.getInstance().updateGUIBalance();
        }
        else if (ownerNum != currentPlayerNum && GameController.getInstance().getPlayerObject(ownerNum).getIsInJail())
            // gui message to show that owner is in jail and no rent is needed
            ViewController.getInstance().showTakeTurnMessageWithPlayerName(19,29,-1,ownerName,"","");
        else if (ownerNum != currentPlayerNum && isMortgaged)
            // gui message to show that field is mortgaged and no rent is needed
            ViewController.getInstance().showTakeTurnMessageWithPlayerName(19,30,-1,ownerName,"","");
        } }

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
        ViewController.getInstance().formatFieldBorder(fieldArrayNum);
        ViewController.getInstance().showTakeTurnMessageWithPlayerName(14,fieldName,""+purchasePrice,"");
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
