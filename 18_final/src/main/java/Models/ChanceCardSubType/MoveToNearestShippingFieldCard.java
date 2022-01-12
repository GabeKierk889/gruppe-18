package Models.ChanceCardSubType;

import Controllers.GameController;
import Controllers.ViewController;
import Controllers.ViewController_GUIMessages;
import Models.*;

public class MoveToNearestShippingFieldCard extends ChanceCard {
    int rentMultiplier;

    public MoveToNearestShippingFieldCard(String text, int rentMultiplier) {
        super(text);
        this.rentMultiplier = rentMultiplier;
    }

    @Override
    public void effect(Player currentplayerobject) {
        Board board = GameController.getInstance().getBoard();
        int nearestShippingFieldNum = 0;
        int onField = currentplayerobject.OnField();
        int arrayNum;
        for (int i = onField; i < onField + board.getTotalNumOfFields(); i++) {
            arrayNum = i % board.getTotalNumOfFields();
            if (board.getFieldObject(arrayNum).isShippingField()) {
                nearestShippingFieldNum = arrayNum;
                break;
            }
        }

        // calls a method that moves the GUI car
        ViewController.getInstance().moveGUICar(onField, nearestShippingFieldNum,GameController.getInstance().getCurrentPlayerNum());

        // checks whether the player would pass START moving there and thus get a START bonus
        if( nearestShippingFieldNum < onField) {
            currentplayerobject.getAccount().depositMoney(GameSettings.STARTBONUS);
            ViewController.getInstance().updateGUIBalance();
            ViewController_GUIMessages.getInstance().startBonusMessage();
        }
        // if double rent is needed, then transfer an additional portion of rent prior to landOnField method being called
        int ownerNum = ((ShippingField) board.getFieldObject(nearestShippingFieldNum)).getOwnerNum();
        int extraRentFactor = rentMultiplier - 1;
        int extraRentAmount;
        if (ownerNum != 0 && ownerNum != GameController.getInstance().getCurrentPlayerNum()) {
            boolean isFieldMortgaged = ((OwnableField)board.getFieldObject(nearestShippingFieldNum)).isMortgaged();
            if (extraRentFactor > 0 && !GameController.getInstance().getPlayerObject(ownerNum).getIsInJail() && !isFieldMortgaged) {
                extraRentAmount = ((OwnableField) board.getFieldObject(nearestShippingFieldNum)).getRent() * extraRentFactor;
                ViewController_GUIMessages.getInstance().showTakeTurnMessageWithPlayerName(48,""+extraRentAmount,"","");
                currentplayerobject.getAccount().transferMoney(extraRentAmount, ownerNum);
                ViewController.getInstance().updateGUIBalance();
            }
        }

        // sets the onField variable to the new field
        currentplayerobject.moveToField(nearestShippingFieldNum);
    }

}
