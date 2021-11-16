public class MoveToTheBeachWalkCard extends ChanceCard {


    @Override
    public String chanceCardText() {
        return "Move to The Beach Walk and enjoy the day on the beach!";
    }

    @Override
    public void effect(Player currentplayerobject) {
        // checks whether the player would pass START and get a START bonus
        MonopolyGame.moveCar(Board.getFieldArrayNumber("The Beach Walk"));
        if(Board.getFieldArrayNumber("The Beach Walk")<currentplayerobject.OnField()) {
            currentplayerobject.getAccount().depositMoney(Account.STARTBONUS);
            MonopolyGame.updatePlayerBalance();
            MonopolyGame.showStartBonusMessage();
        }
        currentplayerobject.movePlayertoField("The Beach Walk");
    }
}
