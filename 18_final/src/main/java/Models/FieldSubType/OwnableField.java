package Models.FieldSubType;

import Controllers.GameController;
import Controllers.ViewController;
import Controllers.ViewController_GUIMessages;
import Models.Board;
import Models.Field;
import Models.Player;

public abstract class OwnableField extends Field {
    protected final int PRICE;
    protected int ownerNum;
    protected boolean isMortgaged;
    protected int currentRent;
    protected final int[] RENTARRAY;
    public final int MORTGAGEVALUE;
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
        if (ownerNum == 0) {
            buyField();
        } else { // pay rent
            String ownerName = GameController.getInstance().getPlayerName(ownerNum);
            if (ownerNum != currentPlayerNum  && !isMortgaged
                    && !GameController.getInstance().getPlayerObject(ownerNum).getIsInJail() ) {
                updateRent();
                showGUIPayRentMessage(ownerName);
                currentplayerobject.getAccount().transferMoney(currentRent, ownerNum);
                ViewController.getInstance().updateGUIBalance();
            } else if (ownerNum != currentPlayerNum && GameController.getInstance().getPlayerObject(ownerNum).getIsInJail())
                // gui message to show that owner is in jail and no rent is needed
                ViewController_GUIMessages.getInstance().showTakeTurnMessageWithPlayerName(19, 29, -1, ownerName, "", "");
            else if (ownerNum != currentPlayerNum && isMortgaged)
                // gui message to show that field is mortgaged and no rent is needed
                ViewController_GUIMessages.getInstance().showTakeTurnMessageWithPlayerName(19, 30, -1, ownerName, "", "");
        }
    }

    public void buyField() {
        boolean playerWantsToBuyField;
        playerWantsToBuyField = ViewController_GUIMessages.getInstance().showMessageAndGetBooleanUserInput(13, 15, 16, fieldName, "" + PRICE, -1);
        int currentPlayerNum = GameController.getInstance().getCurrentPlayerNum();
        if (playerWantsToBuyField) { // use gui to get user input on whether a player wants to buy a field
            buyFieldTransaction(currentPlayerNum, PRICE);
        } else
            auctionField();
    }

    private void buyFieldTransaction(int playerNum, int purchasePrice) {
        Board board = GameController.getInstance().getBoard();
        String playerName = GameController.getInstance().getPlayerName(playerNum);
        int fieldArrayNum = board.getFieldArrayNumber(fieldName);
        setOwnerNum(playerNum);
        ViewController.getInstance().formatFieldBorder(fieldArrayNum);
        GameController.getInstance().getPlayerObject(playerNum).getAccount().withdrawMoney(purchasePrice);
        if (!GameController.getInstance().getPlayerObject(playerNum).getIsBankrupt()) {
            board.updateRentForAllFieldsOfSameType(fieldArrayNum);
            ViewController.getInstance().updateGUIBalance();
            // write out buy field information to player via gui
            sendGUIPurchaseMessage(playerName,purchasePrice); }
    }

    public void auctionField() {
        int currentBid, highestBid, numberOfHighestBidders, auctionWonByPlayerNum;
        int[] bids = new int[GameController.getInstance().getTotalPlayers()];
        ViewController_GUIMessages.getInstance().showTakeTurnMessage(78,"","","");
        int auctionRound = 0;
        highestBid = 0;
        numberOfHighestBidders = 0;
        auctionWonByPlayerNum = 0;
        String playerName;
        do {
            auctionRound++;
            if (auctionRound == 1) {
                for (int i = 0; i < bids.length; i++) {
                    if (!GameController.getInstance().getPlayerObject(i+1).getIsBankrupt()) {
                        playerName = GameController.getInstance().getPlayerName(i+1);
                        currentBid = ViewController_GUIMessages.getInstance().auctionBidding(fieldName, PRICE, playerName);
                        bids[i] = currentBid;
                        if (currentBid > highestBid)
                            highestBid = currentBid;
                    }
                }
            } else {
                highestBid = 0;
                numberOfHighestBidders = 0;
                auctionWonByPlayerNum = 0;
                ViewController_GUIMessages.getInstance().showTakeTurnMessage(80,"","","");
                for (int i = 0; i < bids.length; i++) {
                    if (bids[i] > 0) {
                        playerName = GameController.getInstance().getPlayerName(i+1);
                        currentBid = ViewController_GUIMessages.getInstance().auctionBidding(fieldName, PRICE, playerName);
                        bids[i] = currentBid;
                        if (currentBid > highestBid)
                            highestBid = currentBid;
                    }
                }
            }
            if (highestBid == 0) {
                ViewController_GUIMessages.getInstance().showTakeTurnMessage(81,"","","");
                return; }
            for (int i = 0; i < bids.length; i++) {
                if (bids[i] == highestBid) {
                    numberOfHighestBidders++;
                    auctionWonByPlayerNum = i + 1; }
                else
                    bids[i] = 0; // resets the bids of all other bids except those with winning bids
            }
            playerName = GameController.getInstance().getPlayerName(auctionWonByPlayerNum);
        }
        while (numberOfHighestBidders != 1);
        ViewController_GUIMessages.getInstance().showTakeTurnMessage(79,playerName,""+highestBid,"");
        buyFieldTransaction(auctionWonByPlayerNum,highestBid);
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
            if (board.ownsAllFieldsOfSameType(board.getFieldArrayNumber(fieldName)) && unBuilt) {
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
        ViewController_GUIMessages.getInstance().showTakeTurnMessageWithPlayerName(19, lineArray, 28, ownerName, str, "" + currentRent);
    }

    private void sendGUIPurchaseMessage(String playerName, int purchasePrice) {
        Board board = GameController.getInstance().getBoard();
        String message1 = String.format(ViewController_GUIMessages.getInstance().getTakeTurnGUIMessages(14), fieldName, "" + purchasePrice);
        // if owner owns several shipping fields, display message that rent is higher
        if (this.isShippingField() && 1 < board.numOfShippingFieldsOwned(ownerNum))
            ViewController_GUIMessages.getInstance().showTakeTurnMessageWithPlayerName(playerName,message1, 39);
            // if owner owns several brewery fields, display message that rent is higher
        else if (this.isBreweryField() && board.ownsAllFieldsOfSameType(board.getFieldArrayNumber(fieldName)))
            ViewController_GUIMessages.getInstance().showTakeTurnMessageWithPlayerName(playerName,message1, 40);
            // if owner can collect higher rent from street fields
        else if (this.isStreetField() && board.ownsAllFieldsOfSameType(board.getFieldArrayNumber(fieldName))) {
            String str1, str2, str3;
            str1 = ((StreetField) this).getStreetColor();
            str2 = ViewController_GUIMessages.getInstance().getTakeTurnGUIMessages(25);
            str3 = "" + StreetField.MAXNUMOFHOUSES;
            ViewController_GUIMessages.getInstance().showTakeTurnMessageWithPlayerName(playerName,message1, 36, 37, 38, str1, str2, str3);
        } else ViewController_GUIMessages.getInstance().showTakeTurnMessageWithPlayerName(playerName,message1);
    }

    @Override
    public boolean isOwnableField() { return true; }

    public void setOwnerNum(int playerNum) {
        if (playerNum >= 0 && playerNum <= GameController.getInstance().getTotalPlayers())
            ownerNum = playerNum;
    }

    public int getRent() { return currentRent; }
    public abstract void updateRent();
    public int getOwnerNum() { return ownerNum; }
    public int getFieldPrice() { return PRICE;  }
    public void setMortgageStatus(boolean status) { this.isMortgaged = status; }
    public boolean getIsMortgaged() { return isMortgaged; }
}
