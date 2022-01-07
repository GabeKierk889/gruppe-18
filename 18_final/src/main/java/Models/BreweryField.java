package Models;

import Controllers.GameController;

public class BreweryField extends OwnableField {

    public BreweryField(String fieldName, int price, int[] rentArray) {
        super(fieldName, price, rentArray);
    }

    @Override
    public void updateRent() {
        int diceSum = GameController.getInstance().getDiceCup().getSum();
        Board board = GameController.getInstance().getBoard();
        int fieldArrayNum = board.getFieldArrayNumber(fieldName);
        if (board.ownsAllFieldsOfSameType(fieldArrayNum))
            currentRent = RENTARRAY[1] * diceSum;
        else
            currentRent = RENTARRAY[0] * diceSum;
    }
}
