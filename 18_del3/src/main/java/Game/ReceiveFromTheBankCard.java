package Game;

public class ReceiveFromTheBankCard extends ChanceCard {
    private final String description;
    private final int receiveAmount;

    public ReceiveFromTheBankCard(String description, int receiveAmount) {
        this.description = description;
        this.receiveAmount = receiveAmount; }

    @Override
    public String chanceCardText() {
        String str = "";
        str += description +".\n";
        str += "Receive M$" + receiveAmount +" from the bank.";
        return str;
    }

    @Override
    public void effect(Player currentplayerobject) {
        currentplayerobject.getAccount().depositMoney(receiveAmount);
        MonopolyGame.updatePlayerBalance();
    }
}
