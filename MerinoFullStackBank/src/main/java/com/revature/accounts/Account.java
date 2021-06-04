package com.revature.accounts;


import java.io.Serializable;
import java.util.ArrayList;


public class Account implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -921326158968652254L;
	private final Integer accountNumber;
	private final String accountType;
	private double balance;
	private String approved;
	private ArrayList<Integer> customerList;
	
	
	
	public Account(Integer accNumber, String accType, double balance, String approved) {
		this.accountNumber = accNumber;
		this.accountType = accType;
		this.balance = balance;
		this.approved = approved;
		this.customerList = new ArrayList<Integer>();
		
		
		
	}
	public String getAccountType() {
		return this.accountType;
	}
	
	public int getAccountNumber() {
		return accountNumber;
	}
	
	public double getBalance() {
		return balance;
	}
	public void setBalance(double balance) {
		this.balance = balance;
	}
	public String getApproved() {
		return approved;
	}
	public void setApproved(String approved) {
		this.approved = approved;
	}
	public ArrayList<Integer> getCustomerList(){
		return customerList;
	}
	public Integer getCustomer(int i) {
		return customerList.get(i);
	}
	public void addCustomer(int cusNumber) {
		customerList.add(cusNumber);
	}

	@Override
	public String toString() {
		switch (this.getApproved()) {
		case "Pending":
			return this.getAccountType() + " account [" + this.getAccountNumber() + "] is pending and has a balance of: " + this.getBalance() + ".";
		case "Approved":
			return this.getAccountType() + " account [" + this.getAccountNumber() + "] has a balance of: " + this.getBalance() + ".";
		default:
			return this.getAccountType() + " account [" + this.getAccountNumber() + "] has been canceled.";
		}
	}
	
	
	
	
}
