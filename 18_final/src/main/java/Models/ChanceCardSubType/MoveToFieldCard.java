package Models.ChanceCardSubType;

import Controllers.*;
import Models.*;

public class MoveToFieldCard extends ChanceCard {
    private final String FIELDNAME;

//This code has been modified from previous assignment CDIO 3 by Maj Kyllesbech, Gabriel H, Kierkegaard, Mark Bidstrup & Xiao Chen handed in 26. November 2021

    public MoveToFieldCard(String text, String fieldName) {
        super(text);
        FIELDNAME = fieldName;
    }

    @Override
    public void effect(Player currentplayerobject) {
        Board board = GameController.getInstance().getBoard();
        int moveToFieldNum = board.getFieldArrayNumber(FIELDNAME);
        if (board.getFieldObject(moveToFieldNum) instanceof JailField) {
            moveToFieldNum = 10;
            ViewController.getInstance().goToJailMessage();
        }

        // calls a method that moves the GUI car
        ViewController.getInstance().moveGUICar(currentplayerobject.OnField(),
        moveToFieldNum,GameController.getInstance().getCurrentPlayerNum());

        // checks whether the player would pass START moving there and thus get a START bonus
        // no start bonus will be collected if the player is moving to the jail field
        if(!board.getFieldObject(moveToFieldNum).isJailField()  &&
                moveToFieldNum < currentplayerobject.OnField()) {
            currentplayerobject.getAccount().depositMoney(GameSettings.STARTBONUS);
            ViewController.getInstance().updateGUIBalance();
            ViewController.getInstance().startBonusMessage();
        }
        // sets the onField variable to the new field
        currentplayerobject.moveToField(moveToFieldNum);
    }
}

