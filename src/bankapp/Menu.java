package bankapp;

import java.util.HashMap;
import java.util.InputMismatchException;

import java.util.List;
import java.util.Scanner;

public class Menu {
     private static final Scanner scanner = new Scanner(System.in);

    public static void showMainMenu(BankAccount account) {
        boolean running = true;

        while (running) {
            System.out.println("\n=== Bank Menu ===");
            System.out.println("1. View Current Balance");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. View Balance History");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");

            int option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1:
                    System.out.println("Current Balance: $" + account.getCurrentBalance());
                    break;
                case 2:
                    System.out.print("Amount to deposit: ");
                    double depositAmount = scanner.nextDouble();
                    scanner.nextLine();
                    try {
                        account.deposit(depositAmount);
                        System.out.println("Deposit successful.");
                    } catch (IllegalArgumentException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;
                case 3:
                    System.out.print("Amount to withdraw: ");
                    double withdrawAmount = scanner.nextDouble();
                    scanner.nextLine();
                    try {
                        account.withdraw(withdrawAmount);
                        System.out.println("Withdrawal successful.");
                    } catch (IllegalArgumentException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;
                case 4:
                    List<Double> history = account.getBalanceHistory();
                    System.out.println("Balance History:");
                    for (int i = 0; i < history.size(); i++) {
                        System.out.println("Step " + i + ": $" + history.get(i));
                    }
                    break;
                case 5:
                    System.out.println("Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }
}
