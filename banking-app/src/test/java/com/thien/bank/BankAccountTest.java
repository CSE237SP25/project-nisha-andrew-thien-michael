import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BankAccountTest {

    @Test
    void testAccountTypeAndNumber() {
        BankAccount acc = new BankAccount("Checking");
        assertEquals("Checking", acc.getAccountType());
        assertTrue(acc.getAccountNumber().startsWith("CHK-"));
    }

    @Test
    void testInvalidAccountType() {
        assertThrows(IllegalArgumentException.class, () -> {
            new BankAccount("Business");
        });
    }

    @Test
    void testDepositIncreasesBalance() {
        BankAccount acc = new BankAccount("Savings");
        acc.deposit(200);
        assertEquals(200, acc.getBalance(), 0.001);
    }

    @Test
    void testWithdrawDecreasesBalance() {
        BankAccount acc = new BankAccount("Checking");
        acc.deposit(300);
        acc.withdraw(100);
        assertEquals(200, acc.getBalance(), 0.001);
    }

    @Test
    void testWithdrawOverBalanceFails() {
        BankAccount acc = new BankAccount("Savings");
        acc.deposit(100);
        assertThrows(IllegalArgumentException.class, () -> acc.withdraw(200));
    }

    @Test
    void testMonthlyLimitPreventsOverspending() {
        BankAccount acc = new BankAccount("Checking");
        acc.deposit(500);
        acc.setMonthlyLimit(100);
        acc.withdraw(80);
        assertThrows(IllegalArgumentException.class, () -> acc.withdraw(30));
    }

    @Test
    void testResetMonthlySpent() {
        BankAccount acc = new BankAccount("Checking");
        acc.deposit(500);
        acc.setMonthlyLimit(100);
        acc.withdraw(50);
        acc.resetMonthlySpent();
        assertDoesNotThrow(() -> acc.withdraw(80));
    }
}
