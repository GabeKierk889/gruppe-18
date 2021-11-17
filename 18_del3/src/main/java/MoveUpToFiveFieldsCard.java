public class MoveUpToFiveFieldsCard extends ChanceCard{

    public MoveUpToFiveFieldsCard() {

    }

    @Override
    public String chanceCardText() {
        return "Move up to five fields. Please select a number between zero to five.";
    }

    @Override
    public void effect(Player currentplayerobject) {
        // calls a method that lets the current player pick a number between 0 and 5
        int moveForward = MonopolyGame.pickZeroToFive();

        // calls a method that moves the GUI car
        MonopolyGame.moveCarToFieldArrayNum(currentplayerobject.OnField() + moveForward);
        // checks whether the player would pass START moving there and thus get a START bonus
        if( (currentplayerobject.OnField() + moveForward) % Board.getTotalNumberOfFields() < currentplayerobject.OnField()) {
            currentplayerobject.getAccount().depositMoney(Account.STARTBONUS);
            MonopolyGame.updatePlayerBalance();
            MonopolyGame.showStartBonusMessage();
        }
        // sets the onField variable to the new field
        currentplayerobject.movePlayerSteps(moveForward);
    }
}
