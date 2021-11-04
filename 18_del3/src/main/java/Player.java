public class Player {
    private String name;
    private Account account;
    private int onField;
    private boolean isBankrupt; // Keeps track of whether a player has gone bankrupt/ lost all their money

    public Player(String name){
        Account acct = new Account();
        account = acct;
        this.name = name;
        onField = 0;
        isBankrupt = false;
    }

    public int movePlayer(int stepsToMove) {
        if (onField + stepsToMove < Field.getTotalnumberOfFields())
            onField += stepsToMove;
        else onField = onField + stepsToMove - Field.getTotalnumberOfFields();
        return onField; }

    public void collectStartBonus(int diceThrow) {
        // only applies to regular turns/ passing START by throwing the dice, not chance card situations
        if (OnField() < diceThrow)
        getAccount().depositMoney(Account.STARTBONUS);
    }

    public String getName() {
        return name;
    }
    public Account getAccount() {
        return account;
    }
    public int OnField() {return onField;}
    public void isBankrupt(boolean isPlayerBankrupt) { this.isBankrupt = isPlayerBankrupt; }
}
