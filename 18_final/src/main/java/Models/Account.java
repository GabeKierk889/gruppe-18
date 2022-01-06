package Models;

//This code has been modified from previous assignment CDIO 3 by Maj Kyllesbech, Gabriel H, Kierkegaard, Mark Bidstrup & Xiao Chen handed in 29. October 2021

public class Account {

    private int balance;

    public Account () {
        balance = GameSettings.STARTINGBALANCE;
    }

    // Withdraws money.
    public void withdrawMoney (double withdrawal) {
        // currently allowing a negative balance
        if (withdrawal > 0) {
            this.balance -= withdrawal; }

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
            this.balance += deposit;
        }
    }

    public void transferMoney(double amount, int recipientPlayerNum) {
        withdrawMoney(amount);
        MonopolyGame.game.getPlayerObject(recipientPlayerNum).getAccount().depositMoney(amount);
    }

    public void setBalance(int balance) {
        if (balance >= 0) {
            this.balance = balance;
        }
    }

    public int getBalance() {
        return balance;
    }

}
