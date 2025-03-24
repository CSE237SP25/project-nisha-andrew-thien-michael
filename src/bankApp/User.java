import java.util.Scanner;
import java.util.HashMap;

public class User {
    // Store users: username -> Account object
    private static HashMap<String, Account> accounts = new HashMap<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Welcome to the Banking App!");
        boolean running = true;

        while (running) {
            System.out.println("\n1. Create Account");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt(); scanner.nextLine();

            switch (choice) {
                case 1: createAccount(); break;
                case 2: login(); break;
                case 3: running = false; break;
                default: System.out.println("Invalid option.");
            }
        }
        System.out.println("Thank you for using the app!");
    }

    private static void createAccount() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        if (accounts.containsKey(username)) {
            System.out.println("Username already exists.");
            return;
        }

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        Account newAccount = new Account();
        newAccount.setUserName(username);
        newAccount.setPassword(password);
        accounts.put(username, newAccount);

        System.out.println("Account created successfully!");
    }

    private static void login() {
        System.out.print("Username: ");
        String username = scanner.nextLine();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        Account acc = accounts.get(username);
        if (acc != null && acc.validatePassword(password)) {
            System.out.println("Login successful!");
            accountMenu(acc);
        } else {
            System.out.println("Invalid login credentials.");
        }
    }

    private static void accountMenu(Account acc) {
        boolean loggedIn = true;
        while (loggedIn) {
            System.out.println("\nWelcome, " + acc.getUserName());
            System.out.println("1. Deposit");
            System.out.println("2. Withdraw");
            System.out.println("3. View Balance");
            System.out.println("4. View Transactions");
            System.out.println("5. Logout");
            System.out.print("Choose an option: ");
            int option = scanner.nextInt(); scanner.nextLine();

            switch (option) {
                case 1:
                    System.out.print("Amount to deposit: ");
                    double dep = scanner.nextDouble(); scanner.nextLine();
                    acc.deposit(dep);
                    System.out.println("Deposit successful!");
                    break;

                case 2:
                    System.out.print("Amount to withdraw: ");
                    double wd = scanner.nextDouble(); scanner.nextLine();
                    try {
                        acc.withdraw(wd);
                        System.out.println("Withdrawal successful!");
                    } catch (IllegalArgumentException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;

                case 3:
                    System.out.println("Current balance: $" + acc.getBalance());
                    break;

                case 4:
                    System.out.println("Transaction History:");
                    for (String tx : acc.getTransactionHistory()) {
                        System.out.println("- " + tx);
                    }
                    break;

                case 5:
                    System.out.println("Logging out...");
                    loggedIn = false;
                    break;

                default:
                    System.out.println("Invalid option.");
            }
        }
    }
}
