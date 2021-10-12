package Game;

public class Account {
    private double currentBalance;

    public Account (double startingBalance) {
        this.currentBalance = startingBalance;
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

    public void setCurrentBalance(double balance) {
        this.currentBalance = balance;
    }

    public String getBalance() {
        return "The current balance is: " + currentBalance;
    }
}
