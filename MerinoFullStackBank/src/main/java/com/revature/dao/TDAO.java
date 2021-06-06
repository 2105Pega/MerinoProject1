package com.revature.dao;

public interface TDAO {
	//transaction DAO interface
	public boolean withdraw(int accNumber, double amount);
	public boolean deposit(int accNumber, double amount);
	public boolean transfer(double amount, int sender, int receiver );
	
}
