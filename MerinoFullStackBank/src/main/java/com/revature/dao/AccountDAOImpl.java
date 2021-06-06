package com.revature.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import org.postgresql.util.PSQLException;

import com.revature.accounts.Account;
import com.revature.util.ConnectionUtils;

public class AccountDAOImpl implements AccountDAO {

	@Override
	public Account getAccount(Integer accNumber) {
		// TODO Auto-generated method stub
		try (Connection conn = ConnectionUtils.getConnection()) {
			String sql = "SELECT account_table.bank_account_id AS accNumber, account_table.account_approved AS approved, account_table.account_balance as balance,	 account_table.account_type , user_account_table.user_id FROM account_table inner join user_account_table on account_table.bank_account_id = user_account_table.bank_account_id where user_account_table.bank_account_id ="
					+ accNumber;

			Statement statement = conn.createStatement();
			ResultSet result = statement.executeQuery(sql);
			result.next();
			Account a = new Account(result.getInt("accNumber"), result.getString("account_type"),
					result.getDouble("balance"), result.getString("approved")

			);
			a.addCustomer(result.getInt("user_id"));
			while (result.next()) {
				a.addCustomer(result.getInt("user_id"));
			}
			return a;

		}  catch(PSQLException e1) {
			return null;
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public boolean createAccount(Account acc) {
		// TODO Auto-generated method stub
		int accID;
		try  (Connection conn = ConnectionUtils.getConnection()){
			String sql = "insert into account_table (account_type, account_balance,account_approved) values (?, ?, ?)";
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setString(1, acc.getAccountType());
			statement.setDouble(2, acc.getBalance());
			statement.setString(3, acc.getApproved());
			statement.execute();
		
		} catch(PSQLException e1) {
			return false;
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
		try  (Connection conn = ConnectionUtils.getConnection()){
			String sql = "select max(bank_account_id) as accID from account_table";
			Statement statement = conn.createStatement();
			ResultSet result = statement.executeQuery(sql);
			result.next();
			accID = result.getInt("accID");
		
		} catch(PSQLException e1) {
			return false;
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
		try  (Connection conn = ConnectionUtils.getConnection()){
			String sql = "insert into user_account_table values (" + acc.getCustomerList().get(0) + ", " + accID + ") ";
			for (int i = 1; i< acc.getCustomerList().size(); i++) {
				sql += ", (" + acc.getCustomerList().get(i) + ", " + accID + ") ";
			}
			Statement statement = conn.createStatement();
			statement.execute(sql);
			return true;
		
		} catch(PSQLException e1) {
			
			return false;
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean deleteAccount(Integer accNumber) {
		// TODO Auto-generated method stub
		try (Connection conn = ConnectionUtils.getConnection()){
			String sql = "delete from user_account_table where bank_account_id = ?;delete from account_table where bank_account_id = ?";
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setInt(1, accNumber);
			statement.setInt(2, accNumber);
			statement.execute();
			return true;
		} catch(PSQLException e1) {
			System.out.println(e1.getMessage());
			return false;
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean setApproved(Integer accNumber, String approved) {
		// TODO Auto-generated method stub
		if (getAccount(accNumber) == null) {
			return false;
		}
		try (Connection conn = ConnectionUtils.getConnection()){
			String sql = "update account_table set account_approved = ? where bank_account_id = ?";
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setString(1, approved);
			statement.setInt(2, accNumber);
			statement.execute();
			return true;
		}catch(PSQLException e1) {
			return false;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return false;
	}

}
