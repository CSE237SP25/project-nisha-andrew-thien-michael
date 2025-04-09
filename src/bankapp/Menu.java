package bankapp;

import java.util.List;
import java.util.Scanner;

public class Menu {
    private static final Scanner scanner = new Scanner(System.in);
    
    public static void main(String[] args) {
        System.out.println("=== Welcome ===");
        System.out.print("Enter account type (Checking or Savings): ");
        String accountType = scanner.nextLine().trim();
        
        BankAccount account;
        try {
            account = new BankAccount(accountType);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
            return;
        }
        
        System.out.println("Account created!");
        System.out.println("Account Type: " + account.getAccountType());
        System.out.println("Account Number: " + account.getAccountNumber());

        showMainMenu(account);
    }

    public static void showMainMenu(BankAccount account) {
        boolean running = true;

        while (running) {
            promptUser();
            int option = getUserOption();
            running = processUserInput(option, account);
        }
    }

    private static void promptUser() {
        System.out.println("\n=== Bank Menu ===");
        System.out.println("1. View Current Balance");
        System.out.println("2. Deposit");
        System.out.println("3. Withdraw");
        System.out.println("4. View Balance History");
        System.out.println("5. Set Monthly Spending Limit");
        System.out.println("6. Exit");
        System.out.print("Choose an option: ");
    }

    private static int getUserOption() {
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input. Please enter a number between 1 and 5.");
            scanner.next();
        }
        int option = scanner.nextInt();
        scanner.nextLine();
        return option;
    }

    private static boolean processUserInput(int option, BankAccount account) {
        switch (option) {
            case 1:
                printUserBalance(account);
                break;
            case 2:
                userDeposit(account);
                break;
            case 3:
                userWithdraw(account);
                break;
            case 4:
                getUserHistory(account);
                break;
            case 5:
                setMonthlyLimit(account);
                break;
            case 6:
                System.out.println("Goodbye!");
                return false;
            default:
                System.out.println("Invalid option. Try again.");
        }
        return true;
    }

    private static void printUserBalance(BankAccount account) {
        System.out.println("Current Balance: $" + account.getCurrentBalance());
    }

    private static void userDeposit(BankAccount account) {
        System.out.print("Amount to deposit: ");
        if (!scanner.hasNextDouble()) {
            System.out.println("Invalid amount. Please enter a valid number.");
            scanner.next();
            return;
        }
        double depositAmount = scanner.nextDouble();
        scanner.nextLine();
        try {
            account.deposit(depositAmount);
            System.out.println("Deposit successful.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void userWithdraw(BankAccount account) {
        System.out.print("Amount to withdraw: ");
        if (!scanner.hasNextDouble()) {
            System.out.println("Invalid amount. Please enter a valid number.");
            scanner.next();
            return;
        }
        double withdrawAmount = scanner.nextDouble();
        scanner.nextLine();
        try {
            account.withdraw(withdrawAmount);
            System.out.println("Withdrawal successful.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void getUserHistory(BankAccount account) {
        List<Double> history = account.getBalanceHistory();
        System.out.println("Balance History:");
        for (int i = 0; i < history.size(); i++) {
            System.out.println("Step " + i + ": $" + history.get(i));
        }
    }
    
    private static void setMonthlyLimit(BankAccount account) {
        System.out.print("Enter monthly spending limit: ");
        if (!scanner.hasNextDouble()) {
            System.out.println("Invalid input. Please enter a valid number.");
            scanner.next();
            return;
        }

        double limit = scanner.nextDouble();
        scanner.nextLine();

        try {
            account.setMonthlyLimit(limit);
            System.out.println("Monthly spending limit set to: $" + limit);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
