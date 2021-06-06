package com.revature.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import org.postgresql.util.PSQLException;

import com.revature.services.CustomerService;
import com.revature.users.Customer;
import com.revature.users.Employee;
import com.revature.util.ConnectionUtils;

public class EmployeeDAOImpl implements EmployeeDAO {
	private CustomerService cServ = new CustomerService();
	@Override
	public ArrayList<Customer> getCustomerList() {
		// TODO Auto-generated method stub
		ArrayList<Customer> cusList = new ArrayList<Customer>();
		try (Connection conn = ConnectionUtils.getConnection()) {
			String sql = "select user_id from user_table where user_type = 1";
			Statement statement = conn.createStatement();
			ResultSet result = statement.executeQuery(sql);
			while(result.next()) {
				int userdId = result.getInt("user_id");
				cusList.add(cServ.getCustomer(userdId));
			}
			return cusList;
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		
		return null;
	}
	@Override
	public Employee getEmployee(int userID) {
		// TODO Auto-generated method stub
		Employee emp = null;
		try (Connection conn = ConnectionUtils.getConnection()) {
			String sql = "SELECT user_table.user_id AS userID, user_table.user_name AS username, user_table.user_pass as password,	 user_table.user_f_name as fName , user_table.user_l_name as lName FROM user_table  where user_table.user_id = ?";

			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setInt(1, userID);
			ResultSet result = statement.executeQuery();
			result.next();
			emp = new Employee(result.getInt("userID"), result.getString("username"), result.getString("password"),
					result.getString("fName"), result.getString("lName"), 2

			);
			return emp;

		} catch (PSQLException e1) {
			
			return null;
		}

		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			
		}
		

		return null;
	}
	@Override
	public boolean createEmployee(String user, String password, String fName, String lName) {
		// TODO Auto-generated method stub
		try  (Connection conn = ConnectionUtils.getConnection()){
			String sql = "insert into user_table (user_name, user_pass, user_f_name, user_l_name, user_type) values (?, ?, ?, ?, 2)";
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setString(1, user);
			statement.setString(2, password);
			statement.setString(3, fName);
			statement.setString(4, lName);
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

}
