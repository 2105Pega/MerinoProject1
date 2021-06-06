package com.revature.responses;

import java.util.ArrayList;

import com.revature.users.Customer;

public class Response {
	public boolean fail;
	public String warning;
	public int userID;
	public String userName;
	public String password;
	public String firstName;
	public String lastName;
	public int userType;
	public String address;
	public String phone;
	public ArrayList<Integer> accountList;
	public int numberOfAccounts;
	public Integer accountNumber;
	public String accountType;
	public double balance;
	public String approved;
	public ArrayList<Integer> customerList;
	public ArrayList<Customer> employeeCustomerList;
}
