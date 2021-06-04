package com.revature.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;

import org.postgresql.util.PSQLException;

import com.revature.util.ConnectionUtils;

public class TDAOImpl implements TDAO {

	@Override
	public boolean withdraw(int accNumber, double amount) {
		// TODO Auto-generated method stub
		try (Connection conn = ConnectionUtils.getConnection()){
			String sql = "update account_table set account_balance = account_balance - ? where bank_account_id = ?";
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setDouble(1, amount);
			statement.setInt(2, accNumber);
			statement.execute();
			
			return true;
			
		}  catch(PSQLException e1) {
			return false;
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return false;
	}

	@Override
	public boolean deposit(int accNumber, double amount) {
		// TODO Auto-generated method stub
		try (Connection conn = ConnectionUtils.getConnection()){
			String sql = "update account_table set account_balance = account_balance + ? where bank_account_id = ?";
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setDouble(1, amount);
			statement.setInt(2, accNumber);
			statement.execute();
			
			return true;
			
		}  catch(PSQLException e1) {
			return false;
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return false;
	}

	@Override
	public boolean transfer(double amount, int sender, int receiver) {
		// TODO Auto-generated method stub
		if(withdraw(sender, amount) && deposit(receiver, amount)) {
			return true;
		}
		
		return false;
	}

}
