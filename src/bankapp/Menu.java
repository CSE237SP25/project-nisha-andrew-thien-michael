package bankapp;

import java.util.List;
import java.util.Scanner;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;

public class Menu {
    private final Scanner scanner;
    private final HashMap<String, BankAccount> accounts;
    private BankAccount loggedInAccount;
    
    public Menu() {
    	this.scanner = new Scanner(System.in);
    	this.accounts = new HashMap<>();
    	this.loggedInAccount = null;
    }
    
    public static void main(String[] args) {
        System.out.println("Welcome to the Banking App!");
        Menu bankMenu = new Menu();
        bankMenu.runApplication();
        System.out.println("Thank you for using the app! Exiting.");
        bankMenu.closeScanner();
    }
    
    public void closeScanner() {
    	this.scanner.close();
    }
    
    private void runApplication() {
    	boolean appRunning = true;
    	while(appRunning) {
    		if(this.loggedInAccount == null) {
    			showAuthMenu();
    			int authChoice = getUserAuthOption();
    			appRunning = handleAuthChoice(authChoice);
        			}
    		else {
    			showMainMenu();
    			this.loggedInAccount = null;
    			System.out.println("Logging out...");
        		}
        	}
        }
    
    private void showAuthMenu() {
    	System.out.println("\n--- Authentication Menu ---");
    	System.out.println("1. Create Account");
    	System.out.println("2. Login");
    	System.out.println("3. Exit");
    	System.out.print("Choose an option: ");
    }
    
    private int getUserAuthOption() {
        System.out.print("Choose an option: ");
        int choice = -1;
        try {
            choice = this.scanner.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a whole number.");
        } catch (NoSuchElementException e) {
            System.out.println("Input stream closed unexpectedly.");
            choice = 3; 
        } finally {
            if (this.scanner.hasNextLine()) {
                this.scanner.nextLine();
            }
        }
        return choice;
    }

    private boolean handleAuthChoice(int choice) {
        switch (choice) {
            case 1:
                createAccount();
            case 2:
                login();
                return true;
            case 3:
                return false;
            default:
                if (choice != -1) {
                    System.out.println("Invalid authentication option.");
                }
                return true; 
        }
    }
    
    private void createAccount() {
    	System.out.print("Enter username: ");
    	String username = this.scanner.nextLine().trim();
    	if(username.isEmpty() || this.accounts.containsKey(username)) {
    		System.out.println(username.isEmpty() ? "Username cannot be empty." : "Username already exists.");
    		return;
    	}
    	System.out.print("Enter password: ");
    	String password = this.scanner.nextLine();
    	if(password.isEmpty()){
    		System.out.println("Password cannot be empty.");
    		return;
    	}
    	BankAccount newAccount = new BankAccount();
    	newAccount.setUsername(username);
    	newAccount.setPassword(password);
    	this.accounts.put(username, newAccount);
    	System.out.println("Account created successfully!");
    }
    
    private void login() {
    	boolean loginMenuActive = true;
    	while(loginMenuActive) {
    		System.out.println("\n--- Login / Recovery ---");
    		System.out.println("1. Login");
    		System.out.println("2. Forgot Password");
    		System.out.println("3. Back to Main Menu");
    		System.out.print("Choose an option: ");
    		
    		int option = -1;
    		try {
    			option = this.scanner.nextInt();
    		}
    		catch (InputMismatchException e){
    			System.out.println("Invalid input. Please enter a number.");
    		}
    		catch(NoSuchElementException e) {
    			System.out.println("Input stream closed unexpectedly.");
    			option = 3;
    		}
    		finally {
    			if(this.scanner.hasNextLine()) {
    				this.scanner.nextLine();
    			}
    		}
    		
    		switch(option) {
    		case 1:
    			if(performLogin()) {
    				loginMenuActive = false;
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
    				System.out.println("Invalid option.");
    			}
    		}
    		
    	}
    }
    
    private boolean performLogin() {
    	System.out.print("Username: ");
    	String username = this.scanner.nextLine().trim();
    	System.out.print("Password: ");
    	String password = this.scanner.nextLine();
    	
    	BankAccount acc = this.accounts.get(username);
    	if(acc != null && acc.validatePassword(password)) {
    		this.loggedInAccount = acc;
    		System.out.println("\nLogin successful! Welcome " + acc.getUsername());
    		return true;
    	}
    	else {
    		System.out.println("Invalid login credentials.");
    		return false;
    	}
    }
    
    private void forgotPassword() {
    	System.out.print("Enter username: ");
    	String username = this.scanner.nextLine();
    	BankAccount acc = this.accounts.get(username);
    	
    	if(acc == null) {
    		System.out.println("Username does not exist.");
    		return;
    	}
    	
    	System.out.print("Enter new Password: ");
    	String newPassword = this.scanner.nextLine();
    	if (newPassword.isEmpty()) {
    		System.out.println("Password cannot be empty.");
    		return;
    	}
    	
    	System.out.print("Confirm new password: ");
    	String confirmPassword = this.scanner.nextLine();
    	if(!newPassword.equals(confirmPassword)) {
    		System.out.println("Passwords do not match.");
    		return;
    	}
    	
    	acc.setPassword(newPassword);
    	System.out.println("Password updated successfully!");
    }
    

    public void showMainMenu() {
        boolean running = true;

        while (running) {
            promptUser();
            int option = getUserMenuOption();
            running = processMenuChoice(option);
        }
    }

    private void promptUser() {
    	String accountIdentifier = this.loggedInAccount.getAccountName().isEmpty() ? this.loggedInAccount.getUsername() : this.loggedInAccount.getAccountName();
        System.out.println("\n=== Bank Menu (Account: " + accountIdentifier + ") ===");
        System.out.println("1. View Current Balance");
        System.out.println("2. Deposit");
        System.out.println("3. Withdraw");
        System.out.println("4. View Balance History");
        System.out.println("5. Exit");
		System.out.println("6. Set Account Name");
        System.out.println("7. View Transaction History");
        System.out.println("8. View Transaction Count");
        System.out.println("9. Deposit Multiple Periods");
        System.out.println("10. Withdraw Multiple Periods");
        System.out.print("Choose an option: ");
    }

    private int getUserMenuOption() {
    	int choice = -1;
    	while(choice < 1 || choice > 8) {
    		System.out.print("Choose an option (1-8): ");
    		try {
    			choice = this.scanner.nextInt();
    			if(choice < 1 || choice > 8) {
    				System.out.println("Invalid choice. Please enter a number between 1 and 8.");
    				choice = -1;
    			}
    		}
    		catch (InputMismatchException e) {
    			System.out.println("Invalid input. Please enter a nubmer.");
    			choice = -1;
    		}
    		catch (NoSuchElementException e) {
    			System.out.println("Input stream closed unexpectedly.");
    			choice = 5;
    		}
    		finally {
    			if(this.scanner.hasNextLine()) {
    				this.scanner.nextLine();
    			}
    		}
    	}
    	return choice;
    }

    private boolean processMenuChoice(int option) {
        switch (option) {
            case 1:
                printUserBalance();
                break;
            case 2:
                userDeposit();
                break;
            case 3:
                userWithdraw();
                break;
            case 4:
                getUserHistory();
                break;
            case 5:
                return false;
			      case 6:
                setAccountName();
                break;
            case 7:
                showTransactionHistory();
                break;
            case 8:
            	showTransactionCount();
            	break;
            case 9:
                depositMultiplePeriods();
                break;
            case 10:
                withdrawMultiplePeriods();
                break;
        }
        return true;
    }

    private void printUserBalance() {
        System.out.println("Current Balance: $" + this.loggedInAccount.getCurrentBalance());
    }

    private void userDeposit() {
        System.out.print("Amount to deposit: ");
        if (!this.scanner.hasNextDouble()) {
            System.out.println("Invalid amount. Please enter a valid number.");
            this.scanner.next();
            return;
        }
        double depositAmount = this.scanner.nextDouble();
        this.scanner.nextLine();
        try {
            this.loggedInAccount.deposit(depositAmount);
            System.out.println("Deposit successful.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void userWithdraw() {
        System.out.print("Amount to withdraw: ");
        if (!this.scanner.hasNextDouble()) {
            System.out.println("Invalid amount. Please enter a valid number.");
            this.scanner.next();
            return;
        }
        double withdrawAmount = this.scanner.nextDouble();
        this.scanner.nextLine();
        try {
            this.loggedInAccount.withdraw(withdrawAmount);
            System.out.println("Withdrawal successful.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void getUserHistory() {
        List<Double> history = this.loggedInAccount.getBalanceHistory();
        System.out.println("Balance History:");
        for (int i = 0; i < history.size(); i++) {
            System.out.println("Step " + i + ": $" + history.get(i));
        }
    }

    private void setAccountName() {
      System.out.print("Enter a name for your account: ");
      String name = this.scanner.nextLine().trim();
      if (name.isEmpty()) {
        System.out.println("Account name cannot be empty.");
      } else {
        this.loggedInAccount.setAccountName(name);
        System.out.println("Account name set to: " + this.loggedInAccount.getAccountName());
      }
    }

    
    private void showTransactionHistory() {
        List<Transaction> transactions = this.loggedInAccount.getTransactions();
        if (transactions.isEmpty()) {
            System.out.println("No transactions yet.");
        } else {
            System.out.println("Transaction History:");
            for (Transaction t : transactions) {
                System.out.println(t);
            }
        }
    }
    
    private void showTransactionCount() {
    	int count = this.loggedInAccount.getTransactions().size();
    	System.out.println("\nTotal transactions performed: " + count);
    }

    private void depositMultiplePeriods() {
        System.out.print("Enter amount to deposit per period: ");
        if (!this.scanner.hasNextDouble()) {
            System.out.println("Invalid amount. Please enter a valid number.");
            this.scanner.next();
            return;
        }
        double amount = this.scanner.nextDouble();
        
        System.out.print("Enter number of periods: ");
        if (!this.scanner.hasNextInt()) {
            System.out.println("Invalid number of periods. Please enter a valid integer.");
            this.scanner.next();
            return;
        }
        int periods = this.scanner.nextInt();
        this.scanner.nextLine();
        
        try {
            this.loggedInAccount.depositMultiplePeriods(amount, periods);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void withdrawMultiplePeriods() {
        System.out.print("Enter amount to withdraw per period: ");
        if (!this.scanner.hasNextDouble()) {
            System.out.println("Invalid amount. Please enter a valid number.");
            this.scanner.next();
            return;
        }
        double amount = this.scanner.nextDouble();
        
        System.out.print("Enter number of periods: ");
        if (!this.scanner.hasNextInt()) {
            System.out.println("Invalid number of periods. Please enter a valid integer.");
            this.scanner.next();
            return;
        }
        int periods = this.scanner.nextInt();
        this.scanner.nextLine();
        
        try {
            this.loggedInAccount.withdrawMultiplePeriods(amount, periods);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
