package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import bankapp.Transaction;

public class TransactionTest {

    @Test
    public void testTransactionCreation() {
        Transaction t = new Transaction("deposit", 100.0);
        String output = t.toString();

        assertTrue(output.contains("deposit"));
        assertTrue(output.contains("100.0"));
        assertTrue(output.contains("at")); // Rough check for timestamp
    }

    @Test
    public void testWithdrawTransactionToString() {
        Transaction t = new Transaction("withdraw", 45.5);
        String output = t.toString();

        assertTrue(output.contains("withdraw"));
        assertTrue(output.contains("45.5"));
    }
}
