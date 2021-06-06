package com.revature.services;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.Scanner;

import com.revature.accounts.Account;
import com.revature.controller.Controller;
import com.revature.dao.EmployeeDAO;
import com.revature.dao.EmployeeDAOImpl;
import com.revature.dao.TDAO;
import com.revature.dao.TDAOImpl;
import com.revature.exceptions.InvalidActionException;
import com.revature.users.Customer;
import com.revature.users.Employee;


public class EmployeeService {
	
	private static final Logger logger = LogManager.getLogger(Controller.class);
	private tServices tServ = new tServices();
	private AccountService accServ = new AccountService();
	private CustomerService cServ = new CustomerService();
	private TDAO tDao = new TDAOImpl();
	private EmployeeDAO eDao = new EmployeeDAOImpl();
	
	public void service(Employee employee, Scanner sc) {
		System.out.println(
				"Welcome " + employee.getFirstName() + " " + employee.getLastName() + " this is the list of customers:");
		displayCustomers();
		while (true) {

			System.out.println(
					"To make decisions on pending accounts type 'pending', to cancel an account type 'cancel', to carry out transactions type "
							+ "'transactions', to view a customer's information type 'customer', to logout type 'logout'.");
			String response;
			try {
				response = sc.nextLine();
			} catch (NoSuchElementException e) {
				e.printStackTrace();
				throw new NoSuchElementException("Ctrl-z stops the program. Good bye.");
			}
			if (response.equals("logout")) {
				break;
			}
			switch (response) {
			case "pending":
				pendingService(employee, sc);
				break;
			case "cancel":
				cancelService(employee,sc);
				break;
			case "transactions":
				transactionService(employee, sc);
				break;
			case "customer":
				customerDisplayService(employee, sc);
				break;
			default:
				System.out.println("Invalid selection.");
				continue;
			}

		}
		System.out.println("Good bye " + employee.getFirstName() + " " + employee.getLastName() + ".");
	}

	public String approveAccount(Employee employee, int accNumber) {
		Account acc = accServ.getAccount(accNumber);
		if(acc == null) {
			return "Account doesn't exist";
		}
		if (acc.getApproved().equals("Pending")) {
			acc.setApproved("Approved");
			if (accServ.setApproved(accNumber, "Approved")) {
				logger.trace("Account [" + acc.getAccountNumber() + "] has been approved by " + employee.getFirstName() + " "
						+ employee.getLastName() + ".");
				return "Account [" + acc.getAccountNumber() + "] has been approved by " + employee.getFirstName() + " "
				+ employee.getLastName() + ".";
			} else {
				return "There was an error approving this account. Please talk to your manager.";
			}
			
			
			
		} else {
			return "Only pending accounts may be approved.";
		}

	}

	public String cancelAccount(Employee employee, int accNumber) {
		Account acc = accServ.getAccount(accNumber);
		if(acc == null) {
			return "Account doesn't exist";
		}
		if (acc.getApproved().equals("Approved")) {
			
			if ( accServ.setApproved(accNumber, "Cancelled")) {
				tDao.withdraw(accNumber, acc.getBalance());
				acc.setApproved("Cancelled");
				acc.setBalance(0);
				logger.trace("Account [" + acc.getAccountNumber() + "] has been cancelled by " + employee.getFirstName() + " "
						+ employee.getLastName() + " and its funds have been withdrawn.");
				return "Account [" + acc.getAccountNumber() + "] has been cancelled by " + employee.getFirstName() + " "
						+ employee.getLastName() + " and its funds have been withdrawn.";
			} else {
				return "There was an error while cancelling the account.";
			}
			
		} else {
			return "Only approved accounts may be cancelled.";
		}
	}

	public String rejectAccount(Employee employee, int accNumber) {
		
		Account acc = accServ.getAccount(accNumber);
		if(acc == null) {
			return "Account doesn't exist";
		}
		if (acc.getApproved().equals("Pending")) {
			if ( accServ.setApproved(accNumber, "Cancelled")) {
				tDao.withdraw(accNumber, acc.getBalance());
				acc.setApproved("Cancelled");
				acc.setBalance(0);
				logger.trace("Account [" + acc.getAccountNumber() + "] has been rejected by " + employee.getFirstName() + " "
						+ employee.getLastName() + " and it's funds have been withdrawn.");
				return "Account [" + acc.getAccountNumber() + "] has been rejected by " + employee.getFirstName() + " "
						+ employee.getLastName() + " and it's funds have been withdrawn.";
			} else {
				return "There was an error while rejecting the account.";
			}
			
			
		} else {
			return "Only pending accounts may be rejected.";
		}
	}

	public ArrayList<Customer> getCustomerList() {
		return eDao.getCustomerList();
	}
	
	public void displayCustomers() {
		int list = 1;
		for (Customer cus : getCustomerList()) {
			int pending = pendingAccounts(cus);
			System.out.println(list + ". User ID " + cus.getUserID() + " is assigned to " + cus.getFirstName() + " "
					+ cus.getLastName() + " who has " + cus.getNumberOfAccounts() + " accounts, " + pending
					+ " of which are pending.");
			list++;
		}

	}

	public int pendingAccounts(Customer cus) {
		int count = 0;
		for (int i = 0; i < cus.getNumberOfAccounts(); i++) {
			if (accServ.getAccount(cus.getAccount(i)).getApproved().equals("Pending")) {
				count++;
			}
		}
		return count;
	}

	public void pendingService(Employee emp, Scanner sc) {
		while (true) {
			System.out.println("Please enter the account number you wish to decide upon or type 'exit'");
			int accountNumber;
			Account acc;
			String response;
			try {
				response = sc.nextLine();
			} catch (NoSuchElementException e) {
				e.printStackTrace();
				throw new NoSuchElementException("Ctrl-z stops the program. Good bye.");
			}
			if (response.equals("exit")) {
				break;
			}
			try {

				accountNumber = Integer.valueOf(response);
				acc = accServ.getAccount(accountNumber);
				if (acc == null) {
					System.out.println("Account could not be found.");
					continue;
				}
			} catch (NumberFormatException e) {
				System.out.println("Please type an account number");
				continue;
			} catch (NullPointerException e) {
				System.out.println("Pending account could not be found.");
				continue;
			}
			System.out.println(
					"Account found. Type 'approve' to approve the account, 'reject' to reject it and 'exit' to exit this menu.");
			String decision;
			try {
				decision = sc.nextLine();
			} catch (NoSuchElementException e) {
				e.printStackTrace();
				throw new NoSuchElementException("Ctrl-z stops the program. Good bye.");
			}
			if (decision.equals("exit")) {
				break;
			}
			switch (decision) {
			case "approve":
				String approve = approveAccount(emp, acc.getAccountNumber());
				System.out.println(approve);
				logger.trace(approve);
				continue;
			case "reject":
				String reject = rejectAccount(emp, acc.getAccountNumber());
				System.out.println(reject);
				logger.trace(reject);
				continue;
			default:
				System.out.println("Please type a valid choice.");
				continue;
			}
		}
	}

	public void cancelService(Employee emp, Scanner sc) {
		while (true) {
			System.out.println("Please type the account number you wish to cancel or type 'exit' to exit this menu.");
			
			
			String response;
			try {
				response = sc.nextLine();
			} catch (NoSuchElementException e) {
				e.printStackTrace();
				throw new NoSuchElementException("Ctrl-z stops the program. Good bye.");
			}
			int accountNumber;
			Account acc;
			if (response.equals("exit")) {
				break;
			}
			try {

				accountNumber = Integer.valueOf(response);
				acc = accServ.getAccount(accountNumber);
				if (acc == null) {
					System.out.println("Account could not be found.");
					continue;
				}
			} catch (NumberFormatException e) {
				System.out.println("Please type an account number");
				continue;
			} catch (NullPointerException e) {
				System.out.println("Account could not be found.");
				continue;

			}
			System.out.println("Account found. Account balance is " + acc.getBalance()
					+ ". Type 'cancel' to cancel the account, or 'exit' to exit this menu.");
			String decision;
			try {
				decision = sc.nextLine();
			} catch (NoSuchElementException e) {
				e.printStackTrace();
				throw new NoSuchElementException("Ctrl-z stops the program. Good bye.");
			}
			if (decision.equals("exit")) {
				continue;
			} else if (decision.equals("cancel")) {
				String cancel = cancelAccount(emp, acc.getAccountNumber());
				System.out.println(cancel);
				logger.trace(cancel);
				continue;

			} else {

				System.out.println("Please type a valid choice.");
				continue;
			}

		}
	}

	public void withdrawService(Employee emp, Scanner sc) {
		while (true) {
			System.out.println("Please type the amount to withdraw or type 'exit'");
			double amount;
			Account acc;
			String response;
			try {
				response = sc.nextLine();
			} catch (NoSuchElementException e) {
				e.printStackTrace();
				throw new NoSuchElementException("Ctrl-z stops the program. Good bye.");
			}
			if (response.equals("exit")) {
				break;
			}
			try {
				amount = Double.parseDouble(response);
				amount = Math.round(amount * 100) / 100;
			} catch (NumberFormatException e) {
				System.out.println("Invalid entry.");

				continue;
			}
			System.out.println("Type the account number to withdraw from.");
			String answer;
			try {
				answer = sc.nextLine();
			} catch (NoSuchElementException e) {
				e.printStackTrace();
				throw new NoSuchElementException("Ctrl-z stops the program. Good bye.");
			}
			int accountNumber;
			try {

				accountNumber = Integer.valueOf(answer);
				acc = accServ.getAccount(accountNumber);
				if (acc == null) {
					System.out.println("Account could not be found.");
					continue;
				}
				String withdraw = tServ.withdraw(amount, acc.getAccountNumber());
				System.out.println(withdraw);
				logger.trace("Withdrawl attempted. Result: " + withdraw);
			} catch (NumberFormatException e) {
				System.out.println("Please type an account number");
				continue;
			} catch (NullPointerException e) {
				System.out.println("Account could not be found.");
				continue;

			} catch (InvalidActionException e1) {
				System.out.println(e1.getMessage());
			}
			
		}

	}
	public void depositService(Employee emp, Scanner sc) {
		while (true) {
			System.out.println("Please type the amount to deposit or type 'exit'");
			double amount;
			Account acc;
			String response;
			try {
				response = sc.nextLine();
			} catch (NoSuchElementException e) {
				e.printStackTrace();
				throw new NoSuchElementException("Ctrl-z stops the program. Good bye.");
			}
			if (response.equals("exit")) {
				break;
			}
			try {
				amount = Double.parseDouble(response);
				amount = Math.round(amount * 100) / 100;
			} catch (NumberFormatException e) {
				System.out.println("Invalid entry.");

				continue;
			}
			System.out.println("Type the account number to deposit to.");
			String answer;
			try {
				 answer = sc.nextLine();
			} catch (NoSuchElementException e) {
				e.printStackTrace();
				throw new NoSuchElementException("Ctrl-z stops the program. Good bye.");
				
			}
			int accountNumber;
			try {

				accountNumber = Integer.valueOf(answer);
				acc = accServ.getAccount(accountNumber);
				if (acc == null) {
					System.out.println("Account could not be found.");
					continue;
				}
				String deposit = tServ.deposit(amount, acc.getAccountNumber());
				logger.trace("Deposit attempt was made. Result: " + deposit);
				System.out.println(deposit);
			} catch (NumberFormatException e) {
				System.out.println("Please type an account number");
				continue;
			} catch (NullPointerException e) {
				System.out.println("Account could not be found.");
				continue;

			} catch (InvalidActionException e1) {
				System.out.println(e1.getMessage());
			}
			
		}

	}
	public void transferService(Employee emp, Scanner sc) {
		while (true) {
			System.out.println("Please type the amount to transfer or type 'exit'");
			double amount;
			Account receiver;
			Account sender;
			String response;
			try {
				response = sc.nextLine();
			} catch (NoSuchElementException e) {
				e.printStackTrace();
				throw new NoSuchElementException("Ctrl-z stops the program. Good bye.");
			}
			if (response.equals("exit")) {
				break;
			}
			try {
				amount = Double.parseDouble(response);
				amount = Math.round(amount * 100) / 100;
			} catch (NumberFormatException e) {
				System.out.println("Invalid entry.");

				continue;
			}
			System.out.println("Type the account number to transfer to.");
			String answer = sc.nextLine();
			int accountNumber;
			try {

				accountNumber = Integer.valueOf(answer);
				receiver = accServ.getAccount(accountNumber);
				if (receiver == null) {
					System.out.println("Account could not be found.");
					continue;
				}
			} catch (NumberFormatException e) {
				System.out.println("Please type an account number");
				continue;
			} catch (NullPointerException e) {
				System.out.println("Account could not be found.");
				continue;

			}
			System.out.println("Type the account number to transfer from.");
			answer = sc.nextLine();
			
			try {

				accountNumber = Integer.valueOf(answer);
				sender = accServ.getAccount(accountNumber);
				if (sender == null) {
					System.out.println("Account could not be found.");
					continue;
				}
				String transfer = tServ.transfer(amount, sender.getAccountNumber(), receiver.getAccountNumber());
				logger.trace("Transfer attempt was made. Result: " + transfer);
				System.out.println(transfer);
			} catch (NumberFormatException e) {
				System.out.println("Please type an account number");
				continue;
			} catch (NullPointerException e) {
				System.out.println("Account could not be found.");
				continue;

			} catch (InvalidActionException e1) {
				System.out.println(e1.getMessage());
			}
			
			
		}

	}
	public void transactionService(Employee emp, Scanner sc) {
		while (true) {
			System.out.println("Please type 'deposit' to make a deposit, 'withdrawl' to make a withdrawl, 'transfer' to make a transfer, or 'exit' to exit this menu.");
			String response = sc.nextLine();
			if (response.equals("exit")) {
				break;
			}
			switch (response) {
			case "deposit":
				depositService(emp, sc);
				continue;
			case "withdrawl":
				withdrawService(emp, sc);
				continue;
			case "transfer":
				transferService(emp, sc);
				continue;
			default:
				System.out.println("Invalid entry.");
				continue;
			}
		}
	}
	public void customerDisplayService(Employee emp, Scanner sc) {
		while (true) {
			System.out.println("Please type the user ID of the desired customer or type 'exit' to exit.");
			int user;
			String response;
			Customer cus;
			try {
				response = sc.nextLine();
				
			} catch (NoSuchElementException e) {
				e.printStackTrace();
				throw new NoSuchElementException("Ctrl-z stops the program. Good bye.");
			} 
			if (response.equals("exit")) {
				break;
			}
			
			try {
				user = Integer.valueOf(response);
				cus = cServ.getCustomer(user);
				if (cus == null) {
					System.out.println("No customer found with that user name.");
					continue;
				}
			}catch (NumberFormatException e) {
				System.out.println("Please type an account number");
				continue;
			} catch (NullPointerException e) {
				System.out.println("No customer found with that user name.");
				continue;
			}
			
			
			customerInfo(cus);
			continue;
			
		}
	}
	public void customerInfo(Customer cus) {
		System.out.println("User ID" + cus.getUserID() + " is " + cus.getFirstName() + " " + cus.getLastName() + ".");
		System.out.println("Phone number: " + cus.getPhone() + ".");
		System.out.println("Address: " + cus.getAddress() + ".");
		System.out.println("Account information:");
		int j;
		for(int i = 0;i< cus.getNumberOfAccounts(); i++) {
			j = i +1;
			System.out.println(j + ". " + accServ.getAccount(cus.getAccount(i)));
		}
		if (cus.getNumberOfAccounts() == 0) {
			System.out.println("This customer has no accounts registered.");
		}
		
	}
	
	public Employee getEmployee(int userID) {
		return eDao.getEmployee(userID);
	}
	public boolean createEmployee(String user, String password, String fName, String lName) {
		return eDao.createEmployee(user, password, fName, lName);
	}
}
