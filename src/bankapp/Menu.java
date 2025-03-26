package bankapp;

import java.util.List;
import java.util.Scanner;

public class Menu {
    private static final Scanner scanner = new Scanner(System.in);
    
    public static void main(String[] args) {
        BankAccount account = new BankAccount();
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
        System.out.println("5. Exit");
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
}
