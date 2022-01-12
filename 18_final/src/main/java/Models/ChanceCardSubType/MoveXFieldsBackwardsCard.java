package Models.ChanceCardSubType;

import Controllers.GameController;
import Controllers.ViewController;
import Controllers.ViewController_GUIMessages;
import Models.ChanceCard;
import Models.Player;

public class MoveXFieldsBackwardsCard extends ChanceCard {
    private final int STEPSTOMOVE;

    public MoveXFieldsBackwardsCard(String text, int[] numOfFields) {
        super(text);
        STEPSTOMOVE = numOfFields[0];
    }

    @Override
    public void effect(Player currentplayerobject) {
        int oldLocation = currentplayerobject.OnField();
        int newLocation = oldLocation - STEPSTOMOVE;
        int currentPlayer = GameController.getInstance().getCurrentPlayerNum();

        if (newLocation < 0) {
            newLocation = newLocation + GameController.getInstance().getBoard().getTotalNumOfFields();
        }
        currentplayerobject.moveToField(newLocation);
        ViewController.getInstance().moveGUICar(oldLocation, newLocation, currentPlayer);
    }
}
