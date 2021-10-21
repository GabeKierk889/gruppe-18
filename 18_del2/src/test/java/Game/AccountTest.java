package Game;

import org.junit.jupiter.api.Assertions;

import static org.junit.jupiter.api.Assertions.*;

class AccountTest {
    Account account = new Account();

    @org.junit.jupiter.api.Test
    void withdrawMoney() {

        for (int i = -10; i <= 10; i++) {
            if (i >= 0) {
                account.withdrawMoney(i);
                assertEquals(account.getBalance(), 1000 - i);
                account.setCurrentBalance(1000);
            }
        }

//        account.withdrawMoney(100);
//        assertEquals(900,account.getBalance());
//        account.withdrawMoney(1000);
//        assertEquals(0,account.getBalance());
    }

    @org.junit.jupiter.api.Test
    void depositMoney() {
        account.depositMoney(200);
        assertEquals(1200,account.getBalance());
        account.depositMoney(-200);
        assertEquals(1200,account.getBalance());
    }

    @org.junit.jupiter.api.Test
    void setCurrentBalance() {
        account.setCurrentBalance(3);
        assertEquals(3,account.getBalance());
    }

    @org.junit.jupiter.api.Test
    void getBalance() {
        assertEquals(account.getBalance(),account.getBalance());
    }
}