package com.revature.services;


import com.revature.accounts.Account;
import com.revature.dao.AccountDAO;
import com.revature.dao.AccountDAOImpl;


public class AccountService {
	private AccountDAO aDao = new AccountDAOImpl();
	
	
	public Account getAccount(int accNumber) {
		return aDao.getAccount(accNumber);
	}
	public boolean createAccount(Account acc) {
		return aDao.createAccount(acc);
	}
	public boolean deleteAccount(int accNumber) {
		return aDao.deleteAccount(accNumber);
	}
	public boolean setApproved(int accNumber, String approved) {
		return aDao.setApproved(accNumber, approved);
	}
}
