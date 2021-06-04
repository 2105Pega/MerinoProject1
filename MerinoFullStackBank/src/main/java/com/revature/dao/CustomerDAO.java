package com.revature.dao;

import com.revature.users.Customer;

public interface CustomerDAO {
	public Customer getCustomer(int id);
	public boolean updatePassword(Customer cus, String newPass);
	public boolean updateInfo(Customer cus, String newPhone, String newAddress);
	public boolean createCustomer(String user, String password, String fName, String lName);
	public boolean deleteCustomer(Integer userID);
}
