package Game;

public class Account {
    private int currentBalance;
    public static final int STARTINGBALANCE = 35;
    public static final int STARTBONUS = 2;
    public static final int JAILFEE = 1;

    public Account () {
        currentBalance = STARTINGBALANCE;
    }

    // Withdraws money.
    public void withdrawMoney (double withdrawal) {
        // currently allowing a negative balance
        if (withdrawal > 0) {
        this.currentBalance -= withdrawal; }

        // Only allows withdrawal if the amount is greater than zero and also the withdrawal amount is greater than
        // the current balance.
//        if (withdrawal > 0) {
//            if (withdrawal <= currentBalance) {
//                this.currentBalance -= withdrawal;
//            } else {
//                // If the withdrawal amount is greater than the account balance, then the account balance i reset
//                // to zero.
//                this.currentBalance = 0;
//            }
//        }
    }

    // Deposits money.
    public void depositMoney(double deposit) {
        // Only allows depositing a positive amount to the current balance.
        if (deposit > 0) {
            this.currentBalance += deposit;
        }
    }

    public void transferMoney(double amount, int recipientPlayerNum) {
        withdrawMoney(amount);
        MonopolyGame.game.getPlayerObject(recipientPlayerNum).getAccount().depositMoney(amount);
    }

    public void setCurrentBalance(int balance) {
        if (balance >= 0) {
            this.currentBalance = balance;
        }
    }

    public int getBalance() {
        return currentBalance;
    }
}
