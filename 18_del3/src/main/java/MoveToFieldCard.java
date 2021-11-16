public class MoveToFieldCard extends ChanceCard {
    private String moveTofieldName;

    public MoveToFieldCard(String moveTofieldName) {
        this.moveTofieldName = moveTofieldName; }

    @Override
    public String chanceCardText() {
        String str = "";
        if (moveTofieldName.equalsIgnoreCase("The Beach Walk"))
            str = "Move to The Beach Walk and enjoy the day on the beach!";
        if (moveTofieldName.equalsIgnoreCase("Start"))
            str = "Move to the START field and receive M$"+Account.STARTBONUS;
        return str;
    }

    @Override
    public void effect(Player currentplayerobject) {
        // calls a method that moves the GUI car
        MonopolyGame.moveCar(Board.getFieldArrayNumber(moveTofieldName));
        // checks whether the player would pass START moving there and thus get a START bonus
        if(Board.getFieldArrayNumber(moveTofieldName)<currentplayerobject.OnField()) {
            currentplayerobject.getAccount().depositMoney(Account.STARTBONUS);
            MonopolyGame.updatePlayerBalance();
            MonopolyGame.showStartBonusMessage();
        }
        // sets the onField variable to the new field
        currentplayerobject.movePlayertoField(moveTofieldName);
    }
}
