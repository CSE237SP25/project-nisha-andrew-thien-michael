package bankapp;

import java.util.List;
import java.util.Scanner;
import java.util.HashMap;
import java.util.InputMismatchException;

public class Menu {
    private static Scanner scanner = new Scanner(System.in);
    private static HashMap<String, BankAccount> accounts = new HashMap<>();
    private static BankAccount loggedInAccount = null;
    
    public static void main(String[] args) {
        System.out.println("Welcome to the Banking App!");
        runApplication();
        System.out.println("Thank you for using the app! Exiting.");
        scanner.close();
    }
    
    private static void runApplication() {
    	boolean appRunning = true;
    	while(appRunning) {
    		if(loggedInAccount == null) {
    			showAuthMenu();
    			int authChoice = getUserAuthOption();
    			switch(authChoice) {
        		case 1:
        			createAccount();
        			break;
        		case 2:
        			login();
        			break;
        		case 3:
        			appRunning = false;
        			break;
        		default:
        			if (authChoice != -1) {
        				System.out.println("Invalid authentification option.");
        			}
        		}
        	}
        	else {
        		showMainMenu(loggedInAccount);
        	}
        }
    }
    
    private static void showAuthMenu() {
    	System.out.println("\n--- Authentication Menu ---");
    	System.out.println("1. Create Account");
    	System.out.println("2. Login");
    	System.out.println("3. Exit");
    	System.out.print("Choose an option: ");
    }
    
    private static int getUserAuthOption() {
    	int choice = -1;
    	try {
    		choice = scanner.nextInt();
    	}
    	catch (InputMismatchException e){
    		System.out.println("Invalid input. Please enter a valid choice.");
    	}
    	finally {
    		scanner.nextLine();
    	}
    	return choice;
    }
    
    private static void createAccount() {
    	System.out.print("Enter username: ");
    	String username = scanner.nextLine().trim();
    	if(username.isEmpty()) {
    		System.out.println("Username cannot be empty.");
    		return;
    	}
    	if(accounts.containsKey(username)) {
    		System.out.println("Username already exists.");
    	}
    	System.out.print("Enter password: ");
    	String password = scanner.nextLine();
    	if(password.isEmpty()){
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
    	while(loginMenuActive) {
    		System.out.println("\n--- Login / Recovery ---");
    		System.out.println("1. Login");
    		System.out.println("2. Forgot Password");
    		System.out.println("3. Back to Main Menu");
    		System.out.print("Choose an option: ");
    		
    		int option = -1;
    		try {
    			option = scanner.nextInt();
    		}
    		catch (InputMismatchException e){
    			System.out.println("Invalid input. Please enter a number.");
    		}
    		finally {
    			scanner.nextLine();
    		}
    		
    		switch(option) {
    		case 1:
    			System.out.print("Username: ");
    			String username = scanner.nextLine();
    			System.out.print("Password: ");
    			String password = scanner.nextLine();
    			BankAccount acc = accounts.get(username);
    			if(acc != null && acc.validatePassword(password)) {
    				System.out.println("\nLogin successful! Welcome " + acc.getUsername() + 
    				(acc.getAccountName().isEmpty() ? "" : " to your '" + acc.getAccountName() + "' account") + ".");
    				loggedInAccount = acc;
    				loginMenuActive = false;
    			}
    			else {
    				System.out.println("Invalid login credentials. Please try again.");
    			}
    			break;
    		case 2:
    			forgotPassword();
    			break;
    		case 3:
    			loginMenuActive = false;
    			break;
    		default:
    			if(option != -1) {
    				System.out.println("Invalid option within Login/Recovery menu.");
    			}
    		}
    		
    	}
    }
    
    private static void forgotPassword() {
    	System.out.print("Enter username: ");
    	String username = scanner.nextLine();
    	BankAccount acc = accounts.get(username);
    	
    	if(acc == null) {
    		System.out.println("Username does not exist.");
    		return;
    	}
    	
    	System.out.print("Enter new Password: ");
    	String newPassword = scanner.nextLine();
    	if (newPassword.isEmpty()) {
    		System.out.println("Password cannot be empty.");
    		return;
    	}
    	
    	System.out.print("Confirm new password: ");
    	String confirmPassword = scanner.nextLine();
    	if(!newPassword.equals(confirmPassword)) {
    		System.out.println("Passwords do not match.");
    		return;
    	}
    	
    	acc.setPassword(newPassword);
    	System.out.println("Password updated successfully!");
    }
    

    public static void showMainMenu(BankAccount account) {
        boolean running = true;

        while (running) {
            promptUser(account); // Pass parameter for displaying needs
            int option = getUserOption();
            running = processUserInput(option, account);
        }
    }

    private static void promptUser(BankAccount account) {
        System.out.println("\n=== Bank Menu ===");
        System.out.println("1. View Current Balance");
        System.out.println("2. Deposit");
        System.out.println("3. Withdraw");
        System.out.println("4. View Balance History");
        System.out.println("5. Exit");
		System.out.println("6. Set Account Name");
        System.out.print("Choose an option: ");
    }

    private static int getUserOption() {
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input. Please enter a number between 1 and 6.");
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
			case 6:
				setAccountName(account);
				break;
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

	private static void setAccountName(BankAccount account) {
		System.out.print("Enter a name for your account: ");
		String name = scanner.nextLine().trim();
		if (name.isEmpty()) {
			System.out.println("Account name cannot be empty.");
		} else {
			account.setAccountName(name);
			System.out.println("Account name set to: " + account.getAccountName());
		}
	}

}
