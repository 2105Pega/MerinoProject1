package com.revature.services;

import java.util.ArrayList;

import com.revature.dao.UserDAO;
import com.revature.dao.UserDAOImpl;

import com.revature.users.User;


public class UserService {
	private UserDAO uDao = new UserDAOImpl();
	
	public User getUser(String username) {
		return uDao.getUser(username);
	}
	public User getUser(int userID) {
		return uDao.getUser(userID);
	}
	public ArrayList<String> getUserList(){
		return uDao.getUserList();
	}
	
	//code from implementation via serialization before use of database
//	public static Customer findCustomer(UserList ul, String userName) {
//		for(Customer cus: ul.getCusList()) {
//			if(cus.getUserName().equals(userName)) {
//				return cus;
//			}
//		}
//		return null;
//	}
//	public static Employee findEmployee(UserList ul, String userName) {
//		for(Employee emp: ul.getEmpList()) {
//			if(emp.getUserName().equals(userName)) {
//				return emp;
//			}
//		}
//		return null;
//	}
}
