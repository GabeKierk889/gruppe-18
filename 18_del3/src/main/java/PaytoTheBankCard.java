public class PaytoTheBankCard extends ChanceCard {
    private final String description;
    private final int paymentAmount;

    public PaytoTheBankCard(String description, int paymentAmount) {
        this.description = description;
        this.paymentAmount = paymentAmount; }

    @Override
    public String chanceCardText() {
        String str = "";
        str += description +".\n";
        str += "Pay M$" + paymentAmount+".";
        return str;
    }

    @Override
    public void effect(Player currentplayerobject) {
        currentplayerobject.getAccount().withdrawMoney(paymentAmount);
        MonopolyGame.updatePlayerBalance();
    }
}
