package com.revature.services;

import java.util.ArrayList;

import java.util.Scanner;

import com.revature.accounts.Account;
import com.revature.controller.Controller;
import com.revature.dao.CustomerDAO;
import com.revature.dao.CustomerDAOImpl;
import com.revature.exceptions.InvalidActionException;
import com.revature.users.Customer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CustomerService {

	private static final Logger logger = LogManager.getLogger(Controller.class);
	private tServices tServ = new tServices();
	private AccountService accServ = new AccountService();
	private CustomerDAO cDao = new CustomerDAOImpl();

	public void service(Customer customer, Scanner sc) {

		while (true) {
			// Welcome
			System.out.println("Welcome " + customer.getFirstName() + " " + customer.getLastName()
					+ ". Your user ID is [" + customer.getUserID()+ "]. We have your phone number and address listed as:");
			System.out.println("Phone number: " + customer.getPhone() + ".");
			System.out.println("Address:");
			System.out.println(customer.getAddress());

			// Select to update info, see accounts, or exit.

			System.out.println(
					"Please type 'update' if you would like to update your personal information, 'password' to update it, 'accounts' to see your bank accounts, 'open' to open a new account or 'logout' to logout.");

			String response = sc.nextLine();
			if (response.equals("logout")) {

				break;
			}
			switch (response) {
			case "update":
				update(customer, sc);

				continue;

			case "accounts":
				accounts(customer, sc);

				continue;
			case "password":
				updatePassword(customer, sc);

				continue;
			case "open":
				openAccount(customer, sc);
				continue;
			default:
				System.out.println("Please select and type one of the options provided.");
				break;
			}

		}
		System.out.println("Good bye " + customer.getFirstName() + " " + customer.getLastName() + ".");

	}

	public Customer getCustomer(int id) {
		return cDao.getCustomer(id);
	}

	public void listAccounts(Customer cus) {
		
		for (int i = 0; i < cus.getNumberOfAccounts(); i++) {
			int j = i + 1;
			System.out.println(j + ". " + accServ.getAccount(cus.getAccount(i)));
		}
	}

	public void updatePassword(Customer customer, Scanner sc) {
		while (true) {
			System.out.println("Please type your new password.");
			String pass = sc.nextLine();
			System.out.println("Please type your new password again.");
			String conf = sc.nextLine();
			if (pass.equals(conf)) {
				customer.setPassword(pass);
				cDao.updatePassword(customer, pass);
				logger.trace("Changed password for user " + customer.getUserName());
				System.out.println("Changed password for user " + customer.getUserName());
				break;
			} else {
				System.out.println("The passwords didn't match");
			}
		}
	}
	public boolean updatePasswordService(Customer customer, String newPassword) {
		return cDao.updatePassword(customer, newPassword);
	}

	public void update(Customer customer, Scanner sc) {
		System.out.println("Please provide your new phone number.");

		String newPhone = sc.nextLine();
		customer.setPhone(newPhone);

		System.out.println("Please provide your new address.");
		String newAddress = sc.nextLine();
		customer.setAddress(newAddress);
		if (cDao.updateInfo(customer, newPhone, newAddress)) {
			logger.trace("Updated phone number for user " + customer.getUserName() + ". New phone number is: "
					+ customer.getPhone() + ".");
			logger.trace("Updated address for user " + customer.getUserName() + ". New address is: "
					+ customer.getAddress() + ".");
			System.out.println("Your information has been updated!");
		} else {
			System.out.println(
					"There was an error updating your information. Please try again or talk to a local teller.");
		}

	}
	public boolean updateInfo(Customer customer,String newPhone,String newAddress) {
		return cDao.updateInfo(customer, newPhone, newAddress);
	}
	

	public void accounts(Customer cus, Scanner sc) {
		
		while (true) {
			Customer customer = getCustomer(cus.getUserID());
			System.out.println("These are your current bank accounts:");
			listAccounts(customer);
			System.out.println(
					"If you would like to make a deposit type 'deposit', 'withdraw' for a withdrawl, 'transfer' for a transfer, 'delete' to delete a cancelled account, and 'exit' to exit accounts screen.");
			String response = sc.nextLine();
			if (response.equals("deposit")) {
				System.out.println(
						"Type the number listed for the account in which you wish to deposit. For example type '1' for the first account listed.");
				response = sc.nextLine();
				int index;
				try {
					index = Integer.valueOf(response) - 1;
					if (index < 0) {
						System.out.println("Your number should be bigger than 0");

						continue;
					}

				} catch (NumberFormatException e) {
					System.out.println("Invalid entry.");

					continue;
				}
				System.out.println("Type the amount you wish to deposit.");

				String response2 = sc.nextLine();
				double amount;

				try {
					amount = Double.parseDouble(response2);
					amount = Math.round(amount * 100) / 100;
				} catch (NumberFormatException e) {
					System.out.println("Invalid entry.");

					continue;
				}
				try {
					String deposit = tServ.deposit(amount, customer.getAccount(index));
					System.out.println(deposit);

					logger.trace("Deposit attempt was made. Result: " + deposit);

					continue;
				}catch (InvalidActionException e1) {
					System.out.println(e1.getMessage());
				}
				
				catch (IndexOutOfBoundsException e) {
					System.out.println("You don't have enough accounts to match your choice of account.");

					continue;
				}

			} else if (response.equals("withdraw")) {
				System.out.println(
						"Type the number listed for the account from which you wish to withdraw. For example type '1' for the first account listed.");
				response = sc.nextLine();
				int index;
				try {
					index = Integer.valueOf(response) - 1;
					if (index < 0) {
						System.out.println("Your number should be bigger than 0");

						continue;
					}

				} catch (NumberFormatException e) {
					System.out.println("Invalid entry.");

					continue;
				}
				System.out.println("Type the amount you wish to withdraw.");

				String response2 = sc.nextLine();
				double amount;
				try {
					amount = Double.parseDouble(response2);
					amount = Math.round(amount * 100) / 100;
				} catch (NumberFormatException e) {
					System.out.println("Invalid entry.");

					continue;
				}
				try {
					String withdraw = tServ.withdraw(amount, customer.getAccount(index));
					System.out.println(withdraw);
					logger.trace("A withdrawl attempt was made. Result: " + withdraw);

					continue;
				} catch (IndexOutOfBoundsException e) {
					System.out.println("You don't have enough accounts to match your choice of account.");

					continue;
				} catch (InvalidActionException e1) {
					System.out.println(e1.getMessage());
				}
			} else if (response.equals("transfer")) {
				System.out.println(
						"Type the number listed for the account from which you wish to transfer. For example type '1' for the first account listed.");
				response = sc.nextLine();
				int index;
				try {
					index = Integer.valueOf(response) - 1;
					if (index < 0) {
						System.out.println("Your number should be bigger than 0");

						continue;
					}

				} catch (NumberFormatException e) {
					System.out.println("Invalid entry.");

					continue;
				}
				System.out.println("Type the amount you wish to transfer.");

				String response2 = sc.nextLine();
				double amount;
				try {
					amount = Double.parseDouble(response2);
					amount = Math.round(amount * 100) / 100;
				} catch (NumberFormatException e) {
					System.out.println("Invalid entry.");

					continue;
				}

				System.out.println("Type the full account number you wish to transfer to.");
				String response3 = sc.nextLine();
				int accountReceiving;
				try {
					accountReceiving = Integer.valueOf(response3);

				} catch (NumberFormatException e) {
					System.out.println("Invalid entry.");

					continue;
				}
				try {
					String result = tServ.transfer(amount, customer.getAccount(index), accountReceiving);
					logger.trace("A transfer attempt was made. Result: " + result);
					System.out.println(result);

					continue;
				} catch (IndexOutOfBoundsException e) {
					System.out.println("You don't have enough accounts to match your choice of account.");

					continue;
				} catch (NullPointerException e) {
					System.out.println("Receiving account could not be found.");
				}  catch (InvalidActionException e1) {
					System.out.println(e1.getMessage());
				}
			} else if (response.equals("exit")) {

				break;
			} else if (response.equals("delete")) {
				System.out.println(
						"Type the number listed for the account you wish to delete. For example type '1' for the first account listed.");
				response = sc.nextLine();
				int index;
				try {
					index = Integer.valueOf(response) - 1;
					if (index < 0) {
						System.out.println("Your number should be bigger than 0");

						continue;
					}

				} catch (NumberFormatException e) {
					System.out.println("Invalid entry.");

					continue;
				}

				try {
					Account acc = accServ.getAccount(customer.getAccount(index));
					if (acc.getApproved().equals("Cancelled")) {
						if (accServ.deleteAccount(customer.getAccount(index))) {
							System.out.println("Account [" + acc.getAccountNumber() + "] was successfully deleted.");
							logger.trace("Account [" + acc.getAccountNumber() + "] was successfully deleted by user "
									+ customer.getUserID() + ".");
							customer.removeAccount(index);
							continue;

						} else {
							System.out.println("Account deletion was not successfull");
							logger.debug("Account [" + acc.getAccountNumber()
									+ "] was not successfully deleted by user " + customer.getUserID() + ".");
							continue;
						}

					} else {
						System.out.println("Only cancelled accounts may be deleted.");
					}
				} catch (IndexOutOfBoundsException e) {
					System.out.println("You don't have enough accounts to match your choice of account.");

					continue;
				}

			} else {
				System.out.println("Please type a valid response.");

				continue;
			}

		}
	}

	public void openAccount(Customer customer, Scanner sc) {
		while (true) {
			System.out.println(
					"Please write 'Checking' to open a checking account, write 'Savings' to open a savings account, or write 'exit' to exit this menu.");

			String type = sc.nextLine();
			if (type.equals("exit")) {

				break;
			} else if (!type.equals("Checking") && !type.equals("Savings")) {
				System.out.println("Invalid selection.");

				continue;
			} else {
				System.out.println("Please type your initial deposit amount.");
				String response2 = sc.nextLine();
				double amount;
				try {
					amount = Double.parseDouble(response2);
					amount = Math.round(amount * 100) / 100;
				} catch (NumberFormatException e) {
					System.out.println("Invalid entry.");

					continue;
				}
				ArrayList<Integer> cuslist = new ArrayList<Integer>();
				cuslist.add(customer.getUserID());
				while (true) {
					System.out.println(
							"Please type the user id of any joint account holders or type 'done' when no more users need to be added.");
					String answer = sc.nextLine();
					if (answer.equals("done")) {
						break;
					} else {
						Customer newCus;
						try {
							int userID = Integer.valueOf(answer);
							newCus = getCustomer(userID);
						} catch (NumberFormatException e) {
							System.out.println("Invalid entry.");

							continue;
						}

						if (newCus == null) {
							System.out.println("No such user.");
						} else if (newCus.getUserID() == customer.getUserID()) {
							System.out.println(
									"Can't add yourself as a joint account holder. You are already included in this request.");
						} else {
							cuslist.add(newCus.getUserID());
						}

					}

				}

				Account a = new Account(1, type, amount, "Pending");
				for (int cus : cuslist) {
					a.addCustomer(cus);
				}
				if (accServ.createAccount(a)) {
					Customer updatedCus = getCustomer(customer.getUserID());
					a = accServ.getAccount(findLastAccount(updatedCus));
					
					StringBuilder sb = new StringBuilder();
					sb.append("A new account was created. Account number [");
					sb.append(a.getAccountNumber());
					sb.append("] has balance ");
					sb.append(a.getBalance());
					sb.append(", and is assigned to users:");
					for (int cusID : a.getCustomerList()) {

						Customer cus = getCustomer(cusID);
						sb.append(" " + cus.getUserName() + ";");
					}

					String log = sb.toString();
					logger.trace(log);
					System.out.println("Account [" + a.getAccountNumber() + "] was created with initial balance "
							+ a.getBalance() + ". Account holders are:");
					for (int i = 0; i < cuslist.size(); i++) {
						int j = i + 1;
						System.out.println(j + ". " + getCustomer(cuslist.get(i)).getFirstName() + " "
								+ getCustomer(cuslist.get(i)).getLastName() + ".");
					}
					break;

				} else {
					System.out.println(
							"There was an error creating the account. Please try again or speak to a local teller.");
				}

			}
		}

	}

	public int findLastAccount(Customer cus) {
		int account = 0;
		for (int x : cus.getAccountList()) {
			if (x > account) {
				account = x;
			}
		}
		return account;
	}

	public boolean createCustomer(String user, String password, String fName, String lName) {
		return cDao.createCustomer(user, password, fName, lName);
	}
	public boolean deleteCustomer(Integer userID) {
		return cDao.deleteCustomer(userID);
	}
}
