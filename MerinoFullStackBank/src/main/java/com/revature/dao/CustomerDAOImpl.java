package com.revature.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import org.postgresql.util.PSQLException;

import com.revature.users.Customer;

import com.revature.util.ConnectionUtils;

public class CustomerDAOImpl implements CustomerDAO {

	@Override
	public Customer getCustomer(int id) {
		// TODO Auto-generated method stub
		Customer c = null;
		try (Connection conn = ConnectionUtils.getConnection()) {
			String sql = "SELECT user_table.user_id AS userID, user_table.user_name AS username, user_table.user_pass as password,	 user_table.user_f_name as fName , user_table.user_l_name as lName, personal_information_table.phone_number as phone, personal_information_table.address  FROM user_table inner join personal_information_table on personal_information_table.user_id = user_table.user_id where user_table.user_id = ?";

			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setInt(1, id);
			ResultSet result = statement.executeQuery();
			result.next();
			c = new Customer(result.getInt("userID"), result.getString("username"), result.getString("password"),
					result.getString("fName"), result.getString("lName"), result.getString("address"),
					result.getString("phone")

			);
			

		} catch (PSQLException e1) {
			
			return null;
		}

		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
		try (Connection conn = ConnectionUtils.getConnection()) {
			String sql = "select user_account_table.bank_account_id as bankID from user_account_table where user_account_table.user_id = ?";
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setInt(1, id);
			ResultSet result = statement.executeQuery();
			while(result.next()) {
				c.addAccount(result.getInt("bankID"));
				
			}
			
			return c;
		} catch (PSQLException e1) {
			return c;
		}

		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public boolean updatePassword(Customer cus, String newPass) {
		// TODO Auto-generated method stub
		try (Connection conn = ConnectionUtils.getConnection()) {
			String sql = "update user_table set user_pass = ? where user_id = ?";
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setString(1, newPass);
			statement.setInt(2, cus.getUserID());
			statement.execute();
			return true;
					
					
		}catch(PSQLException e1) {
			System.out.println(e1.getMessage());
			return false;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean updateInfo(Customer cus, String newPhone, String newAddress) {
		// TODO Auto-generated method stub
		try (Connection conn = ConnectionUtils.getConnection()){
			String sql = "update personal_information_table set phone_number = ?, address = ? where user_id = ?";
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setString(1, newPhone);
			statement.setString(2, newAddress);
			statement.setInt(3, cus.getUserID());
			statement.execute();
			return true;
			
		} catch(PSQLException e1) {
			System.out.println(e1.getMessage());
			return false;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean createCustomer(String user, String password, String fName, String lName) {
		// TODO Auto-generated method stub
		int userIDnum;
		try  (Connection conn = ConnectionUtils.getConnection()){
			String sql = "insert into user_table (user_name, user_pass, user_f_name, user_l_name, user_type) values (?, ?, ?, ?, 1)";
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setString(1, user);
			statement.setString(2, password);
			statement.setString(3, fName);
			statement.setString(4, lName);
			statement.execute();
			
		
		} catch(PSQLException e1) {
			return false;
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			
		}
		try  (Connection conn = ConnectionUtils.getConnection()){
			String sql = "select max(user_id) as userID from user_table";
			Statement statement = conn.createStatement();
			ResultSet result = statement.executeQuery(sql);
			result.next();
			userIDnum = result.getInt("userID");
		
		} catch(PSQLException e1) {
			
			return false;
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
		try  (Connection conn = ConnectionUtils.getConnection()){
			String sql = "insert into personal_information_table (user_id)  values (?) ";
			
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setInt(1, userIDnum);
			statement.execute();
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
	public boolean deleteCustomer(Integer userID) {
		// TODO Auto-generated method stub
		try (Connection conn = ConnectionUtils.getConnection()){
			String sql = "delete from user_account_table where user_id = ?;delete from personal_information_table where user_id = ?; delete from user_table where user_id = ?";
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setInt(1, userID);
			statement.setInt(2, userID);
			statement.setInt(3, userID);
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

}
