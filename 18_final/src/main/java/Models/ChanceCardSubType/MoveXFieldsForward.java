package Models.ChanceCardSubType;

import Controllers.GameController;
import Controllers.ViewController;
import Models.ChanceCard;
import Models.Player;

public class MoveXFieldsForward extends ChanceCard {
    private final int STEPSTOMOVE;

    public MoveXFieldsForward(String text, int[] numOfFields) {
        super(text);
        STEPSTOMOVE = numOfFields[0];
    }

    @Override
    public void effect(Player currentplayerobject){
        int oldLocation = currentplayerobject.OnField();
        int newLocation = currentplayerobject.moveSteps(STEPSTOMOVE);
        int currentPlayer = GameController.getInstance().getCurrentPlayerNum();

        ViewController.getInstance().moveGUICar(oldLocation,newLocation,currentPlayer);
        currentplayerobject.collectStartBonus(STEPSTOMOVE);
    }
}
