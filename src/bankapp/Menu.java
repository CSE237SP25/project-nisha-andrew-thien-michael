package bankapp;

import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Menu {
	private static final HashMap<String, BankAccount> accounts = new HashMap<>();
	private static final Scanner scanner = new Scanner(System.in);

	public static void main(String[] args) {
		System.out.println("Welcome to the Banking App!");
		runMainMenu();
		System.out.println("Thank you for using the app!");
		scanner.close();
	}

	private static void runMainMenu() {
		boolean running = true;

		while (running) {
			printMainMenuOptions();
			int choice = getUserIntInput();

			switch (choice) {
				case 1 -> createAccount();
				case 2 -> login();
				case 3 -> running = false;
				default -> {
					if (choice != -1) System.out.println("Invalid option.");
				}
			}
		}
	}

	private static void printMainMenuOptions() {
		System.out.println("\n1. Create Account");
		System.out.println("2. Login");
		System.out.println("3. Exit");
		System.out.print("Choose an option: ");
	}

	private static int getUserIntInput() {
		int choice = -1;
		try {
			choice = scanner.nextInt();
		} catch (InputMismatchException e) {
			System.out.println("Invalid input. Please enter a number.");
		} finally {
			scanner.nextLine();
		}
		return choice;
	}

	private static void createAccount() {
		String username = promptForInput("Enter username: ").trim();
		if (username.isEmpty()) {
			System.out.println("Username cannot be empty.");
			return;
		}

		if (accounts.containsKey(username)) {
			System.out.println("Username already exists.");
			return;
		}

		String password = promptForInput("Enter password: ");
		if (password.isEmpty()) {
			System.out.println("Password cannot be empty.");
			return;
		}

		BankAccount newAccount = new BankAccount();
		newAccount.setUsername(username);
		newAccount.setPassword(password);
		accounts.put(username, newAccount);

		System.out.println("Account created successfully!");
	}

	private static void login() {
		boolean loginMenuActive = true;

		while (loginMenuActive) {
			printLoginMenuOptions();
			int option = getUserIntInput();

			switch (option) {
				case 1 -> handleLogin();
				case 2 -> forgotPassword();
				case 3 -> {
					System.out.println("Returning to main menu...");
					loginMenuActive = false;
				}
				default -> {
					if (option != -1) System.out.println("Invalid option. Please try again.");
				}
			}
		}
	}

	private static void printLoginMenuOptions() {
		System.out.println("\n1. Login");
		System.out.println("2. Forgot Password");
		System.out.println("3. Back to Main Menu");
		System.out.print("Choose an option: ");
	}

	private static void handleLogin() {
		String username = promptForInput("Username: ");
		String password = promptForInput("Password: ");

		BankAccount acc = accounts.get(username);
		if (acc != null && acc.validatePassword(password)) {
			System.out.println("Login successful!");
			accountMenu(acc);
		} else {
			System.out.println("Invalid login credentials. Please try again.");
		}
	}

	private static void forgotPassword() {
		String username = promptForInput("Enter username: ");
		if (!accounts.containsKey(username)) {
			System.out.println("Username does not exist.");
			return;
		}

		String newPassword = promptForInput("Enter new password: ");
		if (newPassword.isEmpty()) {
			System.out.println("Password cannot be empty.");
			return;
		}

		String confirmPassword = promptForInput("Confirm new password: ");
		if (!newPassword.equals(confirmPassword)) {
			System.out.println("Passwords do not match.");
			return;
		}

		accounts.get(username).setPassword(newPassword);
		System.out.println("Password updated successfully!");
	}

	private static void accountMenu(BankAccount acc) {
		boolean loggedIn = true;

		while (loggedIn) {
			System.out.println("\nWelcome, " + acc.getUsername());
			System.out.println("(Account menu - press Enter to logout)");
			scanner.nextLine(); // simulate menu interaction
			loggedIn = false;
		}
		System.out.println("Logged out.");
	}

	private static String promptForInput(String prompt) {
		System.out.print(prompt);
		return scanner.nextLine();
	}
}
