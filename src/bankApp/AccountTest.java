public class AccountTest {

    public static void main(String[] args) {
        testDeposit();
        testWithdraw();
        testWithdrawTooMuch();
        testNegativeDeposit();
        testNegativeWithdrawal();
        testTransactionHistory();
        testPasswordValidation();
        testMultipleTransactions();
        testInitialBalance();
    }

    public static void testDeposit() {
        Account example = new Account();
        example.deposit(100);
        if (example.getBalance() == 100) {
            System.out.println("testDeposit PASSED");
        } else {
            System.out.println("testDeposit FAILED: expected 100, got " + example.getBalance());
        }
    }

    public static void testWithdraw() {
        Account example = new Account();
        example.deposit(200);
        example.withdraw(50);
        if (example.getBalance() == 150) {
            System.out.println("testWithdraw PASSED");
        } else {
            System.out.println("testWithdraw FAILED: expected 150, got " + example.getBalance());
        }
    }

    public static void testWithdrawTooMuch() {
        Account example = new Account();
        example.deposit(100);
        try {
            example.withdraw(200);
            System.out.println("testWithdrawTooMuch FAILED: no exception thrown");
        } catch (IllegalArgumentException e) {
            System.out.println("testWithdrawTooMuch PASSED");
        }
    }

    public static void testNegativeDeposit() {
        Account example = new Account();
        try {
            example.deposit(-50);
            System.out.println("testNegativeDeposit FAILED: no exception thrown");
        } catch (IllegalArgumentException e) {
            System.out.println("testNegativeDeposit PASSED");
        }
    }

    public static void testNegativeWithdrawal() {
        Account example = new Account();
        example.deposit(100);
        try {
            example.withdraw(-30);
            System.out.println("testNegativeWithdrawal FAILED: no exception thrown");
        } catch (IllegalArgumentException e) {
            System.out.println("testNegativeWithdrawal PASSED");
        }
    }

    public static void testTransactionHistory() {
        Account example = new Account();
        example.deposit(50);
        example.withdraw(20);
        int size = example.getTransactionHistory().size();
        if (size == 2) {
            System.out.println("testTransactionHistory PASSED");
        } else {
            System.out.println("testTransactionHistory FAILED: expected 2 transactions, got " + size);
        }
    }

    public static void testPasswordValidation() {
        Account example = new Account();
        example.setPassword("test");

        boolean correct = example.validatePassword("test");
        boolean incorrect = example.validatePassword("wrong");

        if (correct && !incorrect) {
            System.out.println("testPasswordValidation PASSED");
        } else {
            System.out.println("testPasswordValidation FAILED");
        }
    }

    public static void testMultipleTransactions() {
        Account example = new Account();
        example.deposit(100);
        example.deposit(200);
        example.withdraw(50);
        if (example.getBalance() == 250) {
            System.out.println("testMultipleTransactions PASSED");
        } else {
            System.out.println("testMultipleTransactions FAILED: expected 250, got " + example.getBalance());
        }
    }

    public static void testInitialBalance() {
        Account example = new Account();
        if (example.getBalance() == 0) {
            System.out.println("testInitialBalance PASSED");
        } else {
            System.out.println("testInitialBalance FAILED: expected 0, got " + example.getBalance());
        }
    }
}
