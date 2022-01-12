package Models;

import Controllers.GameController;
import Controllers.ViewController;
import Controllers.ViewController_GUIMessages;

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
        int currentPlayerNum = GameController.getInstance().getCurrentPlayerNum();
        // buy field
        if (ownerNum == 0) {
            buyField(currentplayerobject);
        }
        else { // pay rent
            String ownerName = GameController.getInstance().getPlayerName(ownerNum);
            if (ownerNum != currentPlayerNum
                && !GameController.getInstance().getPlayerObject(ownerNum).getIsInJail()
                && !isMortgaged) {
            updateRent();
            showGUIPayRentMessage(ownerName);
            currentplayerobject.getAccount().transferMoney(currentRent,ownerNum);
            ViewController.getInstance().updateGUIBalance();
            }
        else if (ownerNum != currentPlayerNum && GameController.getInstance().getPlayerObject(ownerNum).getIsInJail())
            // gui message to show that owner is in jail and no rent is needed
            ViewController_GUIMessages.getInstance().showTakeTurnMessageWithPlayerName(19,29,-1,ownerName,"","");
        else if (ownerNum != currentPlayerNum && isMortgaged)
            // gui message to show that field is mortgaged and no rent is needed
            ViewController_GUIMessages.getInstance().showTakeTurnMessageWithPlayerName(19,30,-1,ownerName,"","");
        } }

    public void buyField(Player currentplayerobject) {
        boolean playerWantsToBuyField;
        playerWantsToBuyField = ViewController_GUIMessages.getInstance().showMessageAndGetBooleanUserInput(13,15,16,fieldName,""+PRICE,-1);
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
        // write out buy field information to player via gui
        if(!currentplayerobject.getIsBankrupt())
            sendGUIPlayerMessage(purchasePrice);
    }

    public void auctionField() {
        // TODO: for later, lower priority
    }

    public void mortgageField(Player currentplayerobject) {
        if (!isMortgaged) {
            isMortgaged = true;
            currentplayerobject.getAccount().depositMoney(MORTGAGEVALUE);
            // TODO: gui, for later, lower priority
            // output message via gui if they want to put on mortgage
        }
    }

    public void unMortgageField(Player currentplayerobject) {
        if(isMortgaged) {
            // calculate and round interest to nearest int round
            int round = GameSettings.MORTGAGE_INTEREST_ROUNDING;
            int interest = (int) Math.round(MORTGAGEVALUE*GameSettings.MORTGAGE_INTEREST_MULTIPLIER /round) * round;
            // TODO: gui, for later, lower priority
            // output message via gui to pay in order to unmortgage field
            isMortgaged = false;
            currentplayerobject.getAccount().withdrawMoney(MORTGAGEVALUE+interest);
            ViewController.getInstance().updateGUIBalance();
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

    private void showGUIPayRentMessage(String ownerName) {
        // gui message to pay rent
        Board board = GameController.getInstance().getBoard();
        int lineArray = -1; // pass in -1 to method if no extra message needs to be displayed
        String str = "";
        // if owner owns several shipping fields, display message that rent is higher
        if (this.isShippingField() && 1 < board.numOfShippingFieldsOwned(ownerNum))
            lineArray = 21;
            // if owner owns several brewery fields, display message that rent is higher
        else if (this.isBreweryField() && board.ownsAllFieldsOfSameType(board.getFieldArrayNumber(fieldName)))
            lineArray = 22;
            // if owner can collect higher rent from street fields
        else if (this.isStreetField()) {
            boolean unBuilt = !((StreetField) this).hasHotel() && ((StreetField) this).getNumOfHouses() == 0;
            // if streetField is unbuilt and player owns all of same color
            if (board.ownsAllFieldsOfSameType(board.getFieldArrayNumber(fieldName)) && unBuilt ) {
                lineArray = 20;
                str = ((StreetField) this).getStreetColor();
            }
            // if streetField has houses/ hotels
            else if (!unBuilt) {
                lineArray = 23;
                int numHouses = ((StreetField) this).getNumOfHouses();
                if (((StreetField) this).hasHotel())
                    str = ViewController_GUIMessages.getInstance().getTakeTurnGUIMessages(26);
                else if (numHouses == 1)
                    str = ViewController_GUIMessages.getInstance().getTakeTurnGUIMessages(24);
                else
                    str = ViewController_GUIMessages.getInstance().getTakeTurnGUIMessages(25);
            }
        }
        ViewController_GUIMessages.getInstance().showTakeTurnMessageWithPlayerName(19,lineArray,28,ownerName,str,""+currentRent);
    }

    private void sendGUIPlayerMessage(int purchasePrice) {
        Board board = GameController.getInstance().getBoard();
        String message1 = String.format(ViewController_GUIMessages.getInstance().getTakeTurnGUIMessages(14),fieldName,""+purchasePrice);
        // if owner owns several shipping fields, display message that rent is higher
        if (this.isShippingField() && 1 < board.numOfShippingFieldsOwned(ownerNum))
            ViewController_GUIMessages.getInstance().showTakeTurnMessageWithPlayerName(message1,39);
            // if owner owns several brewery fields, display message that rent is higher
        else if (this.isBreweryField() && board.ownsAllFieldsOfSameType(board.getFieldArrayNumber(fieldName)))
            ViewController_GUIMessages.getInstance().showTakeTurnMessageWithPlayerName(message1,40);
            // if owner can collect higher rent from street fields
        else if (this.isStreetField()) {
            if (board.ownsAllFieldsOfSameType(board.getFieldArrayNumber(fieldName))) {
                String str1, str2, str3;
                str1 = ((StreetField) this).getStreetColor();
                str2 = ViewController_GUIMessages.getInstance().getTakeTurnGUIMessages(25);
                str3 = ""+StreetField.MAXNUMOFHOUSES;
                ViewController_GUIMessages.getInstance().showTakeTurnMessageWithPlayerName(message1,36,37,38,str1,str2, str3);
            }
            else ViewController_GUIMessages.getInstance().showTakeTurnMessageWithPlayerName(message1);
        }
        else ViewController_GUIMessages.getInstance().showTakeTurnMessageWithPlayerName(message1);
    }

    public boolean isMortgaged() {
        return isMortgaged;
    }
}
