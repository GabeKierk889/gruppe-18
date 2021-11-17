public class MoveToFieldCard extends ChanceCard {
    private final String moveToFieldName;

    public MoveToFieldCard(String moveToFieldName) {
        this.moveToFieldName = moveToFieldName; }

    @Override
    public String chanceCardText() {
        String str = "";
        if (moveToFieldName.equalsIgnoreCase("The Beach Walk"))
            str = "Move to The Beach Walk and enjoy the day on the beach!";
        if (moveToFieldName.equalsIgnoreCase("Start"))
            str = "Move to the START field and receive M$"+Account.STARTBONUS;
        return str;
    }

    @Override
    public void effect(Player currentplayerobject) {
        // calls a method that moves the GUI car
        MonopolyGame.moveCarToFieldArrayNum(Board.getFieldArrayNumber(moveToFieldName));
        // checks whether the player would pass START moving there and thus get a START bonus
        if(Board.getFieldArrayNumber(moveToFieldName) < currentplayerobject.OnField()) {
            currentplayerobject.getAccount().depositMoney(Account.STARTBONUS);
            MonopolyGame.updatePlayerBalance();
            MonopolyGame.showStartBonusMessage();
        }
        // sets the onField variable to the new field
        currentplayerobject.movePlayerToField(moveToFieldName);
    }
}
