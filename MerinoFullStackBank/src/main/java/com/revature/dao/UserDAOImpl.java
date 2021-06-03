package com.revature.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import org.postgresql.util.PSQLException;

import com.revature.users.User;
import com.revature.util.ConnectionUtils;

public class UserDAOImpl implements UserDAO {

	@Override
	public User getUser(String username) {
		// TODO Auto-generated method stub
		try (Connection conn = ConnectionUtils.getConnection()) {
			String sql = "SELECT user_table.user_id AS userID, user_table.user_name AS username, user_table.user_pass as password,	 user_table.user_f_name as fName , user_table.user_l_name as lName, user_table.user_type  FROM user_table where user_table.user_name = ?";

			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setString(1, username);
			ResultSet result = statement.executeQuery();
			result.next();
			User u = new User(result.getInt("userID"), result.getString("username"),
					result.getString("password"), result.getString("fName"), result.getString("lName"), result.getInt("user_type")

			);
			
			return u;

		} catch(PSQLException e1) {
			return null;
		}
		
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public ArrayList<String> getUserList() {
		// TODO Auto-generated method stub
		ArrayList<String> userList = new ArrayList<String>();
		try (Connection conn = ConnectionUtils.getConnection()) {
			String sql = "select user_name from user_table";
			Statement statement = conn.createStatement();
			ResultSet result = statement.executeQuery(sql);
			
			while(result.next()) {
				userList.add(result.getString("user_name"));
			}
			return userList;
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return null;
	}

	@Override
	public User getUser(int userID) {
		// TODO Auto-generated method stub
		try (Connection conn = ConnectionUtils.getConnection()) {
			String sql = "SELECT user_table.user_id AS userID, user_table.user_name AS username, user_table.user_pass as password,	 user_table.user_f_name as fName , user_table.user_l_name as lName, user_table.user_type  FROM user_table where user_table.user_id = ?";

			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setInt(1, userID);
			ResultSet result = statement.executeQuery();
			result.next();
			User u = new User(result.getInt("userID"), result.getString("username"),
					result.getString("password"), result.getString("fName"), result.getString("lName"), result.getInt("user_type")

			);
			
			return u;

		} catch(PSQLException e1) {
			return null;
		}
		
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}

}
