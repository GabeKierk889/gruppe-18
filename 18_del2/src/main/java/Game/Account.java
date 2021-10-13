package Game;

public class Account {
    private int currentBalance;
    private static int startingBalance = 1000;

    public Account () {
        currentBalance = startingBalance;
    }

    // Withdraws money.
    public void withdrawMoney (double withdrawal) {
        if (withdrawal < 0) {
            System.out.println("You can't withdraw a negative number.");
        } else {
            this.currentBalance -= withdrawal;
        }
    }

    // Deposits money.
    public void depositMoney(double deposit) {
        if (deposit < 0) {
            System.out.println("You can't deposit a negative number.");
        } else {
            this.currentBalance += deposit;
        }
    }

    public void setCurrentBalance(int balance) {
        this.currentBalance = balance;
    }

    public int getBalance() {
        return currentBalance;
    }
}
