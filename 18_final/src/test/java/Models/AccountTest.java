/**
 * TC 11
 * This test class test for the following methods:
 *      withdrawMoney();
 *      depositMoney();
 */

package Models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AccountTest {
    Account account = new Account("GTAbriel");
    int startingBalance = GameSettings.STARTINGBALANCE;

    // Tested by Gabriel & Deniz 13-01-2022.
    @Test
    void withdrawMoney() {;
        for (int i = 0; i <= 1000; i++) {
            account.setBalance(startingBalance);
            account.withdrawMoney(i);
            assertEquals(startingBalance - i, account.getBalance());
        }

        // Can't test for sellAssets since it's an account object and not a player object, and therefore the account
        // doesn't have any assets to sell.
    }

    // Tested by Gabriel & Deniz 13-01-2022.
    @Test
    void testWithdrawMoney() {
        for (int i = 0; i <= 1000; i++) {
            account.setBalance(startingBalance);
            account.withdrawMoney(i);
            assertEquals(startingBalance - i, account.getBalance());
        }

        // Can't test for sellAssets since it's an account object and not a player object, and therefore the account
        // doesn't have any assets to sell.
    }

    // Tested by Gabriel & Deniz 13-01-2022.
    @Test
    void depositMoney() {
        for (int i = 0; i <= 1000; i++) {
            account.setBalance(startingBalance);
            account.depositMoney(i);
            assertEquals(startingBalance + i, account.getBalance());
        }
    }

    // Can't test since account isn't a player object.
    @Test
    void transferMoney() {
    }
}