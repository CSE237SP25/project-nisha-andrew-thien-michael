package bankApp;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;

public class AccountTest {

    public static void main(String[] args) {
        // Account tests
        testDeposit();
        testWithdraw();
        testWithdrawTooMuch();
        testNegativeDeposit();
        testNegativeWithdrawal();
        testTransactionHistory();
        testPasswordValidation();
        testMultipleTransactions();
        testInitialBalance();

        // User tests (sign up, login, forgot password)
        System.out.println("\nRunning User tests...");
        testCreateAccountSuccess();
        testCreateAccountDuplicate();
        testLoginSuccess();
        testLoginFailureWrongPassword();
        testLoginFailureNonExistentUser();
        testForgotPasswordSuccess();
        testForgotPasswordMismatch();
        testForgotPasswordUserNotFound();
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

    // Helper: Reset the static accounts map in User.
    private static void resetUserAccounts() {
        try {
            Field accountsField = User.class.getDeclaredField("accounts");
            accountsField.setAccessible(true);
            HashMap<String, Account> accounts = (HashMap<String, Account>) accountsField.get(null);
            accounts.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Helper: Retrieve the accounts map from User.
    private static HashMap<String, Account> getAccounts() {
        try {
            Field accountsField = User.class.getDeclaredField("accounts");
            accountsField.setAccessible(true);
            return (HashMap<String, Account>) accountsField.get(null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Helper: Run a method from User that expects console input and capture its
    // output.
    // This updated version resets the static scanner to use our test input.
    private static String runMethodWithInput(String inputData, String methodName) {
        PrintStream originalOut = System.out;
        java.io.InputStream originalIn = System.in;
        try {
            ByteArrayInputStream testIn = new ByteArrayInputStream(inputData.getBytes());
            System.setIn(testIn);
            ByteArrayOutputStream testOut = new ByteArrayOutputStream();
            System.setOut(new PrintStream(testOut));

            // Update the static Scanner in User so it uses the test input.
            Field scannerField = User.class.getDeclaredField("scanner");
            scannerField.setAccessible(true);
            scannerField.set(null, new java.util.Scanner(testIn));

            Method method = User.class.getDeclaredMethod(methodName);
            method.setAccessible(true);
            method.invoke(null);

            return testOut.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        } finally {
            System.setIn(originalIn);
            System.setOut(originalOut);
        }
    }

    // Test: Successful account creation.
    public static void testCreateAccountSuccess() {
        resetUserAccounts();
        String input = "user1\npassword1\n";
        String output = runMethodWithInput(input, "createAccount");

        HashMap<String, Account> accounts = getAccounts();
        if (accounts.containsKey("user1") &&
                accounts.get("user1").getPassword().equals("password1") &&
                output.contains("Account created successfully!")) {
            System.out.println("testCreateAccountSuccess PASSED");
        } else {
            System.out.println("testCreateAccountSuccess FAILED");
        }
    }

    // Test: Duplicate account creation.
    public static void testCreateAccountDuplicate() {
        resetUserAccounts();
        String input1 = "user1\npassword1\n";
        runMethodWithInput(input1, "createAccount");

        String input2 = "user1\npassword2\n";
        String output = runMethodWithInput(input2, "createAccount");

        if (output.contains("Username already exists.")) {
            System.out.println("testCreateAccountDuplicate PASSED");
        } else {
            System.out.println("testCreateAccountDuplicate FAILED");
        }
    }

    // Test: Successful login.
    public static void testLoginSuccess() {
        resetUserAccounts();
        String signUpInput = "user1\npassword1\n";
        runMethodWithInput(signUpInput, "createAccount");

        // For login: choose option 1, provide username and password,
        // then in accountMenu choose logout (option 5).
        String loginInput = "1\nuser1\npassword1\n5\n";
        String output = runMethodWithInput(loginInput, "login");

        if (output.contains("Login successful!")) {
            System.out.println("testLoginSuccess PASSED");
        } else {
            System.out.println("testLoginSuccess FAILED");
        }
    }

    // Test: Login failure with incorrect password.
    public static void testLoginFailureWrongPassword() {
        resetUserAccounts();
        String signUpInput = "user1\npassword1\n";
        runMethodWithInput(signUpInput, "createAccount");

        String loginInput = "1\nuser1\nwrongpass\n3\n";
        String output = runMethodWithInput(loginInput, "login");

        if (output.contains("Invalid login credentials")) {
            System.out.println("testLoginFailureWrongPassword PASSED");
        } else {
            System.out.println("testLoginFailureWrongPassword FAILED");
        }
    }

    // Test: Login failure with a non-existent user.
    public static void testLoginFailureNonExistentUser() {
        resetUserAccounts();
        String loginInput = "1\nnonexistent\nanypassword\n3\n";
        String output = runMethodWithInput(loginInput, "login");

        if (output.contains("Invalid login credentials")) {
            System.out.println("testLoginFailureNonExistentUser PASSED");
        } else {
            System.out.println("testLoginFailureNonExistentUser FAILED");
        }
    }

    // Test: Successful forgot password.
    public static void testForgotPasswordSuccess() {
        resetUserAccounts();
        String signUpInput = "user1\npassword1\n";
        runMethodWithInput(signUpInput, "createAccount");

        String forgotInput = "2\nuser1\nnewpass\nnewpass\n3\n";
        String output = runMethodWithInput(forgotInput, "login");

        HashMap<String, Account> accounts = getAccounts();
        Account acc = accounts.get("user1");
        if (output.contains("Password updated successfully!") &&
                acc.getPassword().equals("newpass")) {
            System.out.println("testForgotPasswordSuccess PASSED");
        } else {
            System.out.println("testForgotPasswordSuccess FAILED");
        }
    }

    // Test: Forgot password failure due to mismatched passwords.
    public static void testForgotPasswordMismatch() {
        resetUserAccounts();
        String signUpInput = "user1\npassword1\n";
        runMethodWithInput(signUpInput, "createAccount");

        String forgotInput = "2\nuser1\nnewpass\nmismatch\n3\n";
        String output = runMethodWithInput(forgotInput, "login");

        HashMap<String, Account> accounts = getAccounts();
        Account acc = accounts.get("user1");
        if (output.contains("Passwords do not match.") &&
                acc.getPassword().equals("password1")) {
            System.out.println("testForgotPasswordMismatch PASSED");
        } else {
            System.out.println("testForgotPasswordMismatch FAILED");
        }
    }

    // Test: Forgot password failure when user does not exist.
    public static void testForgotPasswordUserNotFound() {
        resetUserAccounts();
        String forgotInput = "2\nnonexistent\n3\n";
        String output = runMethodWithInput(forgotInput, "login");

        if (output.contains("Username does not exist.")) {
            System.out.println("testForgotPasswordUserNotFound PASSED");
        } else {
            System.out.println("testForgotPasswordUserNotFound FAILED");
        }
    }
}
