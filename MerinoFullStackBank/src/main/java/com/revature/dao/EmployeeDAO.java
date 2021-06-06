package com.revature.dao;

import java.util.ArrayList;

import com.revature.users.Customer;
import com.revature.users.Employee;

public interface EmployeeDAO {
	public ArrayList<Customer> getCustomerList();
	public Employee getEmployee(int userID);
	public boolean createEmployee(String user, String password, String fName, String lName);
}
