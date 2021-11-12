public class Player {
    private String name;
    private Account account;
    private int onField;
    private boolean isBankrupt; // Keeps track of whether a player has gone bankrupt/ lost all their money
    private boolean isInJail;

    public Player(String name){
        Account acct = new Account();
        account = acct;
        this.name = name;
        onField = 0;
        isBankrupt = false;
        isInJail = false;
    }

    public int movePlayer(int stepsToMove) {
        if (onField + stepsToMove < Field.getTotalnumberOfFields())
            onField += stepsToMove;
        else onField = onField + stepsToMove - Field.getTotalnumberOfFields();
        return onField; }

    public void collectStartBonus(int diceThrow) {
        // only applies to regular turns/ passing START by throwing the dice, not chance card situations
        if (onField < diceThrow)
        getAccount().depositMoney(Account.STARTBONUS);
    }

    public void setPlayerOnField(int fieldArrayNumber) {
        if (fieldArrayNumber <= Field.getTotalnumberOfFields()-1)
            onField=fieldArrayNumber;
        else System.out.println("Error - the field number you entered does not exist.");
    }

    public String getName() {
        return name;
    }
    public Account getAccount() {
        return account;
    }
    public int OnField() {return onField;}
    public void isBankrupt(boolean isPlayerBankrupt) { this.isBankrupt = isPlayerBankrupt; }
    public void setIsInJail (boolean isInJail) { this.isInJail = isInJail; }
    public boolean getIsInJail () { return isInJail; }
    public boolean getIsBankrupt () { return isBankrupt; }

}
