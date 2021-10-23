package Game;

import org.junit.jupiter.api.Assertions;

import static org.junit.jupiter.api.Assertions.*;

class AccountTest {
    Account account = new Account();

    // Test that checks for the balance withdrawed can never be negative, and also that the account balance never can
    // go under zero.
    @org.junit.jupiter.api.Test
    void withdrawMoney() {
        for (int i = -1000; i <= 1000; i++) {
            if (i >= 0) {
                account.withdrawMoney(i);
                assertEquals(account.getBalance(), 1000 - i);
                account.setCurrentBalance(1000);
            }

            if (i < 0) {
                account.withdrawMoney(i);
                assertEquals(1000,account.getBalance());
            }
        }
    }

    // Test that checks for the balance deposited can only be positive.
    @org.junit.jupiter.api.Test
    void depositMoney() {

        for (int i = -1000; i<= 1000; i++) {
            if (i >= 0) {
                account.depositMoney(i);
                assertEquals(account.getBalance(),1000 + i);
                account.setCurrentBalance(1000);
            }

            if (i < 0) {
                account.depositMoney(i);
                assertEquals(1000,account.getBalance());
            }
        }
    }

    // Test that checks that it's possible to set the balance, and also that the balance can not be set to a negative
    // number.
    @org.junit.jupiter.api.Test
    void setCurrentBalance() {
        for (int i = -1000; i <= 1000; i++) {
            if (i >= 0) {
                account.setCurrentBalance(i);
                assertEquals(account.getBalance(), i);
            }

            if (i < 0) {
                account.setCurrentBalance(i);
                assertEquals(1000, account.getBalance());
            }
        }
    }

    @org.junit.jupiter.api.Test
    void getBalance() {
        assertEquals(account.getBalance(),account.getBalance());
    }
}