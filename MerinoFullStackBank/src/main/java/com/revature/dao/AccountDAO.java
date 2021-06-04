package com.revature.dao;

import com.revature.accounts.Account;

public interface AccountDAO {
	public Account getAccount(Integer accNumber);
	public boolean createAccount(Account acc);
	public boolean deleteAccount(Integer accNumber);
	public boolean setApproved(Integer accNumber, String approved);
}
