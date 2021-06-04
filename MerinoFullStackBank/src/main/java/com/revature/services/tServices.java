package com.revature.services;

import com.revature.accounts.*;

import com.revature.dao.TDAO;
import com.revature.dao.TDAOImpl;
import com.revature.exceptions.InvalidActionException;

public class tServices {
	private TDAO tDao = new TDAOImpl();
	private AccountService accServ = new AccountService();

	public String withdraw(double amount, int accNumber) throws InvalidActionException {
		Account account = accServ.getAccount(accNumber);
		if(account == null) {
			throw new InvalidActionException("Account not found for withdrawl");
		}
		if (account.getApproved().equals("Pending") ) {
			throw new InvalidActionException("The account is pending approval. No withdrawals allowed until the account is approved.");
		} else if (account.getApproved() == "Cancelled") {
			throw new InvalidActionException("This account has been cancelled. Please talk to a local teller.");
		}
		if (account.getBalance() > amount) {
			if (amount > 0) {

				if (tDao.withdraw(accNumber, amount)) {
					double oldBalance = account.getBalance();
					account = accServ.getAccount(accNumber);

					return "Account [" + account.getAccountNumber() + "] had an account balance of " + oldBalance
							+ " and a withdrawal of " + amount + " was made. The new balance is: "
							+ account.getBalance() + ".";
				} else {
					return "The withdrawl was unsuccessful";
				}
			} else {
				throw new InvalidActionException("You should withdraw an amount bigger than 0.");
			}
		} else {
			throw new InvalidActionException("Insuficient funds to make this withdrawal. Transaction canceled.");
		}
	}

	public String deposit(double amount, int accNumber) throws InvalidActionException {
		Account account = accServ.getAccount(accNumber);
		if(account == null) {
			throw new InvalidActionException("Account not found for deposit");
		}
		if (account.getApproved().equals("Pending")) {
			throw new InvalidActionException("The account is pending approval. No deposits allowed until the account is approved.");
		} else if (account.getApproved().equals("Cancelled") ) {
			throw new InvalidActionException("This account has been cancelled. Please talk to a local teller.");
		}
		if (amount > 0) {
			if (tDao.deposit(accNumber, amount)) {
				double oldBalance = account.getBalance();
				account = accServ.getAccount(accNumber);

				return "Account [" + account.getAccountNumber() + "] had an account balance of " + oldBalance
						+ " and a deposit of " + amount + " was made. The new balance is: "
						+ account.getBalance() + ".";
			} else {
				return "The deposit was unsuccessful";
			}
		} else {
			throw new InvalidActionException("You should deposit an amount bigger than 0.");
		}
	}

	public String transfer(double amount, int senderNumber, int receiverNumber) throws InvalidActionException {
		Account sender = accServ.getAccount(senderNumber);
		Account receiver = accServ.getAccount(receiverNumber);
		if(sender == null) {
			throw new InvalidActionException("Account not found for withdrawl portion. Transfer cancelled.");
		}
		if(receiver == null) {
			throw new InvalidActionException("Account not found for deposit portion. Transfer cancelled.");
		}
		if (amount <= 0) {
			throw new InvalidActionException("The transfer amount should be bigger than 0.");
		} else if (senderNumber == receiverNumber) {
			throw new InvalidActionException("You cannot transfer funds between the same account. Please choose to withdraw or deposit as necessary.");
		} else {
			if (sender.getBalance() < amount) {
				throw new InvalidActionException("Insufficient funds to carry the transaction");
			} else if (sender.getApproved().equals("Pending")) {
				throw new InvalidActionException("The account is pending approval. No transfers allowed until the account is approved.");
			} else if (sender.getApproved().equals("Cancelled")) {
				throw new InvalidActionException("This account has been cancelled. Please talk to a local teller.");
			} else if (receiver.getApproved().equals("Pending") || receiver.getApproved().equals("Cancelled")) {
				throw new InvalidActionException("The recepient account is unable to receive transfers at the moment. Please speak to your recepient.");
			}
			if (sender.getApproved().equals("Approved") && receiver.getApproved().equals("Approved")) {
				if(tDao.transfer(amount, senderNumber, receiverNumber)) {
				
				return "Account [" + sender.getAccountNumber() + "] had an account balance of " + sender.getBalance()
						+ " and a withdrawal of " + amount + " was made. The new balance is: "
						+ accServ.getAccount(senderNumber).getBalance() + "." + " The withdrawn amount was transferred to account [" + receiver.getAccountNumber()
						+ "] and should be available immediately.";
				} else {
					return "Transfer was unsuccessful.";
				}
			}
			return "Unknown account status. Unable to transfer";
		}
	}
}
