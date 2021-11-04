public class Account {
    private int currentBalance;
    private static final int STARTINGBALANCE = 35;

    public Account () {
        currentBalance = STARTINGBALANCE;
    }

    // Withdraws money.
    public void withdrawMoney (double withdrawal) {
        // Only allows withdrawal if the amount is greater than zero and also the withdrawal amount is greater than
        // the current balance.
        if (withdrawal > 0) {
            if (withdrawal <= currentBalance) {
                this.currentBalance -= withdrawal;
            } else {
                // If the withdrawal amount is greater than the account balance, then the account balance i reset
                // to zero.
                this.currentBalance = 0;
            }
        }
    }

    // Deposits money.
    public void depositMoney(double deposit) {
        // Only allows depositing a positive amount to the current balance.
        if (deposit > 0) {
            this.currentBalance += deposit;
        }
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
